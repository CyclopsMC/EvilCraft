package evilcraft.infobook;

import com.google.common.collect.Maps;
import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.infobook.pageelement.SectionAppendix;
import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Section that shows a sorted tag index with links.
 * @author rubensworks
 */
public class InfoSectionTagIndex extends InfoSection {

    public InfoSectionTagIndex(InfoSection parent) {
        super(parent, parent.getSubSections(), "infoBook.tagIndex", new ArrayList<String>(),
                new ArrayList<SectionAppendix>(), new ArrayList<String>());

        // treemap to ensure order by localized tag
        InfoBookParser.configLinks = Maps.newTreeMap(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return L10NHelpers.localize(o1).compareTo(L10NHelpers.localize(o2));
            }
        });
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

    protected void addSoftLinks(Map<String, Pair<InfoSection, Integer>> softLinks, InfoSection section) {
        for(String tag : section.getTags()) {
            if(softLinks.containsKey(tag)) {
                // TODO: add support for multiple tag occurences?
                throw new IllegalArgumentException("The tag " + tag + " occurs multiple times.");
            }
            ExtendedConfig<?> config = EvilCraft._instance.getConfigHandler().getDictionary().get(tag);
            if(config != null) {
                softLinks.put(config.getFullUnlocalizedName(), Pair.of(section, 0));
            }
        }
        for(int i = 0; i < section.getSubSections(); i++) {
            addSoftLinks(softLinks, section.getSubSection(i));
        }
    }
}
