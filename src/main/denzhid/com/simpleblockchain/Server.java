package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.service.MinerService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final int currentPort;
    private final int[] otherPorts;
    private static final String ADDRESS = "localhost";
    private final MinerService minerService;
    private ServerSocket serverSocket;

    public Server(int currentPort, int[] otherPorts) {
        this.currentPort = currentPort;
        this.otherPorts = otherPorts;
        this.minerService = new MinerService();
        try {
            this.serverSocket = new ServerSocket(currentPort);
            System.out.println("Node " + currentPort + ": " +  "Started");
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
                        new Client(currentPort, clientSocket, minerService).start();
                    }
                } finally {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        networkThread.start();
    }

    public void startMine() {
        Thread minerThread = new Thread(() -> {
            while (true) {
                if (minerService.getCurrentIndex() != 1) {
                    sendBlock();
                }
            }
        });
        minerThread.start();
    }

    public void sendBlock() {
        Block block = minerService.generateBlock();
        System.out.println("Node " + currentPort + ": " + "Mined block" + block);
        for (int port : otherPorts) {
            try (
                    Socket socket = new Socket(ADDRESS, port);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            socket.getOutputStream()
                    )
            ) {
                objectOutputStream.writeObject(block);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
