package evilcraft.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;
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
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "boxOfEternalClosure.png");
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileBoxOfEternalClosure.class,
            		new TileEntityBoxOfEternalClosureRenderer(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()),
            		new RenderItemBoxOfEternalClosure(model, texture));
        }
    }
    
}
