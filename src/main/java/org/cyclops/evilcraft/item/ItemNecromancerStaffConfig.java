package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
public class ItemNecromancerStaffConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The capacity of the container.", requiresMcRestart = true)
    public static int capacity = FluidHelpers.BUCKET_VOLUME * 10;

    @ConfigurableProperty(category = "item", comment = "The amount of Blood that will be drained per usage.", isCommandable = true)
    public static int usage = FluidHelpers.BUCKET_VOLUME * 2;

    public ItemNecromancerStaffConfig() {
        super(
                EvilCraft._instance,
            "necromancer_staff",
                eConfig -> new ItemNecromancerStaff(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
    }
    
}
