package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BowlOfPromises}.
 * @author rubensworks
 *
 */
public class BowlOfPromisesConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BowlOfPromisesConfig _instance;

    /**
     * Make a new instance.
     */
    public BowlOfPromisesConfig() {
        super(
                EvilCraft._instance,
            true,
            "bowlOfPromises",
            null,
            BowlOfPromises.class
        );
    }

    public String getBaseDictionaryName() {
        return "materialBowlOfPromises";
    }

    public int getTiers() {
        return 4;
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if(itemStack.getItemDamage() == 0) {
            return super.getModelName(itemStack) + "_dusted";
        } else if(itemStack.getItemDamage() == 1) {
            return super.getModelName(itemStack) + "_empty";
        } else {
            return super.getModelName(itemStack);
        }
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        for(int tier = 0; tier < getTiers(); tier++) {
            for(int i = tier; i < getTiers(); i++) {
                OreDictionary.registerOre(getBaseDictionaryName() + tier, new ItemStack(BowlOfPromises.getInstance(), 1, 2 + i));
            }
        }
    }
}
