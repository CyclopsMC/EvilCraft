package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Config for the {@link ItemBiomeExtract}.
 * @author rubensworks
 *
 */
public class ItemBiomeExtractConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "If creative versions for all variants should be added to the creative tab.", requiresMcRestart = true)
    public static boolean creativeTabVariants = true;

    @ConfigurablePropertyCommon(category = "item",
            comment = "A list of biome names for which no Biome Extracts may be created.")
    public static List<String> craftingBlacklist = Lists.newArrayList();

    /**
     * A list of biome ids for which no Biome Extracts may be used.
     */
    @ConfigurablePropertyCommon(category = "item",
            comment = "A list of biome names for which no Biome Extracts may be used.")
    public static List<String> usageBlacklist = Lists.newArrayList();

    public ItemBiomeExtractConfig() {
        super(
                EvilCraft._instance,
                "biome_extract",
                (eConfig, properties) -> new ItemBiomeExtract(properties)
        );
        EvilCraft._instance.getModEventBus().addListener(this::onCreativeModeTabBuildContents);
    }

    public static boolean isCraftingBlacklisted(Holder<Biome> biome) {
        return craftingBlacklist.contains(biome.getRegisteredName());
    }

    public static boolean isUsageBlacklisted(Holder<Biome> biome) {
        return usageBlacklist.contains(biome.getRegisteredName());
    }

    @Override
    public Collection<java.util.function.Supplier<ItemStack>> getDefaultCreativeTabEntries() {
        // Register tab entries later, when the world is available
        return Collections.emptyList();
    }

    protected void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == getMod().getDefaultCreativeTab()) {
            List<ItemStack> list = Lists.newArrayList();
            list.add(new ItemStack(getInstance()));
            if (creativeTabVariants) {
                try {
                    ((ItemBiomeExtract) getInstance()).getBiomes(event.getParameters().holders()).forEach(biome -> {
                        list.add(((ItemBiomeExtract) getInstance()).createItemStack(biome, 1, event.getParameters().holders().lookupOrThrow(Registries.BIOME)));
                    });
                } catch (RuntimeException e) {
                    // Ignore errors
                }
            }
            event.acceptAll(list);
        }
    }
}
