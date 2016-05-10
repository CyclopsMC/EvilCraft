package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;

/**
 * A dark gem that somehow caught fire.
 * @author rubensworks
 *
 */
public class BurningGemStone extends ConfigurableItem {
    
    private static BurningGemStone _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BurningGemStone getInstance() {
        return _instance;
    }

    public BurningGemStone(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
        this.setMaxDamage(BurningGemStoneConfig.maxDamage);
        this.setNoRepair();
    }
    
    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
    	return EnumRarity.UNCOMMON;
    }

    /**
     * Try damaging a burning gem stone inside the given player's inventory.
     * @param player The player.
     * @param swarmTier The tier of swarm.
     * @param simulate If damaging should be simulated.
     * @return If a burning gem stone was found and damaged.
     */
	public static boolean damageForPlayer(EntityPlayer player, int swarmTier, boolean simulate) {
		PlayerInventoryIterator it = new PlayerInventoryIterator(player);
		while(it.hasNext()) {
            Pair<Integer, ItemStack> current = it.nextIndexed();
            ItemStack itemStack = current.getRight();
			if(itemStack != null && itemStack.getItem() == BurningGemStone.getInstance()) {
				if(!simulate) {
					itemStack.damageItem(1 + swarmTier, player);
                    player.addExhaustion(10);
                    if(itemStack.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(current.getLeft(), null);
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, itemStack, null));
                    }
				}
				return true;
			}
		}
		return false;
	}

}
