package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.item.DarkGem;
import org.cyclops.evilcraft.item.DarkGemConfig;
import org.cyclops.evilcraft.item.DarkGemCrushedConfig;

import java.util.List;
import java.util.Random;

/**
 * Ore that drops {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkOre extends ConfigurableBlock implements IInformationProvider {
    
    private static DarkOre _instance = null;
    private static final int MINIMUM_DROPS = 1; // Minimum amount of drops when mining this blockState
    private static final int INCREASE_DROPS = 3; // Amount that can be increased at random for drops
    private static final int INCREASE_XP = 5; // Amount of XP that can be gained from mining this blockState
    private static final int CRUSHEDCHANCE = 4; // The chance on a crushed dark gem with no fortune.

    @BlockProperty(ignore = true)
    public static final PropertyBool GLOWING = PropertyBool.create("glowing");
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkOre getInstance() {
        return _instance;
    }

    public DarkOre(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setTickRandomly(true);
        this.setHardness(3.0F);
        this.setStepSound(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        if(Configs.isEnabled(DarkGemConfig.class))
            return DarkGem.getInstance();
        else
            return null;
    }
    
    @Override
    public int quantityDroppedWithBonus(int amount, Random random) {
        return this.quantityDropped(random) + random.nextInt(amount / 4 + 1);
    }

    @Override
    public int quantityDropped(Random random) {
        return MINIMUM_DROPS + random.nextInt(INCREASE_DROPS);
    }
    
    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState blockState, float dropchance, int fortune) {
        super.dropBlockAsItemWithChance(world, blockPos, blockState, dropchance, fortune);

        if (this.getItemDropped(blockState, world.rand, fortune) != Item.getItemFromBlock(this)) {
            int xp = 1 + world.rand.nextInt(INCREASE_XP);
            this.dropXpOnBlockBreak(world, blockPos, xp);
        }
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockState, int fortune) {
    	List<ItemStack> drops = super.getDrops(world, blockPos, blockState, fortune);
        Random rand = new Random();
    	if((fortune > 0 || rand.nextInt(CRUSHEDCHANCE) == 0)
    			&& Configs.isEnabled(DarkGemCrushedConfig.class)) {
    		drops.add(new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(),
    				rand.nextInt(fortune / 3 + 1) + 1));
    	}
        return drops;
    }
    
    @Override
    protected ItemStack createStackedBlock(IBlockState blockState) {
        return new ItemStack(DarkOre._instance);
    }
    
    @Override
    public int tickRate(World world) {
        return 30;
    }
    
    @Override
	public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player) {
    	player.addStat(Achievements.FIRST_AGE, 1);
    }
    
    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer player) {
        this.glow(world, blockPos);
        super.onBlockClicked(world, blockPos, player);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState blockState, Entity entity) {
        if(world.isRemote) {
            this.glow(world, blockPos);
        }
        super.onEntityCollidedWithBlock(world, blockPos, blockState, entity);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side,  float motionX, float motionY, float motionZ) {
        this.glow(world, blockPos);
        return super.onBlockActivated(world, blockPos, blockState, player, hand, heldItem, side, motionX, motionY, motionZ);
    }
    
    private boolean isGlowing(World world, BlockPos blockPos) {
        return BlockHelpers.getSafeBlockStateProperty(world.getBlockState(blockPos), GLOWING, false);
    }

    private void glow(World world, BlockPos blockPos) {
    	if (!world.isRemote)
    		return;

        this.sparkle(world, blockPos);

        if (!isGlowing(world, blockPos)) {
            world.setBlockState(blockPos, this.blockState.getBaseState().withProperty(GLOWING, true), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            world.scheduleUpdate(blockPos, this, tickRate(world));
        }
    }
    
    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState state, Random random) {
        if (isGlowing(world, blockPos)) {
            world.setBlockState(blockPos, getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos blockPos, Random random) {
        if (isGlowing(world, blockPos)) {
            this.sparkle(world, blockPos);
        }
    }

    @SideOnly(Side.CLIENT)
    private void sparkle(World world, BlockPos blockPos) {
        if (!world.isRemote)
    		return;
    	
        Random random = world.rand;
        double offset = 0.0625D;

        for (int l = 0; l < 6; ++l) {
            double sparkX = (double)((float)blockPos.getX() + random.nextFloat());
            double sparkY = (double)((float)blockPos.getY() + random.nextFloat());
            double sparkZ = (double)((float)blockPos.getZ() + random.nextFloat());

            if (l == 0 && !world.getBlockState(blockPos.add(0, 1, 0)).isNormalCube()) {
                sparkY = (double)(blockPos.getY() + 1) + offset;
            }

            if (l == 1 && !world.getBlockState(blockPos.add(0, -1, 0)).isNormalCube()) {
                sparkY = (double)(blockPos.getY()) - offset;
            }

            if (l == 2 && !world.getBlockState(blockPos.add(0, 0, 1)).isNormalCube()) {
                sparkZ = (double)(blockPos.getZ() + 1) + offset;
            }

            if (l == 3 && !world.getBlockState(blockPos.add(0, 0, -1)).isNormalCube()) {
                sparkZ = (double)(blockPos.getZ()) - offset;
            }

            if (l == 4 && !world.getBlockState(blockPos.add(1, 0, 0)).isNormalCube()) {
                sparkX = (double)(blockPos.getX() + 1) + offset;
            }

            if (l == 5 && !world.getBlockState(blockPos.add(-1, 0, 0)).isNormalCube()) {
                sparkX = (double)(blockPos.getX()) - offset;
            }

            if (sparkX < (double)blockPos.getX()
                    || sparkX > (double)(blockPos.getX() + 1)
                    || sparkY < 0.0D
                    || sparkY > (double)(blockPos.getY() + 1)
                    || sparkZ < (double)blockPos.getZ()
                    || sparkZ > (double)(blockPos.getZ() + 1)) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, sparkX, sparkY, sparkZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public String getInfo(ItemStack itemStack) {
    	return IInformationProvider.INFO_PREFIX + L10NHelpers.localize(this.getUnlocalizedName()
                + ".info.custom", DarkOreConfig.startY, DarkOreConfig.endY);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}

}
