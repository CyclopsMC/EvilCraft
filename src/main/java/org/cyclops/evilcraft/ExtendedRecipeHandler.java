package org.cyclops.evilcraft;

/**
 * An extended recipe handler.
 * @author rubensworks
 */
public class ExtendedRecipeHandler {

    /*
    TODO: custom recipes
    @Override
    protected void registerCustomRecipes() {
        super.registerCustomRecipes();
        // Entangled Chalice unique id
        if(Configs.isEnabled(BlockEntangledChaliceConfig.class)
                && Configs.isEnabled(ItemDarkGemConfig.class)
                && Configs.isEnabled(ItemCorruptedTearConfig.class)) {
            Item tear = ItemCorruptedTearConfig._instance.getItemInstance();
            CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "entangled_chalice"), new ObservableShapedRecipe("", 3, 3, NonNullList.from(Ingredient.EMPTY,
                    Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(tear), Ingredient.fromItem(Items.GOLD_INGOT),
                    Ingredient.fromItem(ItemDarkGem.getInstance()), Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(ItemDarkGem.getInstance()),
                    Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(Items.GOLD_INGOT)
            ), new ItemStack(Item.getItemFromBlock(BlockEntangledChalice.getInstance()), 2), new IRecipeOutputObserver() {
                @Override
                public ItemStack getRecipeOutput(CraftingInventory craftingGrid, ItemStack output) {
                    ItemStack newStack = output.copy();
                    ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(newStack);
                    if (!MinecraftHelpers.isClientSide()) {
                        fluidHandler.setNextTankID();
                    }
                    return fluidHandler.getContainer();
                }
            }));

            CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "entangled_chalice_1"), new ObservableShapedRecipe("", 3, 3, NonNullList.from(Ingredient.EMPTY,
                    Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(tear), Ingredient.fromItem(Items.GOLD_INGOT),
                    Ingredient.fromItem(ItemDarkGem.getInstance()), Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(BlockEntangledChalice.getInstance()), 1, OreDictionary.WILDCARD_VALUE)), Ingredient.fromItem(ItemDarkGem.getInstance()),
                    Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(Items.GOLD_INGOT), Ingredient.fromItem(Items.GOLD_INGOT)
            ), new ItemStack(Item.getItemFromBlock(BlockEntangledChalice.getInstance()), 2), new IRecipeOutputObserver() {
                @Override
                public ItemStack getRecipeOutput(CraftingInventory craftingGrid, ItemStack output) {
                    ItemStack newStack = output.copy();
                    String tankID = ((ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(craftingGrid.getStackInSlot(4))).getTankID();
                    ((ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(newStack)).setTankID(tankID);
                    return newStack;
                }
            }));
        }

        // Dark tank upgrades
        if(Configs.isEnabled(BlockDarkTankConfig.class)) {
            for(int i = 1; i < 9; i++) {
                ItemBlockFluidContainer tankItem = (ItemBlockFluidContainer) Item.getItemFromBlock(BlockDarkTank.getInstance());
                CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "dark_tank_" + i),
                        new RecipeFluidContainerCombination(i, tankItem, BlockDarkTankConfig.maxTankSize));
            }
        }

        // Blood Extractor upgrades
        if(Configs.isEnabled(ItemBloodExtractorConfig.class) && Configs.isEnabled(BlockDarkTankConfig.class)) {
            for(int i = 1; i < 9; i++) {
                CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "blood_extractor_" + i),
                        new RecipeBloodExtractorCombination(i));
            }
        }

        // Broom crafting
        if(Configs.isEnabled(ItemBroomConfig.class)) {
            for(int i = 1; i < 9; i++) {
                CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "broom_part_" + i),
                        new RecipeBroomPartCombination(i));
            }
        }

        if (Configs.isEnabled(BlockEnvironmentalAccumulatorConfig.class)) {
            ItemStack outputStack = null;
            String recipeName = null;

            // Add the different weather container recipes
            if (Configs.isEnabled(ItemWeatherContainerConfig.class)) {
                ItemStack emptyContainer = ItemWeatherContainer.createItemStack(ItemWeatherContainer.WeatherContainerType.EMPTY, 1);
                WeatherType[] weatherInputs = {WeatherType.CLEAR, WeatherType.LIGHTNING, WeatherType.RAIN};
                WeatherType[] weatherOutputs = {WeatherType.RAIN, WeatherType.RAIN, WeatherType.CLEAR};

                for (int i=0; i < weatherInputs.length; ++i) {
                    recipeName = "weather_container" + weatherInputs[i].getClass().getSimpleName();
                    outputStack = ItemWeatherContainer.createItemStack(
                            ItemWeatherContainer.WeatherContainerType.getWeatherContainerType(weatherInputs[i]), 1);

                    BlockEnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                            recipeName,
                            new EnvironmentalAccumulatorRecipeComponent(
                                    emptyContainer,
                                    weatherInputs[i]
                            ),
                            new EnvironmentalAccumulatorRecipeComponent(
                                    outputStack,
                                    weatherOutputs[i]
                            ),
                            new EnvironmentalAccumulatorRecipeProperties()
                    );
                }
            }

            // Add biome extract recipes
            if(Configs.isEnabled(ItemBiomeExtractConfig.class) && ItemBiomeExtractConfig.hasRecipes) {
                ItemStack emptyContainer = new ItemStack(ItemBiomeExtract.getInstance());
                ItemStack filledContainer = ItemBiomeExtract.getInstance().createItemStack(null, 1); // Still dummy!
                filledContainer.setItemDamage(OreDictionary.WILDCARD_VALUE);
                BlockEnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                        recipeName,
                        new EnvironmentalAccumulatorRecipeComponent(
                                emptyContainer,
                                WeatherType.ANY
                        ),
                        new EnvironmentalAccumulatorRecipeComponent(
                                filledContainer,
                                WeatherType.ANY
                        ),
                        new EnvironmentalAccumulatorRecipeProperties(1000, 500, -1.0D, null, new EnvironmentalAccumulatorRecipeProperties.IEAResultOverride() {
                            @Override
                            public ItemStack getResult(IBlockAccess world, BlockPos pos, ItemStack originalResult) {
                                World worldSafe = (World) world;
                                Biome biome = worldSafe.getBiome(pos);
                                if(ItemBiomeExtractConfig._instance.isCraftingBlacklisted(Biome.getIdForBiome(biome))) {
                                    return ItemBiomeExtract.getInstance().createItemStack(null, 1);
                                } else {
                                    return ItemBiomeExtract.getInstance().createItemStack(biome, 1);
                                }
                            }
                        })
                );
            }

            // Display Stand crafting
            if(Configs.isEnabled(BlockDisplayStandConfig.class)) {
                getTaggedRecipes().put("crafting_recipe:display_stands", new Recipe(
                        new IngredientsRecipeComponent(NonNullList.<Ingredient>create()),
                        new IngredientRecipeComponent(BlockDisplayStand.getInstance().
                                getTypedDisplayStandItem(Blocks.PLANKS.getDefaultState())),
                        new DummyPropertiesComponent()
                ));
                CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "display_stand"), new RecipeDisplayStand(OreDictionary.getOres(Reference.DICT_WOODPLANK)));
            }

            // Undead bush crafting
            getTaggedRecipes().put("crafting_recipe:deadbush", new Recipe(
                    new IngredientsRecipeComponent(NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.SHEARS), new OreIngredient("treeSapling"))),
                    new IngredientRecipeComponent(new ItemStack(Blocks.DEADBUSH)),
                    new DummyPropertiesComponent()
            ));
            CraftingHelpers.registerRecipe(new ResourceLocation(getMod().getModId(), "deadbush"), new RecipeDeadBush());
        }
    }

     */
}
