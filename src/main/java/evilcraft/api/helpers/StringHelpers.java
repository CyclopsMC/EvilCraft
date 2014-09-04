package evilcraft.api.helpers;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * A collection of String helper methods.
 * @author rubensworks
 *
 */
public final class StringHelpers {
	
	private static final String SPACE = " ";
	private static final String NEWLINE_PATTERN = "\\\\n";

	/**
	 * Split the input string into lines while preserving the full words.
	 * This will also forcefully add newlines when '\n' is found in the input string.
	 * @param input The input sentence.
	 * @param maxLength The maximum length of a line.
	 * @param prefix A prefix to add to each produced line. This will not increase the character
	 * count per line.
	 * @return The sentence split into lines.
	 */
	public static List<String> splitLines(String input, int maxLength, String prefix) {
		List<String> list = Lists.newLinkedList();
		
		StringBuilder buffer = null;
		for(String partialInput : input.split(NEWLINE_PATTERN)) {
			buffer = new StringBuilder();
			for(String word : partialInput.split(SPACE)) {
				if(buffer.length() > 0) {
					buffer.append(SPACE);
				}
				buffer.append(word);
				if(buffer.length() >= maxLength) {
					list.add(prefix + buffer.toString());
					buffer = new StringBuilder();
				}
			}
			if(buffer.length() > 0) {
				list.add(prefix + buffer.toString());
			}
		}
		
		return list;
	}
	
}
