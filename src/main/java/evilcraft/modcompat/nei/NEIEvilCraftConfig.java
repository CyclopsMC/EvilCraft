package evilcraft.modcompat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.block.BloodInfuserConfig;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.client.gui.container.GuiExaltedCrafter;
import evilcraft.item.ExaltedCrafterConfig;

/**
 * Helper for registering NEI manager.
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
    	if(NEIModCompat.canBeUsed) {
    		if(Configs.isEnabled(BloodInfuserConfig.class)) {
                API.registerRecipeHandler(new NEIBloodInfuserManager());
                API.registerUsageHandler(new NEIBloodInfuserManager());
            }
            
            if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
                API.registerRecipeHandler(new NEIEnvironmentalAccumulatorManager());
                API.registerUsageHandler(new NEIEnvironmentalAccumulatorManager());
            }
            
            if(Configs.isEnabled(ExaltedCrafterConfig.class)) {
            	API.registerGuiOverlay(GuiExaltedCrafter.class, "crafting");
                API.registerGuiOverlayHandler(GuiExaltedCrafter.class, new ExaltedCrafterOverlayHandler(), "crafting");
            }
    	}
    }
	
}
