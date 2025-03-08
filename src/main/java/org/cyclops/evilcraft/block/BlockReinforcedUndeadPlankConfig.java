package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockReinforcedUndeadPlank}.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlankConfig extends BlockConfigCommon<IModBase> {

    public BlockReinforcedUndeadPlankConfig() {
        super(
                EvilCraft._instance,
            "reinforced_undead_planks",
                (eConfig, properties) -> new BlockReinforcedUndeadPlank(properties
                        .strength(5.0F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
