package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.item.ItemBlockNBT;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.tileentity.RenderBloodChest;

public class BloodChestConfig extends BlockConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If the Blood Chest should add random bad enchants with a small chance to repairing items.", isCommandable = true)
    public static boolean addRandomBadEnchants = true;
    
    public static BloodChestConfig _instance;

    public BloodChestConfig() {
        super(
            Reference.BLOCK_BLOODCHEST,
            "Blood Chest",
            "bloodChest",
            null,
            BloodChest.class
        );
    }
    
    @Override
    public boolean hasSubTypes() {
        return true;
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
