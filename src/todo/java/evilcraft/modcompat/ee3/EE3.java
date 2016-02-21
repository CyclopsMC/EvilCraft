package evilcraft.modcompat.ee3;

import com.google.common.collect.Lists;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;
import evilcraft.Configs;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.*;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.fluid.Blood;
import evilcraft.fluid.Poison;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.item.EnvironmentalAccumulationCoreConfig;
import org.cyclops.evilcraft.item.GarmonboziaConfig;

import java.util.Arrays;
import java.util.List;

/**
 * @author rubensworks
 */
public class EE3 {

    public static void registerItems() {
        registerObjectSafe(DarkGemConfig._instance, 4096);
        registerObjectSafe(DarkOreConfig._instance, 5120);
        registerObjectSafe(DarkGemCrushedConfig._instance, 1024);

        registerObjectSafe(HardenedBloodConfig._instance, 1280);
        registerObjectSafe(HardenedBloodShardConfig._instance, 75);

        registerObjectSafe(PoisonSacConfig._instance, 200);
        registerObjectSafe(WerewolfFleshConfig._instance, OreDictionary.WILDCARD_VALUE, 48);
        registerObjectSafe(EnderTearConfig._instance, 4096);

        registerObjectSafe(UndeadLeavesConfig._instance, 1);
        registerObjectSafe(DarkStickConfig._instance, 1028);

        registerObjectSafe(ObscuredGlassConfig._instance, 513);
        registerObjectSafe(BloodOrbConfig._instance, 260);

        registerObjectSafe(BucketEternalWaterConfig._instance, 4204.25F);
        registerObjectSafe(EternalWaterBlockConfig._instance, 10348.90625F);
        registerObjectSafe(WeatherContainerConfig._instance, 3, 7500);
        registerObjectSafe(WeatherContainerConfig._instance, 2, 4529);

        registerObjectSafe(GarmonboziaConfig._instance, 50000);
        registerObjectSafe(EnvironmentalAccumulationCoreConfig._instance, 10000);
        registerObjectSafe(OriginsOfDarknessConfig._instance, 2000);

        EnergyValueRegistryProxy.addPreAssignedEnergyValue(Blood.getInstance(), 72);
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(Poison.getInstance(), 64);
    }

    public static void registerCrafting() {
        // Blood Infuser recipes
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            for (IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe :
                    BloodInfuser.getInstance().getRecipeRegistry().allRecipes()) {
                List l = Lists.newArrayList(recipe.getInput().getItemStacks());
                l.add(recipe.getInput().getFluidStack());
                RecipeRegistryProxy.addRecipe(recipe.getOutput().getItemStack(), l);
            }
        }

        // Environmental Accumulator recipes
        if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            for (IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent,
                    EnvironmentalAccumulatorRecipeProperties> recipe : EnvironmentalAccumulator.getInstance().
                    getRecipeRegistry().allRecipes()) {
                RecipeRegistryProxy.addRecipe(recipe.getOutput().getItemStack(), Arrays.asList((recipe.getInput().getItemStack())));
            }
        }
    }

    private static void registerObjectSafe(ItemConfig itemConfig, float energyValue) {
        registerObjectSafe(itemConfig, 0, energyValue);
    }

    private static void registerObjectSafe(BlockConfig itemConfig, float energyValue) {
        registerObjectSafe(itemConfig, 0, energyValue);
    }

    private static void registerObjectSafe(ItemConfig itemConfig, int meta, float energyValue) {
        if(itemConfig != null) {
            EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(itemConfig.getItemInstance(), 1, meta), energyValue);
        }
    }

    private static void registerObjectSafe(BlockConfig itemConfig, int meta, float energyValue) {
        if(itemConfig != null) {
            EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(itemConfig.getBlockInstance(), 1, meta), energyValue);
        }
    }

}
