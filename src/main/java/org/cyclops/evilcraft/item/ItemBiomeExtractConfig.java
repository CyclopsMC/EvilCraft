package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Config for the {@link ItemBiomeExtract}.
 * @author rubensworks
 *
 */
public class ItemBiomeExtractConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "If creative versions for all variants should be added to the creative tab.", requiresMcRestart = true)
    public static boolean creativeTabVariants = true;

    @ConfigurableProperty(category = "item",
            comment = "A list of biome names for which no Biome Extracts may be created.")
    public static List<String> craftingBlacklist = Lists.newArrayList();

    /**
     * A list of biome ids for which no Biome Extracts may be used.
     */
    @ConfigurableProperty(category = "item",
            comment = "A list of biome names for which no Biome Extracts may be used.")
    public static List<String> usageBlacklist = Lists.newArrayList();

    public ItemBiomeExtractConfig() {
        super(
                EvilCraft._instance,
                "biome_extract",
                eConfig -> new ItemBiomeExtract(new Item.Properties()
                        )
        );
        if (MinecraftHelpers.isClientSide()) {
            EvilCraft._instance.getModEventBus().addListener(this::onRegisterColors);
        }
        EvilCraft._instance.getModEventBus().addListener(this::onCreativeModeTabBuildContents);
    }

    @OnlyIn(Dist.CLIENT)
    public void onRegisterColors(RegisterColorHandlersEvent.Item event) {
        event.register(new ItemBiomeExtract.ItemColor(), getInstance());
    }

    public static boolean isCraftingBlacklisted(Holder<Biome> biome) {
        return craftingBlacklist.contains(biome.getRegisteredName());
    }

    public static boolean isUsageBlacklisted(Holder<Biome> biome) {
        return usageBlacklist.contains(biome.getRegisteredName());
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
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
