package evilcraft.core.client.gui.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import evilcraft.Reference;
import evilcraft.core.config.ConfigHandler;
import evilcraft.core.config.ElementTypeCategory;

/**
 * Overview config screen.
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
			GuiConfig.getAbridgedConfigPath(ConfigHandler.getInstance().getConfig().toString()));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		for(ElementTypeCategory category : ElementTypeCategory.CATEGORIES) {
			list.add(new DummyCategoryElement(category.toString(),
					category.toString(), ExtendedCategoryEntry.class));
		}
		return list;
	}
	
	/**
	 * A category entry for {@link ElementTypeCategory}.
	 * @author rubensworks
	 *
	 */
	public static class ExtendedCategoryEntry extends CategoryEntry {

		private ElementTypeCategory category;
		
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
			// Cheaty way of getting the current ElementTypeCategory.
			this.category = ElementTypeCategory.valueOf(configElement
					.getName().replaceAll(" ", "").toUpperCase(Locale.ENGLISH));
			
			// Get all the elements inside this category
            List<IConfigElement> elements = (new ConfigElement(ConfigHandler.getInstance().getConfig()
            		.getCategory(category.toString()))).getChildElements();
			return new GuiConfig(this.owningScreen, elements, 
                    this.owningScreen.modID, category.toString(),
                    this.configElement.requiresWorldRestart()
                    	|| this.owningScreen.allRequireWorldRestart, 
                    this.configElement.requiresMcRestart()
                    	|| this.owningScreen.allRequireMcRestart,
                    GuiConfig.getAbridgedConfigPath(ConfigHandler.getInstance().getConfig().toString()));
        }
		
	}
	
}
