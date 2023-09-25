package org.cyclops.evilcraft.blockentity.tickaction.spiritreanimator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.block.BlockSpiritReanimatorConfig;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.helper.MathHelpers;

/**
 * {@link ITickAction} that is able to reanimate boxes with spirits.
 * @author rubensworks
 *
 */
public class ReanimateTickAction implements ITickAction<BlockEntitySpiritReanimator> {

    @Override
    public boolean canTick(BlockEntitySpiritReanimator tile, ItemStack itemStack, int slot, int tick) {
        return tile.getTank().getFluidAmount() >= getRequiredMb(tile, tick) && tile.canWork();
    }

    @Override
    public void onTick(BlockEntitySpiritReanimator tile, ItemStack itemStack, int slot, int tick) {
        // Drain the tank a bit.
        tile.getTank().drain(getRequiredMb(tile, tick), IFluidHandler.FluidAction.EXECUTE);
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            ItemStack spawnEgg = ItemStack.EMPTY;
            EntityType<?> entityType = tile.getEntityType();
            if(entityType != null) {
                SpawnEggItem spawnEggItem = ForgeSpawnEggItem.fromEntityType(entityType);
                if (spawnEggItem != null) {
                    spawnEgg = new ItemStack(spawnEggItem);
                }
            }
            if(!spawnEgg.isEmpty() && addToProduceSlot(tile, spawnEgg)) {
                tile.getInventory().removeItem(BlockEntitySpiritReanimator.SLOT_EGG, 1);
            }
            if(BlockSpiritReanimatorConfig.clearBoxContents) {
                itemStack.setTag(new CompoundTag());
                tile.getInventory().setItem(BlockEntitySpiritReanimator.SLOT_BOX, itemStack);
            }
        }
    }

    protected int getRequiredMb(BlockEntitySpiritReanimator tile, int tick) {
        MutableDouble drain = new MutableDouble(BlockSpiritReanimatorConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, BlockEntitySpiritReanimator.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), tick);
    }

    @Override
    public float getRequiredTicks(BlockEntitySpiritReanimator tile, int slot, int tick) {
        MutableDouble drain = new MutableDouble(BlockSpiritReanimatorConfig.requiredTicks);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, BlockEntitySpiritReanimator.UPGRADEEVENT_SPEED));
        return (int) (double) drain.getValue();
    }

    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where reanimation happened.
     * @param itemStack The item to try to put in the output slot.
     * @return If the item could be added or joined in the output slot.
     */
    public boolean addToProduceSlot(BlockEntitySpiritReanimator tile, ItemStack itemStack) {
        return InventoryHelpers.addToSlot(tile.getInventory(), BlockEntitySpiritReanimator.SLOTS_OUTPUT, itemStack);
    }

}
