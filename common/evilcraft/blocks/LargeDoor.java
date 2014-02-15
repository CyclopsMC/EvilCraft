package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockDoor;
import evilcraft.items.LargeDoorItem;

/**
 * A door that is three blocks high.
 * @author rubensworks
 *
 */
public class LargeDoor extends ConfigurableBlockDoor {
    
    private static LargeDoor _instance = null;
    
    private IIcon[] blockIconUpper;
    private IIcon[] blockIconMiddle;
    private IIcon[] blockIconLower;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new LargeDoor(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static LargeDoor getInstance() {
        return _instance;
    }

    private LargeDoor(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.wood);
        this.setBlockBounds(0, 0.0F, 0, 1, 2.0F, 1); // Height +2
        this.setHardness(3.0F);
        this.setStepSound(soundTypeWood);
        this.disableStats();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.blockIconLower[0];
    }
    
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (side != 1 && side != 0) {
            int meta = world.getBlockMetadata(x, y, z);
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
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float coordX, float coordY, float coordZ) {
        int meta = world.getBlockMetadata(x, y, z);
        int isOpen = (meta & 7) ^ 4; // To open door (metadata)

        // Update the lowest part of the door
        
        if ((meta & 8) == 0) // Lower
        {
            EvilCraft.log("lower");
            world.setBlockMetadataWithNotify(x, y, z, isOpen, 2);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }
        else if ((meta & 8) == 1) // Middle
        {
            EvilCraft.log("middle");
            world.setBlockMetadataWithNotify(x, y - 1, z, isOpen, 2);
            world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
        }
        else if ((meta & 16) == 1) // Upper
        {
            EvilCraft.log("upper");
            world.setBlockMetadataWithNotify(x, y - 2, z, isOpen, 2);
            world.markBlockRangeForRenderUpdate(x, y - 2, z, x, y, z);
        }

        world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIconUpper = new IIcon[2];
        this.blockIconMiddle = new IIcon[2];
        this.blockIconLower = new IIcon[2];
        this.blockIconUpper[0] = iconRegister.registerIcon(this.getTextureName() + "_upper");
        this.blockIconMiddle[0] = iconRegister.registerIcon(this.getTextureName() + "_middle");
        this.blockIconLower[0] = iconRegister.registerIcon(this.getTextureName() + "_lower");
        this.blockIconUpper[1] = new IconFlipped(this.blockIconUpper[0], true, false);
        this.blockIconMiddle[1] = new IconFlipped(this.blockIconMiddle[0], true, false);
        this.blockIconMiddle[1] = new IconFlipped(this.blockIconMiddle[0], true, false);
    }
    
    @Override
    public Item getItemDropped(int meta, Random random, int zero) {
        return LargeDoorItem.getInstance();
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return y >= 255 ? false : (World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)
                && world.isAirBlock(x, y, z)
                && world.isAirBlock(x, y + 1, z)
                && world.isAirBlock(x, y + 2, z));
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
        if (player.capabilities.isCreativeMode && (side & 8) != 0
                && world.getBlock(x, y - 1, z) == this
                && world.getBlock(x, y - 2, z) == this)
        {
            world.setBlockToAir(x, y - 1, z);
            world.setBlockToAir(x, y - 2, z);
        }
    }

}
