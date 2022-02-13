package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemRedstoneGrenade;

import javax.annotation.Nonnull;

/**
 * Entity for the {@link ItemRedstoneGrenade}.
 * @author immortaleeb
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityRedstoneGrenade extends ThrowableEntity implements IRendersAsItem {

    public EntityRedstoneGrenade(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_REDSTONE_GRENADE, entity, world);
    }

    public EntityRedstoneGrenade(EntityType<? extends EntityRedstoneGrenade> type, World world) {
        super(type, world);
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(RayTraceResult pos) {
        if (pos.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockRayTraceResult) pos).getBlockPos();

            if (level.isEmptyBlock(blockPos.relative(((BlockRayTraceResult) pos).getDirection()))) {
                level.setBlockAndUpdate(blockPos.offset(((BlockRayTraceResult) pos).getDirection().getNormal()), RegistryEntries.BLOCK_INVISIBLE_REDSTONE.defaultBlockState());

                if (level.isClientSide()) {
                    Minecraft.getInstance().levelRenderer.addParticle(
                            RedstoneParticleData.REDSTONE, false,
                            blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0, 0);
                }
            }

            this.remove();
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_REDSTONE_GRENADE);
    }
}
