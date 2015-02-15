package evilcraft.core.algorithm;

import evilcraft.api.ISize;

/**
 * Sizes class.
 * @author rubensworks
 *
 */
public class Size extends Location implements ISize {
	
	/**
	 * A null size in three dimensions.
	 */
	public static final Size NULL_SIZE = new Size(0, 0, 0);

	/**
	 * Make a new instance.
	 * @param sizes The sizes.
	 */
	public Size(int... sizes) {
		super(sizes);
	}
	
	@Override
    public int[] getSizes() {
        return super.getCoordinates();
    }

	@Override
    public void setSizes(int[] sizes) {
        super.setCoordinates(sizes);
    }

    @Override
    public Size copy() {
        return new Size(getSizes().clone());
    }

}
