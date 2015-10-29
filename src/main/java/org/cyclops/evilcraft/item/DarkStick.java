package org.cyclops.evilcraft.item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;

/**
 * A dark stick.
 * @author rubensworks
 *
 */
public class DarkStick extends ConfigurableItem {
    
    private static DarkStick _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkStick getInstance() {
        return _instance;
    }

    public DarkStick(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
        return new EntityItemDarkStick(world, (EntityItem) location);
    }

}
