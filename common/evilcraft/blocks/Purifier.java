package evilcraft.blocks;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.entities.tileentities.TilePurifier;
import evilcraft.items.BucketBlood;
import evilcraft.render.block.RenderPurifier;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class Purifier extends ConfigurableBlockContainer {
    
    private static Purifier _instance = null;
    
    @SideOnly(Side.CLIENT)
    private Icon innerIcon;
    @SideOnly(Side.CLIENT)
    private Icon topIcon;
    @SideOnly(Side.CLIENT)
    private Icon bottomIcon;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new Purifier(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Purifier getInstance() {
        return _instance;
    }

    private Purifier(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TilePurifier.class);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float motionX, float motionY, float motionZ) {
        if(world.isRemote) {
            return true;
        } else {
            ItemStack itemStack = player.inventory.getCurrentItem();
            TilePurifier tile = (TilePurifier) world.getBlockTileEntity(x, y, z);
            if(tile != null) {
                if (itemStack == null && tile.getPurifyItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getPurifyItem());
                    tile.setPurifyItem(null);
                } else if (itemStack == null && tile.getBookItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getBookItem());
                    tile.setBookItem(null);
                } else if(itemStack != null && itemStack.getItem() instanceof ItemBucket) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                    if(fluidStack != null && fluidStack.getFluid() == TilePurifier.FLUID) {
                        int buckets = tile.getBucketsFloored();
                        if (buckets < tile.getMaxBuckets()) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Item.bucketEmpty));
                            }
                            tile.setBuckets(buckets + 1, tile.getBucketsRest());
                        }
                        return true;
                    } else if(itemStack.itemID == Item.bucketEmpty.itemID) {
                        int buckets = tile.getBucketsFloored();
                        if (buckets > 0) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(BucketBlood.getInstance()));
                            }
                            tile.setBuckets(buckets - 1, tile.getBucketsRest());
                        }
                        return true;
                    }
                }  else if(itemStack != null && itemStack.getItem().getClass() == TilePurifier.ALLOWED_BOOK && tile.getBookItem() == null) {
                    ItemStack copy = itemStack.copy();
                    copy.stackSize = 1;
                    tile.setBookItem(copy);
                    itemStack.stackSize--;
                    return true;
                } else if(itemStack != null && tile.getPurifyItem() == null) {
                    ItemStack copy = itemStack.copy();
                    copy.stackSize = 1;
                    tile.setPurifyItem(copy);
                    player.inventory.getCurrentItem().stackSize--;
                    return true;
                }
            }
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta) {
        return side == 1 ? this.topIcon : (side == 0 ? this.bottomIcon : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {
        this.innerIcon = iconRegister.registerIcon(this.getTextureName() + "_" + "inner");
        this.topIcon = iconRegister.registerIcon(this.getTextureName() + "_top");
        this.bottomIcon = iconRegister.registerIcon(this.getTextureName() + "_" + "bottom");
        this.blockIcon = iconRegister.registerIcon(this.getTextureName() + "_side");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB area, List collisionBoxes, Entity entity) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        float f = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, area, collisionBoxes, entity);
        this.setBlockBoundsForItemRender();
    }

    /**
     * Get the inner or bottom icon of the purifier, used for rendering.
     * @param icon The "inner" or "bottom" icon.
     * @return An icon.
     */
    @SideOnly(Side.CLIENT)
    public static Icon getPurifierIcon(String icon) {
        return icon.equals("inner") ? Purifier._instance.innerIcon : (icon.equals("bottom") ? Purifier._instance.bottomIcon : null);
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderPurifier.ID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        return world.getBlockMetadata(x, y, z);
    }

}
