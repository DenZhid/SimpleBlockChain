package main.denzhid.com.simpleblockchain.service;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.model.block.BlockFactory;
import main.denzhid.com.simpleblockchain.service.utils.ValidateResults;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MinerService {
    private final BlockFactory blockFactory = new BlockFactory();
    private final AtomicBoolean isLagging = new AtomicBoolean(false);
    private final AtomicBoolean stopMining = new AtomicBoolean(false);
    private List<Block> chain = new CopyOnWriteArrayList<>();

    public synchronized Block generateBlock() {
        try {
            if (chain.isEmpty()) {
                return null;
            }
            Block previousBlock = chain.get(chain.size() - 1);
            Block newBlock = blockFactory.generateBlock(previousBlock.index() + 1, previousBlock.hash());
            addBlock(newBlock);
            return newBlock;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Block generateGenesis() {
        try {
            Block newBlock = blockFactory.generateBlock(1, null);
            addBlock(newBlock);
            return newBlock;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized ValidateResults validateBlock(Block block) {
        if (chain.isEmpty()) {
            addBlock(block);
            return ValidateResults.ADDED;
        }

        Block previousBlock = chain.get(chain.size() - 1);

        if (previousBlock.index() + 1 < block.index()) {
            if (isLagging.get()) {
                return ValidateResults.IGNORED;
            }
            isLagging.set(true);
            return ValidateResults.NEED_CHAIN;
        }

        if (!block.previousHash().equals(previousBlock.hash())
                || !block.hash().endsWith("0000")) {
            return ValidateResults.IGNORED;
        }

        addBlock(block);
        return ValidateResults.ADDED;
    }

    private void addBlock(Block block) {
        chain.add(block);
    }

    public synchronized boolean getIsLagging() {
        return isLagging.get();
    }

    public synchronized boolean getStopMining() {
        return stopMining.get();
    }

    public synchronized List<Block> getChain() {
        return chain;
    }

    public synchronized void setIsLagging(boolean isLagging) {
        this.isLagging.set(isLagging);
    }

    public synchronized void setStopMining(boolean stopMining) {
         this.stopMining.set(stopMining);
    }

    public synchronized  void setChain(List<Block> chain) {
        this.chain = new CopyOnWriteArrayList<>(chain);
    }
}
