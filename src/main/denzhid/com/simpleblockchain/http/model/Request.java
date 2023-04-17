package main.denzhid.com.simpleblockchain.http.model;

import main.denzhid.com.simpleblockchain.model.block.Block;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private final MethodType method;
    private final List<Block> body;
    private final String senderAddress;

    public Request(MethodType method, List<Block> body, String senderAddress) {
        this.method = method;
        this.body = body;
        this.senderAddress = senderAddress;
    }

    public MethodType getMethod() {
        return method;
    }

    public List<Block> getBody() {
        return body;
    }

    public String getSenderAddress() {
        return senderAddress;
    }
}
