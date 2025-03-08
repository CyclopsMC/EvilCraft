package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityWeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityWeatherContainerConfig extends EntityConfigCommon<IModBase, EntityWeatherContainer> {

    public EntityWeatherContainerConfig() {
        super(
                EvilCraft._instance,
            "weather_container",
                eConfig -> EntityType.Builder.<EntityWeatherContainer>of(EntityWeatherContainer::new, MobCategory.MISC)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityWeatherContainer> constructEntityClientConfig() {
        return new EntityWeatherContainerConfigClient(this);
    }
}
