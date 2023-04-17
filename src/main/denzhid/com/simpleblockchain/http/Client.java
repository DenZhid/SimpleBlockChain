package main.denzhid.com.simpleblockchain.http;

import main.denzhid.com.simpleblockchain.http.model.Request;
import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.service.MinerService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class Client extends Thread {
    private final int currentPort;
    private final MinerService minerService;
    private final Server currentServer;
    private final Socket clientSocket;

    public Client(int currentPort, MinerService minerService, Server currentServer, Socket clientSocket) {
        super("Node " + currentPort + ": " + " Client Thread");
        this.currentPort = currentPort;
        this.minerService = minerService;
        this.currentServer = currentServer;
        this.clientSocket = clientSocket;
    }

    @Override
    public void start() {
        try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            Object obj = in.readObject();
            if (obj instanceof Request request) {
                switch (request.getMethod()) {

                    case GET -> {
                        minerService.setStopMining(true);
                        currentServer.sendChain(request.getSenderPort());
                        System.out.println("Node "
                                + currentPort
                                + ": "
                                + "Send chain part to Node "
                                + request.getSenderPort()
                        );
                        minerService.setStopMining(false);
                    }

                    case POST -> {
                        List<Block> body = request.getBody();
                        if (body.size() == 1) {
                            minerService.setStopMining(true);
                            // POST BLOCK
                            Block block = body.get(0);
                            System.out.println("Node "
                                    + currentPort
                                    + ": "
                                    + "Received block: "
                                    + block
                                    + " from Node "
                                    + request.getSenderPort());
                            switch (minerService.validateBlock(block)) {
                                case ADDED -> System.out.println("Node " + currentPort + ": " + "Added block");
                                case IGNORED -> System.out.println("Node " + currentPort + ": " + "Ignored block");
                                case NEED_CHAIN -> currentServer.askForChainPart(request.getSenderPort());
                            }
                            minerService.setStopMining(false);
                            return;
                        }

                        // POST CHAIN_PART
                        minerService.setStopMining(true);
                        minerService.setChain(body);
                        minerService.setIsLagging(false);
                        System.out.println("Node "
                                + currentPort
                                + ": "
                                + "Received chain part" + " from Node "
                                + request.getSenderPort());
                        minerService.setStopMining(false);
                    }
                }
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
