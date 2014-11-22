package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Double coal efficiency.
 * @author rubensworks
 *
 */
public class BloodPotash extends ConfigurableItem {

    private static BloodPotash _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodPotash(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodPotash getInstance() {
        return _instance;
    }

    private BloodPotash(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float coordX, float coordY, float coordZ) {
        boolean done = false;
        int attempts = 0;
        while (attempts < 2) {
            done = ItemDye.applyBonemeal(itemStack.copy(), world, x, y, z, player) | done;
            attempts++;
        }
        if (done) {
            itemStack.stackSize--;
            if (!world.isRemote) {
                world.playAuxSFX(2005, x, y, z, 0);
            }
            return true;
        }
        return super.onItemUse(itemStack, player, world, x, y, z, side, coordX, coordY, coordZ);
    }

}
