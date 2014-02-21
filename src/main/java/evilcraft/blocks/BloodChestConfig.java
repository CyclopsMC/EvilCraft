package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.item.ItemBlockNBT;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.tileentity.RenderBloodChest;

/**
 * Config for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class BloodChestConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static BloodChestConfig _instance;
    
    /**
     * If the Blood Chest should add random bad enchants.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If the Blood Chest should add random bad enchants with a small chance to repairing items.", isCommandable = true)
    public static boolean addRandomBadEnchants = true;

    /**
     * Make a new instance.
     */
    public BloodChestConfig() {
        super(
        	true,
            "bloodChest",
            null,
            BloodChest.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
    @Override
    public void onRegistered() {
        if (Helpers.isClientSide())
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileBloodChest.class, new RenderBloodChest());
    }
    
}
