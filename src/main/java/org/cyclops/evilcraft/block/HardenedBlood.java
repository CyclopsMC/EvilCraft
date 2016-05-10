package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockConnectedTexture;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.item.HardenedBloodShardConfig;

import java.util.Random;

/**
 * A hardened version of {@link Blood}.
 * @author rubensworks
 *
 */
public class HardenedBlood extends ConfigurableBlockConnectedTexture {
    
    private static HardenedBlood _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static HardenedBlood getInstance() {
        return _instance;
    }

    public HardenedBlood(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.ice);
        this.setStepSound(SoundType.STONE);
        this.setHardness(0.5F);
        
        this.setHarvestLevel("pickaxe", 0);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return null;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos blockPos, IBlockState blockState, TileEntity tile, ItemStack heldItem){
        // MCP: mineBlockStatArray
        player.addStat(StatList.func_188055_a(this), 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.silkTouch, heldItem) > 0) {
            ItemStack itemstack = this.createStackedBlock(blockState);

            if (itemstack != null) {
                spawnAsEntity(world, blockPos, itemstack);
            }
        } else {
            Material material = world.getBlockState(blockPos.add(0, -1, 0)).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlockState(blockPos, FluidBlockBlood.getInstance().getDefaultState());
            }
        }
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState blockState) {
        return EnumPushReaction.BLOCK;
    }
    
    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        world.setBlockState(blockPos, FluidBlockBlood.getInstance().getDefaultState());
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float coordX, float coordY, float coordZ) {
        if (heldItem != null && heldItem.getItem() == Items.flint_and_steel
                && (player.capabilities.isCreativeMode || !heldItem.attemptDamageItem(1, world.rand))) {
            splitBlock(world, blockPos);
            return true;
        }
        return super.onBlockActivated(world, blockPos, blockState, player, hand, heldItem, side, coordX, coordY, coordZ);
    }

    private void splitBlock(World world, BlockPos blockPos) {
        ItemStack itemStack = new ItemStack(HardenedBloodShardConfig._instance.getItemInstance(), HardenedBloodShardConfig.minimumDropped
        		+ (int) (Math.random() * (double) HardenedBloodShardConfig.additionalDropped));
        spawnAsEntity(world, blockPos, itemStack);
        world.setBlockToAir(blockPos);
    }
    
    @Override
    public boolean isNormalCube(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
        return false;
    }

}
