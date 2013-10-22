package evilcraft.api.config;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.blocks.LiquidBlockBlood;
import evilcraft.blocks.LiquidBlockBloodConfig;

/**
 * Item food that can hold ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public abstract class ConfigurableItemBucket extends ItemBucket implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    protected boolean canPickUp = true;
    
    public static ElementType TYPE = ElementType.ITEM;
    
    protected ConfigurableItemBucket(ExtendedConfig eConfig, int blockID) {
        super(eConfig.ID, blockID);
        eConfig.ID = this.itemID; // This could've changed.
        this.setConfig(eConfig);
        this.setUnlocalizedName(this.getUniqueName());
        setContainerItem(Item.bucketEmpty);
    }

    // Set a configuration for this item
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    public String getUniqueName() {
        return "items."+eConfig.NAMEDID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.MOD_ID+":"+eConfig.NAMEDID);
    }
    
    public boolean isEntity() {
        return false;
    }
    
    /*@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(canPickUp) {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
            
            if (movingobjectposition == null) {
                return itemStack;
            } else {
                FillBucketEvent event = new FillBucketEvent(player, itemStack, world, movingobjectposition);
                if (MinecraftForge.EVENT_BUS.post(event))  return itemStack;
                
                if (event.getResult() == Event.Result.ALLOW) {
                    if (player.capabilities.isCreativeMode)  return itemStack;
                    if (--itemStack.stackSize <= 0) return event.result;
                    if (!player.inventory.addItemStackToInventory(event.result)) player.dropPlayerItem(event.result);
        
                    return itemStack;
                }
                
                if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
                    int x = movingobjectposition.blockX;
                    int y = movingobjectposition.blockY;
                    int z = movingobjectposition.blockZ;
                    
                    if (!world.canMineBlock(player, x, y, z)) return itemStack;
        
                    if (movingobjectposition.sideHit == 0) --y;
                    if (movingobjectposition.sideHit == 1) ++y;
                    if (movingobjectposition.sideHit == 2) --z;
                    if (movingobjectposition.sideHit == 3) ++z;
                    if (movingobjectposition.sideHit == 4) --x;
                    if (movingobjectposition.sideHit == 5) ++x;
                    
                    if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, itemStack)) return itemStack;
                    if (this.tryPlaceContainedLiquid(world, x, y, z) && !player.capabilities.isCreativeMode)
                        return new ItemStack(Item.bucketEmpty);

                }
            }

            return itemStack;
        }
        return super.onItemRightClick(itemStack, world, player);
    }
    
    public boolean tryPlaceContainedLiquid(World par1World, int par2, int par3, int par4)
    {
        Material material = par1World.getBlockMaterial(par2, par3, par4);
        boolean flag = !material.isSolid();
        if (!par1World.isAirBlock(par2, par3, par4) && !flag)
        {
            return true;
        }
        else
        {
            if (par1World.provider.isHellWorld)
            {
                par1World.playSoundEffect((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);
                for (int l = 0; l < 8; ++l)
                {
                    par1World.spawnParticle("largesmoke", (double)par2 + Math.random(), (double)par3 + Math.random(), (double)par4 + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            }
            else
            {
                if (!par1World.isRemote && flag && !material.isLiquid())
                {
                    par1World.destroyBlock(par2, par3, par4, true);
                }
                par1World.setBlock(par2, par3, par4, LiquidBlockBloodConfig._instance.ID, 0, 3);
            }
            return true;
        }
    }*/
    
}
