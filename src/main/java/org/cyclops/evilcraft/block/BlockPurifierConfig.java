package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.List;

/**
 * Config for the {@link BlockPurifier}.
 * @author rubensworks
 *
 */
public class BlockPurifierConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "machine", comment = "Item that can not be disenchanted. Regular expressions are allowed.", isCommandable = true)
    public static List<String> disenchantBlacklist = Lists.newArrayList(
            "tetra:.*"
    );
    @ConfigurablePropertyCommon(category = "machine", comment = "The duration limit in ticks for which potion effect can be collected. Set to a negative value to allow any duration.", isCommandable = true)
    public static int maxPotionEffectDuration = EvilCraft._instance.getModHelpers().getMinecraftHelpers().getSecondInTicks() * 60 * 5;

    public BlockPurifierConfig() {
        super(
                EvilCraft._instance,
            "purifier",
                (eConfig, properties) -> new BlockPurifier(properties
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, eConfig.createDefaultItemProperties())
        );
    }

}
