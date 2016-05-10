package org.cyclops.evilcraft.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.client.gui.container.GuiColossalBloodChest;
import org.cyclops.evilcraft.core.block.CubeDetector;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 *
 * @author rubensworks
 */
public class ColossalBloodChest extends ConfigurableBlockContainerGuiTankInfo implements CubeDetector.IDetectionListener {

    @BlockProperty
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    private static ColossalBloodChest _instance = null;

    /**
     * Get the unique instance.
     *
     * @return The instance.
     */
    public static ColossalBloodChest getInstance() {
        return _instance;
    }

    public ColossalBloodChest(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileColossalBloodChest.class);
        this.setHardness(5.0F);
        this.setStepSound(SoundType.WOOD);
        this.setHarvestLevel("axe", 2); // Iron tier
        this.setRotatable(false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityplayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float par7, float par8, float par9) {
        return !TileColossalBloodChest.canWork(world, blockPos) ||
                super.onBlockActivated(world, blockPos, blockState, entityplayer, hand, heldItem, side, par7, par8, par9);
    }

    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public String getTankNBTName() {
        return TileSpiritFurnace.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }

    private void triggerDetector(World world, BlockPos blockPos, boolean valid) {
        TileColossalBloodChest.detector.detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        triggerDetector(world, pos, true);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
        super.onBlockAdded(world, blockPos, blockState);
        triggerDetector(world, blockPos, true);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if((Boolean)state.getValue(ACTIVE)) triggerDetector(world, pos, false);
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onDetect(World world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            world.setBlockState(location, world.getBlockState(location).withProperty(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            TileColossalBloodChest tile = TileHelpers.getSafeTile(world, location, TileColossalBloodChest.class);
            if(tile != null) {
                tile.setSize(valid ? size : Vec3i.NULL_VECTOR);
                tile.setCenter(originCorner.add(1, 1, 1));
            }
        }
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerColossalBloodChest.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiColossalBloodChest.class;
    }
}
