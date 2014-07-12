package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;

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
    	// TODO
        /*if (Helpers.isClientSide()) {
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileBloodChest.class, new TileEntityBloodChestRenderer());
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BloodChest.getInstance()), new RenderItemBloodChest());
        }*/
    }
    
}
