package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Purifier action to revert mob heads to the previous step of the Blood Infuser head progression.
 * @author Ruben Taelman
 */
public class MobHeadDowngradePurifyAction implements IPurifierAction {

    /**
     * Mapping of mob heads to the head they are reverted to.
     */
    public static final Map<Item, Item> HEAD_DOWNGRADES = ImmutableMap.of(
            Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD,
            Items.CREEPER_HEAD, Items.ZOMBIE_HEAD,
            Items.ZOMBIE_HEAD, Items.SKELETON_SKULL
    );

    private static final int PURIFY_DURATION = 60;

    @Nullable
    protected Item getDowngrade(ItemStack itemStack) {
        return itemStack.isEmpty() ? null : HEAD_DOWNGRADES.get(itemStack.getItem());
    }

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        return getDowngrade(itemStack) != null;
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canWork(BlockEntityPurifier tile) {
        return tile.getBucketsFloored() == tile.getMaxBuckets()
                && getDowngrade(tile.getPurifyItem()) != null;
    }

    @Override
    public boolean work(BlockEntityPurifier tile) {
        Item downgrade = getDowngrade(tile.getPurifyItem());
        if (downgrade == null) {
            return false;
        }

        Level world = tile.getLevel();
        if (tile.getTick() >= PURIFY_DURATION) {
            if (!world.isClientSide()) {
                tile.setPurifyItem(new ItemStack(downgrade));
            }
            tile.setBuckets(0, tile.getBucketsRest());
            return true;
        }
        if (world.isClientSide()) {
            tile.showEffect();
        }
        return false;
    }

}
