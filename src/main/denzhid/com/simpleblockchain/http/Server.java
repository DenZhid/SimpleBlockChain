package main.denzhid.com.simpleblockchain.http;

import main.denzhid.com.simpleblockchain.http.model.MethodType;
import main.denzhid.com.simpleblockchain.http.model.Request;
import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.service.MinerService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server extends Thread {
    private final String currentNodeAddress;
    private final String[] otherNodesInCluster;
    private final MinerService minerService;
    private ServerSocket serverSocket;

    public Server(String currentNodeAddress, String[] otherNodesInCluster) {
        this.currentNodeAddress = currentNodeAddress;
        this.otherNodesInCluster = otherNodesInCluster;
        this.minerService = new MinerService();
        try {
            String[] addressParts = divideAddress(currentNodeAddress);
            this.serverSocket = new ServerSocket(Integer.parseInt(addressParts[1]));
            System.out.println("Node " + currentNodeAddress + ": " + "Started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        Thread networkThread = new Thread(() -> {
            try {
                try {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        new Client(currentNodeAddress, minerService, this, clientSocket).start();
                    }
                } finally {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Node " + currentNodeAddress + ": " + " Network Thread");
        networkThread.start();
    }

    public void startMine() {
        Thread minerThread = new Thread(() -> {
            while (true) {
                if (!minerService.getIsLagging() && !minerService.getStopMining() && !minerService.getChain().isEmpty()) {
                    sendBlock(minerService.generateBlock());
                }
            }
        }, "Node " + currentNodeAddress + ": " + " Mine Thread");
        minerThread.start();
    }

    public void sendBlock(Block block) {
        System.out.println("Node " + currentNodeAddress + ": " + "Mined block" + block);
        for (String receiverAddress : otherNodesInCluster) {
            String[] addressParts = divideAddress(receiverAddress);
            try (
                    Socket clientSocket = new Socket(addressParts[0], Integer.parseInt(addressParts[1]));
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                out.writeObject(new Request(MethodType.POST, List.of(block), currentNodeAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void askForChainPart(String receiverAddress) {
        String[] addressParts = divideAddress(receiverAddress);
        try (
                Socket clientSocket = new Socket(addressParts[0], Integer.parseInt(addressParts[1]));
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            out.writeObject(new Request(MethodType.GET, null, currentNodeAddress));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Node " + currentNodeAddress + ": " + "Asked Node " + receiverAddress + " for chain part");
    }

    public void sendChain(String receiverAddress) {
        String[] addressParts = divideAddress(receiverAddress);
        List<Block> chain = minerService.getChain();
        try (
                Socket clientSocket = new Socket(addressParts[0], Integer.parseInt(addressParts[1]));
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            out.writeObject(new Request(MethodType.POST, chain, currentNodeAddress));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Первая часть - IP-адрес, вторая часть - порт
    private String[] divideAddress(String address) {
        return address.split(":");
    }

    public MinerService getMinerService() {
        return minerService;
    }
}
