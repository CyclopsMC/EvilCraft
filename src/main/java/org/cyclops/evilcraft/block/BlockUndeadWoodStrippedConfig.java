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
 * Config for the Stripped Undead Wood.
 * @author rubensworks
 *
 */
public class BlockUndeadWoodStrippedConfig extends BlockConfig {

    public BlockUndeadWoodStrippedConfig() {
        super(
                EvilCraft._instance,
            "undead_wood_stripped",
                eConfig -> Blocks.createLogBlock(MaterialColor.ORANGE_TERRACOTTA, MaterialColor.ORANGE_TERRACOTTA),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 20);
        AxeItem.BLOCK_STRIPPING_MAP = Maps.newHashMap(AxeItem.BLOCK_STRIPPING_MAP);
        AxeItem.BLOCK_STRIPPING_MAP.put(RegistryEntries.BLOCK_UNDEAD_WOOD, RegistryEntries.BLOCK_UNDEAD_WOOD_STRIPPED);
    }
    
}
