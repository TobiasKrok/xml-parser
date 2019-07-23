package com.tobias;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {


public static void main(String[] args) {
        if (args.length > 0) {
            if(FilenameUtils.getExtension(args[0]).equals("xml")) {
                // If no root output directory is set, we default it to the current working directory.
                String rootOutputDirectory = ".\\";
                if (args.length == 2) {
                    rootOutputDirectory = (args[1].endsWith("\\") ? args[1] : args[1] + "\\");
                }
                try {
                    XmlParser parser = new XmlParser(args[0], rootOutputDirectory);
                    parser.parse();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Input file is not XML.");
            }
        } else {
            System.out.println("No input file was given.");
        }
    }
}

