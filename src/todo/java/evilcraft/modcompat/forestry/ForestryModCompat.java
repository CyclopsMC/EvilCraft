package evilcraft.modcompat.forestry;

import evilcraft.Configs;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.block.UndeadLogConfig;
import evilcraft.block.UndeadSaplingConfig;
import evilcraft.item.DarkGem;
import evilcraft.item.DarkGemConfig;
import evilcraft.item.PoisonSacConfig;
import evilcraft.modcompat.IModCompat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInterModComms;

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
	                    "farmArboreal@" + Block.blockRegistry.getNameForObject(UndeadSaplingConfig._instance.getBlockInstance()) + ".0");
	        }
	        
	        // Add dark gem to the miner backpack.
	        if(Configs.isEnabled(DarkGemConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "miner@" + Item.itemRegistry.getNameForObject(DarkGem.getInstance()) + ":*");
	        }
	        
	        // Add poison sac to hunter backpack.
	        if(Configs.isEnabled(PoisonSacConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "hunter@" + Item.itemRegistry.getNameForObject(PoisonSacConfig._instance.getItemInstance()) + ":*");
	        }
	        
	        // Add undead clog to forester backpack.
	        if(Configs.isEnabled(UndeadLogConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "forester@" + Block.blockRegistry.getNameForObject(UndeadLogConfig._instance.getBlockInstance()) + ":*");
	        }
	        
	        ForestryRecipeManager.register();
    	}
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Multifarm, squeezer and backpack support.";
	}

}
