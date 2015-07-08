package evilcraft.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.block.BloodInfuser;
import evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import evilcraft.core.recipe.custom.*;
import evilcraft.core.weather.WeatherType;
import evilcraft.infobook.pageelement.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.xml.ConfigRecipeConditionHandler;
import org.cyclops.cyclopscore.recipe.xml.IRecipeConditionHandler;
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
import java.util.Collection;
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
            public SectionAppendix create(Element node) throws InvalidAppendixException {
                return new ImageAppendix(new ResourceLocation(node.getTextContent()),
                        Integer.parseInt(node.getAttribute("width")), Integer.parseInt(node.getAttribute("height")));
            }

        });
        APPENDIX_FACTORIES.put("craftingRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) throws InvalidAppendixException {
                return new CraftingRecipeAppendix(CraftingHelpers.findCraftingRecipe(createStack(node), getIndex(node)));
            }

        });
        APPENDIX_FACTORIES.put("bloodInfuserRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) throws InvalidAppendixException {
                ItemStack itemStack = createStack(node);
                List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>>
                        recipes = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                int index = getIndex(node);
                if(index >= recipes.size()) throw new InvalidAppendixException("Could not find Blood Infuser recipe for " +
                        itemStack.getItem().getUnlocalizedName() + "with index " + index);
                return new BloodInfuserRecipeAppendix(recipes.get(index));
            }

        });
        APPENDIX_FACTORIES.put("furnaceRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) throws InvalidAppendixException {
                return new FurnaceRecipeAppendix(CraftingHelpers.findFurnaceRecipe(createStack(node), getIndex(node)));
            }

        });
        APPENDIX_FACTORIES.put("envirAccRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(Element node) throws InvalidAppendixException {
                ItemStack itemStack = createStack(node);
                List<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>
                        recipes = EnvironmentalAccumulator.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new EnvironmentalAccumulatorRecipeComponent(itemStack, WeatherType.ANY));
                int index = getIndex(node);
                if(index >= recipes.size()) throw new InvalidAppendixException("Could not find Environmental Accumulator recipe for " +
                        itemStack.getItem().getUnlocalizedName() + "with index " + index);
                return new EnvironmentalAccumulatorRecipeAppendix(recipes.get(index));
            }

        });
    }

    private static final Map<String, IAppendixItemFactory> APPENDIX_LIST_FACTORIES = Maps.newHashMap();
    static {
        APPENDIX_LIST_FACTORIES.put("craftingRecipe", new IAppendixItemFactory() {

            @Override
            public SectionAppendix create(ItemStack itemStack) throws InvalidAppendixException {
                return new CraftingRecipeAppendix(CraftingHelpers.findCraftingRecipe(itemStack, 0));
            }

        });
        APPENDIX_LIST_FACTORIES.put("bloodInfuserRecipe", new IAppendixItemFactory() {

            @Override
            public SectionAppendix create(ItemStack itemStack) throws InvalidAppendixException {
                List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>>
                        recipes = BloodInfuser.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new ItemStackRecipeComponent(itemStack));
                return new BloodInfuserRecipeAppendix(recipes.get(0));
            }

        });
        APPENDIX_LIST_FACTORIES.put("furnaceRecipe", new IAppendixItemFactory() {

            @Override
            public SectionAppendix create(ItemStack itemStack) throws InvalidAppendixException {
                return new FurnaceRecipeAppendix(CraftingHelpers.findFurnaceRecipe(itemStack, 0));
            }

        });
        APPENDIX_LIST_FACTORIES.put("envirAccRecipe", new IAppendixItemFactory() {

            @Override
            public SectionAppendix create(ItemStack itemStack) throws InvalidAppendixException {
                List<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>>
                        recipes = EnvironmentalAccumulator.getInstance().getRecipeRegistry().
                        findRecipesByOutput(new EnvironmentalAccumulatorRecipeComponent(itemStack, WeatherType.ANY));
                return new EnvironmentalAccumulatorRecipeAppendix(recipes.get(0));
            }

        });
    }

    public static Map<String, Pair<InfoSection, Integer>> configLinks;

    private static int getIndex(Element node) {
        int index = 0;
        if(!node.getAttribute("index").isEmpty()) {
            index = Integer.parseInt(node.getAttribute("index"));
        }
        return index;
    }

    private static ItemStack createStack(Element node) throws InvalidAppendixException {
        int meta = OreDictionary.WILDCARD_VALUE;
        if(!node.getAttribute("meta").isEmpty()) {
            meta = Integer.parseInt(node.getAttribute("meta"));
        }
        Item item = GameData.getItemRegistry().getObject(node.getTextContent());
        if(item == null) {
            throw new InvalidAppendixException("Invalid item " + node.getTextContent());
        }
        return new ItemStack(item, 1, meta);
    }

    /**
     * Initialize the infobook from the xml file.
     * @return The root of the infobook.
     */
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
        NodeList appendixLists = sectionElement.getElementsByTagName("appendixList");
        ArrayList<String> paragraphList = Lists.newArrayListWithCapacity(paragraphs.getLength());
        ArrayList<SectionAppendix> appendixList = Lists.newArrayListWithCapacity(appendixes.getLength());
        ArrayList<String> tagList = Lists.newArrayListWithCapacity(tags.getLength());
        InfoSection section = createSection(parent, childIndex, sectionElement.getAttribute("type"),
                sectionElement.getAttribute("name"), paragraphList, appendixList, tagList);

        if(sections.getLength() > 0) {
            int subChildIndex = 0;
            for (int i = 0; i < sections.getLength(); i++) {
                Element subsection = (Element) sections.item(i);
                if(subsection.getParentNode() == sectionElement) {
                    InfoSection subsubsection = buildSection(section, subChildIndex, subsection);
                    if(subsubsection != null) {
                        section.registerSection(subsubsection);
                        subChildIndex++;
                    }
                }
            }
        } else {
            for (int j = 0; j < tags.getLength(); j++) {
                Element tag = (Element) tags.item(j);
                String tagString = tag.getTextContent();
                String type = "config";
                if(tag.hasAttribute("type")) {
                    type = tag.getAttribute("type");
                }
                IRecipeConditionHandler conditionHandler = EvilCraft._instance.getRecipeHandler().getRecipeConditionHandlers().get(type);
                if(!conditionHandler.isSatisfied(EvilCraft._instance.getRecipeHandler(), tag.getTextContent())) {
                    return null;
                }
                // Yes, I know this isn't very clean, I am currently more interested in eating grapes than abstracting
                // this whole conditional system.
                if(conditionHandler instanceof ConfigRecipeConditionHandler) {
                    tagList.add(tagString);
                }
            }
            for (int j = 0; j < paragraphs.getLength(); j++) {
                Element paragraph = (Element) paragraphs.item(j);
                paragraphList.add(paragraph.getTextContent());
            }
            for (int j = 0; j < appendixes.getLength(); j++) {
                try {
                    Element appendix = (Element) appendixes.item(j);
                    appendixList.add(createAppendix(appendix.getAttribute("type"), appendix));
                } catch (InvalidAppendixException e) {
                    // Skip this appendix.
                }
            }
            for (int j = 0; j < appendixLists.getLength(); j++) {
                Element appendixListNode = (Element) appendixLists.item(j);
                String type = appendixListNode.getAttribute("type");
                Collection<ItemStack> itemStacks = EvilCraft._instance.getRecipeHandler().getTaggedOutput().get(appendixListNode.getTextContent());
                for(ItemStack itemStack : itemStacks) {
                    try {
                        appendixList.add(createAppendix(appendixListNode.getAttribute("type"), itemStack));
                    } catch (InvalidAppendixException e) {
                        // Skip this appendix.
                    }
                }
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

    protected static SectionAppendix createAppendix(String type, Element node) throws InvalidAppendixException {
        if(type == null) type = "";
        IAppendixFactory factory = APPENDIX_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No appendix of type '" + type + "' was found.");
        }
        return factory.create(node);
    }

    protected static SectionAppendix createAppendix(String type, ItemStack itemStack) throws InvalidAppendixException {
        if(type == null) type = "";
        IAppendixItemFactory factory = APPENDIX_LIST_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No appendix list of type '" + type + "' was found.");
        }
        return factory.create(itemStack);
    }

    protected static interface IInfoSectionFactory {

        public InfoSection create(InfoSection parent, int childIndex, String unlocalizedName,
                                  ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                  ArrayList<String> tagList);

    }

    protected static interface IAppendixFactory {

        public SectionAppendix create(Element node) throws InvalidAppendixException;

    }

    protected static interface IAppendixItemFactory {

        public SectionAppendix create(ItemStack itemStack) throws InvalidAppendixException;

    }

    public static class InfoBookException extends RuntimeException {

        public InfoBookException(String message) {
            super(message);
        }

    }

    public static class InvalidAppendixException extends Exception {

        public InvalidAppendixException(String message) {
            super(message);
        }

    }

}
