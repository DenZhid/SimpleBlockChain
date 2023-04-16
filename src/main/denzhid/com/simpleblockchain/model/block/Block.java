package main.denzhid.com.simpleblockchain.model.block;

import java.io.Serializable;

public record Block(long index, String previousHash, String data, String hash, long nonce) implements Serializable {

    @Override
    public String toString() {
        return "[index: " + index + " "
                + "previousHash:  " + previousHash + " "
                + "hash: " + hash + " "
                + "nonce: " + nonce + "]";
    }
}
