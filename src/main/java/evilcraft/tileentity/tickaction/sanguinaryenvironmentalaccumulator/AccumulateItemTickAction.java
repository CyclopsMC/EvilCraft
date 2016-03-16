package evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.SanguinaryEnvironmentalAccumulatorConfig;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * {@link ITickAction} that can accumulate the environment to an item using blood..
 * @author rubensworks
 *
 */
public class AccumulateItemTickAction implements ITickAction<TileSanguinaryEnvironmentalAccumulator> {

    @Override
    public boolean canTick(TileSanguinaryEnvironmentalAccumulator tile, ItemStack itemStack, int slot, int tick) {
        boolean precondition = false;
        // Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if(tile.getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE) != null && tile.canConsume(tile.getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE))) {
            ItemStack production = tile.getInventory().getStackInSlot(tile.getProduceSlot());
            ItemStack willProduce = willProduceItem(tile);
            if(production == null) {
                precondition = true;
            } else if(willProduce != null && production.getItem() == willProduceItem(tile).getItem() && production.getItemDamage() == willProduceItem(tile).getItemDamage()) {
                if(production.stackSize + willProduce.stackSize <= production.getMaxStackSize())
                    precondition = true;
            }
        }
        return precondition && tile.canWork() && tile.getVirtualTank().getFluidAmount() >= getRequiredFluidAmount(tile, getRecipe(tile));
    }

    @Override
    public void onTick(TileSanguinaryEnvironmentalAccumulator tile, ItemStack itemStack, int slot, int tick) {
        IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe = getRecipe(tile);
        if(tick >= getRequiredTicks(tile, recipe)) {
            if(recipe != null) {
                if(addToProduceSlot(tile, recipe.getProperties().getResultOverride().getResult(tile.getWorldObj(),
                        tile.xCoord, tile.yCoord, tile.zCoord, recipe.getOutput().getConditionalItemStack(itemStack)))) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                    tile.getVirtualTank().drain(getRequiredFluidAmount(tile, recipe), true);
                }
            }
        }
    }

    @Override
    public final float getRequiredTicks(TileSanguinaryEnvironmentalAccumulator tile, int slot, int tick) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, slot));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileSanguinaryEnvironmentalAccumulator.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

    public static int getUsage(EnvironmentalAccumulatorRecipeProperties properties) {
        return properties.getCooldownTime() * SanguinaryEnvironmentalAccumulatorConfig.baseUsage;
    }

    protected int getRequiredFluidAmount(TileSanguinaryEnvironmentalAccumulator tile,
                                         IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        MutableInt amount = new MutableInt(getUsage(recipe.getProperties()));
        Upgrades.sendEvent(tile,
                new UpgradeSensitiveEvent<MutableInt>(amount, TileSanguinaryEnvironmentalAccumulator.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, amount.getValue());
    }
    
    private IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>
        getRecipe(TileSanguinaryEnvironmentalAccumulator tile) {
        return tile.getRecipe(tile.getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE));
    }

    protected int getUnmodifiedRequiredTicks(TileSanguinaryEnvironmentalAccumulator tile, int slot) {
        return getUnmodifiedRequiredTicks(tile, getRecipe(tile));
    }
    
    private int getUnmodifiedRequiredTicks(TileSanguinaryEnvironmentalAccumulator tile,
                                           IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        return recipe.getProperties().getDuration();
    }

    private int getRequiredTicks(TileSanguinaryEnvironmentalAccumulator tile,
                                 IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, recipe));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileSanguinaryEnvironmentalAccumulator.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

    protected ItemStack willProduceItem(TileSanguinaryEnvironmentalAccumulator tile) {
        return getRecipe(tile).getOutput().getConditionalItemStack(tile.getStackInSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE));
    }

    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where infusion happened.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToProduceSlot(TileSanguinaryEnvironmentalAccumulator tile, ItemStack itemStack) {
        return InventoryHelpers.addToSlot(tile.getInventory(), tile.getProduceSlot(), itemStack);
    }
    
}
