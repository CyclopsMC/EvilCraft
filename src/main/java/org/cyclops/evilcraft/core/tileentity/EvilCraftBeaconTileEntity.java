package org.cyclops.evilcraft.core.tileentity;

import net.minecraft.client.renderer.Vector4f;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * Base tile entity for beacons.
 * @author immortaleeb
 *
 */
public abstract class EvilCraftBeaconTileEntity extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    @OnlyIn(Dist.CLIENT)
	private Vector4f beamColor;
	
	private boolean isActive;

	public EvilCraftBeaconTileEntity(TileEntityType<?> type) {
		super(type);
	    isActive = true;
	}
	
	/**
	 * Get the outer color.
	 * @return The outer color.
	 */
	@OnlyIn(Dist.CLIENT)
	public Vector4f getBeamColor() {
		return beamColor;
	}
	
	/**
	 * Set the outer color.
	 * @param beamOuterColor The outer color.
	 */
	@OnlyIn(Dist.CLIENT)
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
	
	@Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }
	
	@Override
	@OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}
}
