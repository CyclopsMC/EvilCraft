package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemRedstoneGrenade;

/**
 * Entity for the {@link ItemRedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class EntityRedstoneGrenade extends ThrowableProjectile implements ItemSupplier {

    public EntityRedstoneGrenade(Level world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_REDSTONE_GRENADE.get(), entity.getX(), entity.getY(), entity.getZ(), world);
        setOwner(entity);
    }

    public EntityRedstoneGrenade(EntityType<? extends EntityRedstoneGrenade> type, Level world) {
        super(type, world);
    }

    @Override
    protected void onHit(HitResult pos) {
        if (pos.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) pos).getBlockPos();

            if (level().isEmptyBlock(blockPos.relative(((BlockHitResult) pos).getDirection()))) {
                level().setBlockAndUpdate(blockPos.offset(((BlockHitResult) pos).getDirection().getUnitVec3i()), RegistryEntries.BLOCK_INVISIBLE_REDSTONE.get().defaultBlockState());

                if (level().isClientSide()) {
                    Minecraft.getInstance().level.addParticle(
                            DustParticleOptions.REDSTONE,
                            blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0, 0);
                }
            }

            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_REDSTONE_GRENADE);
    }
}
