package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.http.Server;

public class Main {
    public static void main(String[] args) {
        int[] hostsMock = {3000, 3001, 3002};

        Server firstNode = new Server(hostsMock[0], new int[]{hostsMock[1], hostsMock[2]});
        Server secondNode = new Server(hostsMock[1], new int[]{hostsMock[0], hostsMock[2]});
        Server thirdNode = new Server(hostsMock[2], new int[]{hostsMock[0], hostsMock[1]});

        firstNode.start();
        secondNode.start();
        thirdNode.start();

        firstNode.sendBlock();

        firstNode.startMine();
        secondNode.startMine();
        thirdNode.startMine();
    }
}
