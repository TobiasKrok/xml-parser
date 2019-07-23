package com.tobias;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XmlParser {

    private Document xmlDocument;
    private File resultFile;
    private File errorFile;
    private int count = 0;
    private int errorCount = 0;


    public XmlParser(String filePath, String rootOutputFolder) throws ParserConfigurationException, SAXException, IOException {
        this.resultFile = new File(rootOutputFolder + "ci_web.properties");
        this.errorFile = new File(rootOutputFolder + "ci_web_errorlog.txt");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        this.xmlDocument = builder.parse(filePath);
        this.xmlDocument.getDocumentElement().normalize();
    }

    public void parse() throws IOException {
        String[] nodes = {
                "textInput",
                "textInputService",
                "values",
                "domainObjectList",
                "selectService",
                "type"
        };
        StringBuilder result = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < nodes.length; i++) {
            NodeList list = xmlDocument.getElementsByTagName(nodes[i]);
            getKeyFromNodes(list, result, errors);

        }
        FileUtils.touch(resultFile);
        writeToFile(resultFile, result.toString());
        System.out.println("Created ci_web.properties at " + resultFile.getAbsolutePath());
        if (errorCount > 0) {
            FileUtils.touch(errorFile);
            writeToFile(errorFile, errors.toString());
            System.out.println("Created ci_web_errorlog.txt at " + errorFile.getAbsolutePath());
        }
        System.out.println("Operation complete with " + count +" keys found and " + errorCount + " errors.");

    }

    private void getKeyFromNodes(NodeList list, StringBuilder result, StringBuilder errors) {
        String[] atrs = {
                "label",
                "groupId",
                "hintText",
                "prompt",
                "help",
                "description"
        };
        for (int i = 0; i < list.getLength(); i++) {
            for (int k = 0; k < atrs.length; k++) {
                Element element = (Element) list.item(i);
                if (element.hasAttribute(atrs[k])) {
                    if (!element.getAttribute(atrs[k] + "Key").isEmpty()) {
                        result.append(element.getAttribute(atrs[k] + "Key") + "=" + element.getAttribute(atrs[k]) + "\n");
                        count++;
                    } else {
                        errors.append("A key is missing!" + nodeToString(element));
                        errorCount++;
                    }
                }
            }
        }
    }

    private void writeToFile(File file, String str) {
        try {
            FileUtils.writeStringToFile(file, str, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error writing to " + file.getName() + ": " + e.getMessage());
        }
    }

    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (TransformerException e) {
            System.out.println(e.getMessage());
        }
        return sw.toString();
    }
}
