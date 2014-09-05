package evilcraft.core.helper;

import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import evilcraft.core.IInformationProvider;

/**
 * A set of localization helpers.
 * @author rubensworks
 *
 */
public final class L10NHelpers {
	
	private static final int MAX_TOOLTIP_LINE_LENGTH = 25;
	private static final String KEY_ENABLED = "general.info.enabled";
	private static final String KEY_DISABLED = "general.info.disabled";
	
	/**
	 * Localize a key that has values in language files.
	 * @param key The key of the language file entry.
	 * @return The localized string.
	 */
	public static String localize(String key) {
		return StatCollector.translateToLocal(key);
	}
	
	/**
	 * Localize a key that has values in language files.
	 * @param key The key of the language file entry.
	 * @param params The parameters of the formatting
	 * @return The localized string.
	 */
	public static String localize(String key, Object... params) {
		return StatCollector.translateToLocalFormatted(key, params);
	}

	/**
	 * Show status info about the activation about an item to the info box.
	 * @param infoLines The list to add info to.
	 * @param isEnabled If the item is enabled.
	 * @param statusPrefixKey The prefix for the l10n key that will show if it is enabled,
	 * this should be a formatted string with one parameter.
	 */
	public static void addStatusInfo(List<String> infoLines, boolean isEnabled, String statusPrefixKey) {
		String autoSupply = EnumChatFormatting.RESET + localize(KEY_DISABLED);
        if(isEnabled) {
            autoSupply = EnumChatFormatting.GREEN + localize(KEY_ENABLED);
        }
        infoLines.add(EnumChatFormatting.BOLD
        		+ localize(statusPrefixKey, new Object[]{autoSupply}));
	}
	
	/**
	 * Localize a given entity id.
	 * @param entityId The unique entity name id.
	 * @return The localized name.
	 */
	public static String getLocalizedEntityName(String entityId) {
		return L10NHelpers.localize("entity." + entityId + ".name");
	}
	
	/**
	 * Add the optional info lines to the item tooltip.
	 * @param list The list to add the lines to.
	 * @param prefix The I18N key prefix, being the unlocalized name of blocks or items.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addOptionalInfo(List list, String prefix) {
		String key = prefix + ".info";
		if(StatCollector.canTranslate(key)) {
			if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				String localized = localize(key);
				list.addAll(StringHelpers.splitLines(localized, MAX_TOOLTIP_LINE_LENGTH,
						IInformationProvider.INFO_PREFIX));
			} else {
				list.add(localize(EnumChatFormatting.GRAY.toString()
						+ EnumChatFormatting.ITALIC.toString()
						+ localize("general.tooltip.info")));
			}
		}
	}
	
}
