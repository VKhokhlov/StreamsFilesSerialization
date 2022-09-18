import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Config {
    public Item load;
    public Item save;
    public Item log;

    public static class Item {
        private boolean enabled;
        private String fileName;
        private String format;

        public Item(boolean enabled, String fileName, String format) {
            this.enabled = enabled;
            this.fileName = fileName;
            this.format = format;
        }

        public Item() {
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFormat() {
            return format;
        }
    }

    public Config(String configFile, Item defaultLoad, Item defaultSave, Item defaultLog) {
        if (!LoadConfig(configFile)) {
            load = defaultLoad;
            save = defaultSave;
            log = defaultLog;
        }
    }

    public boolean LoadConfig(String configFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(configFile));

            load = loadItem(doc, "load");
            save = loadItem(doc, "save");
            log = loadItem(doc, "log");

            return true;
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            return false;
        }
    }

    private Item loadItem(Document doc, String tagName) {
        NodeList root = doc.getElementsByTagName(tagName);
        NodeList childNodes = root.item(0).getChildNodes();
        Item item = new Item();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);

            if (Node.ELEMENT_NODE != node.getNodeType()) {
                continue;
            }

            String name = node.getNodeName();
            String text = node.getTextContent();

            switch (name) {
                case "enabled": {
                    item.setEnabled(Boolean.valueOf(text));
                    break;
                }
                case "fileName": {
                    item.setFileName(text);
                    break;
                }
                case "format": {
                    item.setFormat(text);
                    break;
                }
            }
        }

        return item;
    }
}
