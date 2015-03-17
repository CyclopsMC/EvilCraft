package evilcraft.block;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.item.ItemBlockMetadata;
import net.minecraft.item.ItemBlock;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "How many veins per chunk.")
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
