package evilcraft;

import net.minecraft.util.DamageSource;

/**
 * An extension of the Minecraft {@code DamageSource}.
 * @author rubensworks
 *
 */
public class ExtendedDamageSource extends DamageSource{
    
    /**
     * DamageSource for when entities die without any apparent reason.
     */
    public static ExtendedDamageSource dieWithoutAnyReason = (ExtendedDamageSource)((new ExtendedDamageSource("dieWithoutAnyReason")).setDamageBypassesArmor());
    /**
     * DamageSource for when entities die from distortion not caused by another player.
     */
    public static ExtendedDamageSource distorted = (ExtendedDamageSource)((new ExtendedDamageSource("distored")));
    /**
     * DamageSource for when entities die from a spiked plate.
     */
    public static ExtendedDamageSource spiked = (ExtendedDamageSource)((new ExtendedDamageSource("spiked")));

    protected ExtendedDamageSource(String par1Str) {
        super(par1Str);
    }
    
    /**
     * Get the string identifier of this damage source.
     * @return The unique ID.
     */
    public String getID() {
        return "death.attack." + this.damageType;
    }

}
