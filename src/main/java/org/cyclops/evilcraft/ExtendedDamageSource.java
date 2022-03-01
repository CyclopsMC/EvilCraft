package org.cyclops.evilcraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
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
    public static ExtendedDamageSource dieWithoutAnyReason = (ExtendedDamageSource)((new ExtendedDamageSource("die_without_any_reason")).bypassArmor());
    /**
     * DamageSource for when entities die from distortion not caused by another player.
     */
    public static ExtendedDamageSource distorted = (ExtendedDamageSource)((new ExtendedDamageSource("distorted")));
    /**
     * DamageSource for when entities die from a spiked plate.
     * @param world The world.
     * @return A new damage source instance.
     */
    public static ExtendedDamageSource spikedDamage(ServerLevel world) {
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
            public Component getLocalizedDeathMessage(LivingEntity defender) {
                String s = "death.attack." + this.msgId;
                String s1 = s + ".player";
                return new TranslatableComponent(s1, new Object[] {defender.getDisplayName(), attacker.getDisplayName()});
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
        return "death.attack." + this.msgId;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    public static class VengeanceBeamDamageSource extends ExtendedDamageSource {
        protected VengeanceBeamDamageSource(String unlocalizedName, Entity entity) {
            super(unlocalizedName, entity);
        }
    }
}
