package evilcraft.items;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.liquids.Blood;

public class BloodExtractor extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodExtractor _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodExtractor(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static BloodExtractor getInstance() {
        return _instance;
    }

    private BloodExtractor(ExtendedConfig eConfig) {
        super(eConfig, 5000, Blood.getInstance());
    }

}
