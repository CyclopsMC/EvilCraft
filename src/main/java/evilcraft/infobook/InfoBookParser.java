package evilcraft.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import evilcraft.Reference;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.core.helper.CraftingHelpers;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.infobook.pageelement.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML parser which will generate the infobook.
 * @author rubensworks
 */
public class InfoBookParser {

    private static final String BASE_PATH = "/assets/" + Reference.MOD_ID + "/info/";
    private static final String BOOK = "book.xml";

    private static final Map<String, IInfoSectionFactory> SECTION_FACTORIES = Maps.newHashMap();
    static {
        SECTION_FACTORIES.put("", new IInfoSectionFactory() {

            @Override
            public InfoSection create(InfoSection parent, int childIndex, String unlocalizedName,
                                      ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                      ArrayList<String> tagList) {
                return new InfoSection(parent, childIndex, unlocalizedName, paragraphs, appendixes, tagList);
            }

        });
    }

    private static final Map<String, IAppendixFactory> APPENDIX_FACTORIES = Maps.newHashMap();
    static {
        APPENDIX_FACTORIES.put("image", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) {
                return new ImageAppendix(new ResourceLocation(node.getTextContent()),
                        Integer.parseInt(node.getAttribute("width")), Integer.parseInt(node.getAttribute("height")));
            }

        });
        APPENDIX_FACTORIES.put("craftingRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) {
                return new CraftingRecipeAppendix(CraftingHelpers.findCraftingRecipe(createStack(node), getIndex(node)));
            }

        });
        APPENDIX_FACTORIES.put("bloodInfuserRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) {
                ItemStack itemStack = createStack(node);
                List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>>
                        recipes = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                int index = getIndex(node);
                if(index >= recipes.size()) throw new IllegalArgumentException("Could not find Blood Infuser recipe for " +
                        itemStack.getItem().getUnlocalizedName() + "with index " + index);
                return new BloodInfuserRecipeAppendix(recipes.get(index));
            }

        });
        APPENDIX_FACTORIES.put("furnaceRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) {
                return new FurnaceRecipeAppendix(CraftingHelpers.findFurnaceRecipe(createStack(node), getIndex(node)));
            }

        });
    }

    private static int getIndex(Element node) {
        int index = 0;
        if(node.getAttribute("index") != null) {
            index = Integer.parseInt(node.getAttribute("index"));
        }
        return index;
    }

    private static ItemStack createStack(Element node) {
        return new ItemStack(GameData.getItemRegistry().getObject(node.getTextContent()), 1, OreDictionary.WILDCARD_VALUE);
    }

    public static InfoSection initializeInfoBook() {
        try {
            InputStream is = InfoBookParser.class.getResourceAsStream(BASE_PATH + BOOK);
            StreamSource stream = new StreamSource(is);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream.getInputStream());
            InfoSection root = buildSection(null, 0, doc.getDocumentElement());
            root.registerSection(new InfoSectionTagIndex(root));
            return root;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new InfoBookException("Info Book XML is invalid.");
    }

    protected static InfoSection buildSection(InfoSection parent, int childIndex, Element sectionElement) {
        NodeList sections = sectionElement.getElementsByTagName("section");
        NodeList tags = sectionElement.getElementsByTagName("tag");
        NodeList paragraphs = sectionElement.getElementsByTagName("paragraph");
        NodeList appendixes = sectionElement.getElementsByTagName("appendix");
        ArrayList<String> paragraphList = Lists.newArrayListWithCapacity(paragraphs.getLength());
        ArrayList<SectionAppendix> appendixList = Lists.newArrayListWithCapacity(appendixes.getLength());
        ArrayList<String> tagList = Lists.newArrayListWithCapacity(tags.getLength());
        InfoSection section = createSection(parent, childIndex, sectionElement.getAttribute("type"),
                sectionElement.getAttribute("name"), paragraphList, appendixList, tagList);

        if(sections.getLength() > 0) {
            for (int i = 0; i < sections.getLength(); i++) {
                Element subsection = (Element) sections.item(i);
                section.registerSection(buildSection(section, i, subsection));
            }
        } else {
            for (int j = 0; j < tags.getLength(); j++) {
                Element tag = (Element) tags.item(j);
                tagList.add(tag.getTextContent());
            }
            for (int j = 0; j < paragraphs.getLength(); j++) {
                Element paragraph = (Element) paragraphs.item(j);
                paragraphList.add(paragraph.getTextContent());
            }
            for (int j = 0; j < appendixes.getLength(); j++) {
                Element appendix = (Element) appendixes.item(j);
                appendixList.add(createAppendix(appendix.getAttribute("type"), appendix));
            }
        }

        return section;
    }

    protected static InfoSection createSection(InfoSection parent, int childIndex, String type, String unlocalizedName,
                                               ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                               ArrayList<String> tagList) {
        if(type == null) type = "";
        IInfoSectionFactory factory = SECTION_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No section of type '" + type + "' was found.");
        }
        return factory.create(parent, childIndex, unlocalizedName, paragraphs, appendixes, tagList);
    }

    protected static SectionAppendix createAppendix(String type, Element node) {
        if(type == null) type = "";
        IAppendixFactory factory = APPENDIX_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No appendix of type '" + type + "' was found.");
        }
        return factory.create(node);
    }

    protected static interface IInfoSectionFactory {

        public InfoSection create(InfoSection parent, int childIndex, String unlocalizedName,
                                  ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                  ArrayList<String> tagList);

    }

    protected static interface IAppendixFactory {

        public SectionAppendix create(Element node);

    }

    public static class InfoBookException extends RuntimeException {

        public InfoBookException(String message) {
            super(message);
        }

    }

}
