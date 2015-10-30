package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainerGui;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.client.gui.container.GuiSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class SanguinaryEnvironmentalAccumulator extends ConfigurableBlockContainerGui {

    private static SanguinaryEnvironmentalAccumulator _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryEnvironmentalAccumulator getInstance() {
        return _instance;
    }

    public SanguinaryEnvironmentalAccumulator(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSanguinaryEnvironmentalAccumulator.class);
        this.setStepSound(soundTypeStone);
        this.setRotatable(true);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        TileSanguinaryEnvironmentalAccumulator tile = (TileSanguinaryEnvironmentalAccumulator) world.getTileEntity(pos);
        return tile.isVisuallyWorking() ? 4 : super.getLightValue(world, pos);
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerSanguinaryEnvironmentalAccumulator.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiSanguinaryEnvironmentalAccumulator.class;
    }
}
