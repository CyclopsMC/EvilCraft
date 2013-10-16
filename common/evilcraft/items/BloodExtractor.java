package evilcraft.items;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableItemFluidContainer;
import evilcraft.api.config.ExtendedConfig;

public class BloodExtractor extends ConfigurableItemFluidContainer {
    
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
        super(eConfig);
        this.setMaxStackSize(1);
        //this.setMaxDamage(128);
        //this.setNoRepair();
        this.setCapacity(128);
        //this.fil
    }

}
