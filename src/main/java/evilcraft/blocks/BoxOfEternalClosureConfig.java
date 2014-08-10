package evilcraft.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;
import evilcraft.entities.monster.VengeanceSpiritConfig;
import evilcraft.entities.tileentities.TileBoxOfEternalClosure;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.item.RenderItemBoxOfEternalClosure;
import evilcraft.render.models.BoxOfEternalClosureModel;
import evilcraft.render.tileentity.TileEntityBoxOfEternalClosureRenderer;

/**
 * Config for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class BoxOfEternalClosureConfig extends BlockConfig {
    
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
        if (Helpers.isClientSide()) {
        	ModelBase model = new BoxOfEternalClosureModel();
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "box.png");
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileBoxOfEternalClosure.class,
            		new TileEntityBoxOfEternalClosureRenderer(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()),
            		new RenderItemBoxOfEternalClosure(model, texture));
        }
        
        ItemStack spiritStack = new ItemStack(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()));
        BoxOfEternalClosure.setVengeanceSwarmContent(spiritStack);
        for(String chestCategory : Helpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
            		spiritStack, 1, 5, 5));
        }
    }
    
    @Override
    public boolean isHardDisabled() {
        // Hard dependency on vengeance spirits.
        return !Configs.isEnabled(VengeanceSpiritConfig.class);
    }

}
