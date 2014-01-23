package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.RenderBloodChest;

public class BloodChestConfig extends BlockConfig {
    
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
        ClientProxy.TILE_ENTITY_RENDERERS.put(TileBloodChest.class, new RenderBloodChest());
    }
    
}
