package evilcraft.api.config.elementtypeaction;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraftTab;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;

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
        if(eConfig.hasSubTypes()) {
            GameRegistry.registerBlock(
                    block,
                    eConfig.getItemBlockClass(),
                    eConfig.getSubUniqueName()
                    );
        } else {
            GameRegistry.registerBlock(
                    block,
                    eConfig.getSubUniqueName()
                    );
        }

        // Set creative tab
        block.setCreativeTab(EvilCraftTab.getInstance());

        // Add I18N
        LanguageRegistry.addName(eConfig.getSubInstance(), eConfig.NAME);

        // Also register tile entity
        if(eConfig.getHolderType().equals(ElementType.BLOCKCONTAINER)) {
            ConfigurableBlockContainer container = (ConfigurableBlockContainer) block;
            GameRegistry.registerTileEntity(container.getTileEntity(), eConfig.getSubUniqueName());
        }
    }

}
