package evilcraft.items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ConfigurableItem;
import evilcraft.api.config.ExtendedConfig;

public class ContainedFlux extends ConfigurableItem {
    
    private static ContainedFlux _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new ContainedFlux(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static ContainedFlux getInstance() {
        return _instance;
    }

    private ContainedFlux(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    /*public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        for(WeatherEffect weather : world.weatherEffects) {
            
        }
        Helpers.isDay(world);

        return itemStack;
    }*/

}
