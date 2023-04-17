package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.http.Server;

public class Main {
    public static void main(String[] args) {
        int port = -1;
        int[] otherPorts = new int[]{-1, -1};
        boolean isGenesis = false;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p" -> port = Integer.parseInt(args[++i]);
                case "-o" -> {
                    String[] arguments = args[++i].split(",");
                    otherPorts = new int[]{Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1])};
                }
                case "-g" -> isGenesis = true;
            }
        }

        if (port < 0 || otherPorts[0] < 0 || otherPorts[1] < 0) {
            System.out.println("Configuration hasn't been set");
            System.exit(1);
        }

        Server node = new Server(port, otherPorts);
        node.start();
        if (isGenesis) {
            node.sendBlock(node.getMinerService().generateGenesis());
        }
        node.startMine();
    }
}
