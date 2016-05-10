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
import net.minecraft.tileentity.TileEntity;
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
import org.cyclops.evilcraft.client.gui.container.GuiSpiritFurnace;
import org.cyclops.evilcraft.core.block.CubeDetector;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritFurnace;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class SpiritFurnace extends ConfigurableBlockContainerGuiTankInfo implements CubeDetector.IDetectionListener {

    @BlockProperty
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    private static SpiritFurnace _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritFurnace getInstance() {
        return _instance;
    }

    public SpiritFurnace(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSpiritFurnace.class);
        this.setHardness(5.0F);
        this.setStepSound(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
        this.setRotatable(true);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityplayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float par7, float par8, float par9) {
        return !TileSpiritFurnace.canWork(world, blockPos) ||
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
        TileSpiritFurnace.detector.detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if(!world.isRemote) {
            triggerDetector(world, pos, true);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
        super.onBlockAdded(world, blockPos, blockState);
        if(!world.isRemote) {
            triggerDetector(world, blockPos, true);
        }
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
            boolean change = !(Boolean) world.getBlockState(location).getValue(ACTIVE);
            world.setBlockState(location, world.getBlockState(location).withProperty(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            TileEntity tile = world.getTileEntity(location);
            if(tile != null) {
                ((TileSpiritFurnace) tile).setSize(valid ? size : Vec3i.NULL_VECTOR);
            }
            if(change) {
                TileSpiritFurnace.detectStructure(world, location, size, valid, originCorner);
            }
        }
	}

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerSpiritFurnace.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiSpiritFurnace.class;
    }
}
