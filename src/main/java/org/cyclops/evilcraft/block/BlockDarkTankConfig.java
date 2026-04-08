package org.cyclops.evilcraft.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Config for the {@link BlockDarkTank}.
 * @author rubensworks
 *
 */
public class BlockDarkTankConfig extends BlockConfigCommon<ModBaseNeoForge<?>> {

    @ConfigurablePropertyCommon(category = "machine", comment = "The maximum tank size visible in the creative tabs. (Make sure that you do not cross the max int size.)")
    public static int maxTankCreativeSize = 4096000;

    @ConfigurablePropertyCommon(category = "machine", comment = "If creative versions for all fluids should be added to the creative tab.")
    public static boolean creativeTabFluids = true;

    @ConfigurablePropertyCommon(category = "item", comment = "If held buckets should be autofilled when enabled.", isCommandable = true)
    public static boolean autoFillBuckets = false;

    public BlockDarkTankConfig() {
        super(
                EvilCraft._instance,
            "dark_tank",
                (eConfig, properties) -> new BlockDarkTank(properties
                        .strength(0.5F)
                        .sound(SoundType.GLASS)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, eConfig.createDefaultItemProperties()
                        )
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        // Register items dynamically into tab, because when this is called, capabilities are not initialized yet.
        return Collections.emptyList();
    }

    @Override
    @Nullable
    public BlockClientConfig<ModBaseNeoForge<?>> constructBlockClientConfig() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            return new BlockDarkTankConfigClient(this);
        }
        return null;
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
        ((BlockDarkTank) getInstance()).fillItemCategory(list);
        return list;
    }
}
