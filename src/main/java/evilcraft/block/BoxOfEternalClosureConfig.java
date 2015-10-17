package evilcraft.block;

import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.client.render.item.RenderItemBoxOfEternalClosure;
import evilcraft.client.render.model.ModelBoxOfEternalClosure;
import evilcraft.client.render.tileentity.RenderTileEntityBoxOfEternalClosure;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemBlockNBT;
import evilcraft.entity.monster.VengeanceSpiritConfig;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileBoxOfEternalClosure;
import evilcraft.tileentity.tickaction.spiritfurnace.BoxCookTickAction;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

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

    /**
     * Make a new instance.
     */
    public BoxOfEternalClosureConfig() {
        super(
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
    
    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
        	ModelBase model = new ModelBoxOfEternalClosure();
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "box.png");
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileBoxOfEternalClosure.class,
            		new RenderTileEntityBoxOfEternalClosure(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()),
            		new RenderItemBoxOfEternalClosure(model, texture));
        }

        ItemStack spiritStack = new ItemStack(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()), 1, 0);
        ItemStack swarmStack = spiritStack.copy();
        BoxOfEternalClosure.setVengeanceSwarmContent(swarmStack);
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            for(UUID playerId : BoxCookTickAction.PLAYERDROP_OVERRIDES.keySet()) {
                ItemStack playerStack = spiritStack.copy();
                BoxOfEternalClosure.setPlayerContent(playerStack, playerId);
                ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                        playerStack, 1, 1, 1));
            }
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                    swarmStack, 1, 1, 3));
        }
    }
    
    @Override
    public boolean isHardDisabled() {
        // Hard dependency on vengeance spirits.
        return !Configs.isEnabled(VengeanceSpiritConfig.class);
    }

}
