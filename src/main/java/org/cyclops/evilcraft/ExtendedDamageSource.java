package org.cyclops.evilcraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;

/**
 * An extension of the Minecraft {@code DamageSource}.
 * @author rubensworks
 *
 */
public class ExtendedDamageSource extends DamageSource{
    
    /**
     * DamageSource for when entities die without any apparent reason.
     */
    public static ExtendedDamageSource dieWithoutAnyReason = (ExtendedDamageSource)((new ExtendedDamageSource("die_without_any_reason")).setDamageBypassesArmor());
    /**
     * DamageSource for when entities die from distortion not caused by another player.
     */
    public static ExtendedDamageSource distorted = (ExtendedDamageSource)((new ExtendedDamageSource("distorted")));
    /**
     * DamageSource for when entities die from a spiked plate.
     * @param world The world.
     * @return A new damage source instance.
     */
    public static ExtendedDamageSource spikedDamage(ServerWorld world) {
        return new ExtendedDamageSource("spiked", FakePlayerFactory.getMinecraft(world));
    }
    /**
     * DamageSource for when necromancer's entities that are automatically killed.
     */
    public static ExtendedDamageSource necromancerRecall = (ExtendedDamageSource)((new ExtendedDamageSource("necromancer_recall")));
    /**
     * DamageSource for paling entities.
     */
    public static ExtendedDamageSource paling = (ExtendedDamageSource)((new ExtendedDamageSource("paling")));

    public static ExtendedDamageSource broomDamage(final LivingEntity attacker) {
        return new ExtendedDamageSource("broom", attacker) {
            @Override
            public ITextComponent getDeathMessage(LivingEntity defender) {
                String s = "death.attack." + this.damageType;
                String s1 = s + ".player";
                return new TranslationTextComponent(s1, new Object[] {defender.getDisplayName(), attacker.getDisplayName()});
            }
        };
    }

    public static ExtendedDamageSource vengeanceBeam(final LivingEntity attacker) {
        return new VengeanceBeamDamageSource("vengeance_beam", attacker);
    }

    private final Entity entity;

    protected ExtendedDamageSource(String unlocalizedName, Entity entity) {
        super(Reference.MOD_ID + "." + unlocalizedName);
        this.entity = entity;
    }

    protected ExtendedDamageSource(String unlocalizedName) {
        this(unlocalizedName, null);
    }
    
    /**
     * Get the string identifier of this damage source.
     * @return The unique ID.
     */
    public String getID() {
        return "death.attack." + this.damageType;
    }

    @Override
    public Entity getTrueSource() {
        return entity;
    }

    public static class VengeanceBeamDamageSource extends ExtendedDamageSource {
        protected VengeanceBeamDamageSource(String unlocalizedName, Entity entity) {
            super(unlocalizedName, entity);
        }
    }
}
