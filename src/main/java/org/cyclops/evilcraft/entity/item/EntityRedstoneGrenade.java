package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemRedstoneGrenade;

/**
 * Entity for the {@link ItemRedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class EntityRedstoneGrenade extends ThrowableEntity implements IRendersAsItem {

    public EntityRedstoneGrenade(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_REDSTONE_GRENADE, entity, world);
    }

    public EntityRedstoneGrenade(EntityType<? extends EntityRedstoneGrenade> type, World world) {
        super(type, world);
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        if (pos.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockRayTraceResult) pos).getPos();

            if (world.isAirBlock(blockPos.offset(((BlockRayTraceResult) pos).getFace()))) {
                world.setBlockState(blockPos.add(((BlockRayTraceResult) pos).getFace().getDirectionVec()), RegistryEntries.BLOCK_INVISIBLE_REDSTONE.getDefaultState());

                if (world.isRemote()) {
                    Minecraft.getInstance().worldRenderer.addParticle(
                            RedstoneParticleData.REDSTONE_DUST, false,
                            blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0, 0);
                }
            }

            this.remove();
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_REDSTONE_GRENADE);
    }
}
