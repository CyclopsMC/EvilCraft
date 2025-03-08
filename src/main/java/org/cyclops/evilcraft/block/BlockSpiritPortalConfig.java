package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockSpiritPortal}.
 * @author rubensworks
 *
 */
public class BlockSpiritPortalConfig extends BlockConfigCommon<IModBase> {

    public BlockSpiritPortalConfig() {
        super(
                EvilCraft._instance,
                "spirit_portal",
                (eConfig, properties) -> new BlockSpiritPortal(properties
                        .strength(50.0F, 6000000.0F)
                        .sound(SoundType.WOOL)
                        .lightLevel((state) -> 8)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
