package com.my;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wlb on 2015/12/8.
 */
public class XmlUtil {
    private static Map<String, List<String>> result = null;
    private static String path = "";
    static{
        URL url = null;
        url = XmlUtil.class.getResource("XmlUtil.class");
        String strURL = url.toString();
//      获取tomcat webapps路径
        String os = System.getProperty("os.name");
        if (os.startsWith("Window")) {
            strURL = strURL.substring(strURL.indexOf("/") + 1,
                    strURL.indexOf("webapps"));
        } else {
            strURL = strURL.substring(strURL.indexOf(":") + 1, strURL.indexOf("webapps"));
        }
        path = strURL + "conf/";
    }

    static {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder = null;
        result = new HashMap<String, List<String>>();
        try {
            builder = builderFactory.newDocumentBuilder();
//			Document document = builder.parse(HelpSceneUtil.class
//					.getClassLoader().getResourceAsStream("helpEntry.xml"));

            File f = new File(path+"helpEntry.xml");
            InputStream in = new FileInputStream(f);
            Document document = builder.parse(in);

            Element rootElement = document.getDocumentElement();
            NodeList nodes = rootElement.getElementsByTagName("entry");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);// entry
                if (node instanceof Element) {
                    // a child element to process
                    Element elem = (Element) node; // entry
                    NodeList itemNodes = elem.getElementsByTagName("item");// item
                    NodeList titleNodes = elem.getElementsByTagName("title");// title
                    Element titleElem = (Element) titleNodes.item(0);
                    String title = titleElem.getTextContent();
                    List<String> itemList = new ArrayList<String>();
                    for (int j = 0; j < itemNodes.getLength(); j++) {
                        Element itemElem = (Element) itemNodes.item(j); // item
                        String item = itemElem.getTextContent();
                        itemList.add(item);
                    }
                    result.put(title, itemList);
                }
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> getHelpSceneMap() {
        return result;
    }

    public static String getTomcatConfPath() {
        return path;
    }
}
