package evilcraft.entity.monster;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.render.entity.RenderPoisonousLibelle;
import evilcraft.client.render.model.ModelPoisonousLibelle;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Should the Poisonous Libelle be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;
    
    /**
     * Should the Poisonous Libelle do damage, next to poisoning?
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Should the Poisonous Libelle do damage, next to poisoning?", isCommandable = true)
    public static boolean hasAttackDamage = false;

    /**
     * The minimum Y-level this mob can spawn at.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The minimum Y-level this mob can spawn at.", isCommandable = true)
    public static int minY = 55;

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
        return new RenderPoisonousLibelle(this, new ModelPoisonousLibelle(), 0.5F);
    }
    
    @Override
    public void onRegistered() {
        EntityRegistry.addSpawn(PoisonousLibelle.class, 1, 1, 2, EnumCreatureType.monster, BiomeGenBase.river);
    }
    
}
