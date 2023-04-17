package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.http.Server;

public class Main {
    public static void main(String[] args) {
        String currentNodeAddress = System.getenv("CURRENT_NODE_ADDRESS");
        String[] otherNodesInCluster = System.getenv("NODES_IN_CLUSTER_ADDRESSES").split(",");
        boolean isGenesis = (System.getenv("IS_GENESIS").equals("TRUE"));

        Server node = new Server(currentNodeAddress, otherNodesInCluster);
        node.start();
        if (isGenesis) {
            node.sendBlock(node.getMinerService().generateGenesis());
        }
        node.startMine();
    }
}
