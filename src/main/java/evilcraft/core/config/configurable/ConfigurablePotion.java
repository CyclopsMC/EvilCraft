package evilcraft.core.config.configurable;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.PotionConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A configurable potion effect.
 * @author rubensworks
 */
public abstract class ConfigurablePotion extends Potion implements IConfigurable {

    private static final ResourceLocation resource = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "potions.png");

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * Make a new Enchantment instance
     * @param eConfig Config for this enchantment.
     * @param badEffect If the potion effect is bad.
     * @param color The color of the potion.
     * @param iconIndex The sprite index of the icon.
     */
    protected ConfigurablePotion(ExtendedConfig<PotionConfig> eConfig, boolean badEffect, int color, int iconIndex) {
        super(eConfig.downCast().ID, new ResourceLocation(eConfig.getNamedId()), badEffect, color);
        this.setConfig(eConfig);
        this.setPotionName(eConfig.getUnlocalizedName());
        this.setIconIndex(iconIndex % 8, iconIndex / 8);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
        return super.getStatusIconIndex();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

    public boolean isActiveOn(EntityLivingBase entity) {
        return isActiveOn(entity, this);
    }

    public boolean isActiveOn(EntityLivingBase entity, Potion potion) {
        return entity.getActivePotionEffect(potion) != null;
    }

    public int getAmplifier(EntityLivingBase entity, Potion potion) {
        return entity.getActivePotionEffect(potion).getAmplifier();
    }

    public int getAmplifier(EntityLivingBase entity) {
        return getAmplifier(entity, this);
    }

    protected abstract void onUpdate(EntityLivingBase entity);

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if(isActiveOn(entity)) {
            onUpdate(entity);
        }
    }

}
