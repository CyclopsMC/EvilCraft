package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.item.ItemBlockMetadata;

public class NetherfishSpawnConfig extends BlockConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 250;
    
    public static NetherfishSpawnConfig _instance;

    public NetherfishSpawnConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Nether Monster Egg",
            "netherMonsterBlock",
            null,
            NetherfishSpawn.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockMetadata.class;
    }
    
}
