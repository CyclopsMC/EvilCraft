package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.item.ItemBlockMetadata;
import org.cyclops.evilcraft.EvilCraft;

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
            EvilCraft._instance,
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
