package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodChest;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.RepairItemTickAction;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A chest that is able to repair tools with the use of blood.
 * Partially based on cpw's IronChests.
 * @author rubensworks
 *
 */
public class TileBloodChest extends TickingTankInventoryTileEntity<TileBloodChest> implements INamedContainerProvider, IChestLid {
	
	private static final int TICK_MODULUS = 200;
    
    /**
     * The amount of slots in the chest.
     */
    public static final int SLOTS_CHEST = ContainerBloodChest.CHEST_INVENTORY_ROWS * ContainerBloodChest.CHEST_INVENTORY_COLUMNS;
    /**
     * The total amount of slots.
     */
    public static final int SLOTS = SLOTS_CHEST + 1;
    /**
     * The id of the fluid container slot.
     */
    public static final int SLOT_CONTAINER = SLOTS - 1;

    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidHelpers.BUCKET_VOLUME * 10;

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    public float prevLidAngle;
    public float lidAngle;
    private int playersUsing;
    
    private static final Map<Class<?>, ITickAction<TileBloodChest>> REPAIR_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodChest>>();
    static {
        REPAIR_TICK_ACTIONS.put(Item.class, new RepairItemTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileBloodChest>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodChest>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<TileBloodChest>());
    }

    public TileBloodChest() {
        super(RegistryEntries.TILE_ENTITY_BLOOD_CHEST, SLOTS, 64, LIQUID_PER_SLOT, RegistryEntries.FLUID_BLOOD);
        for(int i = 0; i < SLOTS_CHEST; i++) {
            addTicker(new TickComponent<>(this, REPAIR_TICK_ACTIONS, i));
        }
        addTicker(new TickComponent<>(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true));
    }

    @Override
    protected void addItemHandlerCapabilities() {
        LazyOptional<IItemHandler> itemHandlerChest = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOTS_CHEST));
        LazyOptional<IItemHandler> itemHandlerContainer = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_CONTAINER));
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP, itemHandlerChest);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN, itemHandlerChest);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST, itemHandlerContainer);
    }

    @Override
    public Direction getRotation() {
        // World can be null during world loading
        if (getWorld() == null) {
            return Direction.SOUTH;
        }
        return BlockHelpers.getSafeBlockStateProperty(getBlockState(), BlockBloodChest.FACING, Direction.SOUTH).getOpposite();
    }
    
    @Override
    protected SingleUseTank createTank(int tankSize) {
    	return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize) {
            @Override
            public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
                if(slot == SLOT_CONTAINER)
                    return SlotFluidContainer.checkIsItemValid(itemstack, RegistryEntries.FLUID_BLOOD);
                else if(slot <= SLOTS_CHEST && slot >= 0)
                    return SlotRepairable.checkIsItemValid(itemstack);
                return false;
            }

            @Override
            public void openInventory(PlayerEntity entityPlayer) {
                triggerPlayerUsageChange(1);
            }

            @Override
            public void closeInventory(PlayerEntity entityPlayer) {
                triggerPlayerUsageChange(-1);
            }

            @Override
            public boolean isUsableByPlayer(PlayerEntity entityPlayer) {
                return super.isUsableByPlayer(entityPlayer)
                        && !(world == null || world.getTileEntity(getPos()) != TileBloodChest.this);
            }
        };
    }

    @Override
    public int getNewState() {
        return 0;
    }

    @Override
    public void onStateChanged() {
        
    }
    
    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        int x = getPos().getX();
        int y = getPos().getY();
        int z = getPos().getZ();
        // Resynchronize clients with the server state, the last condition makes sure
        // not all chests are synced at the same time.
        if(world != null
                && !this.world.isRemote()
                && this.playersUsing != 0
                && WorldHelpers.efficientTick(world, TICK_MODULUS, this.getPos())/*(this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0*/) {
            this.playersUsing = 0;
            float range = 5.0F;

            @SuppressWarnings("unchecked")
            List<PlayerEntity> entities = this.world.getEntitiesWithinAABB(
                    PlayerEntity.class,
                    new AxisAlignedBB(
                            (double)((float)x - range),
                            (double)((float)y - range),
                            (double)((float)z - range),
                            (double)((float)(x + 1) + range),
                            (double)((float)(y + 1) + range),
                            (double)((float)(z + 1) + range)
                            )
                    );

            for(PlayerEntity player : entities) {
                if (player.openContainer instanceof ContainerBloodChest) {
                    ++this.playersUsing;
                }
            }
            
            world.addBlockEvent(getPos(), getBlockState().getBlock(), 1, playersUsing);
        }

        prevLidAngle = lidAngle;
        float increaseAngle = 0.1F;
        if (playersUsing > 0 && lidAngle == 0.0F) {
            world.playSound(
                    (double) x + 0.5D,
                    (double) y + 0.5D,
                    (double) z + 0.5D,
                    SoundEvents.BLOCK_CHEST_OPEN,
                    SoundCategory.BLOCKS,
                    0.5F,
                    world.rand.nextFloat() * 0.1F + 0.9F,
                    false
            );
        }
        if (playersUsing == 0 && lidAngle > 0.0F || playersUsing > 0 && lidAngle < 1.0F) {
            float preIncreaseAngle = lidAngle;
            if (playersUsing > 0) {
                lidAngle += increaseAngle;
            } else {
                lidAngle -= increaseAngle;
            }
            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }
            float closedAngle = 0.5F;
            if (lidAngle < closedAngle && preIncreaseAngle >= closedAngle) {
                world.playSound(
                        (double) x + 0.5D,
                        (double) y + 0.5D,
                        (double) z + 0.5D,
                        SoundEvents.BLOCK_CHEST_CLOSE,
                        SoundCategory.BLOCKS,
                        0.5F,
                        world.rand.nextFloat() * 0.1F + 0.9F,
                        false
                );
            }
            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            playersUsing = j;
        }
        return true;
    }
    
    private void triggerPlayerUsageChange(int change) {
        if (world != null) {
            playersUsing += change;
            world.addBlockEvent(getPos(), getBlockState().getBlock(), 1, playersUsing);
        }
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerBloodChest(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.evilcraft.blood_chest");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}
