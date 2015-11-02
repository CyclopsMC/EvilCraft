package org.cyclops.evilcraft.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the {@link DarkStick}.
 * @author rubensworks
 *
 */
public class DarkStickConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkStickConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkStickConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkStick",
            null,
            DarkStick.class
        );
    }

    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODSTICK;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onRegistered() {
        super.onRegistered();
    }

}
