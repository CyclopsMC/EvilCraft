package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.items.HardenedBloodShard;

public class HardenedBlood extends ConfigurableBlockConnectedTexture {
    
    private static HardenedBlood _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new HardenedBlood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static HardenedBlood getInstance() {
        return _instance;
    }

    private HardenedBlood(ExtendedConfig eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(2F);
        this.setStepSound(Block.soundSnowFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return 0;
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta){
        player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
            ItemStack itemstack = this.createStackedBlock(meta);

            if (itemstack != null) {
                this.dropBlockAsItem_do(world, x, y, z, itemstack);
            }
        } else {
            int fortune = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(world, x, y, z, meta, fortune);
            Material material = world.getBlockMaterial(x, y - 1, z);

            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlock(x, y, z, FluidBlockBloodConfig._instance.ID);
            }
        }
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
    
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        world.setBlock(x, y, z, FluidBlockBloodConfig._instance.ID);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9) {
        if (player.getCurrentEquippedItem() != null) {
            if (player.getCurrentEquippedItem().itemID == Item.flintAndSteel.itemID) {
                if(player.capabilities.isCreativeMode || !player.getCurrentEquippedItem().attemptDamageItem(1, world.rand))
                    splitBlock(world, x, y, z);
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9);
    }

    private void splitBlock(World world, int x, int y, int z) {
        ItemStack itemStack = new ItemStack(HardenedBloodShard.getInstance(), 9);
        dropBlockAsItem_do(world, x, y, z, itemStack);
        world.setBlockToAir(x, y, z);
    }

}
