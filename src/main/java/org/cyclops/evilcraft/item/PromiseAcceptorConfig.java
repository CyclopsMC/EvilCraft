package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link PromiseAcceptor}.
 * @author rubensworks
 *
 */
public class PromiseAcceptorConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static PromiseAcceptorConfig _instance;

    /**
     * Make a new instance.
     */
    public PromiseAcceptorConfig() {
        super(
                EvilCraft._instance,
        	true,
            "promise_acceptor",
            null,
            PromiseAcceptor.class
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for(int tier = 0; tier < PromiseAcceptor.COLORS.size(); tier++) {
            OreDictionary.registerOre("materialPromiseAcceptor", new ItemStack(PromiseAcceptor.getInstance(), 1, tier));
        }
    }
}
