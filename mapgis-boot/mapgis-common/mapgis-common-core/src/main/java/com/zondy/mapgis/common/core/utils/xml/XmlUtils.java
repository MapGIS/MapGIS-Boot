package com.zondy.mapgis.common.core.utils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ogc xml工具类
 *
 * @author liuyudong
 * @since 2023/1/10 17:47
 */
public class XmlUtils {
    /**
     * 解析xml
     *
     * @param inputStream
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static Document readXml(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new DOMUtils.NullResolver());

        // db.setErrorHandler( new MyErrorHandler());

        return db.parse(inputStream);
    }

    /**
     * 解析xml
     *
     * @param reader
     * @return dom节点
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static Document readXml(Reader reader) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new DOMUtils.NullResolver());

        // db.setErrorHandler( new MyErrorHandler());
        InputSource ips = new InputSource(reader);
        return db.parse(ips);
    }

    /**
     * 解析xml
     *
     * @param file 文件
     * @return dom节点
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static Document readXml(File file) throws SAXException, IOException, ParserConfigurationException {
        if (!file.exists()) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new DOMUtils.NullResolver());

        // db.setErrorHandler( new MyErrorHandler());

        Document doc = db.parse(file);
        return doc;
    }

    /**
     * 解析xml
     *
     * @param streamSource
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static Document readXml(StreamSource streamSource) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new DOMUtils.NullResolver());

        // db.setErrorHandler( new MyErrorHandler());
        InputSource is2 = new InputSource();
        is2.setSystemId(streamSource.getSystemId());
        is2.setByteStream(streamSource.getInputStream());
        is2.setCharacterStream(streamSource.getReader());

        return db.parse(is2);
    }

    /**
     * 通过节点名称(不区分大小写)获取父节点下的子节点列表
     *
     * @param parent
     * @param localName
     * @return
     */
    public static List<Element> getChildrenByLocalName(Element parent, String localName) {
        List<Element> r = new ArrayList<Element>();
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element) {
                Element e = (Element) n;
                if (localName.equalsIgnoreCase(e.getLocalName())) {
                    r.add(e);
                }
            }
        }
        return r;
    }

    /**
     * 通过节点名称(不区分大小写)获取父节点下的第一个子节点
     *
     * @param parent
     * @param localName
     * @return
     */
    public static Element getFirstChildrenByLocalName(Element parent, String localName) {
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element) {
                Element e = (Element) n;
                if (localName.equalsIgnoreCase(e.getLocalName())) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * 获取父节点下的第一个子节点
     *
     * @param parent
     * @return
     */
    public static Element getFirstChildren(Element parent) {
        Node n = parent.getFirstChild();
        while (n != null && !(n instanceof Element)) {
            n = n.getNextSibling();
        }
        if (n == null) {
            return null;
        }
        return (Element) n;
    }

    /**
     * @param parent
     * @return
     */
    public static List<Element> getChildren(Element parent) {
        List<Element> list = new ArrayList<>();
        Node n = parent.getFirstChild();
        while (n != null) {
            if (n instanceof Element) {
                list.add((Element) n);
            }
            n = n.getNextSibling();
        }
        return list;
    }

    /**
     * 获取节点下的属性值
     *
     * @param element 节点
     * @param attName 属性名称
     * @return
     */
    public static String getAttribute(Element element, String attName) {
        NamedNodeMap attrs = element.getAttributes();
        if (attrs == null) {
            return null;
        }
        Node attN = attrs.getNamedItem(attName);
        if (attN == null) {
            return null;
        }
        return attN.getNodeValue();
    }

    /**
     * 节点转字符串
     *
     * @param node 节点
     * @return
     */
    public static String nodeToString(Node node) {
        StringWriter sw = null;
        try {
            sw = new StringWriter();

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
            transformerFactoryConfigurationError.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }
}