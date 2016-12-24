package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.VengeanceSpiritData;

import java.util.List;
import java.util.Random;

/**
 * A chest that is able to repair tools with the use of blood.
 * Partially based on cpw's IronChests.
 * @author rubensworks
 *
 */
public class TileBoxOfEternalClosure extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

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
	@Delegate
	private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
	/**
	 * Inner rotation of a beam.
	 */
	public int innerRotation;
	@NBTPersist
	private NBTTagCompound spiritTag = new NBTTagCompound();
	@NBTPersist
	private String playerId = "";
	@NBTPersist
	private String playerName = "";

	private VengeanceSpiritData spiritData = null;

	private VengeanceSpirit targetSpirit = null;

	@NBTPersist(useDefaultValue = false)
	private Integer targetSpiritId = NO_TARGET;
	
	@NBTPersist(useDefaultValue = false)
	private Float lidAngle = START_LID_ANGLE;
	private float previousLidAngle = 65F;

	private State state = State.UNKNOWN;
	
    /**
     * Make a new instance.
     */
    public TileBoxOfEternalClosure() {
    	innerRotation = new Random().nextInt(100000);
    }
	
	public static ResourceLocation getSpiritNameOrNullFromNBTTag(NBTTagCompound tag) {
		if(tag != null) {
			NBTTagCompound spiritTag = tag.getCompoundTag(NBTKEY_SPIRIT);
			return VengeanceSpiritData.getSpiritNameOrNullFromNBTTag(spiritTag);
		}
		return null;
	}

	protected NBTTagCompound getSpiritTag() {
		if (this.spiritTag == null) {
			this.spiritTag = new NBTTagCompound();
		}
		return this.spiritTag;
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
		// The unknown state means we're still initializing, so world doesn't exist yet
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
		if (!world.isRemote && hasSpirit())
			releaseSpirit();
	}

	private void onBoxClosed() {
		// Nothing to do
	}

	private void onBoxOpening() {
		if (world.isRemote)
			playOpenSound();
	}

	private void onBoxClosing() {
		if (world.isRemote)
			playCloseSound();
	}

	public boolean hasSpirit() {
		return !getSpiritTag().hasNoTags();
	}

	private void releaseSpirit() {
		VengeanceSpirit spirit = createNewVengeanceSpirit();
		world.spawnEntity(spirit);
		clearSpirit();
	}

	private void playOpenSound() {
		float pitch = randomFloat(0.1f, 0.9f);
		playSound(SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, pitch);
	}

	private void playCloseSound() {
		float pitch = randomFloat(0.1f, 0.9f);
		playSound(SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, pitch);
	}

	private VengeanceSpirit createNewVengeanceSpirit() {
		Random rand = world.rand;

		VengeanceSpirit spirit = VengeanceSpirit.fromNBT(getWorld(), getSpiritTag());
		spirit.setPosition(getPos().getX() + rand.nextDouble(), getPos().getY() + rand.nextDouble(),
				getPos().getZ() + rand.nextDouble());
		spirit.setFrozenDuration(0);
		spirit.setGlobalVengeance(true);
		spirit.setRemainingLife(MathHelper.getInt(world.rand,
				VengeanceSpirit.REMAININGLIFE_MIN, VengeanceSpirit.REMAININGLIFE_MAX));

		return spirit;
	}

	private void clearSpirit() {
		spiritTag = new NBTTagCompound();
		spiritData = null;
	}

	private float randomFloat(float min, float max) {
		return min + world.rand.nextFloat() * max;
	}

	private void playSound(SoundEvent sound, SoundCategory category, float volume, float pitch) {
		world.playSound(getPos().getX(), getPos().getY(), getPos().getZ(), sound, category, volume, pitch, false);
	}

	public void open() {
		setState(State.OPENING);
	}
    
	public void close() {
		setState(State.CLOSING);
	}

	private void initializeState() {
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

	private void setSpirit(VengeanceSpirit spirit) {
		spirit.getData().writeNBT(getSpiritTag());
	}

    @Override
    public void updateTileEntity() {
        super.updateTileEntity();

        innerRotation++;

		if (!world.isRemote)
			updateTileEntityServer();
		else
			updateTileEntityClient();

		updateLidAngle();
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

	private void updateTileEntityServer() {
		if (!hasSpirit() && findsOrHasTargetEntity())
			pullEntity();
	}

	private void pullEntity() {
		VengeanceSpirit target = getTargetSpirit();
		if(target != null) {
			double dx = targetSpirit.posX - getPos().getX() - 0.5D;
			double dy = targetSpirit.posY - getPos().getY() - 0.5D;
			double dz = targetSpirit.posZ - getPos().getZ() - 0.5D;
			double distance = (double)MathHelper.sqrt(dx * dx + dy * dy + dz * dz);

			if(target.isDead || !target.isFrozen()) {
				setTargetSpirit(null);
			} else {
				BlockPos blockPos = getPos();
				AxisAlignedBB boxBoundingBox = getBlock()
						.getBoundingBox(world.getBlockState(blockPos), world, blockPos)
						.offset(blockPos);
				AxisAlignedBB spiritBoundingBox = target
						.getEntityBoundingBox()
						.expand(ABSORB_RADIUS, ABSORB_RADIUS, ABSORB_RADIUS);
				boolean spiritTrapped = spiritBoundingBox.intersectsWith(boxBoundingBox);

				if(spiritTrapped) {
					captureSpirit(targetSpirit);
					close();
				} else {
					double strength = (1D / (distance)) / 50D + 0.01D;
					target.motionX -= dx * strength;
					target.motionY -= dy * strength;
					target.motionZ -= dz * strength;
				}
			}
		}
	}

	private void captureSpirit(VengeanceSpirit targetSpirit) {
		world.removeEntity(targetSpirit);
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
		return WorldHelpers.efficientTick(getWorld(), TICK_MODULUS, getPos())
				&& findNextEntity();
	}

	private void updateTileEntityClient() {
		if(hasTargetSpirit()) {
			playBeamSound();
		}
	}

	private boolean hasTargetSpirit() {
		return getTargetSpirit() != null;
	}

	private void playBeamSound() {
		float volume = randomFloat(0.1f, 0.9f);
		float pitch = randomFloat(0.1f, 0.9f);
		playSound(EvilCraftSoundEvents.effect_box_beam, SoundCategory.AMBIENT, volume, pitch);
	}

	private boolean findNextEntity() {
    	AxisAlignedBB box = new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(),
				getPos().getX(), getPos().getY(), getPos().getZ()).expand(TARGET_RADIUS, TARGET_RADIUS, TARGET_RADIUS);
    	@SuppressWarnings("unchecked")
		List<VengeanceSpirit> entities = world.getEntitiesWithinAABB(VengeanceSpirit.class, box);
    	double minDistance = TARGET_RADIUS + 1;
    	VengeanceSpirit closest = null;
    	for(VengeanceSpirit spirit : entities) {
    		if(spirit.isFrozen() && !spirit.isSwarm()) {
	    		double distance = spirit.getDistance(getPos().getX(), getPos().getY(), getPos().getZ());
	    		if(distance < minDistance) {
	    			minDistance = distance;
	    			closest = spirit;
	    		}
    		}
    	}
    	setTargetSpirit(closest);
    	return targetSpirit != null;
    }
    
    protected void updateLight() {
		IBlockState blockState = getWorld().getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), blockState, blockState, MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
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

	public VengeanceSpirit getTargetSpirit() {
		// Make sure our target spirit is up-to-date with the server-synced target spirit ID.
		if(getWorld().isRemote && targetSpiritId == NO_TARGET) {
			targetSpirit = null;
		} else if(targetSpirit == null && targetSpiritId != NO_TARGET) {
			setTargetSpirit((VengeanceSpirit) getWorld().getEntityByID(targetSpiritId));
		}
		return targetSpirit;
	}

	private void setTargetSpirit(VengeanceSpirit targetSpirit) {
		VengeanceSpirit old = this.targetSpirit;
		this.targetSpirit = targetSpirit;
    	if(targetSpirit != null) {
    		targetSpiritId = targetSpirit.getEntityId();
    	} else {
    		targetSpiritId = NO_TARGET;
    	}
    	
    	if(old != targetSpirit) {
    		sendUpdate();
    	}
	}

	public VengeanceSpiritData getSpiritData() {
		return hasSpirit() ? loadSpiritDataLazy() : null;
	}

	private VengeanceSpiritData loadSpiritDataLazy() {
		if (spiritData == null) {
			spiritData = VengeanceSpiritData.fromNBT(getSpiritTag());
		}
		return spiritData;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}

    @Override
    protected void onSendUpdate() {
        super.onSendUpdate();
        // Trigger comparator update.
        world.notifyNeighborsOfStateChange(getPos(), this.getBlock(), true);
    }

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		initializeState();
		initializeLidAngle();
	}

	public static enum State {
		UNKNOWN, OPEN, OPENING, CLOSING, CLOSED
	}
}
