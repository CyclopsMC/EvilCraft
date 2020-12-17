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

	private static final DataParameter<Integer> WATCHERID_VALID = EntityDataManager.<Integer>createKey(EntityItemDarkStick.class, DataSerializers.VARINT);
	private static final DataParameter<Float> WATCHERID_ANGLE = EntityDataManager.<Float>createKey(EntityItemDarkStick.class, DataSerializers.FLOAT);

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
	public void registerData() {
		super.registerData();
        Float angle = MinecraftHelpers.isClientSide() ? null : loadRotation();
		this.dataManager.register(WATCHERID_VALID, angle != null ? 1 : 0);
		this.dataManager.register(WATCHERID_ANGLE, angle == null ? 0 : angle);
	}

    protected boolean hasMoved() {
        boolean moved = Math.abs(lastPosX - getPosX()) > 0.1F || Math.abs(lastPosZ - getPosZ()) > 0.1F;
        lastPosX = getPosX();
        lastPosY = getPosY();
        lastPosZ = getPosZ();
        return moved;
    }

	@Override
	public void tick() {
        super.tick();
		if (!getEntityWorld().isRemote() && hasMoved()) {
            Float angle = loadRotation();
            setValid(angle != null);
			setAngle(angle == null ? 0 : angle);
		}
	}

	@Nullable
	private Float loadRotation() {
		BlockPos closest = ((ServerWorld)world).getChunkProvider().getChunkGenerator()
				.findNearestStructure(world, RegistryEntries.STRUCTURE_DARK_TEMPLE.getStructureName(), new BlockPos(getPosX(), getPosY(), getPosZ()), 100, false);
        if(closest != null) {
			closest = new BlockPos(closest.getX(), 0, closest.getZ());
			double d = closest.distanceSq(new BlockPos((int) getPosX(), 0, (int) getPosZ()));
            if(d <= WorldHelpers.CHUNK_SIZE * WorldHelpers.CHUNK_SIZE) {
                return null;
            }
			BlockPos normalized = new BlockPos(closest.getX() - (int) getPosX(), 0,
					closest.getZ() - (int) getPosZ());
			return (float) (Math.atan2(normalized.getX(), normalized.getZ()) * 180 / Math.PI);
		}
		return null;
	}
	
	@Override
	protected boolean hasCustomRotation() {
		return isValid();
	}

	public float getAngle() {
		return dataManager.get(WATCHERID_ANGLE);
	}

	protected void setAngle(float angle) {
		this.dataManager.set(WATCHERID_ANGLE, angle);
	}

	public boolean isValid() {
		return dataManager.get(WATCHERID_VALID) == 1;
	}

	protected void setValid(boolean valid) {
		this.dataManager.set(WATCHERID_VALID, valid ? 1 : 0);
	}
	
}