package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class EternalWaterBlockConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static EternalWaterBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public EternalWaterBlockConfig() {
        super(
                EvilCraft._instance,
        	true,
            "eternalWaterBlock",
            null,
            EternalWaterBlock.class
        );
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlock.class;
    }
    
}
