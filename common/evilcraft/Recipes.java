package evilcraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.blocks.DarkBlock;
import evilcraft.items.BloodPearlOfTeleportation;
import evilcraft.items.ContainedFlux;
import evilcraft.items.DarkGem;
import evilcraft.items.WeatherContainer;

/**
 * Holder class of all the recipes.
 */
public class Recipes {
    public static void registerRecipes() {
        // 9 DarkGems -> 1 DarkBlock
        GameRegistry.addRecipe(new ItemStack(DarkBlock.getInstance()),
                "GGG",
                "GGG",
                "GGG",
                'G', new ItemStack(DarkGem.getInstance())
        );
        // 1 DarkBlock -> 9 DarkGems
        GameRegistry.addShapelessRecipe(new ItemStack(DarkGem.getInstance(), 9),
                new ItemStack(DarkBlock.getInstance())
        );
        // Weather Container
        GameRegistry.addRecipe(new ItemStack(WeatherContainer.getInstance()),
                " G ",
                " P ",
                " S ",
                'G', new ItemStack(ContainedFlux.getInstance()),
                'P', new ItemStack(Item.glassBottle),
                'S', new ItemStack(Item.sugar)
        );
        // Blood Pearl of Teleportation
        GameRegistry.addRecipe(new ItemStack(BloodPearlOfTeleportation.getInstance()),
                "EGE",
                "GEG",
                "EGE",
                'G', new ItemStack(ContainedFlux.getInstance()),
                'E', new ItemStack(Item.enderPearl)
        );
    }
}
