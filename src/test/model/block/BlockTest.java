package model.block;

import main.denzhid.com.simpleblockchain.model.block.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BlockTest {
    Block firstBlock;
    Block copyOfFirstBlock;
    Block secondBlock;

    @BeforeEach
    void setUp() {
        firstBlock = new Block(1,
                "cdd7f0a091a95b0f51af70eea97727c1293fb7d872f0ab3956f43fe816d10000",
                "dataString",
                "dd2a35ddfda64ec49c36039cb005653e2817ab1d0651437db7866d5bf30c0000",
                12345);
        copyOfFirstBlock = firstBlock;
        secondBlock = new Block(2,
                "cdd7f0a091a95b0f51af70eea97727c1293fb7d872f0ab3956f43fe816d10000",
                "otherDataString",
                "dd2a35ddfda64ec67c36039cb2145653e2817ab1d0651437db7866d5bf30c0000",
                4324124);
    }

    @Test
    void testToString() {
        assertEquals("[index: 1 previousHash: cdd7f0a091a95b0f51af70eea97727c1293fb7d872f0ab3956f43fe816d10000 hash: dd2a35ddfda64ec49c36039cb005653e2817ab1d0651437db7866d5bf30c0000 nonce: 12345]",
                firstBlock.toString());
        assertEquals("[index: 2 previousHash: cdd7f0a091a95b0f51af70eea97727c1293fb7d872f0ab3956f43fe816d10000"
                        + " hash: dd2a35ddfda64ec67c36039cb2145653e2817ab1d0651437db7866d5bf30c0000 nonce: 4324124]",
                secondBlock.toString());
    }

    @Test
    void testEquals() {
        assertNotEquals(firstBlock, secondBlock);
        assertNotEquals(firstBlock, "Some string");
        assertEquals(firstBlock, firstBlock);
        assertEquals(firstBlock, copyOfFirstBlock);
    }
}