package main.denzhid.com.simpleblockchain.model;

public class Block {
    private final long index;
    private final String previousHash;
    private final String data;
    private String hash;
    private String nonce;

    public Block(long index, String previousHash, String data, String hash, String nonce) {
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

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
