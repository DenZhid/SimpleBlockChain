package main.denzhid.com.simpleblockchain.model.block;

import java.io.Serializable;
import java.util.List;

public record BlockPack(Block[] blocks) implements Serializable {}
