package org.cyclops.evilcraft.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
public class ItemNecromancerStaffConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "The capacity of the container.", requiresMcRestart = true)
    public static int capacity = 1000 * 10;

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of Blood that will be drained per usage.", isCommandable = true)
    public static int usage = 1000 * 2;

    public ItemNecromancerStaffConfig() {
        super(
                EvilCraft._instance,
            "necromancer_staff",
                (eConfig, properties) -> new ItemNecromancerStaff(properties)
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
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
        return ((ItemNecromancerStaff) getInstance()).getDefaultCreativeTabEntries();
    }

}
