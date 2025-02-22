// code by lquifi, made less shit code by someone in myau discord i dont remember who xD

boolean myauTowering = false; 
boolean myauKeepying = true;

void onEnable() {
    client.chat(".scaffold airmotion 82");
}

void onPreUpdate() {
    Entity player = client.getPlayer();
    if (keybinds.isPressed("jump")) {
        myauKeepying = false;

        if (!myauTowering && player.onGround()) {
            client.chat(".scaffold air-motion 110");

            myauTowering = true;
        }
    } else {
        myauTowering = false;

        if (!myauKeepying && client.isMoving()) {
            client.chat(".scaffold air-motion 82"); 

            myauKeepying = true;
        }
    }
}
