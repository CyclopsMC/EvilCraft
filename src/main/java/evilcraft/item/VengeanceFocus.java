package evilcraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import evilcraft.entity.effect.EntityAntiVengeanceBeam;

/**
 * Focus that is able to direct rays of the sun to entangle vengeance spirits.
 * @author rubensworks
 *
 */
public class VengeanceFocus extends ConfigurableItem {
	
	private static final int TICK_MODULUS = 3;
    
    private static VengeanceFocus _instance = null;
    
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new VengeanceFocus(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeanceFocus getInstance() {
        return _instance;
    }

    private VengeanceFocus(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(this.getIconString());
        this.iconArray = new IIcon[4];

        for(int i = 0; i < this.iconArray.length; ++i) {
            this.iconArray[i] = par1IconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
    	if(getItemInUseDuration(player) == getMaxItemUseDuration(usingItem)) {
        	return getIcon(stack, renderPass);
        }
    	return iconArray[Math.min(this.iconArray.length - 1,
    			(player.getItemInUseDuration() / 3))];
    }
    
    private int getItemInUseDuration(EntityPlayer player) {
    	return player.isUsingItem() ? ObfuscationHelpers.getItemInUse(player).getMaxItemUseDuration()
    			- ObfuscationHelpers.getItemInUseCount(player) : 0;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if(getItemInUseDuration(player) > 0) {
			player.clearItemInUse();
		} else {
			player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		}
        return itemStack;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.bow;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }
    
    @Override
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int duration) {
    	if(player.worldObj.isRemote && getItemInUseDuration(player) > 6) {
	    	// Play stop sound
	    	EvilCraft.proxy.playSound(player.posX, player.posY, player.posZ,
	    			"vengeanceBeamStop", 0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    	}
    }
    
    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int duration) {
    	if(getItemInUseDuration(player) > 6) {
    		if(WorldHelpers.efficientTick(player.worldObj, TICK_MODULUS, player.getEntityId())) {
		    	EntityAntiVengeanceBeam beam = new EntityAntiVengeanceBeam(player.worldObj, player);
		    	if(!player.worldObj.isRemote) {
		    		player.worldObj.spawnEntityInWorld(beam);
		        }
    		}
    	} else {
    		if(getItemInUseDuration(player) == 3 && player.worldObj.isRemote) {
			// Play start sound
    		EvilCraft.proxy.playSound(player.posX, player.posY, player.posZ,
        			"vengeanceBeamStart", 0.6F + player.worldObj.rand.nextFloat() * 0.2F, 1.0F);
    		}
    	}
    }

}
