package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Double coal efficiency.
 * @author rubensworks
 *
 */
public class BloodPotash extends ConfigurableItem {

    private static BloodPotash _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodPotash getInstance() {
        return _instance;
    }

    public BloodPotash(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float coordX, float coordY, float coordZ) {
        boolean done = false;
        int attempts = 0;
        while (attempts < 2) {
            done = ItemDye.applyBonemeal(itemStack.copy(), world, blockPos, player) | done;
            attempts++;
        }
        if (done) {
            itemStack.stackSize--;
            if (!world.isRemote) {
                world.playAuxSFX(2005, blockPos, 0);
            }
            return true;
        }
        return super.onItemUse(itemStack, player, world, blockPos, side, coordX, coordY, coordZ);
    }

}
