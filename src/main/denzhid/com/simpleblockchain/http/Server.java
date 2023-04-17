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
    private static final String ADDRESS = "localhost";
    private final int currentPort;
    private final int[] otherPorts;
    private final MinerService minerService;
    private ServerSocket serverSocket;

    public Server(int currentPort, int[] otherPorts) {
        this.currentPort = currentPort;
        this.otherPorts = otherPorts;
        this.minerService = new MinerService();
        try {
            this.serverSocket = new ServerSocket(currentPort);
            System.out.println("Node " + currentPort + ": " + "Started");
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
                        new Client(currentPort, minerService, this, clientSocket).start();
                    }
                } finally {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Node " + currentPort + ": " + " Network Thread");
        networkThread.start();
    }

    public void startMine() {
        Thread minerThread = new Thread(() -> {
            while (true) {
                if (!minerService.getIsLagging() && minerService.getCurrentIndex() != 1) {
                    sendBlock();
                }
            }
        }, "Node " + currentPort + ": " + " Mine Thread");
        minerThread.start();
    }

    public void sendBlock() {
        Block block = minerService.generateBlock();
        System.out.println("Node " + currentPort + ": " + "Mined block" + block);
        for (int receiverPort : otherPorts) {
            try (
                    Socket clientSocket = new Socket(ADDRESS, receiverPort);
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                out.writeObject(new Request(MethodType.POST, List.of(block), currentPort, receiverPort));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void askForChainPart(int receiverPort) {
        try (
                Socket clientSocket = new Socket(ADDRESS, receiverPort);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            out.writeObject(new Request(MethodType.GET, null, currentPort, receiverPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Node " + currentPort + ": " + "Asked Node " + receiverPort + " for chain part");
    }

    public void sendChain(int receiverPort) {
        List<Block> chain = minerService.getChain();
        for (int port : otherPorts) {
            try (
                    Socket clientSocket = new Socket(ADDRESS, port);
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                out.writeObject(new Request(MethodType.POST, chain, currentPort, receiverPort));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
