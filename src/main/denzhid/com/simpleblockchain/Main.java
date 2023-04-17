package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.http.Server;

public class Main {
    public static void main(String[] args) {
        String currentNodeAddress = "";
        String[] otherNodesInCluster = new String[]{"", ""};
        boolean isGenesis = false;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p" -> currentNodeAddress = args[++i];
                case "-o" -> {
                    String[] addresses = args[++i].split(",");
                    otherNodesInCluster = new String[]{addresses[0], addresses[1]};
                }
                case "-g" -> isGenesis = true;
            }
        }

        if (currentNodeAddress.isEmpty() || otherNodesInCluster[0].isEmpty() || otherNodesInCluster[1].isEmpty()) {
            System.out.println("Configuration hasn't been set");
            System.exit(1);
        }

        Server node = new Server(currentNodeAddress, otherNodesInCluster);
        node.start();
        if (isGenesis) {
            node.sendBlock(node.getMinerService().generateGenesis());
        }
        node.startMine();
    }
}
