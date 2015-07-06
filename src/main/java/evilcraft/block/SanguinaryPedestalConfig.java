package evilcraft.block;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

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
                EvilCraft._instance,
        	true,
            "sanguinaryPedestal",
            null,
            SanguinaryPedestal.class
        );
    }
    
    @Override
    public void onRegistered() {
        // TODO
        /*if(MinecraftHelpers.isClientSide()) {
        	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "pedestal.png");
        	ModelWavefront model = new ModelPedestal(texture);
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileSanguinaryPedestal.class,
            		new RenderTileEntitySanguinaryPedestal(model, texture));
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(SanguinaryPedestal.getInstance()),
            		new RenderItemSanguinaryPedestal(model, texture));
        }*/
    }
    
}
