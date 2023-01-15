package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.cyclops.evilcraft.block.BlockSpiritReanimator;
import org.cyclops.evilcraft.blockentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.blockentity.tickaction.spiritreanimator.ReanimateTickAction;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.blockentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimator;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A furnace that is able to cook spirits for their inner entity drops.
 * @author rubensworks
 *
 */
public class BlockEntitySpiritReanimator extends BlockEntityWorking<BlockEntitySpiritReanimator, MutableDouble> implements MenuProvider {

    /**
     * The id of the fluid container drainer slot.
     */
    public static final int SLOT_CONTAINER = 0;
    /**
     * The id of the box slot.
     */
    public static final int SLOT_BOX = 1;
    /**
     * The id of the egg slot.
     */
    public static final int SLOT_EGG = 2;
    /**
     * The id of the output slot.
     */
    public static final int SLOTS_OUTPUT = 3;

    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 4;

    public static Metadata METADATA = new Metadata();

    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidHelpers.BUCKET_VOLUME * 10;

    private static final Map<Class<?>, ITickAction<BlockEntitySpiritReanimator>> REANIMATE_COOK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntitySpiritReanimator>>();
    static {
        REANIMATE_COOK_TICK_ACTIONS.put(BlockBoxOfEternalClosure.class, new ReanimateTickAction());
    }

    private static final Map<Class<?>, ITickAction<BlockEntitySpiritReanimator>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntitySpiritReanimator>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<BlockEntitySpiritReanimator>());
    }
    public static int TICKERS = 2;

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private int reanimateTicker;
    @NBTPersist
    private Boolean caughtError = false;

    /**
     * Make a new instance.
     */
    public BlockEntitySpiritReanimator(BlockPos blockPos, BlockState blockState) {
        super(
                RegistryEntries.BLOCK_ENTITY_SPIRIT_REANIMATOR,
                blockPos,
                blockState,
                SLOTS,
                64,
                LIQUID_PER_SLOT,
                RegistryEntries.FLUID_BLOOD);
        reanimateTicker = addTicker(new TickComponent<>(this, REANIMATE_COOK_TICK_ACTIONS, SLOT_BOX, true, false));
        addTicker(new TickComponent<>(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true));

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<BlockEntitySpiritReanimator, MutableDouble>(1) {
            @Override
            public void applyUpgrade(BlockEntitySpiritReanimator upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableDouble> event) {
                if(event.getType() == UPGRADEEVENT_SPEED) {
                    double val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    double val = event.getObject().getValue();
                    val *= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<BlockEntitySpiritReanimator, MutableDouble>(2) {
            @Override
            public void applyUpgrade(BlockEntitySpiritReanimator upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableDouble> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    double val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
    }

    @Override
    protected void addItemHandlerCapabilities() {
        LazyOptional<IItemHandler> itemHandlerInput = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_EGG));
        LazyOptional<IItemHandler> itemHandlerOutput = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOTS_OUTPUT));
        LazyOptional<IItemHandler> itemHandlerContainer = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_CONTAINER, SLOT_BOX));
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.UP, itemHandlerInput);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN, itemHandlerOutput);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.EAST, itemHandlerContainer);
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory<BlockEntitySpiritReanimator>(inventorySize, stackSize, this) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack itemStack) {
                if(slot == SLOT_BOX)
                    return getTileWorkingMetadata().canConsume(itemStack, getLevel());
                if(slot == SLOT_EGG)
                    return itemStack.getItem() == Items.EGG
                            /*&& ResurgenceEgg.getInstance().isEmpty(itemStack) also enable in acceptance slot in container*/;
                if(slot == SLOT_CONTAINER)
                    return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD);
                return super.canPlaceItem(slot, itemStack);
            }
        };
    }

    @Override
    public Direction getRotation() {
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockSpiritReanimator.FACING, Direction.NORTH).getOpposite();
    }

    @Override
    protected SingleUseTank createTank(int tankSize) {
        return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
    }

    @Override
    protected int getWorkTicker() {
        return reanimateTicker;
    }

    /**
     * Get the entity type that is contained in a box.
     * @return The entity type or null if no box or invalid box.
     */
    @Nullable
    public EntityType<?> getEntityType() {
        ItemStack boxStack = getInventory().getItem(getConsumeSlot());
        if(boxStack.getItem() == getAllowedCookItem()) {
            return BlockBoxOfEternalClosure.getSpiritTypeRaw(boxStack.getTag());
        }
        return null;
    }

    /**
     * Get the allowed cooking item for this furnace.
     * @return The allowed item.
     */
    public static Item getAllowedCookItem() {
        return RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE;
    }

    /**
     * Get the id of the box slot.
     * @return id of the box slot.
     */
    public int getConsumeSlot() {
        return SLOT_BOX;
    }

    @Override
    public Metadata getTileWorkingMetadata() {
        return METADATA;
    }

    @Override
    public boolean canWork() {
        ItemStack eggStack = getInventory().getItem(SLOT_EGG);
        ItemStack outputStack = getInventory().getItem(BlockEntitySpiritReanimator.SLOTS_OUTPUT);
        EntityType<?> entityType = getEntityType();
        boolean validNameStack = entityType != null
                && (outputStack.isEmpty() ||
                    (outputStack.getMaxStackSize() > outputStack.getCount()
                        && ForgeSpawnEggItem.fromEntityType(entityType) == outputStack.getItem()));
        return !eggStack.isEmpty() && validNameStack;
    }

    @Override
    public void onStateChanged() {
        level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos()).setValue(BlockSpiritReanimator.ON, isWorking()));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new ContainerSpiritReanimator(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.evilcraft.spirit_reanimator");
    }

    private static class Metadata extends BlockEntityWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, Level world) {
            return !itemStack.isEmpty() && getAllowedCookItem() == itemStack.getItem();
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_SPIRIT_REANIMATOR;
        }
    }

}
