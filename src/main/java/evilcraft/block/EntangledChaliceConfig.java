package evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.client.render.item.RenderItemEntangledChalice;
import evilcraft.client.render.model.ModelChalice;
import evilcraft.client.render.model.ModelGem;
import evilcraft.client.render.tileentity.RenderTileEntityEntangledChalice;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
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
        return EntangledChaliceItem.class;
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
        	ResourceLocation textureGem = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "gem.png");
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "chalice.png");
        	ModelGem gem = new ModelGem(textureGem);
        	ModelChalice model = new ModelChalice(texture, gem);
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileEntangledChalice.class,
            		new RenderTileEntityEntangledChalice(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(EntangledChalice.getInstance()),
            		new RenderItemEntangledChalice(model, texture));
        }
    }
    
}
