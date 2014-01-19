package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.item.ItemBlockNBT;

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
    
}
