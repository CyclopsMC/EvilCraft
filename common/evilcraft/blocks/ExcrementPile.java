package evilcraft.blocks;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;
import evilcraft.items.BroomConfig;

/**
 * The excrement that is dropped by animals.
 * @author rubensworks
 *
 */
public class ExcrementPile extends ConfigurableBlock implements IInformationProvider {
    
    private static ExcrementPile _instance = null;
    
    private static final int CHANCE_DESPAWN = 1; // 1/CHANCE_DESPAWN per random tick chance for despawning and potentially triggering bonemeal event
    private static final int CHANCE_BONEMEAL = 3; // 1/CHANCE_BONEMEAL for ground below to be bonemealed or turn into grass from dirt
    private static final int POISON_DURATION = 3;
    private static final int PIG_BOOST_DURATION = 3;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new ExcrementPile(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ExcrementPile getInstance() {
        return _instance;
    }

    private ExcrementPile(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.clay);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickRandomly(true);
        this.setBlockBoundsForPileDepth(0);
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return ExcrementPileConfig._instance.ID;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBoundsForPileDepth(0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setBlockBoundsForPileDepth(world.getBlockMetadata(x, y, z));
    }
    
    protected void setBlockBoundsForPileDepth(int meta) {
        int j = meta & 7;
        float f = (float)(2 * (1 + j)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int l = world.getBlockId(x, y - 1, z);        
        Block block = Block.blocksList[l];
        if (block == null) return false;
        if (block == this && (world.getBlockMetadata(x, y - 1, z) & 7) == 7) return true;
        if (!block.isLeaves(world, x, y - 1, z) && !Block.blocksList[l].isOpaqueCube()) return false;
        return world.getBlockMaterial(x, y - 1, z).blocksMovement();
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return player.getCurrentEquippedItem() != null
                && BroomConfig._instance.isEnabled()
                && player.getCurrentEquippedItem().itemID == BroomConfig._instance.ID;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
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
    public void updateTick(World world, int x, int y, int z, Random random) {
        if(random.nextInt(CHANCE_DESPAWN) == 0) {
            if(random.nextInt(CHANCE_BONEMEAL) == 0) {
                for(int xr = x - 2; xr <= x + 2; xr++) {
                    for(int zr = z - 2; zr <= z + 2; zr++) {
                        if(random.nextInt(9) == 0) {
                            int blockIDBelow = world.getBlockId(xr, y - 1, zr);
                            if(blockIDBelow == Block.dirt.blockID) {
                                world.setBlock(xr, y - 1, zr, Block.grass.blockID);
                            } else if(blockIDBelow == Block.grass.blockID) {
                                ItemDye.func_96604_a(new ItemStack(Item.dyePowder, 1, 15), world, xr, y - 1, zr);
                            }
                            ItemDye.func_96604_a(new ItemStack(Item.dyePowder, 1, 15), world, xr, y, zr);
                        }
                    }
                }
            }
            world.setBlockToAir(x, y, z);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if(random.nextInt(100) == 0) {
            int meta = world.getBlockMetadata(x, y, z);
            float height = (float)(meta + 1) / 8.0F;
            double d0 = (double)x + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)y + 0.0625F + height);
            double d2 = (double)z + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.2D;
            
            float f1 = 0.1F-random.nextFloat()*0.2F;
            float f2 = 0.1F-random.nextFloat()*0.2F;
            float f3 = 0.1F-random.nextFloat()*0.2F;
            world.spawnParticle("happyVillager", d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        return side == 1 ? true : super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return (meta & 7) + 1;
    }

    @Override
    public boolean isBlockReplaceable(World world, int x, int y, int z) {
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourBlockID) {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            // Pigs love excrement
            if(entity instanceof EntityPig) {
                if(isChanceWithHeight(world, x, y, z)) {
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.heal.id, PIG_BOOST_DURATION * 20, 1));
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, PIG_BOOST_DURATION * 20, 1));
                }
            } else if(entity instanceof EntityPlayer || ExcrementPileConfig.poisonEntities) {
                // Poison player or mob with a chance depending on the height of the pile.
                if(isChanceWithHeight(world, x, y, z))
                    ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, POISON_DURATION * 20, 1));
            }
        }
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }
    
    private boolean isChanceWithHeight(World world, int x, int y, int z) {
        float height = ((float) (world.getBlockMetadata(x, y, z) & 7) + 1) * 0.125F;
        return world.rand.nextFloat() * 10 < height;
    }
    
    /**
     * If the height of this block can be increased.
     * @param world the World
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return If this pile can become higher.
     */
    public static boolean canHeightenPileAt(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta < 7;
    }

    /**
     * Set the height of a pile.
     * @param world the World
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public static void heightenPileAt(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if(meta < 7)
            world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return IInformationProvider.INFO_PREFIX + "Will form below animals.";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}

}
