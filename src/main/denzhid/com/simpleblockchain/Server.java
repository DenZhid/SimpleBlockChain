package main.denzhid.com.simpleblockchain;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.service.MinerService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

public class Server extends Thread {
    private static final MinerService minerService = new MinerService();
    private static final int THREAD_CORE_POOL_CAPACITY = 2;
    private static final int THREAD_POOL_CAPACITY = 4;
    private static final long KEEP_ALIVE_TIME = 60;
    private static final ThreadPoolExecutor executors = new ThreadPoolExecutor(
            THREAD_CORE_POOL_CAPACITY,
            THREAD_POOL_CAPACITY,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );
    private static final String HOST_NAME = "localhost";
    private final int currentHost;
    private int[] hostsInNet;
    private final Client client;

    public Server(int currentHost, int[] hostsInNet) {
        this.currentHost = currentHost;
        this.hostsInNet = hostsInNet;
        this.client = new Client();
        System.out.println("Node" + currentHost + "is starting");
    }

    public void sendBlock() {
        Block block = minerService.generateBlock();
        for (int port : hostsInNet) {
            executors.execute(() -> {
                try (
                        Socket socket = new Socket(HOST_NAME, port);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
                ) {
                    objectOutputStream.writeObject(block);
                } catch (UnknownHostException e) {
                    //System.out.println("UnknownHostException");
                } catch (IOException e) {
                    //System.out.println("IOException");
                }
            });
        }
    }

    public void generateFirstBlock() {
        Block block = minerService.generateBlock();
        for (int port : hostsInNet) {
            try (
                    Socket socket = new Socket(HOST_NAME, port);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
            ) {
                System.out.println(block);
                objectOutputStream.writeObject(block);
                System.out.println("Send first object");
            } catch (UnknownHostException e) {
                //System.out.println("UnknownHostException");
            } catch (IOException e) {
                //System.out.println("IOException");
            }
        }
    }

        @Override
        public void start () {
            while (true) {
                sendBlock();
            }
        }
    }
