final int defaultSetbacks = 20; // 18 works but for skywars 21 is better
final long joinDelay = 200, delay = 0, checkDisabledTime = 4000, timeout = 12000;
final double min_offset = 0.2; // max 0.25, randomizing too much doesnt work either, can also set as motion z

long joinTime, lobbyTime, finished;
boolean awaitJoin, joinTick, awaitSetback, noRotate, hideProgress, awaitJump, awaitGround;
int setbackCount, airTicks, disablerAirTicks;
double minSetbacks, zOffset;
float savedYaw, savedPitch;

String text;
int[] disp;
int width;

boolean waitForJump = true;

void onLoad() {
    modules.registerSlider("Offset", " ticks", 0, -10, 10, 1);
    modules.registerButton("Hide progress", false);
    modules.registerButton("00 disabler", false);
}

void onDisable() {
    resetVars();
    waitForJump = true;
    bridge.remove("disabler");
}

void resetVars() {
    if (noRotate) {
        modules.enable("NoRotate");
    }
    awaitJoin = joinTick = awaitSetback = noRotate = awaitJump = false;
    minSetbacks = zOffset = lobbyTime = finished = setbackCount = 0;
}

void onPreMotion(PlayerState state) {
    long now = client.time();
    Entity player = client.getPlayer();
    String screen = client.getScreen();
    
    if (player.onGround()) {
        airTicks = 0;
    } else {
        airTicks++;
    }
    
    if (modules.getButton(scriptName, "00 disabler")) {
        if (modules.isEnabled("Scaffold")) {
            waitForJump = true;
        } else {
            Vec3 pos = client.getPlayer().getPosition();
            pos.y -= 0.5;
            Block block = world.getBlockAt(pos);
            if (block.type.equals("BlockStairs") || (block.type.contains("BlockHalf") && block.type.contains("Slab"))) {
                waitForJump = true;
            } else {
                if (waitForJump && airTicks > 3) {
                    waitForJump = false;
                }
                if (!waitForJump && player.onGround() && state.y % 1 == 0) {
                    state.y += 1e-3;
                    //client.print("floating");
                }
            }
        }
    }
    
    if (!awaitGround && !player.onGround()) {
        disablerAirTicks++;
    } else {
        awaitGround = false;
        disablerAirTicks = 0;
    }
    
    if (awaitJoin && now >= joinTime + joinDelay) {
        ItemStack item = inventory.getStackInSlot(8);
        if (item != null && item.name.equals("bed") || isPit()) {
            if (modules.isEnabled("NoRotate") && (isSkywars() || isPit())) {
                modules.disable("NoRotate");
                noRotate = true;
            }
            awaitJoin = false;
            joinTick = true;
        }
    }

    if (awaitSetback) {
        hideProgress = modules.getButton(scriptName, "Hide progress") || (!screen.isEmpty() && !screen.startsWith("GuiChat"));
        text = util.colorSymbol +   "7running disabler " + util.colorSymbol + "b" + util.round((now - lobbyTime) / 1000d, 1) + "s " + ((int) util.round(100 * (setbackCount / minSetbacks), 0)) + "%";
        disp = client.getDisplaySize();
        width = render.getFontWidth(text) / 2 - 2;
    } else {
        text = null;
    }

    if (finished != 0 && player.onGround() && now - finished > checkDisabledTime) {
        client.print("&7[&dR&7] &adisabler enabled");
        finished = 0;
        bridge.add("disabler", true);
    }

    if (awaitJump && disablerAirTicks == 5) {
        keybinds.setPressed("jump", false);
        awaitJump = false;

        minSetbacks = defaultSetbacks + modules.getSlider(scriptName, "Offset");
        savedYaw = state.yaw; // pitch will be 0
        lobbyTime = now;
        awaitSetback = true;
    }

    if (joinTick) {
        joinTick = false;
        client.print("&7[&dR&7] running disabler...");
        if (player.onGround() || (player.getFallDistance() < 0.3 && !isPit())) {
            awaitJump = true;
            keybinds.setPressed("jump", true);
        } else {
            minSetbacks = defaultSetbacks + modules.getSlider(scriptName, "Offset");
            savedYaw = state.yaw; // pitch will be 0
            lobbyTime = now;
            awaitSetback = true;
        }
        return;
    }

    if (awaitSetback) {
        if (setbackCount >= minSetbacks) {
            client.print("&7[&dR&7] &afinished in &b" + util.round((now - lobbyTime) / 1000d, 1) + "&as, wait a few seconds...");
            resetVars();
            finished = now;
            return;
        } else if (lobbyTime != 0 && now - lobbyTime > timeout) {
            client.print("&7[&dR&7] &cdisabler failed");
            resetVars();
            return;
        }
        if (now - lobbyTime > delay) {
            player.setYaw(state.yaw = savedYaw);
            player.setPitch(state.pitch = savedPitch);
            client.setMotion(0, 0, 0);
            if (isSkywars()) {
                zOffset = min_offset * 0.7;
                if (player.getTicksExisted() % 2 == 0) {
                    zOffset *= -1;
                }
                state.z += zOffset;
            } else {
                state.z += (zOffset += min_offset);
            }
        }
    }
}

boolean onPacketReceived(SPacket packet) {
    if (awaitSetback && packet.name.startsWith("S08")) {
        setbackCount++;
        zOffset = 0;
    }
    return true;
}

void onPostPlayerInput() {
    if (awaitSetback) {
        client.setForward(0);
        client.setStrafe(0);
        client.setJump(false);
    }
}

void onRenderTick(float partialTicks) {
    if (awaitSetback) {
        if (hideProgress || text == null) {
            return;
        }
        render.text2d(text, disp[0] / 2 - width, disp[1] / 2 + 13, 1, -1, true);
    }
}

void onWorldJoin(Entity en) {
    if (en == client.getPlayer()) {
        long joinTime = client.time();
        if (awaitSetback) {
            client.print("&7[&dR&7] &cdisabling disabler");
            resetVars();
        }
        bridge.remove("disabler");
        awaitJoin = awaitGround = true;
    }
}

boolean isSkywars() {
    List<String> sidebar = world.getScoreboard();
    return sidebar != null && ((sidebar.size() > 0 && util.strip(sidebar.get(0)).contains("SKYWARS")) || (sidebar.size() > 8 && util.strip(sidebar.get(8)).contains("SkyWars")));
}

boolean isPit() {
    List<String> sidebar = world.getScoreboard();
    return sidebar != null && util.strip(sidebar.get(0)).contains("THE HYPIXEL PIT");
}
