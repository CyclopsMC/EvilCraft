package org.cyclops.evilcraft.item;

import com.google.common.collect.Sets;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.IChangedCallback;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Set;

/**
 * Config for the {@link BiomeExtract}.
 * @author rubensworks
 *
 */
public class BiomeExtractConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BiomeExtractConfig _instance;

    /**
     * If creative versions for all variants should be added to the creative tab.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If creative versions for all variants should be added to the creative tab.", requiresMcRestart = true)
    public static boolean creativeTabVariants = true;

    /**
     * If this should have recipes inside the Environmental Accumulator.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If this should have recipes inside the Environmental Accumulator.", requiresMcRestart = true)
    public static boolean hasRecipes = true;

    /**
     * The cooldown time int the Environmental Accumulator recipe.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The cooldown time int the Environmental Accumulator recipe.", requiresMcRestart = true)
    public static int envirAccCooldownTime = 500;

    /**
     * A list of biome ids for which no Biome Extracts may be created.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM,
            comment = "A list of biome names for which no Biome Extracts may be created.",
            changedCallback = CraftingBlacklistChanged.class)
    public static String[] craftingBlacklist = new String[]{
            String.valueOf(BiomeGenBase.biomeRegistry.getNameForObject(Biomes.sky)),
    };

    /**
     * A list of biome ids for which no Biome Extracts may be used.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM,
            comment = "A list of biome names for which no Biome Extracts may be used.",
            changedCallback = UsageBlacklistChanged.class)
    public static String[] usageBlacklist = new String[]{
            String.valueOf(BiomeGenBase.biomeRegistry.getNameForObject(Biomes.sky)),
    };

    private Set<ResourceLocation> craftingBlacklistIds = Sets.newHashSet();
    private Set<ResourceLocation> usageBlacklistIds = Sets.newHashSet();

    /**
     * Make a new instance.
     */
    public BiomeExtractConfig() {
        super(
                EvilCraft._instance,
                true,
                "biomeExtract",
                null,
                BiomeExtract.class
        );
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if(((BiomeExtract) getItemInstance()).isEmpty(itemStack)) {
            return super.getModelName(itemStack) + "_empty";
        }
        return super.getModelName(itemStack);
    }

    /**
     * Callback for when the crafting blacklist property is changed.
     * @author rubensworks
     *
     */
    public static class CraftingBlacklistChanged implements IChangedCallback {

        private static boolean calledOnce = false;

        @Override
        public void onChanged(Object value) {
            if(calledOnce) {
                BiomeExtractConfig._instance.setBlacklist((String[]) value, BiomeExtractConfig._instance.craftingBlacklistIds);
            }
            calledOnce = true;
        }

        @Override
        public void onRegisteredPostInit(Object value) {
            onChanged(value);
        }

    }

    /**
     * Callback for when the usage blacklist property is changed.
     * @author rubensworks
     *
     */
    public static class UsageBlacklistChanged implements IChangedCallback {

        private static boolean calledOnce = false;

        @Override
        public void onChanged(Object value) {
            if(calledOnce) {
                BiomeExtractConfig._instance.setBlacklist((String[]) value, BiomeExtractConfig._instance.usageBlacklistIds);
            }
            calledOnce = true;
        }

        @Override
        public void onRegisteredPostInit(Object value) {
            onChanged(value);
        }

    }

    /**
     * Register the usage multipliers config from the given string array.
     * @param config The config where each element is in the form 'biomename'.
     * @param blacklistNames The set of biome names to set.
     */
    public void setBlacklist(String[] config, Set<ResourceLocation> blacklistNames) {
        blacklistNames.clear();
        for (String line : config) {
            try {
                ResourceLocation biomeKey = new ResourceLocation(line);
                if (!BiomeGenBase.biomeRegistry.containsKey(biomeKey)) {
                    EvilCraft.clog("Invalid line '" + line + "' found for "
                            + "a Biome Extract blacklist config: " + line + " does not refer to an existing biome; skipping.");
                } else {
                    blacklistNames.add(biomeKey);
                }
            } catch (NumberFormatException e) {
                EvilCraft.clog("Invalid line '" + line + "' found for "
                        + "a Biome Extract blacklist config: " + line + " is not a number; skipping.");
            }
        }
    }

    /**
     * If the given biome id is blacklisted for crafting.
     * @param biomeId The biome id
     * @return If blacklisted
     */
    public boolean isCraftingBlacklisted(int biomeId) {
        return craftingBlacklistIds.contains(biomeId);
    }

    /**
     * If the given biome is blacklisted for usage.
     * @param biome The biome
     * @return If blacklisted
     */
    public boolean isUsageBlacklisted(BiomeGenBase biome) {
        return usageBlacklistIds.contains(BiomeGenBase.biomeRegistry.getNameForObject(biome).toString());
    }
}
