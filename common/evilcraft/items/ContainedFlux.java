package evilcraft.items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class ContainedFlux extends ConfigurableItem {
    
    private static ContainedFlux _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new ContainedFlux(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static ContainedFlux getInstance() {
        return _instance;
    }

    private ContainedFlux(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }

}
