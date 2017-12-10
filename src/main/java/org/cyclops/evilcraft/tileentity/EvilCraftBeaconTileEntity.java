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
	private Vector4f beamColor;
	
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
	 * Make a new instance with set color.
	 * @param beamColor The color for the beam.
	 */
	@SideOnly(Side.CLIENT)
	public EvilCraftBeaconTileEntity(Vector4f beamColor) {
		this.beamColor = beamColor;
		
		isActive = true;
	}
	
	/**
	 * Get the outer color.
	 * @return The outer color.
	 */
	@SideOnly(Side.CLIENT)
	public Vector4f getBeamColor() {
		return beamColor;
	}
	
	/**
	 * Set the outer color.
	 * @param beamOuterColor The outer color.
	 */
	@SideOnly(Side.CLIENT)
	public void setBeamColor(Vector4f beamOuterColor) {
	    this.beamColor = beamOuterColor;
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
            int diff = (int)(this.world.getTotalWorldTime() - lastUpdated);
            lastUpdated = this.world.getTotalWorldTime();

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
