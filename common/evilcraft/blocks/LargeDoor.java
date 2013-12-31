package evilcraft.blocks;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlockDoor;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.items.LargeDoorItemConfig;

public class LargeDoor extends ConfigurableBlockDoor {
    
    private static LargeDoor _instance = null;
    
    private Icon[] blockIconUpper;
    private Icon[] blockIconMiddle;
    private Icon[] blockIconLower;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new LargeDoor(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static LargeDoor getInstance() {
        return _instance;
    }

    private LargeDoor(ExtendedConfig eConfig) {
        super(eConfig, Material.wood);
        this.setBlockBounds(0, 0.0F, 0, 1, 2.0F, 1); // Height +2
        this.setHardness(3.0F);
        this.setStepSound(soundWoodFootstep);
        this.disableStats();
    }
    
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return this.blockIconLower[0];
    }
    
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
        if (side != 1 && side != 0)
        {
            int meta = this.getFullMetadata(par1IBlockAccess, x, y, z);
            int orientation = meta & 3;
            boolean isOpen = (meta & 4) != 0;
            boolean flipped = false;
            boolean isMiddle = (meta & 8) != 0;
            boolean isUpper = (meta & 16) != 0;

            if (isOpen) {
                if (orientation == 0 && side == 2) {
                    flipped = true;
                }
                else if (orientation == 1 && side == 5) {
                    flipped = true;
                }
                else if (orientation == 2 && side == 3) {
                    flipped = true;
                }
                else if (orientation == 3 && side == 4) {
                    flipped = true;
                }
            } else {
                if (orientation == 0 && side == 5) {
                    flipped = true;
                }
                else if (orientation == 1 && side == 3) {
                    flipped = true;
                }
                else if (orientation == 2 && side == 4) {
                    flipped = true;
                }
                else if (orientation == 3 && side == 2) {
                    flipped = true;
                }

                if ((orientation & 16) != 0) {
                    flipped = true;
                }
            }
            
            if(isUpper) return this.blockIconUpper[flipped ? 1 : 0];
            else if(isMiddle) return this.blockIconMiddle[flipped ? 1 : 0];
            else           return this.blockIconLower[flipped ? 1 : 0];
        } else {
            return this.blockIconMiddle[0];
        }
    }
    
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        int meta = this.getFullMetadata(par1World, par2, par3, par4);
        int isOpen = (meta & 7) ^ 4; // To open door (metadata)

        // Update the lowest part of the door
        
        if ((meta & 8) == 0) // Lower
        {
            EvilCraft.log("lower");
            par1World.setBlockMetadataWithNotify(par2, par3, par4, isOpen, 2);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
        }
        else if ((meta & 8) == 1) // Middle
        {
            EvilCraft.log("middle");
            par1World.setBlockMetadataWithNotify(par2, par3 - 1, par4, isOpen, 2);
            par1World.markBlockRangeForRenderUpdate(par2, par3 - 1, par4, par2, par3, par4);
        }
        else if ((meta & 16) == 1) // Upper
        {
            EvilCraft.log("upper");
            par1World.setBlockMetadataWithNotify(par2, par3 - 2, par4, isOpen, 2);
            par1World.markBlockRangeForRenderUpdate(par2, par3 - 2, par4, par2, par3, par4);
        }

        par1World.playAuxSFXAtEntity(par5EntityPlayer, 1003, par2, par3, par4, 0);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIconUpper = new Icon[2];
        this.blockIconMiddle = new Icon[2];
        this.blockIconLower = new Icon[2];
        this.blockIconUpper[0] = par1IconRegister.registerIcon(this.getTextureName() + "_upper");
        this.blockIconMiddle[0] = par1IconRegister.registerIcon(this.getTextureName() + "_middle");
        this.blockIconLower[0] = par1IconRegister.registerIcon(this.getTextureName() + "_lower");
        this.blockIconUpper[1] = new IconFlipped(this.blockIconUpper[0], true, false);
        this.blockIconMiddle[1] = new IconFlipped(this.blockIconMiddle[0], true, false);
        this.blockIconMiddle[1] = new IconFlipped(this.blockIconMiddle[0], true, false);
    }
    
    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return LargeDoorItemConfig._instance.ID;
    }
    
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int x, int y, int z)
    {
        return y >= 255 ? false : (par1World.doesBlockHaveSolidTopSurface(x, y - 1, z)
                && par1World.isAirBlock(x, y, z)
                && par1World.isAirBlock(x, y + 1, z)
                && par1World.isAirBlock(x, y + 2, z));
    }
    
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return LargeDoorItemConfig._instance.ID;
    }

    /**
     * Called when the block is attempted to be harvested
     */
    public void onBlockHarvested(World par1World, int x, int y, int z, int side, EntityPlayer par6EntityPlayer)
    {
        if (par6EntityPlayer.capabilities.isCreativeMode && (side & 8) != 0
                && par1World.getBlockId(x, y - 1, z) == this.blockID
                && par1World.getBlockId(x, y - 2, z) == this.blockID)
        {
            par1World.setBlockToAir(x, y - 1, z);
            par1World.setBlockToAir(x, y - 2, z);
        }
    }

}
