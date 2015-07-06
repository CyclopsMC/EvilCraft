package evilcraft.core.client.gui.config;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Overview config screen.
 * TODO: abstract to cyclops
 * @author rubensworks
 *
 */
public class GuiConfigOverview extends GuiConfig {

	/**
	 * Make a new instance.
	 * @param parentScreen the parent GuiScreen object
	 */
	public GuiConfigOverview(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), Reference.MOD_ID, false, false,
			GuiConfig.getAbridgedConfigPath(getConfigHandler().getConfig().toString()));
	}

	private static final ConfigHandler getConfigHandler() {
		return EvilCraft._instance.getConfigHandler();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		/*for(ConfigurableTypeCategory category : ConfigurableTypeCategory.CATEGORIES) {
			list.add(new DummyCategoryElement(category.toString(),
					category.toString(), ExtendedCategoryEntry.class));
		}*/
		return list;
	}
	
	/**
	 * A category entry for {@link ConfigurableTypeCategory}.
	 * @author rubensworks
	 *
	 */
	public static class ExtendedCategoryEntry extends CategoryEntry {

		private ConfigurableTypeCategory category;
		
		/**
		 * Make a new instance.
		 * @param config The config gui.
		 * @param entries The gui entries.
		 * @param element The config element for this category.
		 */
		@SuppressWarnings("rawtypes")
		public ExtendedCategoryEntry(GuiConfig config, GuiConfigEntries entries,
				IConfigElement element) {
			super(config, entries, element);			
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
        protected GuiScreen buildChildScreen() {
			// Cheaty way of getting the current ConfigurableTypeCategory.
			this.category = ConfigurableTypeCategory.valueOf(configElement
					.getName().replaceAll(" ", "").toUpperCase(Locale.ENGLISH));
			
			// Get all the elements inside this category
            List<IConfigElement> elements = (new ConfigElement(getConfigHandler().getConfig()
            		.getCategory(category.toString()))).getChildElements();
			return new GuiConfig(this.owningScreen, elements, 
                    this.owningScreen.modID, category.toString(),
                    this.configElement.requiresWorldRestart()
                    	|| this.owningScreen.allRequireWorldRestart, 
                    this.configElement.requiresMcRestart()
                    	|| this.owningScreen.allRequireMcRestart,
                    GuiConfig.getAbridgedConfigPath(getConfigHandler().getConfig().toString()));
        }
		
	}
	
}
