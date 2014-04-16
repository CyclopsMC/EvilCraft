package evilcraft.modcompat.forestry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.blocks.UndeadLeaves;
import evilcraft.blocks.UndeadLeavesConfig;
import evilcraft.blocks.UndeadLog;
import evilcraft.blocks.UndeadLogConfig;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.fluids.Blood;
import evilcraft.fluids.BloodConfig;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.HardenedBloodShard;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.PoisonSacConfig;
import evilcraft.modcompat.IModCompat;
import forestry.api.recipes.RecipeManagers;

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
    public void preInit() {
        
    }

    @Override
    public void init() {
        // Register the Undead Sapling.
        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-farmable-sapling",
                    "farmArboreal@" + UndeadSaplingConfig._instance.ID + ".0");
        }
        
        // Add dark gem to the miner backpack.
        if(Configs.isEnabled(DarkGemConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
                    "miner@" + DarkGemConfig._instance.ID + ":*");
        }
        
        // Add poison sac to hunter backpack.
        if(Configs.isEnabled(PoisonSacConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
                    "hunter@" + PoisonSacConfig._instance.ID + ":*");
        }
        
        // Add undead log to forester backpack.
        if(Configs.isEnabled(UndeadLogConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
                    "forester@" + UndeadLogConfig._instance.ID + ":*");
        }
        
        // Register Undead Log squeezer recipe.
        if(Configs.isEnabled(UndeadLogConfig.class)
                && Configs.isEnabled(BloodConfig.class)
                && Configs.isEnabled(HardenedBloodShardConfig.class)) {
            int time = 20;
            ItemStack[] input = {new ItemStack(UndeadLog.getInstance())};
            FluidStack fluidStack = new FluidStack(Blood.getInstance(),
                    FluidContainerRegistry.BUCKET_VOLUME / 5);
            ItemStack output = new ItemStack(HardenedBloodShard.getInstance());
            int outputChance = 25; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }
        
        // Register Undead Leaves squeezer recipes.
        if(Configs.isEnabled(UndeadLeavesConfig.class)
                && Configs.isEnabled(BloodConfig.class)
                && Configs.isEnabled(HardenedBloodShardConfig.class)) {
            int time = 10;
            ItemStack[] input = {new ItemStack(UndeadLeaves.getInstance())};
            FluidStack fluidStack = new FluidStack(Blood.getInstance(),
                    FluidContainerRegistry.BUCKET_VOLUME / 10);
            ItemStack output = new ItemStack(HardenedBloodShard.getInstance());
            int outputChance = 10; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }
    }

    @Override
    public void postInit() {
        
    }

}
