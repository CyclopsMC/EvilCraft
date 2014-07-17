package evilcraft.api.algorithms;

/**
 * Sizes class.
 * @author rubensworks
 *
 */
public class Size extends Location {
	
	/**
	 * A null size in three dimensions.
	 */
	public static final Size NULL_SIZE = new Size(new int[]{0, 0, 0});

	/**
	 * Make a new instance.
	 * @param sizes The sizes.
	 */
	public Size(int[] sizes) {
		super(sizes);
	}
	
	/**
     * Get the sizes for this size.
     * @return An array of sizes of the dimension for this size.
     */
    public int[] getSizes() {
        return super.getCoordinates();
    }

	/**
     * Set the sizes for this size.
     * @param sizes The sizes.
     */
    public void setSizes(int[] sizes) {
        super.setCoordinates(sizes);
    }

    @Override
    public Size copy() {
        return new Size(getSizes().clone());
    }

}
