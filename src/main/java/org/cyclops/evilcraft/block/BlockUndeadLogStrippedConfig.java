package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.world.BlockEvent;
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
        MinecraftForge.EVENT_BUS.addListener(this::toolActionEvent);
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 20);
    }

    public void toolActionEvent(BlockEvent.BlockToolInteractEvent event) {
        if (event.getToolAction() == ToolActions.AXE_STRIP && event.getState().getBlock() == RegistryEntries.BLOCK_UNDEAD_LOG) {
            BlockState blockStateNew = RegistryEntries.BLOCK_UNDEAD_LOG_STRIPPED.defaultBlockState();
            for (Property property : event.getState().getProperties()) {
                if(blockStateNew.hasProperty(property))
                    blockStateNew = blockStateNew.setValue(property, event.getState().getValue(property));
            }
            event.setFinalState(blockStateNew);
        }
    }
    
}
