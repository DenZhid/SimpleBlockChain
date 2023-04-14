package main.denzhid.com.simpleblockchain.model.block;

public record Block(long index, String previousHash, String data, String hash, long nonce) {
}
