package evilcraft.blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlockFluidClassic;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.fluids.Blood;

public class LiquidBlockBlood extends ConfigurableBlockFluidClassic {

    private static LiquidBlockBlood _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new LiquidBlockBlood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static LiquidBlockBlood getInstance() {
        return _instance;
    }

    private LiquidBlockBlood(ExtendedConfig eConfig) {
        super(eConfig, Blood.getInstance(), Material.water);
        this.setParticleColor(1.0F, 0.0F, 0.0F);
    }

}
