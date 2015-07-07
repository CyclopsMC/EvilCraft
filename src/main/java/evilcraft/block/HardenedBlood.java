package evilcraft.block;

import evilcraft.fluid.Blood;
import evilcraft.item.HardenedBloodShardConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Random;

/**
 * A hardened version of {@link Blood}.
 * TODO: connected textures
 * @author rubensworks
 *
 */
public class HardenedBlood extends ConfigurableBlock {
    
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
        this.setStepSound(soundTypeStone);
        this.setHardness(0.5F);
        
        this.setHarvestLevel("pickaxe", 0);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return null;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos blockPos, IBlockState blockState, TileEntity tile){
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
            ItemStack itemstack = this.createStackedBlock(blockState);

            if (itemstack != null) {
                spawnAsEntity(world, blockPos, itemstack);
            }
        } else {
            Material material = world.getBlockState(blockPos.add(0, -1, 0)).getBlock().getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlockState(blockPos, FluidBlockBlood.getInstance().getDefaultState());
            }
        }
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
    
    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        world.setBlockState(blockPos, FluidBlockBlood.getInstance().getDefaultState());
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side, float coordX, float coordY, float coordZ) {
        if (player.getCurrentEquippedItem() != null) {
            if (player.getCurrentEquippedItem().getItem() == Items.flint_and_steel) {
                if(player.capabilities.isCreativeMode || !player.getCurrentEquippedItem().attemptDamageItem(1, world.rand))
                    splitBlock(world, blockPos);
                return true;
            }
        }
        return super.onBlockActivated(world, blockPos, blockState, player, side, coordX, coordY, coordZ);
    }

    private void splitBlock(World world, BlockPos blockPos) {
        ItemStack itemStack = new ItemStack(HardenedBloodShardConfig._instance.getItemInstance(), HardenedBloodShardConfig.minimumDropped
        		+ (int) (Math.random() * (double) HardenedBloodShardConfig.additionalDropped));
        spawnAsEntity(world, blockPos, itemStack);
        world.setBlockToAir(blockPos);
    }
    
    @Override
    public boolean isNormalCube(IBlockAccess world, BlockPos blockPos) {
        return false;
    }

}
