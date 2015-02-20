package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.entity.block.EntityLightningBombPrimed;
import evilcraft.entity.item.EntityLightningGrenade;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A bomb that spawns lightning when ignited.
 * @author rubensworks
 *
 */
public class LightningBomb extends ConfigurableBlock {
    
    private static LightningBomb _instance = null;
    
    private IIcon blockIconTop;
    private IIcon blockIconBottom;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new LightningBomb(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static LightningBomb getInstance() {
        return _instance;
    }

    private LightningBomb(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.tnt);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
        if (!world.isRemote) {
            EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), explosion.getExplosivePlacedBy());
            entityprimed.fuse = world.rand.nextInt(entityprimed.fuse / 4) + entityprimed.fuse / 8;
            world.spawnEntityInWorld(entityprimed);
        }
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        this.primeBomb(world, x, y, z, meta, (EntityLivingBase)null);
    }
    
    /**
     * spawns the primed bomb and plays the fuse sound.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param meta Meta of this block.
     * @param placer The entity that primed the bomb.
     */
    public void primeBomb(World world, int x, int y, int z, int meta, EntityLivingBase placer) {
        if (!world.isRemote) {
            if ((meta & 1) == 1) {
                EntityLightningBombPrimed entityprimed = new EntityLightningBombPrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), placer);
                world.spawnEntityInWorld(entityprimed);
                world.playSoundAtEntity(entityprimed, "random.fuse", 1.0F, 1.0F);
            }
        }
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 0 ? this.blockIconBottom : (side == 1 ? this.blockIconTop : this.blockIcon);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister ironRegister) {
        this.blockIcon = ironRegister.registerIcon(this.getTextureName() + "_side");
        this.blockIconTop = ironRegister.registerIcon(this.getTextureName() + "_top");
        this.blockIconBottom = ironRegister.registerIcon(this.getTextureName() + "_bottom");
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float coordX, float coordY, float coordZ) {
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.flint_and_steel) {
            this.primeBomb(world, x, y, z, 1, player);
            world.setBlockToAir(x, y, z);
            player.getCurrentEquippedItem().damageItem(1, player);
            return true;
        } else {
            return super.onBlockActivated(world, x, y, z, player, meta, coordX, coordY, coordZ);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityArrow && !world.isRemote) {
            EntityArrow entityarrow = (EntityArrow)entity;

            if (entityarrow.isBurning()) {
                this.primeBomb(world, x, y, z, 1, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                world.setBlockToAir(x, y, z);
            }
        } else if (entity instanceof EntityLightningGrenade && !world.isRemote) {
            EntityLightningGrenade entitygrenade = (EntityLightningGrenade)entity;

            this.primeBomb(world, x, y, z, 1, entitygrenade.getThrower() != null ? entitygrenade.getThrower() : null);
            world.setBlockToAir(x, y, z);
        }
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

}
