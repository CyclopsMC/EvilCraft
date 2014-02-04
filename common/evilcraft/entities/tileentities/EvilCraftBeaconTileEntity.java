package evilcraft.entities.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;

public abstract class EvilCraftBeaconTileEntity extends EvilCraftTileEntity {
	
    @SideOnly(Side.CLIENT)
	private Vector4f beamInnerColor;
    @SideOnly(Side.CLIENT)
	private Vector4f beamOuterColor;
	
	private boolean isActive;
	
	@SideOnly(Side.CLIENT)
	private long lastUpdated;
	@SideOnly(Side.CLIENT)
    private float renderVariable;
	
	public EvilCraftBeaconTileEntity() {
	    isActive = true;
	}
	
	@SideOnly(Side.CLIENT)
	public EvilCraftBeaconTileEntity(Vector4f beamInnerColor, Vector4f beamOuterColor) {
		this.beamInnerColor = beamInnerColor;
		this.beamOuterColor = beamOuterColor;
		
		isActive = true;
	}
	
	@SideOnly(Side.CLIENT)
	public Vector4f getBeamInnerColor() {
		return beamInnerColor;
	}
	
	@SideOnly(Side.CLIENT)
	public void setBeamInnerColor(Vector4f beamInnerColor) {
	    this.beamInnerColor = beamInnerColor;
	}
	
	@SideOnly(Side.CLIENT)
	public Vector4f getBeamOuterColor() {
		return beamOuterColor;
	}
	
	@SideOnly(Side.CLIENT)
	public void setBeamOuterColor(Vector4f beamOuterColor) {
	    this.beamOuterColor = beamOuterColor;
	}
	
	public boolean isBeamActive() {
		return isActive;
	}
	
	public void setBeamActive(boolean active) {
	    this.isActive = active;
	}
	
	@SideOnly(Side.CLIENT)
    public float getBeamRenderVariable()
    {
        if (!this.isBeamActive())
        {
            return 0.0F;
        }
        else
        {
            int diff = (int)(this.worldObj.getTotalWorldTime() - lastUpdated);
            lastUpdated = this.worldObj.getTotalWorldTime();

            if (diff > 1)
            {
                this.renderVariable -= (float)diff / 40.0F;

                if (this.renderVariable < 0.0F)
                {
                    this.renderVariable = 0.0F;
                }
            }

            this.renderVariable += 0.025F;

            if (this.renderVariable > 1.0F)
            {
                this.renderVariable = 1.0F;
            }

            return this.renderVariable;
        }
    }
	
	@SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }
	
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}
}
