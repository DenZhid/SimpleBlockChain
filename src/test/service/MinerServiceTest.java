package service;

import main.denzhid.com.simpleblockchain.model.block.Block;
import main.denzhid.com.simpleblockchain.service.MinerService;
import main.denzhid.com.simpleblockchain.service.utils.ValidateResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

class MinerServiceTest {
    private static final int NUMBER_OF_ITERATIONS = 3;
    MinerService minerService;

    @BeforeEach
    void setUp() {
        minerService = new MinerService();
    }

    @Test
    void generateGenesis() {
        List<Block> chain = minerService.getChain();

        Block gensisBlock = chain.get(0);

        // GenesisBlock test
        assertNotNull(gensisBlock);
        assertNotNull(gensisBlock.hash());
        assertTrue(gensisBlock.hash().endsWith("0000"));
        assertNull(gensisBlock.previousHash());
        assertEquals(1, gensisBlock.index());
    }


    @Test
    void generateSeveralBlocks() {
        minerService.generateGenesis();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            List<Block> chain = minerService.getChain();
            Block lastBlock = chain.get(chain.size() - 1);

            Block newBlock = minerService.generateBlock();
            chain = minerService.getChain();

            Block blockFromChain = chain.get(chain.size() - 1);

            // Chain test
            assertFalse(chain.isEmpty());
            assertEquals(blockFromChain, newBlock);

            // Block test
            assertNotNull(blockFromChain);
            assertNotNull(blockFromChain.hash());
            assertTrue(blockFromChain.hash().endsWith("0000"));
            assertEquals(lastBlock.hash(), blockFromChain.previousHash());
            assertEquals(i + 2, blockFromChain.index());
        }
    }

    @Test
    void validateAndIgnoreBlock() {
        Block newBlock = new Block(2,
                "cdd7f0a091a95b0f51af70eea97727c1293fb7d872f0ab3956f43fe816d10000",
                "dataString",
                "dd2a35ddfda64ec49c36039cb005653e2817ab1d0651437db7866d5bf30c0000",
                12345);
        minerService.generateGenesis();
        minerService.generateBlock();
        minerService.generateBlock();

        assertEquals(ValidateResults.IGNORED, minerService.validateBlock(newBlock));
    }

    @Test
    void validateAndAddBlock() {
        Block genesis = minerService.generateGenesis();

        Block newBlock = new Block(2,
                genesis.hash(),
                "dataString",
                "dd2a35ddfda64ec49c36039cb005653e2817ab1d0651437db7866d5bf30c0000",
                12345);
        assertEquals(ValidateResults.ADDED, minerService.validateBlock(newBlock));
    }

    @Test
    void validateAskChainBlock() {
        Block newBlock = new Block(11,
                "cdd7f0a091a95b0f51af70eea97727c1293fb7d872f0ab3956f43fe816d10000",
                "dataString",
                "dd2a35ddfda64ec49c36039cb005653e2817ab1d0651437db7866d5bf30c0000",
                12345);
        minerService.generateGenesis();
        minerService.generateBlock();

        assertEquals(ValidateResults.NEED_CHAIN, minerService.validateBlock(newBlock));
    }
}