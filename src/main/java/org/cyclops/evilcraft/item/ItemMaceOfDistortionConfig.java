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
 * Config for the {@link ItemMaceOfDistortion}.
 * @author rubensworks
 *
 */
public class ItemMaceOfDistortionConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "The attack speed modifier of this mace, which is added to the default player attack speed of 4.", isCommandable = true)
    public static double attackSpeed = -2.4;

    public ItemMaceOfDistortionConfig() {
        super(
                EvilCraft._instance,
            "mace_of_distortion",
                (eConfig, properties) -> new ItemMaceOfDistortion(properties
                        .enchantable(15))
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
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
        return ((ItemMaceOfDistortion) getInstance()).getDefaultCreativeTabEntries();
    }

}
