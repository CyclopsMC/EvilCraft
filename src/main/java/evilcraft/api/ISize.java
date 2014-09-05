package evilcraft.api;

/**
 * Size interface.
 * @author rubensworks
 *
 */
public interface ISize extends ILocation {

	/**
     * Get the sizes for this size.
     * @return An array of sizes of the dimension for this size.
     */
    public int[] getSizes();
    
    /**
     * Set the sizes for this size.
     * @param sizes The sizes.
     */
    public void setSizes(int[] sizes);
	
}
