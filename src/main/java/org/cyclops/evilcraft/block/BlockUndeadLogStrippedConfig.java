package org.cyclops.evilcraft.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.AxeItem;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Stripped Undead Log.
 * @author rubensworks
 *
 */
public class BlockUndeadLogStrippedConfig extends BlockConfig {

    public BlockUndeadLogStrippedConfig() {
        super(
                EvilCraft._instance,
            "undead_log_stripped",
                eConfig -> Blocks.log(MaterialColor.TERRACOTTA_ORANGE, MaterialColor.TERRACOTTA_ORANGE),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 20);
        AxeItem.STRIPABLES = Maps.newHashMap(AxeItem.STRIPABLES);
        AxeItem.STRIPABLES.put(RegistryEntries.BLOCK_UNDEAD_LOG, RegistryEntries.BLOCK_UNDEAD_LOG_STRIPPED);
    }
    
}
