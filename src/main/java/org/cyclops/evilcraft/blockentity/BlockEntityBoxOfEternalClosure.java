package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.Advancements;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * A chest that is able to repair tools with the use of blood.
 * Partially based on cpw's IronChests.
 * @author rubensworks
 *
 */
public class BlockEntityBoxOfEternalClosure extends CyclopsBlockEntity {

    /**
     * The name of the NBT tag that will hold spirit entity data.
     */
    public static final String NBTKEY_SPIRIT = "spiritTag";
    /**
     * The name of the NBT tag that will hold the player id.
     */
    public static final String NBTKEY_PLAYERID = "playerId";
    /**
     * The name of the NBT tag that will hold the player name.
     */
    public static final String NBTKEY_PLAYERNAME = "playerName";
    /**
     * The lid angle for when this box is open.
     */
    public static final float START_LID_ANGLE = 65F;
    private static final int TICK_MODULUS = 10;
    private static final int TARGET_RADIUS = 10;
    private static final double ABSORB_RADIUS = 0.5D;
    private static final int NO_TARGET = -1;
    private static final float LID_STEP = 11.5F;
    /**
     * Inner rotation of a beam.
     */
    public int innerRotation;
    @NBTPersist
    private CompoundTag spiritTag = new CompoundTag();
    @NBTPersist
    private String playerId = "";
    @NBTPersist
    private String playerName = "";

    private EntityVengeanceSpiritData spiritData = null;

    private EntityVengeanceSpirit targetSpirit = null;

    @NBTPersist(useDefaultValue = false)
    private Integer targetSpiritId = NO_TARGET;

    @NBTPersist(useDefaultValue = false)
    private Float lidAngle = START_LID_ANGLE;
    private float previousLidAngle = 65F;

    private State state = State.UNKNOWN;

