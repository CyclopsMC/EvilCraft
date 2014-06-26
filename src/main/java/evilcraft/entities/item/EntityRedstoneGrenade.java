package evilcraft.entities.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.blocks.InvisibleRedstoneBlock;
import evilcraft.blocks.InvisibleRedstoneBlockConfig;
import evilcraft.items.RedstoneGrenade;

/**
 * Entity for the {@link RedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class EntityRedstoneGrenade extends EntityThrowable implements Configurable {
    
    protected ExtendedConfig<?> eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ENTITY;
    
    /**
     * Maps a side number to the offset of the X, Y and Z coordinates where the
     * redstone block will be spawned when the redstone grenade hits the ground 
     * Bottom = 0, Top = 1, East = 2, West = 3, North = 4, South = 5.
     */
    private static final int[] sideXOffsets = { 0, 0,  0, 0, -1, 1};
    private static final int[] sideYOffsets = {-1, 1,  0, 0,  0, 0};
    private static final int[] sideZOffsets = { 0, 0, -1, 1,  0, 0};
    
    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityRedstoneGrenade(World world) {
        super(world);
    }
    
    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entityLivingBase The {@link EntityLivingBase} that placed this {@link Entity}.
     */
    public EntityRedstoneGrenade(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
    }
    
    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public EntityRedstoneGrenade(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @SuppressWarnings("rawtypes")
    @Override
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
			if(Configs.isEnabled(InvisibleRedstoneBlockConfig.class)) {
	            worldObj.setBlock(
	                pos.blockX + dx, 
	                pos.blockY + dy, 
	                pos.blockZ + dz,
	                InvisibleRedstoneBlock.getInstance());
			}
            
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
