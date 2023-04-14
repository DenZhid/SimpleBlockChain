package main.denzhid.com.simpleblockchain.model.block;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class BlockFactory {
    private static final Random RANDOMIZER = new Random();
    private static final String ALGORITHM = "SHA-256";

    public Block generateBlock(long index, String previousHash) throws NoSuchAlgorithmException {
        long nonce = 0L;
        String data = generateRandomString();

        String hash = countHash(index, previousHash, data, nonce);
        while (!hash.endsWith("0000")) {
            nonce++;
            hash = countHash(index, previousHash, data, nonce);
        }
        return new Block(index, previousHash, data, hash, nonce);
    }

    private String generateRandomString() {
        byte[] array = new byte[256];
        RANDOMIZER.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    private String countHash(
            long index,
            String previousHash,
            String data,
            long nonce
    ) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        return bytesToHexConverter(digest.digest((index + previousHash + data + nonce).getBytes(StandardCharsets.UTF_8))
        );
    }

    private String bytesToHexConverter(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
