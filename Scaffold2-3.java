int rotTick = 0;

void onPreUpdate() {
    modules.setSlider("Scaffold", "Fast scaffold", 6);
    modules.setSlider("Scaffold", "Fast scaffold motion", 0.94);
    modules.setSlider("Scaffold", "Rotation", 0);
    modules.setButton("Scaffold", "Highlight blocks", true);
    modules.setButton("Scaffold", "Silent swing", true);
    client.setSprinting(true);
    if (keybinds.isPressed("jump")) {
        modules.setSlider("Scaffold", "Fast scaffold", 1);
    }
}
    

void onPreMotion(PlayerState state) {
    Entity player = client.getPlayer();
    boolean towering = modules.isTowering();
    
    if (client.isMoving()) {
        if (player.onGround() && keybinds.isMouseDown(1) && !towering ) {
            client.jump();
        }
           
    if (modules.isEnabled("Scaffold") && player.isHoldingBlock() && client.isMoving()) {
        boolean w = keybinds.isPressed("forward");
        boolean a = keybinds.isPressed("left");
        boolean s = keybinds.isPressed("back");
        boolean d = keybinds.isPressed("right");
        boolean space = keybinds.isPressed("jump");

        if (rotTick <= 30) {
            if (w && a && s && d || w && !a && !s && !d || w && !a && s && !d) {
                state.yaw = player.getYaw() - 137;
            } else if (w && !a && !s && d) {
                state.yaw = player.getYaw() - -185;
            } else if (w && a && !s && !d) {
                state.yaw = player.getYaw() - 185;
            } else if (!w && a && !s && !d) {
                state.yaw = player.getYaw() - 270;
            } else if (!w && a && s && !d) {
                state.yaw = player.getYaw() - 310;
            } else if (!w && !a && !s && d) {
                state.yaw = player.getYaw() - 90;
            } else if (!w && !a && s && d) {
                state.yaw = player.getYaw() - 50;
            }
        }
        if (rotTick == 30) {
            state.yaw = -player.getYaw();
        }
        if (rotTick >= 31) {
            rotTick = 0;
        }}
        state.pitch = 80;    
    }}
    
void onEnable() {
    rotTick = 0;
    modules.enable("Scaffold");
}

void onDisable() {
    modules.disable("Scaffold");
}