package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.configurable.ConfigurableProperty;
import evilcraft.core.item.ItemBlockMetadata;

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
        	true,
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
