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
public class EntityBiomeExtractConfig extends EntityConfigCommon<IModBase, EntityBiomeExtract> {

    public EntityBiomeExtractConfig() {
        super(
                EvilCraft._instance,
            "biome_extract",
                eConfig -> EntityType.Builder.<EntityBiomeExtract>of(EntityBiomeExtract::new, MobCategory.MISC)
                        .sized(0.6F, 1.8F)
                        .setShouldReceiveVelocityUpdates(true)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityBiomeExtract> constructEntityClientConfig() {
        return new EntityBiomeExtractConfigClient(this);
    }
}
