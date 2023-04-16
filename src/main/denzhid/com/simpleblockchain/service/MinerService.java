package main.denzhid.com.simpleblockchain.service;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.model.block.BlockFactory;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MinerService {
    private final BlockFactory blockFactory = new BlockFactory();
    private final List<Block> chain = new LinkedList<>();
    private long currentIndex = 1;
    private String previousHash = null;

    public synchronized Block generateBlock() {
        try {
            Block newBlock = blockFactory.generateBlock(currentIndex, previousHash);
            addBlock(newBlock);
            return newBlock;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized boolean validateBlock(Block block) {
        if (
                (previousHash != null && !block.previousHash().equals(previousHash))
                        || block.index() != currentIndex
                        || !block.hash().endsWith("0000")
        ) {
            return false;
        }

        addBlock(block);
        return true;
    }

    private void addBlock(Block block) {
        chain.add(block);
        this.currentIndex++;
        this.previousHash = block.hash();
    }

    public synchronized List<Block> getChain() {
        return chain;
    }

    public synchronized long getCurrentIndex() {
        return currentIndex;
    }
}
