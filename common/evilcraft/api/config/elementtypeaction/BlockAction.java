package evilcraft.api.config.elementtypeaction;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraftTab;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGui;
import evilcraft.gui.GuiHandler;

/**
 * The action used for {@link BlockConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class BlockAction extends IElementTypeAction<BlockConfig> {

    @Override
    public void preRun(BlockConfig eConfig, Configuration config) {
        // Get property in config file and set comment
        Property property = config.getBlock(eConfig.getHolderType().getCategory(), eConfig.NAMEDID, eConfig.ID);
        property.comment = eConfig.COMMENT;
        
        // Update the ID, it could've changed
        eConfig.ID = property.getInt();
    }

    @Override
    public void postRun(BlockConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();

        Block block = (Block) eConfig.getSubInstance();

        // Register
        GameRegistry.registerBlock(
                block,
                eConfig.getItemBlockClass(),
                eConfig.getSubUniqueName()
                );

        // Set creative tab
        block.setCreativeTab(EvilCraftTab.getInstance());

        // Add I18N
        LanguageRegistry.addName(eConfig.getSubInstance(), eConfig.NAME);

        // Also register tile entity
        if(eConfig.getHolderType().equals(ElementType.BLOCKCONTAINER)) {
            ConfigurableBlockContainer container = (ConfigurableBlockContainer) block;
            GameRegistry.registerTileEntity(container.getTileEntity(), eConfig.getSubUniqueName());
            
            // If the block has a GUI, go ahead and register that.
            if(container.hasGui()) {
                ConfigurableBlockContainerGui gui = (ConfigurableBlockContainerGui) container;
                
                if (Helpers.isClientSide())
                    GuiHandler.GUIS.put(gui.getGuiID(), gui.getGUI());
                GuiHandler.CONTAINERS.put(gui.getGuiID(), gui.getContainer());
            }
        }
        
        // Register optional ore dictionary ID
        if(eConfig.getOreDictionaryId() != null) {
            OreDictionary.registerOre(eConfig.getOreDictionaryId(), new ItemStack((Block)eConfig.getSubInstance()));
        }
        
        // Register buildcraft facade
        if(eConfig.isMultipartEnabled() && Loader.isModLoaded(Reference.MOD_BUILDCRAFT_TRANSPORT)) {
            FMLInterModComms.sendMessage(Reference.MOD_BUILDCRAFT_TRANSPORT, "add-facade", eConfig.ID+"@"+0);
        }
    }

}
