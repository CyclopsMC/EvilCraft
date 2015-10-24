package evilcraft.item;

import com.google.common.collect.Sets;
import evilcraft.EvilCraft;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.IChangedCallback;
import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.world.biome.BiomeGenBase;

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
    public static int envirAccCooldownTime = 50;

    /**
     * A list of biome ids for which no Biome Extracts may be created.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM,
            comment = "A list of biome ids for which no Biome Extracts may be created.",
            changedCallback = CraftingBlacklistChanged.class)
    public static String[] craftingBlacklist = new String[]{
            String.valueOf(BiomeGenBase.sky.biomeID),
    };

    /**
     * A list of biome ids for which no Biome Extracts may be used.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM,
            comment = "A list of biome ids for which no Biome Extracts may be used.",
            changedCallback = UsageBlacklistChanged.class)
    public static String[] usageBlacklist = new String[]{
            String.valueOf(BiomeGenBase.sky.biomeID),
    };

    private Set<Integer> craftingBlacklistIds = Sets.newHashSet();
    private Set<Integer> usageBlacklistIds = Sets.newHashSet();

    /**
     * Make a new instance.
     */
    public BiomeExtractConfig() {
        super(
                true,
                "biomeExtract",
                null,
                BiomeExtract.class
        );
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
     * @param config The config where each element is in the form 'potionid:multiplier'.
     * @param blacklistIds The set of ids to set
     */
    public void setBlacklist(String[] config, Set<Integer> blacklistIds) {
        blacklistIds.clear();
        for (String line : config) {
            try {
                int biomeId = Integer.parseInt(line);
                if (biomeId >= BiomeGenBase.getBiomeGenArray().length || BiomeGenBase.getBiomeGenArray()[biomeId] == null) {
                    EvilCraft.log("Invalid line '" + line + "' found for "
                            + "a Biome Extract blacklist config: " + line + " does not refer to an existing biome; skipping.");
                }
                blacklistIds.add(biomeId);
            } catch (NumberFormatException e) {
                EvilCraft.log("Invalid line '" + line + "' found for "
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
     * If the given biome id is blacklisted for usage.
     * @param biomeId The biome id
     * @return If blacklisted
     */
    public boolean isUsageBlacklisted(int biomeId) {
        return usageBlacklistIds.contains(biomeId);
    }

}
