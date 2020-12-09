package org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.block.BlockSanguinaryEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link ITickAction} that can accumulate the environment to an item using blood..
 * @author rubensworks
 *
 */
public class AccumulateItemTickAction implements ITickAction<TileSanguinaryEnvironmentalAccumulator> {

    @Override
    public boolean canTick(TileSanguinaryEnvironmentalAccumulator tile, ItemStack itemStack, int slot, int tick) {
        // Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if(tile.getInventory().getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE) != null
                && tile.getTileWorkingMetadata().canConsume(tile.getInventory()
                .getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE), tile.getWorld())) {
            ItemStack production = tile.getInventory().getStackInSlot(tile.getTileWorkingMetadata().getProduceSlot());
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
    public void onTick(TileSanguinaryEnvironmentalAccumulator tile, ItemStack itemStack, int slot, int tick) {
        Optional<RecipeEnvironmentalAccumulator> optionalRecipe = getRecipe(tile);
        if(optionalRecipe.isPresent() && tick >= getRequiredTicks(tile, optionalRecipe.get())) {
            RecipeEnvironmentalAccumulator recipe = optionalRecipe.get();
            ItemStack result = recipe.getCraftingResult(tile.getInventory());
            if(addToProduceSlot(tile, result)) {
                tile.getInventory().decrStackSize(tile.getTileWorkingMetadata().getConsumeSlot(), 1);
                tile.getVirtualTank().drain(getRequiredFluidAmount(tile, recipe), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public final float getRequiredTicks(TileSanguinaryEnvironmentalAccumulator tile, int slot, int tick) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(getRecipe(tile).get()));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileSanguinaryEnvironmentalAccumulator.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

    public static int getUsage(int baseCooldownTime) {
        return baseCooldownTime * BlockSanguinaryEnvironmentalAccumulatorConfig.baseUsage;
    }

    protected int getRequiredFluidAmount(TileSanguinaryEnvironmentalAccumulator tile, RecipeEnvironmentalAccumulator recipe) {
        MutableInt amount = new MutableInt(getUsage(recipe.getCooldownTime()));
        Upgrades.sendEvent(tile,
                new UpgradeSensitiveEvent<MutableInt>(amount, TileSanguinaryEnvironmentalAccumulator.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, amount.getValue());
    }
    
    private Optional<RecipeEnvironmentalAccumulator> getRecipe(TileSanguinaryEnvironmentalAccumulator tile) {
        return tile.getRecipe(tile.getInventory().getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE));
    }
    
    private int getUnmodifiedRequiredTicks(RecipeEnvironmentalAccumulator recipe) {
        return recipe.getDuration();
    }

    private int getRequiredTicks(TileSanguinaryEnvironmentalAccumulator tile, RecipeEnvironmentalAccumulator recipe) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(recipe));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<>(duration, TileSanguinaryEnvironmentalAccumulator.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

    protected ItemStack willProduceItem(TileSanguinaryEnvironmentalAccumulator tile, RecipeEnvironmentalAccumulator recipe) {
        return recipe.getCraftingResult(tile.getInventory());
    }

    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where infusion happened.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToProduceSlot(TileSanguinaryEnvironmentalAccumulator tile, ItemStack itemStack) {
        return InventoryHelpers.addToSlot(tile.getInventory(), tile.getTileWorkingMetadata().getProduceSlot(), itemStack);
    }
    
}
