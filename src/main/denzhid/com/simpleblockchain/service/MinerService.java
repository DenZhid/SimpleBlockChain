package main.denzhid.com.simpleblockchain.service;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.model.block.BlockFactory;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MinerService {
    private static final BlockFactory blockFactory = new BlockFactory();
    private long currentIndex = 1;
    private String previousHash = null;
    private final List<Block> currentChain = new ArrayList<>();

    public Block generateBlock() {
        try {
            Block newBlock = blockFactory.generateBlock(currentIndex, previousHash);
            this.currentIndex = newBlock.index();
            this.previousHash = newBlock.hash();
            currentChain.add(newBlock);
            return newBlock;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Block> getCurrentChain() {
        return currentChain;
    }
}
