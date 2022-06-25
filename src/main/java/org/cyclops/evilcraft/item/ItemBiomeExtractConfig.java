package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                        .tab(EvilCraft._instance.getDefaultItemGroup()))
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onModLoaded(FMLLoadCompleteEvent event) {
        Minecraft.getInstance().getItemColors().register(new ItemBiomeExtract.ItemColor(), getInstance());
    }

    public static boolean isCraftingBlacklisted(Biome biome) {
        return craftingBlacklist.contains(ForgeRegistries.BIOMES.getKey(biome).toString());
    }

    public static boolean isUsageBlacklisted(Biome biome) {
        return usageBlacklist.contains(ForgeRegistries.BIOMES.getKey(biome).toString());
    }
}
