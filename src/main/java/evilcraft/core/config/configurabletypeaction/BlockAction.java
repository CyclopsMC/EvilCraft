package evilcraft.core.config.configurabletypeaction;

import evilcraft.EvilCraftTab;
import evilcraft.client.gui.GuiHandler;
import evilcraft.client.gui.GuiHandler.GuiType;
import evilcraft.core.config.ConfigurableType;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGui;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.modcompat.fmp.ForgeMultipartHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * The action used for {@link BlockConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BlockAction extends ConfigurableTypeAction<BlockConfig> {

    @Override
    public void preRun(BlockConfig eConfig, Configuration config, boolean startup) {
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

        // Also register tile entity
        if(eConfig.getHolderType().equals(ConfigurableType.BLOCKCONTAINER)) {
            ConfigurableBlockContainer container = (ConfigurableBlockContainer) block;
            GameRegistry.registerTileEntity(container.getTileEntity(), eConfig.getSubUniqueName());
            
            // If the blockState has a GUI, go ahead and register that.
            if(container.hasGui()) {
                ConfigurableBlockContainerGui gui = (ConfigurableBlockContainerGui) container;
                GuiHandler.registerGUI(gui, GuiType.BLOCK);
            }
        }
        
        // Register optional ore dictionary ID
        if(eConfig.getOreDictionaryId() != null) {
            OreDictionary.registerOre(eConfig.getOreDictionaryId(), new ItemStack((Block)eConfig.getSubInstance()));
        }
        
        // Register third-party mod blockState parts.
        if(eConfig.isMultipartEnabled()) {
            ForgeMultipartHelper.registerMicroblock(eConfig);
        }
    }

}
