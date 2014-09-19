package evilcraft.modcompat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.block.BloodInfuserConfig;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.client.gui.container.GuiExaltedCrafter;
import evilcraft.item.ExaltedCrafterConfig;
import evilcraft.modcompat.IModCompat;
import evilcraft.modcompat.ModCompatLoader;

/**
 * Config for the NEI integration of this mod.
 * @author rubensworks
 *
 */
public class NEIEvilCraftConfig implements IConfigureNEI, IModCompat {
	
	private static boolean canBeUsed = false;

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
    	if(canBeUsed) {
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

	@Override
	public void onInit(Step initStep) {
		if(initStep == Step.PREINIT) {
			canBeUsed = ModCompatLoader.shouldLoadModCompat(this);
		}
	}

	@Override
	public String getModID() {
		return Reference.MOD_NEI;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Blood Infuser and Environmental Accumulator recipes.";
	}

}
