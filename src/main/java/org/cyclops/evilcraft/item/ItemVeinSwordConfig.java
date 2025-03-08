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
 * Config for the {@link ItemVeinSword}.
 * @author rubensworks
 *
 */
public class ItemVeinSwordConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "The multiply boost this sword has on the blood that is obtained.", isCommandable = true)
    public static double extractionBoost = 2.0;

    @ConfigurablePropertyCommon(category = "item", comment = "Maximum uses for this item.")
    public static int durability = 32;

    public ItemVeinSwordConfig() {
        super(
                EvilCraft._instance,
                "vein_sword",
                (eConfig, properties) -> new ItemVeinSword(properties)
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        return Collections.emptyList();
    }

    protected Collection<ItemStack> dynamicCreativeTabEntries(CreativeModeTab.ItemDisplayParameters parameters) {
        return Collections.singleton(((ItemVeinSword) getInstance()).getEnchantedItemStack(parameters.holders()));
    }

    protected void fillCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EvilCraft._instance.getDefaultCreativeTab()) {
            for (ItemStack itemStack : dynamicCreativeTabEntries(event.getParameters())) {
                event.accept(itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
}
