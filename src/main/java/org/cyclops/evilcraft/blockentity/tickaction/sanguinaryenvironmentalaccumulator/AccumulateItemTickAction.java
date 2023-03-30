package org.cyclops.evilcraft.blockentity.tickaction.sanguinaryenvironmentalaccumulator;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.block.BlockSanguinaryEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link ITickAction} that can accumulate the environment to an item using blood..
 * @author rubensworks
 *
 */
public class AccumulateItemTickAction implements ITickAction<BlockEntitySanguinaryEnvironmentalAccumulator> {

    @Override
    public boolean canTick(BlockEntitySanguinaryEnvironmentalAccumulator tile, ItemStack itemStack, int slot, int tick) {
        // Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if (!tile.getInventory().getItem(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE).isEmpty()
                && tile.getTileWorkingMetadata().canConsume(tile.getInventory()
                .getItem(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE), tile.getLevel())) {
            ItemStack production = tile.getInventory().getItem(tile.getTileWorkingMetadata().getProduceSlot());
            return getRecipe(tile)
                    .map(recipe -> {
                        boolean precondition = false;
                        ItemStack willProduce = willProduceItem(tile, recipe);
                        if (production.isEmpty()) {
                            precondition = true;
                        } else if (!willProduce.isEmpty()
                                && production.getItem() == willProduceItem(tile, recipe).getItem()
                                && Objects.equals(production.getTag(), willProduceItem(tile, recipe).getTag())) {
                            if(production.getCount() + willProduce.getCount() <= production.getMaxStackSize())
                                precondition = true;
                        }
                        return precondition && tile.canWork() && tile.getVirtualTank().getFluidAmount() >= getRequiredFluidAmount(tile, recipe);
                    })
                    .orElse(false);
        }
        return false;
    }

    @Override
    public void onTick(BlockEntitySanguinaryEnvironmentalAccumulator tile, ItemStack itemStack, int slot, int tick) {
        Optional<RecipeEnvironmentalAccumulator> optionalRecipe = getRecipe(tile);
        if(optionalRecipe.isPresent() && tick >= getRequiredTicks(tile, optionalRecipe.get())) {
            RecipeEnvironmentalAccumulator recipe = optionalRecipe.get();
            ItemStack result = recipe.assemble(tile.getInventory(), tile.getLevel().registryAccess());
            if(addToProduceSlot(tile, result)) {
                tile.getInventory().removeItem(tile.getTileWorkingMetadata().getConsumeSlot(), 1);
                tile.getVirtualTank().drain(getRequiredFluidAmount(tile, recipe), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public final float getRequiredTicks(BlockEntitySanguinaryEnvironmentalAccumulator tile, int slot, int tick) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(getRecipe(tile).get()));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, BlockEntitySanguinaryEnvironmentalAccumulator.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

    public static int getUsage(int baseCooldownTime) {
        return baseCooldownTime * BlockSanguinaryEnvironmentalAccumulatorConfig.baseUsage;
    }

    protected int getRequiredFluidAmount(BlockEntitySanguinaryEnvironmentalAccumulator tile, RecipeEnvironmentalAccumulator recipe) {
        MutableInt amount = new MutableInt(getUsage(recipe.getCooldownTime()));
        Upgrades.sendEvent(tile,
                new UpgradeSensitiveEvent<MutableInt>(amount, BlockEntitySanguinaryEnvironmentalAccumulator.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, amount.getValue());
    }

    private Optional<RecipeEnvironmentalAccumulator> getRecipe(BlockEntitySanguinaryEnvironmentalAccumulator tile) {
        return tile.getRecipe(tile.getInventory().getItem(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE));
    }

    private int getUnmodifiedRequiredTicks(RecipeEnvironmentalAccumulator recipe) {
        return recipe.getDuration();
    }

    private int getRequiredTicks(BlockEntitySanguinaryEnvironmentalAccumulator tile, RecipeEnvironmentalAccumulator recipe) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(recipe));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<>(duration, BlockEntitySanguinaryEnvironmentalAccumulator.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

    protected ItemStack willProduceItem(BlockEntitySanguinaryEnvironmentalAccumulator tile, RecipeEnvironmentalAccumulator recipe) {
        return recipe.assemble(tile.getInventory(), tile.getLevel().registryAccess());
    }

    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where infusion happened.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToProduceSlot(BlockEntitySanguinaryEnvironmentalAccumulator tile, ItemStack itemStack) {
        return InventoryHelpers.addToSlot(tile.getInventory(), tile.getTileWorkingMetadata().getProduceSlot(), itemStack);
    }

}
