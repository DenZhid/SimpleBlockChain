package main.denzhid.com.simpleblockchain.model.block;

import java.io.Serializable;

public record Block(int index, String previousHash, String data, String hash, long nonce) implements Serializable {

    @Override
    public String toString() {
        return "[index: " + index + " "
                + "previousHash: " + previousHash + " "
                + "hash: " + hash + " "
                + "nonce: " + nonce + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Block block = (Block) o;

        return (this.index == block.index
                && this.previousHash.equals(block.previousHash)
                && this.data.equals(block.data)
                && this.hash.equals(block.hash)
                && this.nonce == block.nonce);
    }
}
