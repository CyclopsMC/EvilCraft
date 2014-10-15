package evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.client.render.model.ModelChalice;
import evilcraft.client.render.tileentity.RenderTileEntityEntangledChalice;
import evilcraft.core.client.render.item.RenderModelWavefrontItem;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileEntangledChalice;

/**
 * Config for the {@link EntangledChalice}.
 * @author rubensworks
 *
 */
public class EntangledChaliceConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static EntangledChaliceConfig _instance;

    /**
     * Make a new instance.
     */
    public EntangledChaliceConfig() {
        super(
        	true,
            "entangledChalice",
            null,
            EntangledChalice.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockFluidContainer.class;
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "pedestal.png"); // TODO
        	ModelWavefront model = new ModelChalice(texture);
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileEntangledChalice.class,
            		new RenderTileEntityEntangledChalice(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(EntangledChalice.getInstance()),
            		new RenderModelWavefrontItem(model, texture)); // TODO: show fluid in render
        }
    }
    
}
