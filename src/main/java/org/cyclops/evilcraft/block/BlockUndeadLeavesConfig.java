package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockUndeadLeaves}.
 * @author rubensworks
 *
 */
public class BlockUndeadLeavesConfig extends BlockConfigCommon<EvilCraft> {

    @ConfigurablePropertyCommon(category = "block", comment = "How much Blood (mB) can be produced at most as a Blood Stain on each random tick.")
    public static int maxBloodStainAmount = 25;

    public BlockUndeadLeavesConfig() {
        super(
                EvilCraft._instance,
            "undead_leaves",
                (eConfig, properties) -> new BlockUndeadLeaves(properties
                        .replaceable()
                        .strength(0.5F)
                        .sound(SoundType.GRAVEL)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance, properties -> properties
                        .compostable(ContextIntProviders.COMPOSTABLE_LOW))
        );
    }


    @Override
    public BlockClientConfig<EvilCraft> constructBlockClientConfig() {
        return new BlockUndeadLeavesConfigClient(this);
    }

}
