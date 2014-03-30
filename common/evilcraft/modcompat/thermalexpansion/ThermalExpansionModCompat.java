package evilcraft.modcompat.thermalexpansion;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.EvilCraft;
import evilcraft.Recipes;
import evilcraft.Reference;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.DarkOre;
import evilcraft.blocks.UndeadLog;
import evilcraft.blocks.UndeadPlank;
import evilcraft.fluids.Blood;
import evilcraft.fluids.Poison;
import evilcraft.items.DarkGem;
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
    public void preInit() {

    }

    @Override
    public void init() {
        registerThermalExpansionRecipes();
    }

    @Override
    public void postInit() {

    }

    private void registerThermalExpansionRecipes() {
        String TE = getModID();
        EvilCraft.log("Registering " + TE + " recipes");
        // Sawmill undead wood
        NBTTagCompound sawmillUndeadWood = new NBTTagCompound();
        sawmillUndeadWood.setInteger("energy", 2000);
        sawmillUndeadWood.setCompoundTag("input", new NBTTagCompound());
        sawmillUndeadWood.setCompoundTag("primaryOutput", new NBTTagCompound());

        new ItemStack(UndeadLog.getInstance()).writeToNBT(sawmillUndeadWood.getCompoundTag("input"));
        new ItemStack(UndeadPlank.getInstance(), 6).writeToNBT(sawmillUndeadWood.getCompoundTag("primaryOutput"));
        FMLInterModComms.sendMessage(TE, "SawmillRecipe", sawmillUndeadWood);

        // Pulverizer dark ore
        NBTTagCompound pulverizerDarkOre = new NBTTagCompound();
        pulverizerDarkOre.setInteger("energy", 2000);
        pulverizerDarkOre.setCompoundTag("input", new NBTTagCompound());
        pulverizerDarkOre.setCompoundTag("primaryOutput", new NBTTagCompound());

        new ItemStack(DarkOre.getInstance()).writeToNBT(pulverizerDarkOre.getCompoundTag("input"));
        new ItemStack(DarkGem.getInstance(), 2).writeToNBT(pulverizerDarkOre.getCompoundTag("primaryOutput"));
        FMLInterModComms.sendMessage(TE, "PulverizerRecipe", pulverizerDarkOre);

        // Crucible poison
        ArrayList<ItemStack> materialPoisonousList = OreDictionary.getOres(Reference.DICT_MATERIALPOISONOUS);
        for(ItemStack materialPoisonous : materialPoisonousList) {
            NBTTagCompound cruciblePoison = new NBTTagCompound();
            cruciblePoison.setInteger("energy", 2000);
            cruciblePoison.setCompoundTag("input", new NBTTagCompound());
            cruciblePoison.setCompoundTag("output", new NBTTagCompound());

            materialPoisonous.writeToNBT(cruciblePoison.getCompoundTag("input"));
            new FluidStack(Poison.getInstance(), 250).writeToNBT(cruciblePoison.getCompoundTag("output"));
            FMLInterModComms.sendMessage(TE, "CrucibleRecipe", cruciblePoison);
        }

        // Crucible blood
        for(int i = 0; i < BloodStainedBlock.getInstance().getInnerBlocks(); i++) {
            ItemStack materialPoisonous = new ItemStack(BloodStainedBlock.getInstance(), 1, i);
            NBTTagCompound crucibleBlood = new NBTTagCompound();
            crucibleBlood.setInteger("energy", 2000);
            crucibleBlood.setCompoundTag("input", new NBTTagCompound());
            crucibleBlood.setCompoundTag("output", new NBTTagCompound());

            materialPoisonous.writeToNBT(crucibleBlood.getCompoundTag("input"));
            new FluidStack(Blood.getInstance(), 750).writeToNBT(crucibleBlood.getCompoundTag("output"));
            FMLInterModComms.sendMessage(TE, "CrucibleRecipe", crucibleBlood);
        }

        // Fluid Transposer: blood infuse
        Map<CustomRecipe, CustomRecipeResult> bloodInfuseRecipes = CustomRecipeRegistry.getRecipesForFactory(BloodInfuser.getInstance());
        for(Entry<CustomRecipe, CustomRecipeResult> entry : bloodInfuseRecipes.entrySet()) {
            NBTTagCompound bloodInfuse = new NBTTagCompound();
            bloodInfuse.setInteger("energy", entry.getKey().getDuration() * 100);
            bloodInfuse.setCompoundTag("input", new NBTTagCompound());
            bloodInfuse.setCompoundTag("output", new NBTTagCompound());
            bloodInfuse.setCompoundTag("fluid", new NBTTagCompound());

            entry.getKey().getItemStack().writeToNBT(bloodInfuse.getCompoundTag("input"));
            entry.getValue().getResult().writeToNBT(bloodInfuse.getCompoundTag("output"));
            bloodInfuse.setBoolean("reversible", false);
            FluidStack fluid = entry.getKey().getFluidStack().copy();
            fluid.amount *= 1.5;
            fluid.writeToNBT(bloodInfuse.getCompoundTag("fluid"));
            FMLInterModComms.sendMessage(TE, "TransposerFillRecipe", bloodInfuse);
        }

        // Fluid Transposer: buckets
        for(Entry<Item, FluidStack> entry : Recipes.BUCKETS.entrySet()) {
            NBTTagCompound fill = new NBTTagCompound();
            fill.setInteger("energy", 2000);
            fill.setCompoundTag("input", new NBTTagCompound());
            fill.setCompoundTag("output", new NBTTagCompound());
            fill.setCompoundTag("fluid", new NBTTagCompound());

            new ItemStack(entry.getKey()).writeToNBT(fill.getCompoundTag("input"));
            new ItemStack(Item.bucketEmpty).writeToNBT(fill.getCompoundTag("output"));
            fill.setBoolean("reversible", true);
            entry.getValue().copy().writeToNBT(fill.getCompoundTag("fluid"));
            FMLInterModComms.sendMessage(TE, "TransposerFillRecipe", fill);
        }

        // Compression Dynamo: blood
        NBTTagCompound compression = new NBTTagCompound();
        compression.setString("fluidName", Blood.getInstance().getName());
        compression.setInteger("energy", 600000);
        FMLInterModComms.sendMessage(TE, "CompressionFuel", compression);
    }

}
