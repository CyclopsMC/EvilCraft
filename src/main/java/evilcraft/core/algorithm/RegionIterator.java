package evilcraft.core.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import evilcraft.api.ILocation;

/**
 * An iterator for an N-dimensional region defined by a location in that space with an offset in each direction.
 * @author rubensworks
 *
 */
public class RegionIterator implements Iterator<ILocation> {

	private ILocation center;
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
	public RegionIterator(ILocation center, int offset, boolean shuffle) {
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
		long seed = 0;
		for(int c : center.getCoordinates()) {
			seed += c;
		}
		return seed;
	}
	
	/**
	 * Make a new instance without shuffling the locations.
	 * @param center The center.
	 * @param offset The offset.
	 */
	public RegionIterator(ILocation center, int offset) {
		this(center, offset, false);
	}
	
	private int getArea() {
		return 2 * offset + 1;
	}
	
	private void addTickOffset(ILocation center) {
		int tick = shuffledTicks.get(loopBlockTick);
		for(int i = 0; i < center.getDimensions(); i++) {
			int mod = (int) Math.pow(getArea(), i + 1);
			int prevMod = (int) Math.pow(getArea(), i);
			center.getCoordinates()[i] += -offset
					+ ((tick % mod) - (tick % prevMod)) / prevMod;
			
		}
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public ILocation next() {
		loopBlockTick = (loopBlockTick + 1) % maxTick;
		ILocation next = center.copy();
		addTickOffset(next);
		return next;
	}

	@Override
	public void remove() {
		// Do nothing.
	}

}
