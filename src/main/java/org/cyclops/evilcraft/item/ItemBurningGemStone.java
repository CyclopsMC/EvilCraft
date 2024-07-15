package org.cyclops.evilcraft.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A dark gem that somehow caught fire.
 * @author rubensworks
 *
 */
public class ItemBurningGemStone extends Item {

    public ItemBurningGemStone(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ItemBurningGemStoneConfig.maxDamage;
    }

    /**
     * Try damaging a burning gem stone inside the given player's inventory.
     * @param player The player.
     * @param swarmTier The tier of swarm.
     * @param simulate If damaging should be simulated.
     * @return If a burning gem stone was found and damaged.
     */
    public static boolean damageForPlayer(Player player, int swarmTier, boolean simulate) {
        PlayerInventoryIterator it = new PlayerInventoryIterator(player);
        while(it.hasNext()) {
            Pair<Integer, ItemStack> current = it.nextIndexed();
            ItemStack itemStack = current.getRight();
            if(!itemStack.isEmpty() && itemStack.getItem() == RegistryEntries.ITEM_BURNING_GEM_STONE.get()) {
                if(!simulate) {
                    itemStack.hurtAndBreak(1 + swarmTier, (ServerLevel) player.level(), player, (i) -> {
                        player.getInventory().setItem(current.getLeft(), ItemStack.EMPTY);
                        NeoForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, itemStack, null));
                    });
                    player.causeFoodExhaustion(10);
                }
                return true;
            }
        }
        return false;
    }

}
