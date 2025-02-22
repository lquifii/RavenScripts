// made this script in like 2 minutes so its lots of shit code but ye.

boolean myauTowering = false; 
boolean myauKeepying = true;

void onEnable() {
    client.chat(".scaffold airmotion 80");
}

void onPreUpdate() {
    Entity player = client.getPlayer();
    if (keybinds.isPressed("jump")) {
        if (!myauTowering && player.onGround() && keybinds.isPressed("jump")) {
            client.chat(".scaffold air-motion 100");

            myauTowering = true;
        }
    } else {
        myauTowering = false;
    }
    if (!keybinds.isPressed("jump")) {
        if (!myauKeepying && !myauTowering && client.isMoving()) {
            client.chat(".scaffold air-motion 80");

            myauKeepying = true;
        }
    } else {
        myauKeepying = false;
    }
} 