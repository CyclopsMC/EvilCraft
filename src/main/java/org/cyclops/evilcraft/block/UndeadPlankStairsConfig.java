package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockStairs;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class UndeadPlankStairsConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static UndeadPlankStairsConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                true,
                "undeadPlankStairs",
                null,
                null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableBlockStairs block = (ConfigurableBlockStairs) new ConfigurableBlockStairs(this, UndeadPlankConfig._instance.getBlockInstance().getDefaultState()) {
            @SuppressWarnings("deprecation")
            @Override
            public SoundType getSoundType() {
                return SoundType.WOOD;
            }
        }.setHardness(2.0F);
        return block;
    }

    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_STAIRWOOD;
    }

    @Override
    public void onRegistered() {
        Blocks.FIRE.setFireInfo(getBlockInstance(), 5, 20);
    }

}
