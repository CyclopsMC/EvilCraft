package evilcraft.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evilcraft.Reference;
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
            public InfoSection create(String unlocalizedName, ArrayList<ArrayList<String>> pages) {
                return new InfoSection(unlocalizedName, pages);
            }

        });
    }

    public static InfoSection initializeInfoBook() {
        try {
            InputStream is = InfoBookParser.class.getResourceAsStream(BASE_PATH + BOOK);
            StreamSource stream = new StreamSource(is);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream.getInputStream());
            return buildSection(doc.getDocumentElement());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new InfoBookException("Info Book XML is invalid.");
    }

    protected static InfoSection buildSection(Element sectionElement) {
        NodeList sections = sectionElement.getElementsByTagName("section");
        NodeList pages = sectionElement.getElementsByTagName("page");
        ArrayList<ArrayList<String>> pageList = Lists.newArrayListWithCapacity(pages.getLength());
        InfoSection section = createSection(sectionElement.getAttribute("type"), sectionElement.getAttribute("name"), pageList);

        if(sections.getLength() > 0) {
            for (int i = 0; i < sections.getLength(); i++) {
                Element subsection = (Element) sections.item(i);
                section.registerSection(buildSection(subsection));
            }
        } else {
            for (int i = 0; i < pages.getLength(); i++) {
                Element page = (Element) pages.item(i);
                NodeList paragraphs = page.getElementsByTagName("paragraph");
                ArrayList<String> paragraphList = Lists.newArrayListWithCapacity(paragraphs.getLength());
                for (int j = 0; j < paragraphs.getLength(); j++) {
                    Element paragraph = (Element) paragraphs.item(j);
                    paragraphList.add(paragraph.getTextContent());
                }
                pageList.add(paragraphList);
            }
        }
        section.bakeSection();

        return section;
    }

    protected static InfoSection createSection(String type, String unlocalizedName, ArrayList<ArrayList<String>> pages) {
        if(type == null) type = "";
        IInfoSectionFactory factory = SECTION_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No section of type '" + type + "' was found.");
        }
        return factory.create(unlocalizedName, pages);
    }

    protected static interface IInfoSectionFactory {

        public InfoSection create(String unlocalizedName, ArrayList<ArrayList<String>> pages);

    }

    public static class InfoBookException extends RuntimeException {

        public InfoBookException(String message) {
            super(message);
        }

    }

}
