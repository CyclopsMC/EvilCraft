package evilcraft.blocks;
import java.util.List;
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
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.fluids.Blood;
import evilcraft.items.HardenedBloodShard;

/**
 * A hardened version of {@link Blood}.
 * @author rubensworks
 *
 */
public class HardenedBlood extends ConfigurableBlockConnectedTexture implements IInformationProvider {
    
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
        this.setStepSound(Block.soundStoneFootstep);
        this.setHardness(0.5F);
        
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return 0;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float coordX, float coordY, float coordZ) {
        if (player.getCurrentEquippedItem() != null) {
            if (player.getCurrentEquippedItem().itemID == Item.flintAndSteel.itemID) {
                if(player.capabilities.isCreativeMode || !player.getCurrentEquippedItem().attemptDamageItem(1, world.rand))
                    splitBlock(world, x, y, z);
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, meta, coordX, coordY, coordZ);
    }

    private void splitBlock(World world, int x, int y, int z) {
        ItemStack itemStack = new ItemStack(HardenedBloodShard.getInstance(), 9);
        dropBlockAsItem_do(world, x, y, z, itemStack);
        world.setBlockToAir(x, y, z);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return IInformationProvider.INFO_PREFIX + "Created when Blood dries out. Will liquidify when raining.";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}
    
    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z) {
        return false;
    }

}
