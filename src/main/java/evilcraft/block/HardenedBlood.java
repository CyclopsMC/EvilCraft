package evilcraft.block;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.fluid.Blood;
import evilcraft.item.HardenedBloodShard;

/**
 * A hardened version of {@link Blood}.
 * @author rubensworks
 *
 */
public class HardenedBlood extends ConfigurableBlockConnectedTexture {
    
    private static HardenedBlood _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new HardenedBlood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static HardenedBlood getInstance() {
        return _instance;
    }

    private HardenedBlood(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.ice);
        this.setStepSound(soundTypeStone);
        this.setHardness(0.5F);
        
        this.setHarvestLevel("pickaxe", 0);
    }
    
    @Override
    public Item getItemDropped(int par1, Random random, int zero) {
        return null;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta){
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
            ItemStack itemstack = this.createStackedBlock(meta);

            if (itemstack != null) {
                this.dropBlockAsItem(world, x, y, z, itemstack);
            }
        } else {
            Material material = world.getBlock(x, y - 1, z).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlock(x, y, z, FluidBlockBlood.getInstance());
            }
        }
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
    
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        world.setBlock(x, y, z, FluidBlockBlood.getInstance());
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float coordX, float coordY, float coordZ) {
        if (player.getCurrentEquippedItem() != null) {
            if (player.getCurrentEquippedItem().getItem() == Items.flint_and_steel) {
                if(player.capabilities.isCreativeMode || !player.getCurrentEquippedItem().attemptDamageItem(1, world.rand))
                    splitBlock(world, x, y, z);
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, meta, coordX, coordY, coordZ);
    }

    private void splitBlock(World world, int x, int y, int z) {
        ItemStack itemStack = new ItemStack(HardenedBloodShard.getInstance(), 9);
        dropBlockAsItem(world, x, y, z, itemStack);
        world.setBlockToAir(x, y, z);
    }
    
    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

}
