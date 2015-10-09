package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.render.block.RenderPurifier;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.item.BucketBloodConfig;
import evilcraft.tileentity.TilePurifier;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Block that can remove bad enchants from items.
 * @author rubensworks
 *
 */
public class Purifier extends ConfigurableBlockContainer {
    
    private static Purifier _instance = null;
    
    @SideOnly(Side.CLIENT)
    private IIcon innerIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon bottomIcon;
    
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
            TilePurifier tile = (TilePurifier) world.getTileEntity(x, y, z);
            if(tile != null) {
                if (itemStack == null && tile.getPurifyItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getPurifyItem());
                    tile.setPurifyItem(null);
                } else if (itemStack == null && tile.getAdditionalItem() != null) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, tile.getAdditionalItem());
                    tile.setAdditionalItem(null);
                } else if(itemStack != null && itemStack.getItem() instanceof ItemBucket) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                    if(fluidStack != null && tile.getTank().canTankAccept(fluidStack.getFluid())
                    		&& tile.getTank().canCompletelyFill(fluidStack)
                    		&& itemStack.getItem() != Items.bucket) {
                    	tile.getTank().fill(fluidStack, true);
                    	tile.sendUpdate();
                    	if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                        }
                        return true;
                    } else if(itemStack.getItem() == Items.bucket) {
                        int buckets = tile.getBucketsFloored();
                        if (buckets > 0) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(BucketBloodConfig._instance.getItemInstance()));
                            }
                            tile.setBuckets(buckets - 1, tile.getBucketsRest());
                        }
                        return true;
                    }
                }  else if(itemStack != null && tile.getActions().isItemValidForAdditionalSlot(itemStack) && tile.getAdditionalItem() == null) {
                    ItemStack copy = itemStack.copy();
                    copy.stackSize = 1;
                    tile.setAdditionalItem(copy);
                    itemStack.stackSize--;
                    return true;
                } else if(itemStack != null && tile.getActions().isItemValidForMainSlot(itemStack) && tile.getPurifyItem() == null) {
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
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? this.topIcon : (side == 0 ? this.bottomIcon : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
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
    public static IIcon getPurifierIcon(String icon) {
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
