package evilcraft.items;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Recipes;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.FluidBlockBlood;
import evilcraft.blocks.FluidBlockBloodConfig;

public class PoisonSac extends ConfigurableItem {
    
    private static PoisonSac _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new PoisonSac(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static PoisonSac getInstance() {
        return _instance;
    }

    private PoisonSac(ExtendedConfig eConfig) {
        super(eConfig);
    }

}
