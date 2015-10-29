package org.cyclops.evilcraft.modcompat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.*;
import org.cyclops.evilcraft.client.gui.container.GuiExaltedCrafter;
import org.cyclops.evilcraft.item.ExaltedCrafterConfig;

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
                API.registerRecipeHandler(new NEIBloodInfuserFluidsManager());
                API.registerUsageHandler(new NEIBloodInfuserFluidsManager());
            }
            
            if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
                API.registerRecipeHandler(new NEIEnvironmentalAccumulatorManager());
                API.registerUsageHandler(new NEIEnvironmentalAccumulatorManager());
            }

            if(Configs.isEnabled(SanguinaryEnvironmentalAccumulatorConfig.class)) {
                API.registerRecipeHandler(new NEISanguinaryEnvironmentalAccumulatorManager());
                API.registerUsageHandler(new NEISanguinaryEnvironmentalAccumulatorManager());
            }
            
            if(Configs.isEnabled(ExaltedCrafterConfig.class)) {
            	API.registerGuiOverlay(GuiExaltedCrafter.class, "crafting");
                API.registerGuiOverlayHandler(GuiExaltedCrafter.class, new ExaltedCrafterOverlayHandler(), "crafting");
            }

            if(Configs.isEnabled(BloodStainedBlockConfig.class)) {
                API.hideItem(new ItemStack(BloodStainedBlock.getInstance()));
            }

            if(Configs.isEnabled(InvisibleRedstoneBlockConfig.class)) {
                API.hideItem(new ItemStack(InvisibleRedstoneBlock.getInstance()));
            }
    	}
    }
	
}
