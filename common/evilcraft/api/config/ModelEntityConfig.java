package evilcraft.api.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import evilcraft.api.render.ModelRender;

public abstract class ModelEntityConfig extends EntityConfig{

    public ModelEntityConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Entity> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    @Override
    protected Render getRender() {
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
    
    public abstract Class<? extends ModelRender> getRenderClass();
}
