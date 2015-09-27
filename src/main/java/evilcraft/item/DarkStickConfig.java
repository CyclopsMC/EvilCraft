package evilcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.client.render.entity.RenderEntityItemDarkStick;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.proxy.ClientProxy;

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
        ClientProxy.ITEM_RENDERERS.put(getItemInstance(), new RenderEntityItemDarkStick());
    }

}
