package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Entity for the dark stick item.
 * @author rubensworks
 *
 */
public class EntityItemDarkStick extends EntityItemDefinedRotation {

	private static final int WATCHERID_VALID = 20;
	private static final int WATCHERID_ANGLE = 21;

    private double lastPosX = -1;
    private double lastPosY = -1;
    private double lastPosZ = -1;
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param original The original entity item.
	 */
	public EntityItemDarkStick(World world, EntityItem original) {
		super(world, original);
	}

    /**
     * Make a new instance.
     * @param world The world.
     */
    public EntityItemDarkStick(World world) {
        super(world);
    }

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     */
    public EntityItemDarkStick(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    /**
     * Make a new instance.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack The item stack.
     */
    public EntityItemDarkStick(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }

	@Override
	public void entityInit() {
		super.entityInit();
        Float angle = MinecraftHelpers.isClientSide() ? null : loadRotation();
		this.dataWatcher.addObject(WATCHERID_VALID, angle != null ? 1 : 0);
		this.dataWatcher.addObject(WATCHERID_ANGLE, angle == null ? 0 : angle);
	}

    protected boolean hasMoved() {
        boolean moved = Math.abs(lastPosX - posX) > 0.1F || Math.abs(lastPosZ - posZ) > 0.1F;
        lastPosX = posX;
        lastPosY = posY;
        lastPosZ = posZ;
        return moved;
    }

	@Override
	public void onEntityUpdate() {
        super.onEntityUpdate();
		if(!MinecraftHelpers.isClientSide() && hasMoved()) {
            Float angle = loadRotation();
            setValid(angle != null);
			setAngle(angle == null ? 0 : angle);
		}
	}
	
	private Float loadRotation() {
		/*BlockPos closest = DarkTempleGenerator.getClosestForCoords(worldObj, (int) posX, (int) posZ);
        if(closest != null) {
			closest = new BlockPos(closest.getX(), 0, closest.getZ());
			double d = closest.distanceSq(new BlockPos((int) posX, 0, (int) posZ));
            if(d <= WorldHelpers.CHUNK_SIZE * 2) {
                return null;
            }
			return (float) LocationHelpers.getPitch(closest, new BlockPos((int) posX, 0, (int) posZ));
		}*/
		// TODO: rewrite
		return null;
	}
	
	@Override
	protected boolean hasCustomRotation() {
		return isValid() && false;// TODO: DarkTempleGenerator.canGenerate(worldObj);
	}

	public float getAngle() {
		return dataWatcher.getWatchableObjectFloat(WATCHERID_ANGLE);
	}

	protected void setAngle(float angle) {
		this.dataWatcher.updateObject(WATCHERID_ANGLE, angle);
	}

	public boolean isValid() {
		return dataWatcher.getWatchableObjectInt(WATCHERID_VALID) == 1;
	}

	protected void setValid(boolean valid) {
		this.dataWatcher.updateObject(WATCHERID_VALID, valid ? 1 : 0);
	}
	
}