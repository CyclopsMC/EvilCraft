package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link ItemBroomPart}.
 * @author rubensworks
 *
 */
public class ItemBroomPartConfig extends ItemConfigCommon<ModBaseNeoForge<?>> {

    public ItemBroomPartConfig() {
        super(
            EvilCraft._instance,
            "broom_part",
                (eConfig, properties) -> new ItemBroomPart(properties)
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public @Nullable ItemClientConfig<ModBaseNeoForge<?>> constructItemClientConfig() {
        return new ItemBroomPartConfigClient(this);
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
        NonNullList<ItemStack> list = NonNullList.create();
        ((ItemBroomPart) getInstance()).fillItemCategory(list);
        return list;
    }

}
