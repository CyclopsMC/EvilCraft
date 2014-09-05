package evilcraft.modcompat.thermalexpansion;

import java.util.ArrayList;
import java.util.Map.Entry;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.Configs;
import evilcraft.EvilCraft;
import evilcraft.IInitListener;
import evilcraft.Recipes;
import evilcraft.Reference;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.block.BloodInfuserConfig;
import evilcraft.block.BloodStainedBlock;
import evilcraft.block.BloodStainedBlockConfig;
import evilcraft.block.DarkOre;
import evilcraft.block.DarkOreConfig;
import evilcraft.block.UndeadLog;
import evilcraft.block.UndeadLogConfig;
import evilcraft.block.UndeadPlank;
import evilcraft.block.UndeadPlankConfig;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemAndFluidStackRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.fluid.Blood;
import evilcraft.fluid.Poison;
import evilcraft.item.BloodExtractorConfig;
import evilcraft.item.DarkGem;
import evilcraft.item.DarkGemConfig;
import evilcraft.item.DarkGemCrushed;
import evilcraft.item.DarkGemCrushedConfig;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for Thermal Expansion.
 * @author rubensworks
 *
 */
public class ThermalExpansionModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_THERMALEXPANSION;
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.INIT) {
    		registerThermalExpansionRecipes();
    	}
    }

    private void registerThermalExpansionRecipes() {
        String TE = getModID();
        EvilCraft.log("Registering " + TE + " recipes");
        // Sawmill undead wood
        if(Configs.isEnabled(UndeadLogConfig.class) && Configs.isEnabled(UndeadPlankConfig.class)) {
            NBTTagCompound sawmillUndeadWood = new NBTTagCompound();
            sawmillUndeadWood.setInteger("energy", 2000);
            sawmillUndeadWood.setTag("input", new NBTTagCompound());
            sawmillUndeadWood.setTag("primaryOutput", new NBTTagCompound());
    
            new ItemStack(UndeadLog.getInstance()).writeToNBT(sawmillUndeadWood.getCompoundTag("input"));
            new ItemStack(UndeadPlank.getInstance(), 6).writeToNBT(sawmillUndeadWood.getCompoundTag("primaryOutput"));
            FMLInterModComms.sendMessage(TE, "SawmillRecipe", sawmillUndeadWood);
        }

        // Pulverizer dark ore
        if(Configs.isEnabled(DarkOreConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
        	boolean crushedEnabled = Configs.isEnabled(DarkGemCrushedConfig.class);
            NBTTagCompound pulverizerDarkOre = new NBTTagCompound();
            pulverizerDarkOre.setInteger("energy", 2000);
            pulverizerDarkOre.setTag("input", new NBTTagCompound());
            pulverizerDarkOre.setTag("primaryOutput", new NBTTagCompound());
            if(crushedEnabled) {
	            pulverizerDarkOre.setTag("secondaryOutput", new NBTTagCompound());
	            pulverizerDarkOre.setInteger("secondaryChance", 30);
            }
    
            new ItemStack(DarkOre.getInstance()).writeToNBT(pulverizerDarkOre.getCompoundTag("input"));
            new ItemStack(DarkGem.getInstance(), 2).writeToNBT(pulverizerDarkOre.getCompoundTag("primaryOutput"));
            if(crushedEnabled) {
            	new ItemStack(DarkGemCrushed.getInstance(), 1).writeToNBT(pulverizerDarkOre.getCompoundTag("secondaryOutput"));
            }
            FMLInterModComms.sendMessage(TE, "PulverizerRecipe", pulverizerDarkOre);
        }
        
        // Pulverizer dark ore -> crushed
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkGemCrushedConfig.class)) {
            NBTTagCompound pulverizerDarkOre = new NBTTagCompound();
            pulverizerDarkOre.setInteger("energy", 4000);
            pulverizerDarkOre.setTag("input", new NBTTagCompound());
            pulverizerDarkOre.setTag("primaryOutput", new NBTTagCompound());
    
            new ItemStack(DarkGem.getInstance()).writeToNBT(pulverizerDarkOre.getCompoundTag("input"));
            new ItemStack(DarkGemCrushed.getInstance(), 1).writeToNBT(pulverizerDarkOre.getCompoundTag("primaryOutput"));
            FMLInterModComms.sendMessage(TE, "PulverizerRecipe", pulverizerDarkOre);
        }

        // Crucible poison
        ArrayList<ItemStack> materialPoisonousList = OreDictionary.getOres(Reference.DICT_MATERIALPOISONOUS);
        for(ItemStack materialPoisonous : materialPoisonousList) {
            NBTTagCompound cruciblePoison = new NBTTagCompound();
            cruciblePoison.setInteger("energy", 2000);
            cruciblePoison.setTag("input", new NBTTagCompound());
            cruciblePoison.setTag("output", new NBTTagCompound());

            materialPoisonous.writeToNBT(cruciblePoison.getCompoundTag("input"));
            new FluidStack(Poison.getInstance(), 250).writeToNBT(cruciblePoison.getCompoundTag("output"));
            FMLInterModComms.sendMessage(TE, "CrucibleRecipe", cruciblePoison);
        }

        // Crucible blood
        if(Configs.isEnabled(BloodStainedBlockConfig.class)) {
            for(int i = 0; i < BloodStainedBlock.getInstance().getInnerBlocks(); i++) {
                ItemStack materialPoisonous = new ItemStack(BloodStainedBlock.getInstance(), 1, i);
                NBTTagCompound crucibleBlood = new NBTTagCompound();
                crucibleBlood.setInteger("energy", 2000);
                crucibleBlood.setTag("input", new NBTTagCompound());
                crucibleBlood.setTag("output", new NBTTagCompound());
    
                materialPoisonous.writeToNBT(crucibleBlood.getCompoundTag("input"));
                new FluidStack(Blood.getInstance(), BloodExtractorConfig.maxMB).writeToNBT(crucibleBlood.getCompoundTag("output"));
                FMLInterModComms.sendMessage(TE, "CrucibleRecipe", crucibleBlood);
            }
        }

        // Fluid Transposer: blood infuse
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            for (IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe :
                    BloodInfuser.getInstance().getRecipeRegistry().allRecipes()) {
                NBTTagCompound bloodInfuse = new NBTTagCompound();
                bloodInfuse.setInteger("energy", recipe.getProperties().getDuration() * 100);
                bloodInfuse.setTag("input", new NBTTagCompound());
                bloodInfuse.setTag("output", new NBTTagCompound());
                bloodInfuse.setTag("fluid", new NBTTagCompound());

                recipe.getInput().getItemStack().writeToNBT(bloodInfuse.getCompoundTag("input"));
                recipe.getOutput().getItemStack().writeToNBT(bloodInfuse.getCompoundTag("output"));
                bloodInfuse.setBoolean("reversible", false);
                FluidStack fluid = recipe.getInput().getFluidStack().copy();
                fluid.amount *= 1.5;
                fluid.writeToNBT(bloodInfuse.getCompoundTag("fluid"));
                FMLInterModComms.sendMessage(TE, "TransposerFillRecipe", bloodInfuse);
            }
        }

        // Fluid Transposer: buckets
        for(Entry<Item, FluidStack> entry : Recipes.BUCKETS.entrySet()) {
            NBTTagCompound fill = new NBTTagCompound();
            fill.setInteger("energy", 2000);
            fill.setTag("input", new NBTTagCompound());
            fill.setTag("output", new NBTTagCompound());
            fill.setTag("fluid", new NBTTagCompound());

            new ItemStack(entry.getKey()).writeToNBT(fill.getCompoundTag("input"));
            new ItemStack(Items.bucket).writeToNBT(fill.getCompoundTag("output"));
            fill.setBoolean("reversible", true);
            entry.getValue().copy().writeToNBT(fill.getCompoundTag("fluid"));
            FMLInterModComms.sendMessage(TE, "TransposerFillRecipe", fill);
        }
    }

}
