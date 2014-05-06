package evilcraft.modcompat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;

/**
 * Config for the NEI integration of this mod.
 * @author rubensworks
 *
 */
public class NEIEvilCraftConfig implements IConfigureNEI {

    @Override
    public String getName() {
        return Reference.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Reference.MOD_VERSION;
    }

    @Override
    public void loadConfig() {
    	if(false) {
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            API.registerRecipeHandler(new NEIBloodInfuserManager());
            API.registerUsageHandler(new NEIBloodInfuserManager());
        }
        
        if (Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            API.registerRecipeHandler(new NEIEnvironmentalAccumulatorManager());
            API.registerUsageHandler(new NEIEnvironmentalAccumulatorManager());
        }
    	}
    }

}
