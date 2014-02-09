package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.item.ItemBlockMetadata;

/**
 * Config for the {@link NetherfishSpawn}.
 * @author rubensworks
 *
 */
public class NetherfishSpawnConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static NetherfishSpawnConfig _instance;
    
    /**
     * The amount of veins per chunk.
     */
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 250;

    /**
     * Make a new instance.
     */
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
