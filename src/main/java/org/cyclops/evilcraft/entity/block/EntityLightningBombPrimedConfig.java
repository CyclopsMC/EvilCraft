package org.cyclops.evilcraft.entity.block;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EntityLightningBombPrimed}.
 * @author rubensworks
 *
 */
public class EntityLightningBombPrimedConfig extends EntityConfigCommon<IModBase, EntityLightningBombPrimed> {

    @ConfigurablePropertyCommon(category = "entity", comment = "The amount of ticks (on average), this bomb should tick before explosion.")
    public static int fuse = 100;

    public EntityLightningBombPrimedConfig() {
        super(
                EvilCraft._instance,
                "lightning_bomb_primed",
                eConfig -> EntityType.Builder.<EntityLightningBombPrimed>of(EntityLightningBombPrimed::new, MobCategory.MISC)
        );
    }

    @Override
    public EntityClientConfig<IModBase, EntityLightningBombPrimed> constructEntityClientConfig() {
        return new EntityLightningBombPrimedConfigClient(this);
    }
}
