package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;

/**
 * Config for the {@link ItemInvigoratingPendant}.
 * @author rubensworks
 *
 */
public class ItemInvigoratingPendantConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The capacity of the pendant.", requiresMcRestart = true)
    public static int capacity = FluidHelpers.BUCKET_VOLUME * 5;

    @ConfigurableProperty(category = "item", comment = "The amount of blood to drain after each clearing of one bad effect.", isCommandable = true)
    public static int usage = 100;

    @ConfigurableProperty(category = "item", comment = "The amount of seconds that will be reduced from the first found bad effect.", isCommandable = true)
    public static int reduceDuration = 30;

    @ConfigurableProperty(category = "item", comment = "The amount of Blood to drain after one reduction/clearing of fire. -1 to disable fire extinguishing.", isCommandable = true)
    public static int fireUsage = 500;

    public ItemInvigoratingPendantConfig() {
        super(
                EvilCraft._instance,
                "invigorating_pendant",
                eConfig -> new ItemInvigoratingPendant(new Item.Properties()

                        .stacksTo(1))
        );
    }

    @Override
    protected String getConfigPropertyPrefix(ConfigurableProperty annotation) {
        return "invig_pendant";
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
        return ((ItemInvigoratingPendant) getInstance()).getDefaultCreativeTabEntries();
    }

}
