package evilcraft.infobook;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.block.BloodInfuser;
import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.weather.WeatherType;
import evilcraft.infobook.pageelement.BloodInfuserRecipeAppendix;
import evilcraft.infobook.pageelement.EnvironmentalAccumulatorRecipeAppendix;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Infobook class for the Origins of Darkness.
 * @author rubensworks
 */
public class OriginsOfDarknessBook extends InfoBook {

    private static OriginsOfDarknessBook _instance = null;

    static {
        InfoBookParser.registerFactory(Reference.MOD_ID + ":bloodInfuserRecipe", new InfoBookParser.IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                ItemStack itemStack = InfoBookParser.createStack(node);
                List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>>
                        recipes = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                int index = InfoBookParser.getIndex(node);
                if(index >= recipes.size()) throw new InfoBookParser.InvalidAppendixException("Could not find Blood Infuser recipe for " +
                        itemStack.getItem().getUnlocalizedName() + "with index " + index);
                return new BloodInfuserRecipeAppendix(infoBook, recipes.get(index));
            }

        });

        InfoBookParser.registerFactory(Reference.MOD_ID + ":envirAccRecipe", new InfoBookParser.IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                ItemStack itemStack = InfoBookParser.createStack(node);
                List<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>
                        recipes = EnvironmentalAccumulator.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new EnvironmentalAccumulatorRecipeComponent(itemStack, WeatherType.ANY));
                int index = InfoBookParser.getIndex(node);
                if(index >= recipes.size()) throw new InfoBookParser.InvalidAppendixException("Could not find Environmental Accumulator recipe for " +
                        itemStack.getItem().getUnlocalizedName() + "with index " + index);
                return new EnvironmentalAccumulatorRecipeAppendix(infoBook, recipes.get(index));
            }

        });

        InfoBookParser.registerFactory(Reference.MOD_ID + ":bloodInfuserRecipe", new InfoBookParser.IAppendixItemFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, ItemStack itemStack) throws InfoBookParser.InvalidAppendixException {
                List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>>
                        recipes = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                return new BloodInfuserRecipeAppendix(infoBook, recipes.get(0));
            }

        });

        InfoBookParser.registerFactory(Reference.MOD_ID + ":envirAccRecipe", new InfoBookParser.IAppendixItemFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, ItemStack itemStack) throws InfoBookParser.InvalidAppendixException {
                List<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>
                        recipes = EnvironmentalAccumulator.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new EnvironmentalAccumulatorRecipeComponent(itemStack, WeatherType.ANY));
                return new EnvironmentalAccumulatorRecipeAppendix(infoBook, recipes.get(0));
            }

        });
    }

    private OriginsOfDarknessBook() {
        super(EvilCraft._instance);
    }

    public static OriginsOfDarknessBook getInstance() {
        if(_instance == null) {
            _instance = new OriginsOfDarknessBook();
        }
        return _instance;
    }
}
