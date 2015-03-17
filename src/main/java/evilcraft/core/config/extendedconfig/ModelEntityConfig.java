package evilcraft.core.config.extendedconfig;

import evilcraft.core.client.render.model.RenderModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Config for Entities with a custom Model.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ModelEntityConfig extends EntityConfig{

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public ModelEntityConfig(boolean enabled, String namedId, String comment, Class<? extends Entity> element) {
        super(enabled, namedId, comment, element);
    }
    
    @Override
    protected Render getRender(RenderManager renderManager, RenderItem renderItem) {
        Constructor<? extends Render> constructor;
        Render render = null;
        try {
            constructor = getRenderClass().getConstructor(ExtendedConfig.class);
            render = constructor.newInstance(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return render;
    }
    
    /**
     * Get the {@link RenderModel} class for the configurable.
     * @return The class for the model of the configurable.
     */
    public abstract Class<? extends RenderModel<?>> getRenderClass();
}
