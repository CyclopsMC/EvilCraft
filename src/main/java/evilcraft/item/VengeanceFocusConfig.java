package evilcraft.item;

import evilcraft.EvilCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.init.IInitListener;

/**
 * Config for the {@link VengeanceFocus}.
 * @author rubensworks
 *
 */
public class VengeanceFocusConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static VengeanceFocusConfig _instance;

    /**
     * Make a new instance.
     */
    public VengeanceFocusConfig() {
        super(
                EvilCraft._instance,
        	true,
            "vengeanceFocus",
            null,
            VengeanceFocus.class
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onInit(IInitListener.Step step) {
        super.onInit(step);
        if(step == Step.INIT) {
            // Some fake meta hack to make sure our models are loaded, this could be improved with a custom loader.
            ModelResourceLocation[] modelArray = VengeanceFocus.getInstance().modelArray;
            for(int i = 0; i < modelArray.length; i++) {
                String identifier = getMod().getModId() + ":" + getNamedId() + "_" + i;
                modelArray[i] = new ModelResourceLocation(identifier, "inventory");
                ModelBakery.addVariantName(getItemInstance(), identifier);
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
                        getItemInstance(), i + 1, modelArray[i]);
            }
        }
    }
    
}
