package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderPoisonousLibelle;
import org.cyclops.evilcraft.client.render.model.ModelPoisonousLibelle;

/**
 * Config for the {@link PoisonousLibelle}.
 * @author rubensworks
 *
 */
public class PoisonousLibelleConfig extends MobConfig<PoisonousLibelle> {
    
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
     * 1/X chance on getting poisoned when hit.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "1/X chance on getting poisoned when hit.", isCommandable = true)
    public static int poisonChance = 20;

    /**
     * Make a new instance.
     */
    public PoisonousLibelleConfig() {
        super(
                EvilCraft._instance,
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
        return Helpers.RGBToInt(57, 125, 27);
    }

    @Override
    public int getForegroundEggColor() {
        return Helpers.RGBToInt(196, 213, 57);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<PoisonousLibelle> getRender(RenderManager renderManager) {
        return new RenderPoisonousLibelle(renderManager, this, new ModelPoisonousLibelle(), 0.5F);
    }
    
    @Override
    public void onRegistered() {
        super.onRegistered();
        EntityRegistry.addSpawn(PoisonousLibelle.class, 1, 1, 2, EnumCreatureType.MONSTER, Biomes.river);
    }
    
}
