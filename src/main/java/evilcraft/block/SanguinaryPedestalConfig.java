package evilcraft.block;

import evilcraft.Reference;
import evilcraft.client.render.item.RenderItemSanguinaryPedestal;
import evilcraft.client.render.model.ModelPedestal;
import evilcraft.client.render.tileentity.RenderTileEntitySanguinaryPedestal;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileSanguinaryPedestal;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

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
     * Blood multiplier when Efficiency is active.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "Blood multiplier when Efficiency is active.", isCommandable = true)
    public static double efficiencyBoost = 1.5D;

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
            		new RenderTileEntitySanguinaryPedestal(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(SanguinaryPedestal.getInstance()),
            		new RenderItemSanguinaryPedestal(model, texture));
        }
    }
    
}
