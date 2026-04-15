package org.cyclops.evilcraft.block;

import net.minecraft.client.color.block.BlockTintSources;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class BlockUndeadLeavesConfigClient extends BlockClientConfig<IModBase> {

    // Dominant Undead Leaves texture color (#2E0F0F = R46 G15 B15), encoded as ARGB 0xFF2E0F0F.
    private static final int UNDEAD_LEAVES_COLOR = -13758705;

    public BlockUndeadLeavesConfigClient(BlockConfigCommon<IModBase> blockConfig) {
        super(blockConfig);
    }

    @Override
    public net.minecraft.client.color.block.BlockTintSource getBlockColorHandler() {
        return BlockTintSources.constant(UNDEAD_LEAVES_COLOR);
    }
}
