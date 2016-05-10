package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.core.block.CubeDetector;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

/**
 * Part of the Colossal Blood Chest multiblock structure.
 * @author rubensworks
 *
 */
public class ReinforcedUndeadPlank extends ConfigurableBlock implements CubeDetector.IDetectionListener {

    @BlockProperty
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    private static ReinforcedUndeadPlank _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ReinforcedUndeadPlank getInstance() {
        return _instance;
    }

    public ReinforcedUndeadPlank(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(SoundType.WOOD);
        this.setHarvestLevel("axe", 2); // Iron tier
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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

    @Override
    public boolean canCreatureSpawn(IBlockState blockState, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    private void triggerDetector(World world, BlockPos blockPos, boolean valid) {
        TileColossalBloodChest.detector.detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if(!world.captureBlockSnapshots) {
            triggerDetector(world, pos, true);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
        super.onBlockAdded(world, blockPos, blockState);
        if(!world.captureBlockSnapshots) {
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
            if(change) {
                TileColossalBloodChest.detectStructure(world, location, size, valid, originCorner);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player,
                                    EnumHand hand, ItemStack heldItem, EnumFacing side,
                                    float posX, float posY, float posZ) {
        if((Boolean) blockState.getValue(ACTIVE)) {
            final Wrapper<BlockPos> tileLocationWrapper = new Wrapper<BlockPos>();
            TileColossalBloodChest.detector.detect(world, blockPos, null, new CubeDetector.IValidationAction() {

                @Override
                public void onValidate(BlockPos location, Block block) {
                    if(block == ColossalBloodChest.getInstance()) {
                        tileLocationWrapper.set(location);
                    }
                }

            }, false);
            BlockPos tileLocation = tileLocationWrapper.get();
            if(tileLocation != null) {
                world.getBlockState(tileLocation).getBlock().
                        onBlockActivated(world, tileLocation, world.getBlockState(tileLocation),
                                player, hand, heldItem, side, posX, posY, posZ);
                return true;
            }
        }
        return super.onBlockActivated(world, blockPos, blockState, player, hand, heldItem, side, posX, posY, posZ);
    }

}
