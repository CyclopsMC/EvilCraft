package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemInvigoratingPendant}.
 * @author rubensworks
 *
 */
public class ItemInvigoratingPendantConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "The capacity of the pendant.", requiresMcRestart = true)
    public static int capacity = IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume() * 5;

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of blood to drain after each clearing of one bad effect.", isCommandable = true)
    public static int usage = 100;

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of seconds that will be reduced from the first found bad effect.", isCommandable = true)
    public static int reduceDuration = 30;

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of Blood to drain after one reduction/clearing of fire. -1 to disable fire extinguishing.", isCommandable = true)
    public static int fireUsage = 500;

    public ItemInvigoratingPendantConfig() {
        super(
                EvilCraft._instance,
                "invigorating_pendant",
                (eConfig, properties) -> new ItemInvigoratingPendant(properties
                        .stacksTo(1))
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public String getConfigPropertyPrefix(ConfigurablePropertyCommon annotation) {
        return "invig_pendant";
    }

    @Override
    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries()) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries() {
        return ((ItemInvigoratingPendant) getInstance()).getDefaultCreativeTabEntries();
    }

}
