package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidContainerRegistry;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.IChangedCallback;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Map;

/**
 * Config for the {@link PrimedPendant}.
 * @author rubensworks
 *
 */
public class PrimedPendantConfig extends ItemConfig {

    private static final String DELIMITER = ":";

    /**
     * The unique instance.
     */
    public static PrimedPendantConfig _instance;
    /**
     * The capacity of the pendant.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The capacity of the pendant.", requiresMcRestart = true)
    public static int capacity = FluidContainerRegistry.BUCKET_VOLUME * 5;

    /**
     * The amount of Blood to drain after one effect application.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of Blood to drain after one effect application.", isCommandable = true)
    public static int usage = 10;

    /**
     * Usage multipliers. Potion ids are first, followed by floating numbers. A number smaller than one blacklists that potion.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM,
            comment = "Usage multipliers. Potion ids are first, followed by floating numbers. A number smaller than one blacklists that potion.",
            changedCallback = PotionMultipliersChanged.class)
    public static String[] potionMultipliers = new String[]{
            Potion.getIdFromPotion(MobEffects.heal) + DELIMITER + "-1",
            Potion.getIdFromPotion(MobEffects.regeneration) + DELIMITER + "10",
    };

    private Map<Integer, Double> multipliers = Maps.newHashMap();

    /**
     * Make a new instance.
     */
    public PrimedPendantConfig() {
        super(
                EvilCraft._instance,
                true,
                "primedPendant",
                null,
                PrimedPendant.class
        );
    }

    /**
     * Callback for when the multipliers property is changed.
     * @author rubensworks
     *
     */
    public static class PotionMultipliersChanged implements IChangedCallback {

        private static boolean calledOnce = false;

        @Override
        public void onChanged(Object value) {
            if(calledOnce) {
                PrimedPendantConfig._instance.registerFromConfig((String[]) value);
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
     */
    public void registerFromConfig(String[] config) {
        multipliers.clear();
        for(String line : config) {
            String[] split = line.split(DELIMITER);
            if(split.length != 2) {
                throw new IllegalArgumentException("Invalid line '" + line + "' found for "
                        + "a Primed Pendant potion multiplier config.");
            }
            try {
                int potionId = Integer.parseInt(split[0]);
                if(Potion.getPotionById(potionId) == null) {
                    EvilCraft.clog("Invalid line '" + line + "' found for "
                            + "a Primed Pendant potion multiplier config: " + split[0] + " does not refer to an existing potion; skipping.");
                }
                Potion potion = Potion.getPotionById(potionId);
                double multiplier = 1.0D;
                try {
                    multiplier = Double.parseDouble(split[1]);
                } catch (NumberFormatException e) {
                    EvilCraft.clog("Invalid ratio '" + split[1] + "' in "
                            + "a Primed Pendant potion multiplier config, using 1.0.", Level.ERROR);
                }
                multipliers.put(Potion.getIdFromPotion(potion), multiplier);
            } catch (NumberFormatException e) {
                EvilCraft.clog("Invalid line '" + line + "' found for "
                        + "a Primed Pendant potion multiplier config: " + split[0] + " is not a number; skipping.");
            }
        }
    }

    public Double getMultiplier(Potion potion) {
        return multipliers.get(Potion.getIdFromPotion(potion));
    }
    
}
