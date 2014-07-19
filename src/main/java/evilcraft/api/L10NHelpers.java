package evilcraft.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * A set of localization helpers.
 * @author rubensworks
 *
 */
public final class L10NHelpers {
	
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
	 * Get localized info for the given block for display in tooltips.
	 * @param block The block.
	 * @return Localized info.
	 */
	public static String getLocalizedInfo(Block block) {
		return IInformationProvider.INFO_PREFIX + localize(block.getUnlocalizedName() + ".info");
	}

	/**
	 * Get localized info for the given item for display in tooltips.
	 * @param item The item.
	 * @return Localized info.
	 */
	public static String getLocalizedInfo(Item item) {
		return getLocalizedInfo(item, "");
	}

	/**
	 * Get localized info for the given item for display in tooltips.
	 * @param item The item.
	 * @param suffix The suffix to add to the unlocalized name.
	 * @return Localized info.
	 */
	public static String getLocalizedInfo(Item item, String suffix) {
		return getLocalizedInfo(item.getUnlocalizedName(), suffix);
	}
	
	/**
	 * Get localized info for the given block for display in tooltips.
	 * @param block The block.
	 * @param suffix The suffix to add to the unlocalized name.
	 * @return Localized info.
	 */
	public static String getLocalizedInfo(Block block, String suffix) {
		return getLocalizedInfo(block.getUnlocalizedName(), suffix);
	}
	
	/**
	 * Get localized info for the given unlocalized name for display in tooltips.
	 * @param unlocalizedName The unlocalized name.
	 * @param suffix The suffix to add to the unlocalized name.
	 * @return Localized info.
	 */
	public static String getLocalizedInfo(String unlocalizedName, String suffix) {
		return IInformationProvider.INFO_PREFIX + localize(unlocalizedName + ".info" + suffix);
	}
	
	/**
	 * Localize a given entity id.
	 * @param entityId The unique entity name id.
	 * @return The localized name.
	 */
	public static String getLocalizedEntityName(String entityId) {
		return L10NHelpers.localize("entity." + entityId + ".name");
	}
	
}
