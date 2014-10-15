package evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.client.render.model.ModelPedestal;
import evilcraft.core.client.render.item.RenderModelWavefrontItem;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.client.render.tileentity.RenderTileEntityModelWavefront;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileSanguinaryPedestal;

/**
 * Config for the {@link SanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class SanguinaryPedestalConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static SanguinaryPedestalConfig _instance;

    /**
     * Make a new instance.
     */
    public SanguinaryPedestalConfig() {
        super(
        	true,
            "sanguinaryPedestal",
            null,
            SanguinaryPedestal.class
        );
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "pedestal.png");
        	ModelWavefront model = new ModelPedestal(texture);
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileSanguinaryPedestal.class,
            		new RenderTileEntityModelWavefront(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(SanguinaryPedestal.getInstance()),
            		new RenderModelWavefrontItem(model, texture));
        }
    }
    
}
