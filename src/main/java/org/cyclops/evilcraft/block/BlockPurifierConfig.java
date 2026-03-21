package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import java.util.List;

/**
 * Config for the {@link BlockPurifier}.
 * @author rubensworks
 *
 */
public class BlockPurifierConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", comment = "Item that can not be disenchanted. Regular expressions are allowed.", isCommandable = true)
    public static List<String> disenchantBlacklist = Lists.newArrayList(
            "tetra:.*"
    );
    @ConfigurableProperty(category = "machine", comment = "Enchantments to be disqualified from disenchantment, or curses disallowed from purification. Regular expressions are allowed.", isCommandable = true)
    public static List<String> enchantmentIdBlacklist = Lists.newArrayList();
    @ConfigurableProperty(category = "machine", comment = "The duration limit in ticks for which potion effect can be collected. Set to a negative value to allow any duration.", isCommandable = true)
    public static int maxPotionEffectDuration = MinecraftHelpers.SECOND_IN_TICKS * 60 * 5;

    /**
     * Check if the given enchantment is blacklisted.
     * @param enchantment The enchantment to check.
     * @return True if blacklisted.
     */
    public static boolean isEnchantmentBlacklisted(Holder<Enchantment> enchantment) {
        String enchantmentId = enchantment.getRegisteredName();
        for (String pattern : enchantmentIdBlacklist) {
            if (enchantmentId.matches(pattern)) {
                return true;
            }
        }
        return false;
    }

    public BlockPurifierConfig() {
        super(
                EvilCraft._instance,
            "purifier",
                eConfig -> new BlockPurifier(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2.5F)
                        .sound(SoundType.STONE)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        )
        );
    }

}
