package org.cyclops.evilcraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

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
    public static ExtendedDamageSource distorted = (ExtendedDamageSource)((new ExtendedDamageSource("distorted")));
    /**
     * DamageSource for when entities die from a spiked plate.
     */
    public static ExtendedDamageSource spiked = (ExtendedDamageSource)((new ExtendedDamageSource("spiked")));
    /**
     * DamageSource for when necromancer's entities that are automatically killed.
     */
    public static ExtendedDamageSource necromancerRecall = (ExtendedDamageSource)((new ExtendedDamageSource("necromancerRecall")));
    /**
     * DamageSource for paling entities.
     */
    public static ExtendedDamageSource paling = (ExtendedDamageSource)((new ExtendedDamageSource("paling")));

    public static ExtendedDamageSource broomDamage(final EntityLivingBase attacker) {
        return new ExtendedDamageSource("broom") {
            @Override
            public IChatComponent getDeathMessage(EntityLivingBase defender) {
                String s = "death.attack." + this.damageType;
                String s1 = s + ".player";
                return attacker != null && StatCollector.canTranslate(s1)
                        ? new ChatComponentTranslation(s1, new Object[] {defender.getDisplayName(), attacker.getDisplayName()})
                        : new ChatComponentTranslation(s, new Object[] {defender.getDisplayName()});
            }
        };
    }

    protected ExtendedDamageSource(String unlocalizedName) {
        super(Reference.MOD_ID + "." + unlocalizedName);
    }
    
    /**
     * Get the string identifier of this damage source.
     * @return The unique ID.
     */
    public String getID() {
        return "death.attack." + this.damageType;
    }

}
