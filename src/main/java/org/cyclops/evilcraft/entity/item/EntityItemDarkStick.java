package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

	private static final DataParameter<Integer> WATCHERID_VALID = EntityDataManager.<Integer>defineId(EntityItemDarkStick.class, DataSerializers.INT);
	private static final DataParameter<Float> WATCHERID_ANGLE = EntityDataManager.<Float>defineId(EntityItemDarkStick.class, DataSerializers.FLOAT);

    private double lastPosX = -1;
    private double lastPosY = -1;
    private double lastPosZ = -1;

	public EntityItemDarkStick(EntityType<? extends EntityItemDarkStick> type, World world) {
		super(type, world);
	}

	public EntityItemDarkStick(World world, ItemEntity original) {
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
		BlockPos closest = ((ServerWorld)level).getChunkSource().getGenerator()
				.findNearestMapFeature((ServerWorld) level, RegistryEntries.STRUCTURE_DARK_TEMPLE, new BlockPos(getX(), getY(), getZ()), 100, false);
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