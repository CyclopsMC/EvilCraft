package evilcraft.core.config.configurabletypeaction;

import evilcraft.EvilCraftTab;
import evilcraft.Reference;
import evilcraft.client.gui.GuiHandler;
import evilcraft.client.gui.GuiHandler.GuiType;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.inventory.IGuiContainerProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * The action used for {@link ItemConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ItemAction extends ConfigurableTypeAction<ItemConfig>{

    @Override
    public void preRun(ItemConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(),
        		eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();
        
        if(startup) {
	        // Update the ID, it could've changed
	        eConfig.setEnabled(property.getBoolean(true));
        }
    }

    @Override
    public void postRun(ItemConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        Item item = (Item) eConfig.getSubInstance();
        
        // Register
        GameRegistry.registerItem(
                item,
                eConfig.getSubUniqueName()
        );
        
        // Set creative tab
        item.setCreativeTab(EvilCraftTab.getInstance());
        
        // Optionally register gui
        if(item instanceof IGuiContainerProvider) {
        	IGuiContainerProvider gui = (IGuiContainerProvider) item;
        	GuiHandler.registerGUI(gui, GuiType.ITEM);
        }
    }

    @Override
    public void polish(ItemConfig config) {
        for(int meta : config.getSubTypes()) {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(config.getItemInstance(), meta,
                    new ModelResourceLocation(Reference.MOD_ID + ":" + config.getUnlocalizedName(), "inventory"));
            // ModelBakery.addVariantName(yourItem, new String[]{"different", "variant", "namesOfModelFiles"});
        }
    }

}
