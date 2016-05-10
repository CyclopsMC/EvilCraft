package org.cyclops.evilcraft.core.algorithm;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * An iterator for an N-dimensional region defined by a location in that space with an offset in each direction.
 * @author rubensworks
 *
 */
public class RegionIterator implements Iterator<BlockPos> {

	private BlockPos center;
	private int offset;
	private boolean shuffle;
	
	private int loopBlockTick;
	private int maxTick;
	private ArrayList<Integer> shuffledTicks;
	
	/**
	 * Make a new instance.
	 * @param center The center.
	 * @param offset The offset.
	 * @param shuffle If the resulting locations from the iterator should deterministicly be shuffled.
	 */
	public RegionIterator(BlockPos center, int offset, boolean shuffle) {
		this.center = center;
		this.offset = offset;
		this.shuffle = shuffle;
		
		this.maxTick = (int) Math.pow(getArea(), 3);
		this.shuffledTicks = Lists.newArrayList(ContiguousSet.create(Range.closed(0, maxTick), DiscreteDomain.integers()).asList());
		
		if(this.shuffle) {
			Random random = new Random();
			random.setSeed(getShuffleSeed());
			Collections.shuffle(shuffledTicks, random);
		}
	}
	
	private long getShuffleSeed() {
		return center.toLong();
	}
	
	/**
	 * Make a new instance without shuffling the locations.
	 * @param center The center.
	 * @param offset The offset.
	 */
	public RegionIterator(BlockPos center, int offset) {
		this(center, offset, false);
	}
	
	private int getArea() {
		return 2 * offset + 1;
	}
	
	private BlockPos addTickOffset(BlockPos center) {
		int tick = shuffledTicks.get(loopBlockTick);
        int[] c = new int[3];
		for(int i = 0; i < 3; i++) {
			int mod = (int) Math.pow(getArea(), i + 1);
			int prevMod = (int) Math.pow(getArea(), i);
			c[i] = -offset
					+ ((tick % mod) - (tick % prevMod)) / prevMod;
		}
		return center.add(c[0], c[1], c[2]);
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public BlockPos next() {
		loopBlockTick = (loopBlockTick + 1) % maxTick;
		BlockPos next = new BlockPos(center.getX(), center.getY(), center.getZ());
		return addTickOffset(next);
	}

	@Override
	public void remove() {
		// Do nothing.
	}

}
