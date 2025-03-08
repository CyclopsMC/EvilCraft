package org.cyclops.evilcraft.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemDarkStick}.
 * @author rubensworks
 *
 */
public class EntityItemDarkStickConfig extends EntityConfigCommon<IModBase, EntityItemDarkStick> {

    public EntityItemDarkStickConfig() {
        super(
                EvilCraft._instance,
                "item_dark_stick",
                eConfig -> EntityType.Builder.<EntityItemDarkStick>of(EntityItemDarkStick::new, MobCategory.MISC)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityItemDarkStick> constructEntityClientConfig() {
        return new EntityItemDarkStickConfigClient(this);
    }
}
