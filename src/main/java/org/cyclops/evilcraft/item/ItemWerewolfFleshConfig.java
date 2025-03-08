package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemWerewolfFlesh}
 * @author rubensworks
 *
 */
public class ItemWerewolfFleshConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "Humanoid flesh will drop in a 1/X chance.", isCommandable = true)
    public static int humanoidFleshDropChance = 5;

    public ItemWerewolfFleshConfig(boolean humanoid) {
        super(
                EvilCraft._instance,
                humanoid ? "flesh_humanoid" : "flesh_werewolf",
                (eConfig, properties) -> new ItemWerewolfFlesh(properties
                        .rarity(humanoid ? Rarity.RARE : Rarity.EPIC)
                        .stacksTo(16), humanoid)
        );
    }

}
