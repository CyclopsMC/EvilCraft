package org.cyclops.evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.client.model.SingleModelLoader;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.entity.monster.VengeanceSpiritConfig;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;
import org.cyclops.evilcraft.tileentity.tickaction.spiritfurnace.BoxCookTickAction;

import java.util.UUID;

/**
 * Config for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosureConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static BoxOfEternalClosureConfig _instance;

    @SideOnly(Side.CLIENT)
    public static ResourceLocation boxModel;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation boxLidModel;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation boxLidRotatedModel;

    /**
     * Make a new instance.
     */
    public BoxOfEternalClosureConfig() {
        super(
            EvilCraft._instance,
        	true,
            "boxOfEternalClosure",
            null,
            BoxOfEternalClosure.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }

    @SideOnly(Side.CLIENT)
    public void onRegisteredClient() {
        EvilCraft._instance.getProxy().registerRenderer(TileBoxOfEternalClosure.class,
                new RenderTileEntityBoxOfEternalClosure());

        boxModel = new ResourceLocation(getMod().getModId() + ":block/box");
        boxLidModel = new ResourceLocation(getMod().getModId() + ":block/box_lid");
        boxLidRotatedModel = new ResourceLocation(getMod().getModId() + ":block/box_lid_rotated");

        ModelLoaderRegistry.registerLoader(new SingleModelLoader(
                Reference.MOD_ID, "models/item/boxOfEternalClosure", new ModelBoxOfEternalClosure()));
    }

    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
            onRegisteredClient();
        }

        BoxOfEternalClosure.boxOfEternalClosureFilled = new ItemStack(BoxOfEternalClosure.getInstance());
        BoxOfEternalClosure.setVengeanceSwarmContent(BoxOfEternalClosure.boxOfEternalClosureFilled);

        ItemStack spiritStack = new ItemStack(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()), 1, 0);
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            for(UUID playerId : BoxCookTickAction.PLAYERDROP_OVERRIDES.keySet()) {
                ItemStack playerStack = spiritStack.copy();
                BoxOfEternalClosure.setPlayerContent(playerStack, playerId);
                ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                        playerStack, 1, 1, 1));
            }
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                    BoxOfEternalClosure.boxOfEternalClosureFilled, 1, 1, 3));
        }
    }
    
    @Override
    public boolean isHardDisabled() {
        // Hard dependency on vengeance spirits.
        return !Configs.isEnabled(VengeanceSpiritConfig.class);
    }

}
