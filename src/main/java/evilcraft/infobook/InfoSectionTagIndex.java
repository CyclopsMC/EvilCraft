package evilcraft.infobook;

import com.google.common.collect.Maps;
import evilcraft.core.config.ConfigHandler;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.infobook.pageelement.SectionAppendix;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Map;

/**
 * Section that shows a sorted tag index with links.
 * @author rubensworks
 */
public class InfoSectionTagIndex extends InfoSection {

    public InfoSectionTagIndex(InfoSection parent) {
        super(parent, parent.getSubSections(), "infoBook.tagIndex", new ArrayList<String>(),
                new ArrayList<SectionAppendix>(), new ArrayList<String>());

        // treemap to ensure order by tag
        InfoBookParser.configLinks = Maps.newTreeMap();
        addSoftLinks(InfoBookParser.configLinks, getParent());
    }

    public void bakeSection(FontRenderer fontRenderer, int width, int maxLines, int lineHeight) {
        if(paragraphs.size() == 0) {
            addLinks(maxLines, lineHeight, InfoBookParser.configLinks);
        }
        super.bakeSection(fontRenderer, width, maxLines, lineHeight);
    }

    protected boolean shouldAddIndex() {
        return false;
    }

    protected void addSoftLinks(Map<String, InfoSection> softLinks, InfoSection section) {
        for(String tag : section.getTags()) {
            if(softLinks.containsKey(tag)) {
                // TODO: add support for multiple tag occurences?
                throw new IllegalArgumentException("The tag " + tag + " occurs multiple times.");
            }
            ExtendedConfig<?> config = ConfigHandler.getInstance().getDictionary().get(tag);
            if(config != null) {
                softLinks.put(config.getFullUnlocalizedName(), section);
            }
        }
        for(int i = 0; i < section.getSubSections(); i++) {
            addSoftLinks(softLinks, section.getSubSection(i));
        }
    }
}
