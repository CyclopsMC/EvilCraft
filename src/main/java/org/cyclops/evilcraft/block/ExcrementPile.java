package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.item.Broom;
import org.cyclops.evilcraft.item.BroomConfig;

import java.util.Random;

/**
 * The excrement that is dropped by animals.
 * @author rubensworks
 *
 */
public class ExcrementPile extends ConfigurableBlock {

    @BlockProperty
    public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);

    private static ExcrementPile _instance = null;
    
    private static final int CHANCE_DESPAWN = 1; // 1/CHANCE_DESPAWN per random tick chance for despawning and potentially triggering bonemeal event
    private static final int CHANCE_BONEMEAL = 3; // 1/CHANCE_BONEMEAL for ground below to be bonemealed or turn into grass from dirt
    private static final int POISON_DURATION = 3;
    private static final int PIG_BOOST_DURATION = 3;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ExcrementPile getInstance() {
        return _instance;
    }

    public ExcrementPile(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.clay);
        this.setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(LAYERS) < 5;
    }

    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        AxisAlignedBB bb = getBoundingBox(blockState, worldIn, pos);
        int i = blockState.getValue(LAYERS) - 1;
        float f = 0.125F;
        return new AxisAlignedBB((double)pos.getX() + bb.minX, (double)pos.getY() + bb.minY, (double)pos.getZ() + bb.minZ, (double)pos.getX() + bb.maxX, (double)((float)pos.getY() + (float)i * f), (double)pos.getZ() + bb.maxZ);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, (float)getHeight(blockState) / 8.0F, 1.0F);
    }

    protected int getHeight(IBlockState blockState) {
        return blockState.getValue(LAYERS);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos.add(0, -1, 0));
        if (blockState == null) return false;
        if (blockState.getBlock() == this && (Integer) blockState.getValue(LAYERS) == 8) return true;
        return (blockState.getBlock().isLeaves(blockState, world, blockPos.add(0, -1, 0))
                || blockState.isOpaqueCube()) && blockState.getBlock().getMaterial(blockState).blocksMovement();
    }
    
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos blockPos, EntityPlayer player) {
        return player.getActiveItemStack() != null
                && Configs.isEnabled(BroomConfig.class)
                && player.getActiveItemStack().getItem() == Broom.getInstance();
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos blockPos, IBlockState blockState, TileEntity tile, ItemStack heldItem) {
        super.harvestBlock(world, player, blockPos, blockState, tile, heldItem);
        world.setBlockToAir(blockPos);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    
    @Override
    public int tickRate(World world) {
        return 100;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState blockState, Random random) {
        if(random.nextInt(CHANCE_DESPAWN) == 0) {
            if(random.nextInt(CHANCE_BONEMEAL) == 0) {
                for(int xr = - 2; xr <= 2; xr++) {
                    for(int zr = - 2; zr <= 2; zr++) {
                        if(random.nextInt(9) == 0) {
                            IBlockState blockBelow = world.getBlockState(blockPos.add(xr, -1, zr));
                            if(blockBelow.getBlock() == Blocks.dirt) {
                                world.setBlockState(blockPos.add(xr, -1, zr), Blocks.grass.getDefaultState());
                            } else if(blockBelow == Blocks.grass) {
                                ItemDye.applyBonemeal(new ItemStack(Items.dye, 1), world, blockPos.add(xr, -1, zr));
                            }
                            ItemDye.applyBonemeal(new ItemStack(Items.dye, 1), world, blockPos.add(xr, -1, zr));
                        }
                    }
                }
            }
            world.setBlockToAir(blockPos);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random random) {
        if(random.nextInt(100) == 0) {
            float height = (float)(getHeight(blockState)) / 8.0F;
            double d0 = (double)blockPos.getX() + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)blockPos.getY() + 0.0625F + height);
            double d2 = (double)blockPos.getZ() + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            
            float f1 = 0.1F-random.nextFloat()*0.2F;
            float f2 = 0.1F-random.nextFloat()*0.2F;
            float f3 = 0.1F-random.nextFloat()*0.2F;
            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos blockPos, EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(blockState, world, blockPos, side);
    }

    @Override
    public int quantityDropped(IBlockState blockState, int fortune, Random random) {
        return getHeight(blockState);
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos blockPos) {
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState blockState, Block neighbourBlock) {
        if (!this.canPlaceBlockAt(world, blockPos)) {
            world.setBlockToAir(blockPos);
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            // Pigs love excrement
            if(entity instanceof EntityPig) {
                if(isChanceWithHeight(world, blockPos)) {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.heal, PIG_BOOST_DURATION * 20, 1));
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.moveSpeed, PIG_BOOST_DURATION * 20, 1));
                }
            } else if(entity instanceof EntityPlayer || ExcrementPileConfig.poisonEntities) {
                // Poison player or mob with a chance depending on the height of the pile.
                if(isChanceWithHeight(world, blockPos))
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.poison, POISON_DURATION * 20, 1));
            }
        }
        super.onEntityCollidedWithBlock(world, blockPos, entity);
    }
    
    private boolean isChanceWithHeight(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        float height = ((float) getHeight(blockState)) * 0.125F;
        return world.rand.nextFloat() * 10 < height;
    }
    
    /**
     * If the height of this blockState can be increased.
     * @param world The World
     * @param blockPos The position.
     * @return If this pile can become higher.
     */
    public boolean canHeightenPileAt(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        return getHeight(blockState) < 8;
    }

    /**
     * Set the height of a pile.
     * @param world the World
     * @param blockPos The position.
     */
    public void heightenPileAt(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        int height = getHeight(blockState);
        if(height < 8)
            world.setBlockState(blockPos, this.blockState.getBaseState().withProperty(LAYERS, height + 1), 2);
    }

}