    /**
     * Make a new instance.
     */
    public BlockEntityBoxOfEternalClosure(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_BOX_OF_ETERNAL_CLOSURE, blockPos, blockState);
        innerRotation = new Random().nextInt(100000);
    }

    @Nullable
    public static EntityType<?> getSpiritType(@Nullable CompoundTag tag) {
        if(tag != null) {
            CompoundTag spiritTag = tag.getCompound(NBTKEY_SPIRIT);
            return EntityVengeanceSpiritData.getSpiritType(spiritTag);
        }
        return null;
    }

    public CompoundTag getSpiritTag() {
        if (this.spiritTag == null) {
            this.spiritTag = new CompoundTag();
        }
        return this.spiritTag;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
        setChanged();
        sendUpdate();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        setChanged();
        sendUpdate();
    }

    public void setSpiritTag(CompoundTag tag) {
        this.spiritTag = tag;
        setChanged();
        sendUpdate();
    }

    public boolean isClosed() {
        return getState() == State.CLOSED;
    }

    public State getState() {
        if (state == State.UNKNOWN)
            updateState();
        return state;
    }

    private void setState(State newState) {
        if (newState != state) {
            State oldState = state;
            state = newState;
            onStateChanged(oldState, state);
        }
    }

    private void updateState() {
        if (lidAngle <= 0)
            setState(State.CLOSED);
        else if (lidAngle >= START_LID_ANGLE)
            setState(State.OPEN);
    }

    private void onStateChanged(State oldState, State newState) {
        // The unknown state means we're still initializing, so level doesn't exist yet
        // ignore this state
        if (oldState == State.UNKNOWN) return;

        switch (state) {
            case OPEN:
                onBoxOpened();
                break;
            case CLOSED:
                onBoxClosed();
                break;
            case OPENING:
                onBoxOpening();
                break;
            case CLOSING:
                onBoxClosing();
                break;
            default:
        }
    }

    private void onBoxOpened() {
        if (!level.isClientSide() && hasSpirit())
            releaseSpirit();
    }

    private void onBoxClosed() {
        // Nothing to do
    }

    private void onBoxOpening() {
        if (level.isClientSide())
            playOpenSound();
    }

    private void onBoxClosing() {
        if (level.isClientSide())
            playCloseSound();
    }

    public boolean hasSpirit() {
        return !getSpiritTag().isEmpty();
    }

    private void releaseSpirit() {
        EntityVengeanceSpirit spirit = createNewVengeanceSpirit();
        level.addFreshEntity(spirit);
        clearSpirit();
    }

    private void playOpenSound() {
        float pitch = randomFloat(0.1f, 0.9f);
        playSound(SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5f, pitch);
    }

    private void playCloseSound() {
        float pitch = randomFloat(0.1f, 0.9f);
        playSound(SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5f, pitch);
    }

    private EntityVengeanceSpirit createNewVengeanceSpirit() {
        RandomSource rand = level.random;

        EntityVengeanceSpirit spirit = EntityVengeanceSpirit.fromNBT(getLevel(), getSpiritTag());
        spirit.setPos(getBlockPos().getX() + rand.nextDouble(), getBlockPos().getY() + rand.nextDouble(),
                getBlockPos().getZ() + rand.nextDouble());
        spirit.setFrozenDuration(0);
        spirit.setGlobalVengeance(true);
        spirit.setRemainingLife(Mth.nextInt(level.random,
                EntityVengeanceSpirit.REMAININGLIFE_MIN, EntityVengeanceSpirit.REMAININGLIFE_MAX));

        return spirit;
    }

    private void clearSpirit() {
        spiritTag = new CompoundTag();
        spiritData = null;
    }

    private float randomFloat(float min, float max) {
        return min + level.random.nextFloat() * max;
    }

    private void playSound(SoundEvent sound, SoundSource category, float volume, float pitch) {
        level.playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), sound, category, volume, pitch, false);
    }

    public void open() {
        setState(State.OPENING);
    }

    public void close() {
        setState(State.CLOSING);
    }

    public void initializeState() {
        if (hasSpirit())
            setState(State.CLOSED);
        else
            setState(State.OPEN);
    }

    private void initializeLidAngle() {
        if (getState() == State.OPEN) {
            previousLidAngle = lidAngle = START_LID_ANGLE;
        } else if (getState() == State.CLOSED) {
            previousLidAngle = lidAngle = 0f;
        }
    }

    private void setSpirit(EntityVengeanceSpirit spirit) {
        spirit.getData().writeNBT(getSpiritTag());
    }

    private void updateLidAngle() {
        State state = getState();
        previousLidAngle = lidAngle;

        if (state == State.OPENING)
            incrementLidAngle(LID_STEP);
        else if (state == State.CLOSING)
            incrementLidAngle(-LID_STEP);

        if (state == State.OPENING || state == State.CLOSING)
            updateState();
    }

    private void pullEntity() {
        EntityVengeanceSpirit target = getTargetSpirit();
        if(target != null) {
            double dx = targetSpirit.getX() - getBlockPos().getX() - 0.5D;
            double dy = targetSpirit.getY() - getBlockPos().getY() - 0.5D;
            double dz = targetSpirit.getZ() - getBlockPos().getZ() - 0.5D;
            double distance = (double) Mth.sqrt((float) (dx * dx + dy * dy + dz * dz));

            if (!target.isAlive() || !target.isFrozen()) {
                setTargetSpirit(null);
            } else {
                BlockPos blockPos = getBlockPos();
                AABB boxBoundingBox = getBlockState()
                        .getCollisionShape(level, blockPos)
                        .move(blockPos.getX(), blockPos.getY(), blockPos.getZ())
                        .bounds();
                AABB spiritBoundingBox = target
                        .getBoundingBox()
                        .inflate(ABSORB_RADIUS);
                boolean spiritTrapped = spiritBoundingBox.intersects(boxBoundingBox);

                if(spiritTrapped) {
                    captureSpirit(targetSpirit);
                    close();
                } else {
                    double strength = (1D / (distance)) / 50D + 0.01D;
                    target.setDeltaMovement(target.getDeltaMovement().subtract(dx * strength, dy * strength, dz * strength));
                }
            }
        }
    }

    private void captureSpirit(EntityVengeanceSpirit targetSpirit) {
        for (ServerPlayer player : targetSpirit.getEntanglingPlayers()) {
            Advancements.BOX_OF_ETERNAL_CLOSURE_CAPTURE.test(player, targetSpirit.getInnerEntity());
        }
        targetSpirit.remove(Entity.RemovalReason.DISCARDED);
        this.playerId = targetSpirit.getPlayerId();
        this.playerName = targetSpirit.getPlayerName();
        setSpirit(targetSpirit);
        setTargetSpirit(null);
        close();
    }

    private boolean findsOrHasTargetEntity() {
        return hasTargetSpirit() || findsTargetEntity();
    }

    private boolean findsTargetEntity() {
        return WorldHelpers.efficientTick(getLevel(), TICK_MODULUS, getBlockPos())
                && findNextEntity();
    }

    private boolean hasTargetSpirit() {
        return getTargetSpirit() != null;
    }

    private void playBeamSound() {
        float volume = randomFloat(0.1f, 0.9f);
        float pitch = randomFloat(0.1f, 0.9f);
        playSound(EvilCraftSoundEvents.effect_box_beam, SoundSource.AMBIENT, volume, pitch);
    }

    private boolean findNextEntity() {
        AABB box = new AABB(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
                getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()).inflate(TARGET_RADIUS, TARGET_RADIUS, TARGET_RADIUS);
        List<EntityVengeanceSpirit> entities = level.getEntitiesOfClass(EntityVengeanceSpirit.class, box);
        double minDistanceSquared = Math.pow(TARGET_RADIUS + 1, 2);
        EntityVengeanceSpirit closest = null;
        for(EntityVengeanceSpirit spirit : entities) {
            if(spirit.isFrozen() && !spirit.isSwarm()) {
                double distance = spirit.distanceToSqr(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
                if(distance < minDistanceSquared) {
                    minDistanceSquared = distance;
                    closest = spirit;
                }
            }
        }
        setTargetSpirit(closest);
        return targetSpirit != null;
    }

    protected void updateLight() {
        BlockState blockState = getLevel().getBlockState(getBlockPos());
        level.sendBlockUpdated(getBlockPos(), blockState, blockState, MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    }

    public float getLidAngle() {
        return lidAngle;
    }

    private void setLidAngle(float newLidAngle) {
        if (newLidAngle != lidAngle) {
            lidAngle = newLidAngle;

            if (lidAngle < 0f) {
                lidAngle = 0f;
                updateLight();
            } else if (lidAngle > START_LID_ANGLE) {
                lidAngle = START_LID_ANGLE;
                updateLight();
            }
        }
    }

    private void incrementLidAngle(float increment) {
        setLidAngle(lidAngle + increment);
    }

    public float getPreviousLidAngle() {
        return previousLidAngle;
    }

    public EntityVengeanceSpirit getTargetSpirit() {
        // Make sure our target spirit is up-to-date with the server-synced target spirit ID.
        if(getLevel().isClientSide() && targetSpiritId == NO_TARGET) {
            targetSpirit = null;
        } else if(targetSpirit == null && targetSpiritId != NO_TARGET) {
            setTargetSpirit((EntityVengeanceSpirit) getLevel().getEntity(targetSpiritId));
        }
        return targetSpirit;
    }

    private void setTargetSpirit(EntityVengeanceSpirit targetSpirit) {
        EntityVengeanceSpirit old = this.targetSpirit;
        this.targetSpirit = targetSpirit;
        if(targetSpirit != null) {
            targetSpiritId = targetSpirit.getId();
        } else {
            targetSpiritId = NO_TARGET;
        }

        if(old != targetSpirit) {
            sendUpdate();
        }
    }

    public EntityVengeanceSpiritData getSpiritData() {
        return hasSpirit() ? loadSpiritDataLazy() : null;
    }

    private EntityVengeanceSpiritData loadSpiritDataLazy() {
        if (spiritData == null) {
            spiritData = EntityVengeanceSpiritData.fromNBT(getSpiritTag());
        }
        return spiritData;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return BlockEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        initializeState();
        initializeLidAngle();
    }

    public static enum State {
        UNKNOWN, OPEN, OPENING, CLOSING, CLOSED
    }

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntityBoxOfEternalClosure> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityBoxOfEternalClosure blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            blockEntity.innerRotation++;

            if (!blockEntity.hasSpirit() && blockEntity.findsOrHasTargetEntity())
                blockEntity.pullEntity();

            blockEntity.updateLidAngle();
        }

        @Override
        protected void onSendUpdate(Level level, BlockPos pos) {
            super.onSendUpdate(level, pos);

            // Trigger comparator update.
            level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock());
        }
    }

    public static class TickerClient extends BlockEntityTickerDelayed<BlockEntityBoxOfEternalClosure> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityBoxOfEternalClosure blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            blockEntity.innerRotation++;

            if (blockEntity.hasTargetSpirit()) {
                blockEntity.playBeamSound();
            }

            blockEntity.updateLidAngle();
        }
    }
}
