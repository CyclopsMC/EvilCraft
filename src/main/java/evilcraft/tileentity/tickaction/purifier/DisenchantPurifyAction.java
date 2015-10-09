package evilcraft.tileentity.tickaction.purifier;

import evilcraft.api.tileentity.purifier.IPurifierAction;
import evilcraft.core.helper.EnchantmentHelpers;
import evilcraft.item.BlookConfig;
import evilcraft.tileentity.TilePurifier;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Purifier action to remove enchantments from tools.
 * @author Ruben Taelman
 */
public class DisenchantPurifyAction implements IPurifierAction {

    /**
     * The allowed book instance.
     */
    public static final Item ALLOWED_BOOK = BlookConfig._instance.downCast().getItemInstance();

    private static final int PURIFY_DURATION = 60;

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        return itemStack.getItem() == ALLOWED_BOOK;
    }

    @Override
    public boolean canWork(TilePurifier tile) {
        if(tile.getBucketsFloored() == tile.getMaxBuckets() && tile.getAdditionalItem() != null &&
                tile.getAdditionalItem().getItem() == ALLOWED_BOOK) {
            NBTTagList enchantmentList = tile.getPurifyItem().getEnchantmentTagList();
            return enchantmentList != null && enchantmentList.tagCount() > 0;
        }
        return false;
    }

    @Override
    public boolean work(TilePurifier tile) {
        boolean done = false;

        ItemStack purifyItem = tile.getPurifyItem();
        World world = tile.getWorldObj();
        int tick = tile.getTick();

        // Try disenchanting.
        NBTTagList enchantmentList = purifyItem.getEnchantmentTagList();
        if (enchantmentList != null && enchantmentList.tagCount() > 0) {
            if (tick >= PURIFY_DURATION) {
                if (!world.isRemote) {
                    // Init enchantment data.
                    int enchantmentListID = world.rand.nextInt(enchantmentList.tagCount());
                    int level = EnchantmentHelpers.getEnchantmentLevel(purifyItem, enchantmentListID);
                    int id = EnchantmentHelpers.getEnchantmentID(purifyItem, enchantmentListID);
                    ItemStack enchantedItem = new ItemStack(Items.enchanted_book, 1);

                    // Set the enchantment book.
                    Map<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
                    enchantments.put(id, level);
                    EnchantmentHelper.setEnchantments(enchantments, enchantedItem);

                    // Define the enchanted book level.
                    EnchantmentHelpers.setEnchantmentLevel(purifyItem, enchantmentListID, 0);

                    // Put the enchanted book in the book slot.
                    tile.setAdditionalItem(enchantedItem);
                }
                tile.setBuckets(0, tile.getBucketsRest());
                done = true;
            }
            if (world.isRemote) {
                tile.showEffect();
                tile.showEnchantingEffect();
            }
        }

        return done;
    }

}
