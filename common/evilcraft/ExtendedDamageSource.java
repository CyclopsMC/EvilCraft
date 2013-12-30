package evilcraft;

import net.minecraft.util.DamageSource;

public class ExtendedDamageSource extends DamageSource{
    
    public static ExtendedDamageSource dieWithoutAnyReason = (ExtendedDamageSource)((new ExtendedDamageSource("dieWithoutAnyReason")).setDamageBypassesArmor());

    protected ExtendedDamageSource(String par1Str) {
        super(par1Str);
    }
    
    public String getID() {
        return "death.attack." + this.damageType;
    }

}
