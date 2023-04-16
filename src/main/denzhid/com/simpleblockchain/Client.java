package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.service.MinerService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client extends Thread {
    private final int currentPort;
    private final Socket clientSocket;
    private final MinerService minerService;

    public Client(int currentPort, Socket clientSocket, MinerService minerService) {
        this.currentPort = currentPort;
        this.clientSocket = clientSocket;
        this.minerService = minerService;
    }

    @Override
    public void start() {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof Block block) {
                if (minerService.validateBlock(block)) {
                    System.out.println("Node " + currentPort + ": " + "Recieved block: " + block);
                } else {
                    System.out.println("Node " + currentPort + ": " + "Block: " + block + " was ignored");
                }
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
