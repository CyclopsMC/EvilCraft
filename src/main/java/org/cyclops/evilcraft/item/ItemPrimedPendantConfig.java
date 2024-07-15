package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Config for the {@link ItemPrimedPendant}.
 * @author rubensworks
 *
 */
public class ItemPrimedPendantConfig extends ItemConfig {

    private static final String DELIMITER = ";";

    @ConfigurableProperty(category = "item", comment = "The capacity of the pendant.", requiresMcRestart = true)
    public static int capacity = FluidHelpers.BUCKET_VOLUME * 5;

    @ConfigurableProperty(category = "item", comment = "The amount of Blood to drain after one effect application.", isCommandable = true)
    public static int usage = 10;

    @ConfigurableProperty(category = "item",
            comment = "Usage multipliers. Potion ids are first, followed by floating numbers. A number smaller than one blacklists that potion.")
    public static List<String> potionMultipliers = Lists.newArrayList(
            "minecraft:health_boost" + DELIMITER + "-1",
            "minecraft:regeneration" + DELIMITER + "10"
    );

    public ItemPrimedPendantConfig() {
        super(
                EvilCraft._instance,
                "primed_pendant",
                eConfig -> new ItemPrimedPendant(new Item.Properties()

                        .stacksTo(1))
        );
        EvilCraft._instance.getModEventBus().addListener(this::fillCreativeTab);
    }

    public static double getMultiplier(Holder<MobEffect> potion) {
        Map<Integer, Double> multipliers = Maps.newHashMap();
        multipliers.clear();
        for(String line : potionMultipliers) {
            String[] split = line.split(DELIMITER);
            if(split.length != 2) {
                throw new IllegalArgumentException("Invalid line '" + line + "' found for "
                        + "a Primed Pendant potion multiplier config.");
            }
            if (split[0].equals(potion.getRegisteredName())) {
                try {
                    double multiplier = 1.0D;
                    try {
                        multiplier = Double.parseDouble(split[1]);
                    } catch (NumberFormatException e) {
                        EvilCraft.clog("Invalid ratio '" + split[1] + "' in "
                                + "a Primed Pendant potion multiplier config, using 1.0.", Level.ERROR);
                    }
                    return multiplier;
                } catch (NumberFormatException e) {
                    EvilCraft.clog("Invalid line '" + line + "' found for "
                            + "a Primed Pendant potion multiplier config: " + split[0] + " is not a number; skipping.");
                }
            }
        }

        return 1.0D;
    }

    @Override
    protected Collection<ItemStack> getDefaultCreativeTabEntries() {
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
        return ((ItemPrimedPendant) getInstance()).getDefaultCreativeTabEntries();
    }

}
