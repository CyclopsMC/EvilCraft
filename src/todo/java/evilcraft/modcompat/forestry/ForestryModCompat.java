package evilcraft.modcompat.forestry;

import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.UndeadLogConfig;
import org.cyclops.evilcraft.block.UndeadSaplingConfig;
import org.cyclops.evilcraft.item.DarkGem;
import org.cyclops.evilcraft.item.DarkGemConfig;
import org.cyclops.evilcraft.item.PoisonSacConfig;
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
	                    "farmArboreal@" + Block.blockRegistry.getNameForObject(UndeadSaplingConfig._instance.getBlockInstance()).toString() + ".0");
	        }
	        
	        // Add dark gem to the miner backpack.
	        if(Configs.isEnabled(DarkGemConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "miner@" + Item.itemRegistry.getNameForObject(DarkGem.getInstance()).toString() + ":*");
	        }
	        
	        // Add poison sac to hunter backpack.
	        if(Configs.isEnabled(PoisonSacConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "hunter@" + Item.itemRegistry.getNameForObject(PoisonSacConfig._instance.getItemInstance()).toString() + ":*");
	        }
	        
	        // Add undead clog to forester backpack.
	        if(Configs.isEnabled(UndeadLogConfig.class)) {
	            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
	                    "forester@" + Block.blockRegistry.getNameForObject(UndeadLogConfig._instance.getBlockInstance()).toString() + ":*");
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
