package main.denzhid.com.simpleblockchain.model;

public record Block(long index, String previousHash, String data, String hash, long nonce) {
}
