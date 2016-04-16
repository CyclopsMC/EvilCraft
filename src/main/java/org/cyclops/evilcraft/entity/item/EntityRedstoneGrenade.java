package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.InvisibleRedstoneBlock;
import org.cyclops.evilcraft.block.InvisibleRedstoneBlockConfig;
import org.cyclops.evilcraft.item.RedstoneGrenade;

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
    protected void onImpact(RayTraceResult pos) {
        BlockPos blockPos = pos.getBlockPos();

        if (worldObj.isAirBlock(blockPos.add(pos.sideHit.getDirectionVec()))) {
			if(Configs.isEnabled(InvisibleRedstoneBlockConfig.class)) {
	            worldObj.setBlockState(blockPos.add(pos.sideHit.getDirectionVec()), InvisibleRedstoneBlock.getInstance().getDefaultState());
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
