package evilcraft.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.entities.effect.EntityNeutronBeam;

/**
 * Focus that is able to direct rays of the sun to entangle vengeance spirits.
 * @author rubensworks
 *
 */
public class VengeanceFocus extends ConfigurableItem {
    
    private static VengeanceFocus _instance = null;
    
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
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	if (player.capabilities.isCreativeMode || true) {// TODO: if has power
    		// TODO: nice start animation & sound
    		if (!world.isRemote)
    			if(player.getItemInUseDuration() > 0)
    				player.clearItemInUse();
    			else
    				player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }
        return itemStack;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE; // TODO: base on remaining charge? (run on lightning containers?)
    }
    
    @Override
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int duration) {
    	// TODO: nice stop sound & animation
    }
    
    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer player, int duration) {
    	EntityNeutronBeam beam = new EntityNeutronBeam(player.worldObj, player);
    	if (!player.worldObj.isRemote) {
    		player.worldObj.spawnEntityInWorld(beam);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        list.add(L10NHelpers.getLocalizedInfo(this, ".main"));
    }

}
