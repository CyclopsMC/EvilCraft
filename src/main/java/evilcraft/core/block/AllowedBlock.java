package evilcraft.core.block;

import net.minecraft.block.state.IBlockState;

/**
 * Holder class for the allowed blocks in a structure.
 * @author rubensworks
 *
 */
public class AllowedBlock {
	
	private IBlockState blockState;
	private int maxOccurences = -1;
	
	/**
	 * Make a new instance.
	 * @param blockState The allowed blockState.
	 */
	public AllowedBlock(IBlockState blockState) {
		this.blockState = blockState;
	}
	
	/**
	 * Set the maximum occurences.
	 * @param maxOccurences The maximum amount of times this blockState should occur.
	 * A negative value here will ignore this condition.
	 * @return This instance.
	 */
	public AllowedBlock setMaxOccurences(int maxOccurences) {
		this.maxOccurences = maxOccurences;
		return this;
	}

	/**
	 * @return the blockState
	 */
	public IBlockState getBlock() {
		return blockState;
	}

	/**
	 * @return the maxOccurences
	 */
	public int getMaxOccurences() {
		return maxOccurences;
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof AllowedBlock && getBlock().equals(((AllowedBlock)object).getBlock());
	}
	
}