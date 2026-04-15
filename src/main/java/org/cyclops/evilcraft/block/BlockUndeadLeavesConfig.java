package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockUndeadLeaves}.
 * @author rubensworks
 *
 */
public class BlockUndeadLeavesConfig extends BlockConfigCommon<IModBase> {

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
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    @Override
    public void onRegistryRegistered() {
        super.onRegistryRegistered();
        ComposterBlock.COMPOSTABLES.put(getItemInstance(), 0.3F);
    }

    @Override
    public BlockClientConfig<IModBase> constructBlockClientConfig() {
        return new BlockUndeadLeavesConfigClient(this);
    }

}
