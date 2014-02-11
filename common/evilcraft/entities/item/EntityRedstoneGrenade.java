package evilcraft.entities.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.blocks.InvisibleRedstoneBlock;

public class EntityRedstoneGrenade extends EntityThrowable implements Configurable {
    
    /**
     * Maps a side number to the offset of the X, Y and Z coordinates where the
     * redstone block will be spawned when the redstone grenade hits the ground 
     * Bottom = 0, Top = 1, East = 2, West = 3, North = 4, South = 5.
     */
    private static final int[] sideXOffsets = { 0, 0,  0, 0, -1, 1};
    private static final int[] sideYOffsets = {-1, 1,  0, 0,  0, 0};
    private static final int[] sideZOffsets = { 0, 0, -1, 1,  0, 0};
    
    public EntityRedstoneGrenade(World world) {
        super(world);
    }
    
    public EntityRedstoneGrenade(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
    }
    
    @SideOnly(Side.CLIENT)
    public EntityRedstoneGrenade(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

    @Override
    protected void onImpact(MovingObjectPosition pos) {
        int dx = sideXOffsets[pos.sideHit];
        int dy = sideYOffsets[pos.sideHit];
        int dz = sideZOffsets[pos.sideHit];
        
        if (worldObj.isAirBlock(pos.blockX + dx, pos.blockY + dy, pos.blockZ + dz)) {
            worldObj.setBlock(
                pos.blockX + dx, 
                pos.blockY + dy, 
                pos.blockZ + dz,
                InvisibleRedstoneBlock.getInstance().blockID);
            
            if (worldObj.isRemote) {
                double x = dx + ((dx >= 0) ? 0.5 : 0.9) + ((dx == 1) ? -0.5 : 0);
                double y = dy + ((dy >= 0) ? 0.5 : 0.9) + ((dy == 1) ? -0.5 : 0);
                double z = dz + ((dz >= 0) ? 0.5 : 0.9) + ((dz == 1) ? -0.5 : 0);
                
                worldObj.spawnParticle("reddust", 
                        pos.blockX + x, 
                        pos.blockY + y,
                        pos.blockZ + z, 1, 0, 0);
            }
        }
        
        this.setDead();
    }
}
