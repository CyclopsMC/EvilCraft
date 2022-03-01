package org.cyclops.evilcraft.infobook;

import com.google.common.collect.Maps;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.infobook.pageelement.BloodInfuserRecipeAppendix;
import org.cyclops.evilcraft.infobook.pageelement.BroomModifierRecipeAppendix;
import org.cyclops.evilcraft.infobook.pageelement.EnvironmentalAccumulatorRecipeAppendix;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * Infobook class for the Origins of Darkness.
 * @author rubensworks
 */
public class OriginsOfDarknessBook extends InfoBook {

    private static OriginsOfDarknessBook _instance = null;

    static {
        InfoBookParser.registerAppendixRecipeFactories(RegistryEntries.RECIPETYPE_BLOOD_INFUSER, BloodInfuserRecipeAppendix::new);
        InfoBookParser.registerAppendixRecipeFactories(RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR, EnvironmentalAccumulatorRecipeAppendix::new);

        InfoBookParser.registerAppendixFactory(Reference.MOD_ID + ":broom_modifier", new InfoBookParser.IAppendixFactory() {
            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                String id = node.getTextContent();
                Map<ItemStack, Float> values = Maps.newHashMap();
                BroomModifier finalModifier = null;
                for (BroomModifier modifier : BroomModifiers.REGISTRY.getModifiers()) {
                    if (modifier.getId().toString().equals(id)) {
                        finalModifier = modifier;
                        values.putAll(BroomModifiers.REGISTRY.getItemsFromModifier(modifier));
                    }
                }
                if (finalModifier == null) {
                    throw new InfoBookParser.InvalidAppendixException("Could not find the broom modifier " + id);
                }
                if (values.isEmpty()) {
                    throw new InfoBookParser.InvalidAppendixException("The broom modifier " + id + " has no valid items");
                }
                return new BroomModifierRecipeAppendix(infoBook, finalModifier, values);
            }
        });
    }

    private OriginsOfDarknessBook() {
        super(EvilCraft._instance, 2, Reference.BOOK_URL);
    }

    public static synchronized OriginsOfDarknessBook getInstance() {
        if(_instance == null) {
            _instance = new OriginsOfDarknessBook();
        }
        return _instance;
    }
}
