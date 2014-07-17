package evilcraft.api.algorithms;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Location class.
 * @author rubensworks
 *
 */
public class Location implements ILocation {

    private int[] coordinates;
    
    /**
     * Make a new instance.
     * @param coordinates The coordinates.
     */
    public Location(int... coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
    public int getDimensions() {
        return this.coordinates.length;
    }

    @Override
    public int[] getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
	public Size getDifference(ILocation location) {
    	if(location.getDimensions() != this.getDimensions()) {
    		throw new IllegalArgumentException(
    				"The dimensions of this and the given dimension are not the same.");
    	}
    	int[] diffs = new int[getDimensions()];
    	for(int i = 0; i < diffs.length; i++) {
    		diffs[i] = Math.abs(location.getCoordinates()[i] - this.getCoordinates()[i]);
    	}
		return new Size(diffs);
	}
    
    @Override
    public int getDistance(ILocation location) {
    	Size difference = getDifference(location);
    	int distance = 0;
    	for(int i = 0; i < difference.getDimensions(); i++) {
    		distance += difference.getSizes()[i];
    	}
    	return (int) Math.pow(distance, 1 / difference.getDimensions());
    }

    @Override
    public ILocation copy() {
        return new Location(coordinates.clone());
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("[");
    	for(int coord : getCoordinates()) {
    		builder.append(coord);
    		builder.append(';');
    	}
    	builder.append("]");
    	return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
    	if(object instanceof ILocation && object != null) {
    		return ((ILocation) object).getCoordinates().equals(getCoordinates());
    	}
    	return false;
    }

	@Override
	public int compareTo(ILocation o) {
		if(getDimensions() != o.getDimensions()) {
			return getDimensions() - o.getDimensions();
		}
		int i = 0;
		while(i < getDimensions()) {
			if(getCoordinates()[i] != o.getCoordinates()[i]) {
				return getCoordinates()[i] - o.getCoordinates()[i];
			}
			i++;
		}
		return 0;
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("coordinates", getCoordinates());
		return tag;
	}

	@Override
	public void fromNBT(NBTTagCompound tag) {
		setCoordinates(tag.getIntArray("coordinates"));
	}

}
