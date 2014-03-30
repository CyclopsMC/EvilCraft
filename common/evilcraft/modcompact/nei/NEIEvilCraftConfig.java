package evilcraft.modcompact.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import evilcraft.Reference;

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
        API.registerRecipeHandler(new NEIBloodInfuserManager());
        API.registerUsageHandler(new NEIBloodInfuserManager());
    }

}
