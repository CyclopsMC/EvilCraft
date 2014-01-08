package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.block.EntityLightningBombPrimed;

public class LightningBomb extends ConfigurableBlock {
    
    private static LightningBomb _instance = null;
    
    private Icon blockIconTop;
    private Icon blockIconBottom;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new LightningBomb(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static LightningBomb getInstance() {
        return _instance;
    }

    private LightningBomb(ExtendedConfig eConfig) {
        super(eConfig, Material.tnt);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundPowderFootstep);
    }
    
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);

        if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)) {
            this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourID) {
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
    
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        this.primeBomb(world, x, y, z, meta, (EntityLivingBase)null);
    }
    
    /**
     * spawns the primed bomb and plays the fuse sound.
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
    public Icon getIcon(int par1, int par2) {
        return par1 == 0 ? this.blockIconBottom : (par1 == 1 ? this.blockIconTop : this.blockIcon);
    }
    
    @Override
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(this.getTextureName() + "_side");
        this.blockIconTop = par1IconRegister.registerIcon(this.getTextureName() + "_top");
        this.blockIconBottom = par1IconRegister.registerIcon(this.getTextureName() + "_bottom");
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par5EntityPlayer.getCurrentEquippedItem() != null && par5EntityPlayer.getCurrentEquippedItem().itemID == Item.flintAndSteel.itemID) {
            this.primeBomb(par1World, par2, par3, par4, 1, par5EntityPlayer);
            par1World.setBlockToAir(par2, par3, par4);
            par5EntityPlayer.getCurrentEquippedItem().damageItem(1, par5EntityPlayer);
            return true;
        } else {
            return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par5Entity instanceof EntityArrow && !par1World.isRemote) {
            EntityArrow entityarrow = (EntityArrow)par5Entity;

            if (entityarrow.isBurning()) {
                this.primeBomb(par1World, par2, par3, par4, 1, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                par1World.setBlockToAir(par2, par3, par4);
            }
        }
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

}
