package evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.client.render.model.ModelPedestal;
import evilcraft.core.client.render.item.RenderModelWavefrontItem;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.client.render.tileentity.RenderTileEntityModelWavefront;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.item.BloodExtractorConfig;
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
     * The amount of blood (mB) that will be gained as a result from extraction.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The amount of blood (mB) that will be gained as a result from extraction.")
    public static int extractMB = BloodExtractorConfig.maxMB;

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
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "pedestal.png"); // TODO
        	ModelWavefront model = new ModelPedestal(texture);
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileSanguinaryPedestal.class,
            		new RenderTileEntityModelWavefront(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(SanguinaryPedestal.getInstance()),
            		new RenderModelWavefrontItem(model, texture));
        }
    }
    
}
