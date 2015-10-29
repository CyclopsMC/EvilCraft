package org.cyclops.evilcraft.core.block;

import net.minecraft.block.Block;

/**
 * Holder class for the allowed blocks in a structure.
 * @author rubensworks
 *
 */
public class AllowedBlock {
	
	private Block block;
	private int maxOccurences = -1;
	private int exactOccurences = -1;
	
	/**
	 * Make a new instance.
	 * @param block The allowed block.
	 */
	public AllowedBlock(Block block) {
		this.block = block;
	}
	
	/**
	 * Set the maximum occurences.
	 * @param maxOccurences The maximum amount of times this blockState should occur.
	 * A negative value here will ignore this condition.
	 * @return This instance.
	 */
	public AllowedBlock setMaxOccurences(int maxOccurences) {
		this.maxOccurences = maxOccurences;
		if(exactOccurences != -1) {
			throw new IllegalStateException("Can not set both maximum and exact occurences.");
		}
		return this;
	}

	/**
	 * Set the exact required occurences.
	 * @param exactOccurences The exact amount of times this block should occur.
	 * A negative value here will ignore this condition.
	 * @return This instance.
	 */
	public AllowedBlock setExactOccurences(int exactOccurences) {
		this.exactOccurences = exactOccurences;
		if(maxOccurences != -1) {
			throw new IllegalStateException("Can not set both maximum and exact occurences.");
		}
		return this;
	}

	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * @return the maxOccurences
	 */
	public int getMaxOccurences() {
		return maxOccurences;
	}

	/**
	 * @return the exactOccurences
	 */
	public int getExactOccurences() {
		return exactOccurences;
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof AllowedBlock && getBlock().equals(((AllowedBlock)object).getBlock());
	}
	
}