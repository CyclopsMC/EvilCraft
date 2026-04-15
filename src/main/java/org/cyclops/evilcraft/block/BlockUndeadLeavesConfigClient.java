package org.cyclops.evilcraft.block;

import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

import java.util.List;

/**
 * @author rubensworks
 */
public class BlockUndeadLeavesConfigClient extends BlockClientConfig<EvilCraft> {

    // Dominant Undead Leaves texture color (#2E0F0F = R46 G15 B15), encoded as ARGB 0xFF2E0F0F.
    private static final int UNDEAD_LEAVES_COLOR = -13758705;

    public BlockUndeadLeavesConfigClient(BlockConfigCommon<EvilCraft> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener(this::onRegisterColors);
    }

    public void onRegisterColors(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(BlockTintSources.constant(UNDEAD_LEAVES_COLOR)), getBlockConfig().getInstance());
    }
}
