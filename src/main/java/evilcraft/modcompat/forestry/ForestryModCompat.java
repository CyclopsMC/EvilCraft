package evilcraft.modcompat.forestry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.Configs;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.blocks.UndeadLog;
import evilcraft.blocks.UndeadLogConfig;
import evilcraft.blocks.UndeadSapling;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.PoisonSac;
import evilcraft.items.PoisonSacConfig;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for Forestry.
 * @author rubensworks
 *
 */
public class ForestryModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_FORESTRY;
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.INIT) {
	        // Register the Undead Sapling.
	        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-farmable-sapling",
	                    "farmArboreal@" + Block.blockRegistry.getNameForObject(UndeadSapling.getInstance()) + ".0");
	        }
	        
	        // Add dark gem to the miner backpack.
	        if(Configs.isEnabled(DarkGemConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "miner@" + Item.itemRegistry.getNameForObject(DarkGem.getInstance()) + ":*");
	        }
	        
	        // Add poison sac to hunter backpack.
	        if(Configs.isEnabled(PoisonSacConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "hunter@" + Item.itemRegistry.getNameForObject(PoisonSac.getInstance()) + ":*");
	        }
	        
	        // Add undead log to forester backpack.
	        if(Configs.isEnabled(UndeadLogConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "forester@" + Block.blockRegistry.getNameForObject(UndeadLog.getInstance()) + ":*");
	        }
	        
	        ForestryRecipeManager.register();
    	}
    }

}
