package org.cyclops.evilcraft.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.lwjgl.util.vector.Vector4f;

/**
 * Base tile entity for beacons.
 * @author immortaleeb
 *
 */
public abstract class EvilCraftBeaconTileEntity extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {
	
    @SideOnly(Side.CLIENT)
	private Vector4f beamInnerColor;
    @SideOnly(Side.CLIENT)
	private Vector4f beamOuterColor;
	
	private boolean isActive;
	
	@SideOnly(Side.CLIENT)
	private long lastUpdated;
	@SideOnly(Side.CLIENT)
    private float renderVariable;
	
	/**
	 * Make a new instance.
	 */
	public EvilCraftBeaconTileEntity() {
	    isActive = true;
	}
	
	/**
	 * Make a new instance with set colors.
	 * @param beamInnerColor The inner color for the beam.
	 * @param beamOuterColor The outer color (glow) for the beam.
	 */
	@SideOnly(Side.CLIENT)
	public EvilCraftBeaconTileEntity(Vector4f beamInnerColor, Vector4f beamOuterColor) {
		this.beamInnerColor = beamInnerColor;
		this.beamOuterColor = beamOuterColor;
		
		isActive = true;
	}
	
	/**
	 * Get the inner color.
	 * @return The inner color.
	 */
	@SideOnly(Side.CLIENT)
	public Vector4f getBeamInnerColor() {
		return beamInnerColor;
	}
	
	/**
	 * Set the inner color.
	 * @param beamInnerColor The inner color.
	 */
	@SideOnly(Side.CLIENT)
	public void setBeamInnerColor(Vector4f beamInnerColor) {
	    this.beamInnerColor = beamInnerColor;
	}
	
	/**
	 * Get the outer color.
	 * @return The outer color.
	 */
	@SideOnly(Side.CLIENT)
	public Vector4f getBeamOuterColor() {
		return beamOuterColor;
	}
	
	/**
	 * Set the outer color.
	 * @param beamOuterColor The outer color.
	 */
	@SideOnly(Side.CLIENT)
	public void setBeamOuterColor(Vector4f beamOuterColor) {
	    this.beamOuterColor = beamOuterColor;
	}
	
	/**
	 * If the beam should be rendered.
	 * @return If it is active.
	 */
	public boolean isBeamActive() {
		return isActive;
	}
	
	/**
	 * Set if the beam should be rendered.
	 * @param active If it is active.
	 */
	public void setBeamActive(boolean active) {
	    this.isActive = active;
	}
	
	/**
	 * Get the beam render variable that is used for the rotation based on the tick time.
	 * @return The beam render variable.
	 */
	@SideOnly(Side.CLIENT)
    public float getBeamRenderVariable() {
        if (!this.isBeamActive()) {
            return 0.0F;
        } else {
            int diff = (int)(this.worldObj.getTotalWorldTime() - lastUpdated);
            lastUpdated = this.worldObj.getTotalWorldTime();

            if (diff > 1) {
                this.renderVariable -= (float)diff / 40.0F;

                if (this.renderVariable < 0.0F) {
                    this.renderVariable = 0.0F;
                }
            }

            this.renderVariable += 0.025F;

            if (this.renderVariable > 1.0F) {
                this.renderVariable = 1.0F;
            }

            return this.renderVariable;
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}
}
