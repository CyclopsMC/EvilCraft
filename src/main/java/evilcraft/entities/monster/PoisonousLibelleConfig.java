package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.MobConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.helpers.RenderHelpers;
import evilcraft.render.entity.RenderPoisonousLibelle;
import evilcraft.render.models.PoisonousLibelleModel;

/**
 * Config for the {@link PoisonousLibelle}.
 * @author rubensworks
 *
 */
public class PoisonousLibelleConfig extends MobConfig {
    
    /**
     * The unique instance.
     */
    public static PoisonousLibelleConfig _instance;
    
    /**
     * Should the PoisonousLibelle be enabled?
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Poisonous Libelle be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;
    
    /**
     * Should the Poisonous Libelle do damage, next to poisoning?
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Poisonous Libelle do damage, next to poisoning?", isCommandable = true)
    public static boolean hasAttackDamage = false;

    /**
     * Make a new instance.
     */
    public PoisonousLibelleConfig() {
        super(
        	true,
            "poisonousLibelle",
            null,
            PoisonousLibelle.class
        );
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
    @Override
    public int getBackgroundEggColor() {
        return RenderHelpers.RGBToInt(57, 125, 27);
    }

    @Override
    public int getForegroundEggColor() {
        return RenderHelpers.RGBToInt(196, 213, 57);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderPoisonousLibelle(this, new PoisonousLibelleModel(), 0.5F);
    }
    
    @Override
    public void onRegistered() {
        EntityRegistry.addSpawn(PoisonousLibelle.class, 1, 1, 2, EnumCreatureType.monster, BiomeGenBase.river);
    }
    
}
