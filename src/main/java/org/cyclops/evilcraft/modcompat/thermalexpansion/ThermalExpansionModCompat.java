package org.cyclops.evilcraft.modcompat.thermalexpansion;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.*;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.fluid.Poison;
import org.cyclops.evilcraft.item.*;

import java.util.List;

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
    public void onInit(Step step) {
    	if(step == Step.INIT) {
    		registerThermalExpansionRecipes();
    	}
    }

    private void registerThermalExpansionRecipes() {
        String TE = getModID();
        EvilCraft.clog("Registering " + TE + " recipes");
        // Sawmill undead wood
        if(Configs.isEnabled(UndeadLogConfig.class) && Configs.isEnabled(UndeadPlankConfig.class)) {
            NBTTagCompound sawmillUndeadWood = new NBTTagCompound();
            sawmillUndeadWood.setInteger("energy", 2000);
            sawmillUndeadWood.setTag("input", new ItemStack(
                    UndeadLogConfig._instance.getBlockInstance()).writeToNBT(new NBTTagCompound()));
            sawmillUndeadWood.setTag("primaryOutput", new ItemStack(
                    UndeadPlankConfig._instance.getBlockInstance(), 6).writeToNBT(new NBTTagCompound()));
            FMLInterModComms.sendMessage(TE, "addsawmillrecipe", sawmillUndeadWood);
        }

        // Pulverizer dark ore
        if(Configs.isEnabled(DarkOreConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
        	boolean crushedEnabled = Configs.isEnabled(DarkGemCrushedConfig.class);
            NBTTagCompound pulverizerDarkOre = new NBTTagCompound();
            pulverizerDarkOre.setInteger("energy", 2000);
            pulverizerDarkOre.setTag("input", new ItemStack(
                    DarkOre.getInstance()).writeToNBT(new NBTTagCompound()));
            pulverizerDarkOre.setTag("primaryOutput", new ItemStack(
                    DarkGem.getInstance(), 2).writeToNBT(new NBTTagCompound()));
            if(crushedEnabled) {
	            pulverizerDarkOre.setTag("secondaryOutput", new ItemStack(
                        DarkGemCrushedConfig._instance.getItemInstance(), 1).writeToNBT(new NBTTagCompound()));
	            pulverizerDarkOre.setInteger("secondaryChance", 30);
            }
            FMLInterModComms.sendMessage(TE, "addpulverizerrecipe", pulverizerDarkOre);
        }
        
        // Pulverizer dark ore -> crushed
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkGemCrushedConfig.class)) {
            NBTTagCompound pulverizerDarkOre = new NBTTagCompound();
            pulverizerDarkOre.setInteger("energy", 4000);
            pulverizerDarkOre.setTag("input", new ItemStack(
                    DarkGem.getInstance()).writeToNBT(pulverizerDarkOre.getCompoundTag("input")));
            pulverizerDarkOre.setTag("primaryOutput", new ItemStack(
                    DarkGemCrushedConfig._instance.getItemInstance(), 1).writeToNBT(pulverizerDarkOre.getCompoundTag("primaryOutput")));
            FMLInterModComms.sendMessage(TE, "addpulverizerrecipe", pulverizerDarkOre);
        }

        // Crucible poison
        List<ItemStack> materialPoisonousList = OreDictionary.getOres(Reference.DICT_MATERIALPOISONOUS);
        for(ItemStack materialPoisonous : materialPoisonousList) {
            if(materialPoisonous.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                materialPoisonous = materialPoisonous.copy();
                materialPoisonous.setItemDamage(0);
            }
            NBTTagCompound cruciblePoison = new NBTTagCompound();
            cruciblePoison.setInteger("energy", 2000);
            cruciblePoison.setTag("input", materialPoisonous.writeToNBT(new NBTTagCompound()));
            cruciblePoison.setTag("output", new FluidStack(Poison.getInstance(), 250).writeToNBT(new NBTTagCompound()));
            FMLInterModComms.sendMessage(TE, "addcruciblerecipe", cruciblePoison);
        }

        // Crucible ender
        if(Configs.isEnabled(EnderTearConfig.class)) {
            Fluid ender = FluidRegistry.getFluid("ender");
            if(ender != null) {
                NBTTagCompound crucibleEnder = new NBTTagCompound();
                crucibleEnder.setInteger("energy", 40000);
                crucibleEnder.setTag("input", new ItemStack(
                        EnderTearConfig._instance.getItemInstance()).writeToNBT(new NBTTagCompound()));
                crucibleEnder.setTag("output", new FluidStack(
                        ender, EnderTearConfig.mbLiquidEnder).writeToNBT(new NBTTagCompound()));
                FMLInterModComms.sendMessage(TE, "addcruciblerecipe", crucibleEnder);
            }
        }

        // Crucible hardened blood shard
        if(Configs.isEnabled(HardenedBloodShardConfig.class)) {
            NBTTagCompound crucibleBloodShard = new NBTTagCompound();
            crucibleBloodShard.setInteger("energy", 200);
            crucibleBloodShard.setTag("input", new ItemStack(
                    HardenedBloodShardConfig._instance.getItemInstance()).writeToNBT(new NBTTagCompound()));
            crucibleBloodShard.setTag("output", new FluidStack(
                    Blood.getInstance(), 100).writeToNBT(new NBTTagCompound()));
            FMLInterModComms.sendMessage(TE, "addcruciblerecipe", crucibleBloodShard);
        }

        // Fluid Transposer: blood infuse
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            for (IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe :
                    BloodInfuser.getInstance().getRecipeRegistry().allRecipes()) {
                if(recipe.getInput().getTier() == 0) {
                    NBTTagCompound bloodInfuse = new NBTTagCompound();
                    bloodInfuse.setInteger("energy", recipe.getProperties().getDuration() * 10);

                    bloodInfuse.setTag("input", recipe.getInput().getItemStack().writeToNBT(new NBTTagCompound()));
                    bloodInfuse.setTag("output", recipe.getOutput().getItemStack().writeToNBT(new NBTTagCompound()));
                    bloodInfuse.setBoolean("reversible", false);
                    FluidStack fluid = recipe.getInput().getFluidStack().copy();
                    fluid.amount *= 1.5;
                    bloodInfuse.setTag("fluid", fluid.writeToNBT(new NBTTagCompound()));
                    FMLInterModComms.sendMessage(TE, "addtransposerfillrecipe", bloodInfuse);
                }
            }
        }
        
        // Pulverize Blood-Waxed Coal
        if(Configs.isEnabled(BloodWaxedCoalConfig.class)) {
            NBTTagCompound pulverizerDustCoal = new NBTTagCompound();
            pulverizerDustCoal.setInteger("energy", 2400);
            pulverizerDustCoal.setTag("input", new ItemStack(BloodWaxedCoalConfig._instance.getItemInstance()).writeToNBT(new NBTTagCompound()));

            List<ItemStack> dustCoalList = OreDictionary.getOres("dustCoal");
            if(!dustCoalList.isEmpty()) {
                ItemStack dustCoal = dustCoalList.get(0).copy();
                dustCoal.setCount(2);
                pulverizerDustCoal.setTag("primaryOutput", dustCoal.writeToNBT(new NBTTagCompound()));

                List<ItemStack> sulfurList = OreDictionary.getOres("dustSulfur");
                if(!sulfurList.isEmpty()) {
                    ItemStack dustSulfur = sulfurList.get(0).copy();
                    dustSulfur.setCount(1);
                    pulverizerDustCoal.setTag("secondaryOutput", dustSulfur.writeToNBT(new NBTTagCompound()));
                    pulverizerDustCoal.setInteger("secondaryChance", 20);
                }

                FMLInterModComms.sendMessage(TE, "addpulverizerrecipe", pulverizerDustCoal);
            }
        }
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Sawmill, Pulverizer, Magma Crucible and Fluid Transposer recipes.";
	}

}
