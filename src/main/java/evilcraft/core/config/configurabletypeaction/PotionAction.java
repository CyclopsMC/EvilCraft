package evilcraft.core.config.configurabletypeaction;

import evilcraft.GeneralConfig;
import evilcraft.core.config.extendedconfig.PotionConfig;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * The action used for {@link evilcraft.core.config.extendedconfig.PotionConfig}.
 * @author rubensworks
 * @see evilcraft.core.config.configurabletypeaction.ConfigurableTypeAction
 */
public class PotionAction extends ConfigurableTypeAction<PotionConfig> {

    @Override
    public void preRun(PotionConfig eConfig, Configuration config, boolean startup) {
        growPotionArray();

        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.ID);
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();

        if(startup) {
            // Update the ID, it could've changed
            eConfig.ID = property.getInt();
        }
    }

    @Override
    public void postRun(PotionConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
    }

    private static void growPotionArray() {
        int size = GeneralConfig.minimumPotionTypesArraySize;
        if(Potion.potionTypes.length < size) {
            Potion[] oldPotionTypes = Potion.potionTypes;
            final Potion[] potionTypes = new Potion[size];
            System.arraycopy(oldPotionTypes, 0, potionTypes, 0, oldPotionTypes.length);
            ObfuscationHelpers.setPotionTypesArray(potionTypes);
        }
    }

}
