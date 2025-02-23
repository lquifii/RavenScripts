/* 
made by Ihasedich
improved by itsjsbh
*/
String ign;

void onLoad() {
    modules.enable(scriptName);
}

boolean onPacketSent(CPacket packet) {
    if (packet instanceof C01) {
        C01 c01 = (C01) packet;
        String[] parts = c01.message.split(" ");

        if (c01.message.startsWith(".ign")) {
            Entity player = client.getPlayer();
            String displayName = player.getDisplayName();
            client.print("Your Ign is: " + displayName);
            return false;
        }

        switch (c01.message) {
            case "/1S":
                client.chat("/play bedwars_eight_one");
                return false;
            case "/2S":
                client.chat("/play bedwars_eight_two");
                return false;
            case "/3S":
                client.chat("/play bedwars_four_three");
                return false;
            case "/4S":
                client.chat("/play bedwars_four_four");
                return false;
            case "/1s":
                client.chat("/play bedwars_eight_one");
                return false;
            case "/2s":
                client.chat("/play bedwars_eight_two");
                return false;
            case "/3s":
                client.chat("/play bedwars_four_three");
                return false;
            case "/4s":
                client.chat("/play bedwars_four_four");
                return false;
            case "/sw":
                client.chat("/play solo_normal");
                return false;    
            case "/rq":
                client.chat("/play bedwars_four_four");
                return false;
            case "/rq1":
                client.chat("/play bedwars_eight_one");
                return false;
            case "/rq2":
                client.chat("/play bedwars_eight_two");
                return false;
            case "/rq3":
                client.chat("/play bedwars_four_three");
                return false;
            case "/RQ":
                client.chat("/play bedwars_four_four");
                return false;
            case "/RQ1":
                client.chat("/play bedwars_eight_one");
                return false; 
            case "/RQ2":
                client.chat("/play bedwars_eight_two");
                return false;
            case "/RQ3":
                client.chat("/play bedwars_four_three");
                return false;
        }     

        if (parts.length > 1) {
            ign = parts[1];
        } else {
            return true;
        }

        if (c01.message.startsWith(".friend") || c01.message.startsWith(".f")) {
            if (client.isFriend(ign)) {
                client.removeFriend(ign);
            } else {
                client.addFriend(ign);
            }
            return false;
        } else if (c01.message.startsWith(".enemy") || c01.message.startsWith(".e")) {
            if (client.isEnemy(ign)) {
                client.removeEnemy(ign);
            } else {
                client.addEnemy(ign);
            }
            return false;
        }
    }
    return true;
}

