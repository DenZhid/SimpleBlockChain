package main.denzhid.com.simpleblockchain.http.model;

import main.denzhid.com.simpleblockchain.model.block.Block;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private final MethodType method;
    private final List<Block> body;
    private final int senderPort;
    private final int receiverPort;

    public Request(MethodType method, List<Block> body, int senderPort, int receiverPort) {
        this.method = method;
        this.body = body;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
    }

    public MethodType getMethod() {
        return method;
    }

    public List<Block> getBody() {
        return body;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public int getReceiverPort() {
        return receiverPort;
    }
}
