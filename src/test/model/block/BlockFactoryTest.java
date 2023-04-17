package model.block;

import main.denzhid.com.simpleblockchain.model.block.BlockFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.denzhid.com.simpleblockchain.model.block.Block;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockFactoryTest {
    static BlockFactory generator;
    static Random randomizer;

    @BeforeAll
    static void setUp() {
        generator = new BlockFactory();
        randomizer = new Random();
    }

    @Test
    void generateGenesis() throws NoSuchAlgorithmException {
        Block genesisBlock = generator.generateBlock(1, null);
        assertNotNull(genesisBlock);
        assertNotNull(genesisBlock.hash());
        assertTrue(genesisBlock.hash().endsWith("0000"));
        assertNull(genesisBlock.previousHash());
        assertEquals(1, genesisBlock.index());
    }

    @Test
    void generateBlock() throws NoSuchAlgorithmException {
        String previousHash = "dd2a35ddfda64ec49c36039cb005653e2817ab1d0651437db7866d5bf30c0000";
        int index = randomizer.nextInt();
        Block block = generator.generateBlock(index, previousHash);
        assertNotNull(block);
        assertNotNull(block.hash());
        assertTrue(block.hash().endsWith("0000"));
        assertEquals(previousHash, block.previousHash());
        assertEquals(index, block.index());
    }
}