package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;

import javax.annotation.Nullable;

/**
 * Entity for the dark stick item.
 * @author rubensworks
 *
 */
public class EntityItemDarkStick extends EntityItemDefinedRotation {

    private static final EntityDataAccessor<Integer> WATCHERID_VALID = SynchedEntityData.<Integer>defineId(EntityItemDarkStick.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> WATCHERID_ANGLE = SynchedEntityData.<Float>defineId(EntityItemDarkStick.class, EntityDataSerializers.FLOAT);

    private double lastPosX = -1;
    private double lastPosY = -1;
    private double lastPosZ = -1;

    public EntityItemDarkStick(EntityType<? extends EntityItemDarkStick> type, Level world) {
        super(type, world);
    }

    public EntityItemDarkStick(Level world, ItemEntity original) {
        super(RegistryEntries.ENTITY_ITEM_DARK_STICK, world, original);
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        Float angle = MinecraftHelpers.isClientSide() ? null : loadRotation();
        this.entityData.define(WATCHERID_VALID, angle != null ? 1 : 0);
        this.entityData.define(WATCHERID_ANGLE, angle == null ? 0 : angle);
    }

    protected boolean hasMoved() {
        boolean moved = Math.abs(lastPosX - getX()) > 0.1F || Math.abs(lastPosZ - getZ()) > 0.1F;
        lastPosX = getX();
        lastPosY = getY();
        lastPosZ = getZ();
        return moved;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getCommandSenderWorld().isClientSide() && hasMoved()) {
            Float angle = loadRotation();
            setValid(angle != null);
            setAngle(angle == null ? 0 : angle);
        }
    }

    @Nullable
    private Float loadRotation() {
        // MCP: findNearestStructure
        BlockPos closest = ((ServerLevel)level).getChunkSource().getGenerator()
                .findNearestMapFeature((ServerLevel) level, RegistryEntries.STRUCTURE_DARK_TEMPLE, new BlockPos(getX(), getY(), getZ()), 100, false);
        if(closest != null) {
            closest = new BlockPos(closest.getX(), 0, closest.getZ());
            double d = closest.distSqr(new BlockPos((int) getX(), 0, (int) getZ()));
            if(d <= WorldHelpers.CHUNK_SIZE * WorldHelpers.CHUNK_SIZE) {
                return null;
            }
            BlockPos normalized = new BlockPos(closest.getX() - (int) getX(), 0,
                    closest.getZ() - (int) getZ());
            return (float) (Math.atan2(normalized.getX(), normalized.getZ()) * 180 / Math.PI);
        }
        return null;
    }

    @Override
    protected boolean hasCustomRotation() {
        return isValid();
    }

    public float getAngle() {
        return entityData.get(WATCHERID_ANGLE);
    }

    protected void setAngle(float angle) {
        this.entityData.set(WATCHERID_ANGLE, angle);
    }

    public boolean isValid() {
        return entityData.get(WATCHERID_VALID) == 1;
    }

    protected void setValid(boolean valid) {
        this.entityData.set(WATCHERID_VALID, valid ? 1 : 0);
    }

}
