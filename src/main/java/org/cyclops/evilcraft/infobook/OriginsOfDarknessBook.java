package org.cyclops.evilcraft.infobook;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.block.BloodInfuserConfig;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.block.EnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.infobook.pageelement.BloodInfuserRecipeAppendix;
import org.cyclops.evilcraft.infobook.pageelement.EnvironmentalAccumulatorRecipeAppendix;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Infobook class for the Origins of Darkness.
 * @author rubensworks
 */
public class OriginsOfDarknessBook extends InfoBook {

    private static OriginsOfDarknessBook _instance = null;

    static {
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":bloodInfuserRecipe", new InfoBookParser.IAppendixFactory() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                    ItemStack itemStack = InfoBookParser.createStack(node, infoBook.getMod().getRecipeHandler());
                    List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>>
                            recipes = BloodInfuser.getInstance().getRecipeRegistry().
                            findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                    int index = InfoBookParser.getIndex(node);
                    if (index >= recipes.size())
                        throw new InfoBookParser.InvalidAppendixException("Could not find Blood Infuser recipe for " +
                                itemStack.getItem().getUnlocalizedName() + "with index " + index);
                    return new BloodInfuserRecipeAppendix(infoBook, recipes.get(index));
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":bloodInfuserRecipe");
        }

        InfoBookParser.registerFactory(Reference.MOD_ID + ":envirAccRecipe", new InfoBookParser.IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                ItemStack itemStack = InfoBookParser.createStack(node, infoBook.getMod().getRecipeHandler());
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
                List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>>
                        recipes = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                return new BloodInfuserRecipeAppendix(infoBook, recipes.get(0));
            }

        });

        if(Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            InfoBookParser.registerFactory(Reference.MOD_ID + ":envirAccRecipe", new InfoBookParser.IAppendixItemFactory() {

                @Override
                public SectionAppendix create(IInfoBook infoBook, ItemStack itemStack) throws InfoBookParser.InvalidAppendixException {
                    List<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>
                            recipes = EnvironmentalAccumulator.getInstance().getRecipeRegistry().
                            findRecipesByOutput(new EnvironmentalAccumulatorRecipeComponent(itemStack, WeatherType.ANY));
                    return new EnvironmentalAccumulatorRecipeAppendix(infoBook, recipes.get(0));
                }

            });
        } else {
            InfoBookParser.registerIgnoredFactory(Reference.MOD_ID + ":envirAccRecipe");
        }
    }

    private OriginsOfDarknessBook() {
        super(EvilCraft._instance, 2);
    }

    public static OriginsOfDarknessBook getInstance() {
        if(_instance == null) {
            _instance = new OriginsOfDarknessBook();
        }
        return _instance;
    }
}
