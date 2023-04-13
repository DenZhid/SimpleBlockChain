package main.denzhid.com.simpleblockchain.model;

public class Block {
    private final long index;
    private final String previousHash;
    private final String data;
    private String hash;
    private long nonce;

    public Block(long index, String previousHash, String data, String hash, long nonce) {
        this.index = index;
        this.previousHash = previousHash;
        this.data = data;
        this.hash = hash;
        this.nonce = nonce;
    }

    public long getIndex() {
        return index;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public long getNonce() {
        return nonce;
    }
}
