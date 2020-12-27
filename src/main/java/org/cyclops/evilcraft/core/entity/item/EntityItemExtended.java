package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

/**
 * An extended version of the entity item.
 * @author rubensworks
 *
 */
public abstract class EntityItemExtended extends ItemEntity {

    public EntityItemExtended(EntityType<? extends EntityItemExtended> type, World world) {
        super(type, world);
        this.setPickupDelay(40);
    }

	public EntityItemExtended(EntityType<? extends EntityItemExtended> type, World world, ItemEntity original) {
        super(type, world);
        this.setPickupDelay(40);
        this.setMotion(original.getMotion());
        this.setPosition(original.getPosX(), original.getPosY(), original.getPosZ());
        this.setItem(original.getItem());
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // Needed for particle rendering
    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemEntity func_234273_t_() {
        EntityItemExtended entity = (EntityItemExtended) getType().create(world);
        entity.setItem(this.getItem().copy());
        entity.copyLocationAndAnglesFrom(this);
        //entity.age = this.getAge();
        //entity.hoverStart = this.hoverStart;
        return entity;
    }
}
