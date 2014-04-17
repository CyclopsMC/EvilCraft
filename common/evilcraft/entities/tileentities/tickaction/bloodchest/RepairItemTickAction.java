package evilcraft.entities.tileentities.tickaction.bloodchest;

import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import evilcraft.Configs;
import evilcraft.api.config.configurable.ConfigurableEnchantment;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.entities.tileentities.TileBloodChest;

/**
 * {@link ITickAction} that can repair items using blood.
 * @author rubensworks
 *
 */
public class RepairItemTickAction implements ITickAction<TileBloodChest> {
    
    private static final int CHANCE_RANDOM_ENCHANT = 10000;
    /**
     * All the possible bad enchantments
     */
    public static final LinkedList<ConfigurableEnchantment> BAD_ENCHANTS = new LinkedList<ConfigurableEnchantment>();
    static {
        if(Configs.isEnabled(EnchantmentBreakingConfig.class)) {
            BAD_ENCHANTS.add(EnchantmentBreaking.getInstance());
        }
    }
    
    @Override
    public boolean canTick(TileBloodChest tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isEmpty() && itemStack != null;
    }
    
    private void repair(TileBloodChest tile, ItemStack itemStack) {
        tile.getTank().drain(BloodChestConfig.mBPerDamage, true);
        
        // Repair the item
        int newDamage = itemStack.getItemDamage() - 1;
        itemStack.setItemDamage(newDamage);
        
        // Add bad enchant with a certain chance
        if(BloodChestConfig.addRandomBadEnchants
                && tile.getWorldObj().rand.nextInt(CHANCE_RANDOM_ENCHANT) == 0) {
            ConfigurableEnchantment enchantment = BAD_ENCHANTS.get(tile.getWorldObj().rand.nextInt(BAD_ENCHANTS.size()));
            itemStack.addEnchantment(
                    enchantment,
                    enchantment.getMinLevel() + tile.getWorldObj().rand.nextInt(
                            enchantment.getMaxLevel() - enchantment.getMinLevel())
                    );
        }
    }

    @Override
    public void onTick(TileBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot)) {
            if(
                    !tile.getTank().isEmpty()
                    && tile.getTank().getFluidAmount() >= BloodChestConfig.mBPerDamage
                    && itemStack != null
                    && itemStack.isItemDamaged()
                    && itemStack.getItem().isRepairable()
                    ) {
                repair(tile, itemStack);
            }
        }
    }

    @Override
    public int getRequiredTicks(TileBloodChest tile, int slot) {
        return BloodChestConfig.ticksPerDamage;
    }
    
}
