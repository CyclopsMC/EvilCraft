package org.cyclops.evilcraft.core.config.extendedconfig;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Config for upgradable blocks with tile entities.
 * @author rubensworks
 */
public class UpgradableBlockContainerConfig extends BlockConfig {

    public static final Map<Block, Set<Upgrades.Upgrade>> BLOCK_UPGRADES = Maps.newIdentityHashMap();

    public UpgradableBlockContainerConfig(ModBase mod, String namedId, Function<BlockConfig, ? extends Block> blockConstructor, @Nullable BiFunction<BlockConfig, Block, ? extends Item> itemConstructor) {
        super(mod, namedId, blockConstructor, itemConstructor);
    }

    public static Set<Upgrades.Upgrade> getBlockUpgrades(Block block) {
        return BLOCK_UPGRADES.getOrDefault(block, Collections.emptySet());
    }

    /**
     * @return The set of upgrades that can be applied to this machine.
     */
    public Set<Upgrades.Upgrade> getUpgrades() {
        return Sets.newHashSet();
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        for (Upgrades.Upgrade upgrade : getUpgrades()) {
            upgrade.addUpgradableInfo(this);
        }
        BLOCK_UPGRADES.put(getInstance(), getUpgrades());
    }
}
