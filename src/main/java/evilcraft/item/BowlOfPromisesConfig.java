package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Config for the {@link evilcraft.item.BowlOfPromises}.
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
    public void onRegistered() {
        super.onRegistered();
        for(int tier = 0; tier < getTiers(); tier++) {
            for(int i = tier; i < getTiers(); i++) {
                OreDictionary.registerOre(getBaseDictionaryName() + tier, new ItemStack(BowlOfPromises.getInstance(), 1, 2 + i));
            }
        }
    }
    
}
