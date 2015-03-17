package evilcraft.entity.item;

import evilcraft.Configs;
import evilcraft.block.InvisibleRedstoneBlock;
import evilcraft.block.InvisibleRedstoneBlockConfig;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.item.RedstoneGrenade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Entity for the {@link RedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class EntityRedstoneGrenade extends EntityThrowable implements IConfigurable {
    
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

    @Override
    protected void onImpact(MovingObjectPosition pos) {
        BlockPos blockPos = pos.func_178782_a();
        
        if (worldObj.isAirBlock(blockPos.add(pos.field_178784_b.getDirectionVec()))) {
			if(Configs.isEnabled(InvisibleRedstoneBlockConfig.class)) {
	            worldObj.setBlockState(blockPos.add(pos.field_178784_b.getDirectionVec()), InvisibleRedstoneBlock.getInstance().getDefaultState());
			}
            
            if (worldObj.isRemote) {
                worldObj.spawnParticle(EnumParticleTypes.REDSTONE,
                        blockPos.getX() + 0.5,
                        blockPos.getY() + 0.5,
                        blockPos.getZ() + 0.5, 1, 0, 0);
            }
        }
        
        this.setDead();
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

}
