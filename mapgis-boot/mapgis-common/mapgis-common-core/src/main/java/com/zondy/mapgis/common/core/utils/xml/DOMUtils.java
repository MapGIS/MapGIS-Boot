package com.zondy.mapgis.common.core.utils.xml;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Few simple utils to read DOM. This is originally from the Jakarta Commons Modeler.
 *
 * @author Costin Manolache
 * @since 2023/1/10 17:43
 */
public class DOMUtils {
    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;
    private static final String XMLNAMESPACE = "xmlns";

    private DOMUtils() {
    }

    private static synchronized DocumentBuilder getBuilder() throws ParserConfigurationException {
        if (builder == null) {
            FACTORY.setNamespaceAware(true);
            builder = FACTORY.newDocumentBuilder();
        }
        return builder;
    }

    /**
     * This function is much like getAttribute, but returns null, not "", for a nonexistent attribute.
     *
     * @param e
     * @param attributeName
     * @return
     */
    public static String getAttributeValueEmptyNull(Element e, String attributeName) {
        Attr node = e.getAttributeNode(attributeName);
        if (node == null) {
            return null;
        }
        return node.getValue();
    }

    /**
     * Get the trimmed text content of a node or null if there is no text
     */
    public static String getContent(Node n) {
        String s = getRawContent(n);
        if (s != null) {
            s = s.trim();
        }
        return s;
    }

    /**
     * Get the raw text content of a node or null if there is no text
     */
    public static String getRawContent(Node n) {
        if (n == null) {
            return null;
        }
        StringBuilder b = null;
        String s = null;
        Node n1 = n.getFirstChild();
        while (n1 != null) {
            if (n1.getNodeType() == Node.TEXT_NODE) {
                if (b != null) {
                    b.append(n1.getNodeValue());
                } else if (s == null) {
                    s = n1.getNodeValue();
                } else {
                    b = new StringBuilder(s).append(n1.getNodeValue());
                    s = null;
                }
            }
            n1 = n1.getNextSibling();
        }
        if (b != null) {
            return b.toString();
        }
        return s;
    }

    /**
     * Get the first element child.
     *
     * @param parent lookup direct childs
     * @param name   name of the element. If null return the first element.
     */
    public static Node getChild(Node parent, String name) {
        if (parent == null) {
            return null;
        }

        Node first = parent.getFirstChild();
        if (first == null) {
            return null;
        }

        for (Node node = first; node != null; node = node.getNextSibling()) {
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (name != null && name.equals(node.getNodeName())) {
                return node;
            }
            if (name == null) {
                return node;
            }
        }
        return null;
    }

    /**
     * Get the first element child.
     *
     * @param parent    lookup direct childs
     * @param localName localname of the element. If null return the first element.
     */
    public static Node getChildByLocalName(Node parent, String localName) {
        if (parent == null) {
            return null;
        }

        Node first = parent.getFirstChild();
        if (first == null) {
            return null;
        }

        for (Node node = first; node != null; node = node.getNextSibling()) {
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (localName != null && localName.equalsIgnoreCase(node.getLocalName())) {
                return node;
            }
            if (localName == null) {
                return node;
            }
        }
        return null;
    }

    public static String getAttribute(Node element, String attName) {
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

    public static String getAttribute(Element element, QName attName) {
        Attr attr;
        if (isEmpty(attName.getNamespaceURI())) {
            attr = element.getAttributeNode(attName.getLocalPart());
        } else {
            attr = element.getAttributeNodeNS(attName.getNamespaceURI(), attName.getLocalPart());
        }
        return attr == null ? null : attr.getValue();
    }

    public static void setAttribute(Node node, String attName, String val) {
        NamedNodeMap attributes = node.getAttributes();
        Node attNode = node.getOwnerDocument().createAttribute(attName);
        attNode.setNodeValue(val);
        attributes.setNamedItem(attNode);
    }

    public static void removeAttribute(Node node, String attName) {
        NamedNodeMap attributes = node.getAttributes();
        attributes.removeNamedItem(attName);
    }

    /**
     * Set or replace the text value
     */
    public static void setText(Node node, String val) {
        Node chld = DOMUtils.getChild(node, Node.TEXT_NODE);
        if (chld == null) {
            Node textN = node.getOwnerDocument().createTextNode(val);
            node.appendChild(textN);
            return;
        }
        // change the value
        chld.setNodeValue(val);
    }

    /**
     * Find the first direct child with a given attribute.
     *
     * @param parent
     * @param elemName name of the element, or null for any
     * @param attName  attribute we're looking for
     * @param attVal   attribute value or null if we just want any
     */
    public static Node findChildWithAtt(Node parent, String elemName, String attName, String attVal) {

        Node child = DOMUtils.getChild(parent, Node.ELEMENT_NODE);
        if (attVal == null) {
            while (child != null && (elemName == null || elemName.equals(child.getNodeName())) && DOMUtils.getAttribute(child, attName) != null) {
                child = getNext(child, elemName, Node.ELEMENT_NODE);
            }
        } else {
            while (child != null && (elemName == null || elemName.equals(child.getNodeName())) && !attVal.equals(DOMUtils.getAttribute(child, attName))) {
                child = getNext(child, elemName, Node.ELEMENT_NODE);
            }
        }
        return child;
    }

    /**
     * Get the first child's content ( ie it's included TEXT node ).
     */
    public static String getChildContent(Node parent, String name) {
        Node first = parent.getFirstChild();
        if (first == null) {
            return null;
        }
        for (Node node = first; node != null; node = node.getNextSibling()) {
            if (name.equals(node.getNodeName())) {
                return getRawContent(node);
            }
        }
        return null;
    }

    public static QName getElementQName(Element el) {
        return new QName(el.getNamespaceURI(), el.getLocalName());
    }

    /**
     * Get the first direct child with a given type
     */
    public static Element getFirstElement(Node parent) {
        Node n = parent.getFirstChild();
        while (n != null && Node.ELEMENT_NODE != n.getNodeType()) {
            n = n.getNextSibling();
        }
        if (n == null) {
            return null;
        }
        return (Element) n;
    }

    public static Element getNextElement(Element el) {
        Node nd = el.getNextSibling();
        while (nd != null) {
            if (nd.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) nd;
            }
            nd = nd.getNextSibling();
        }
        return null;
    }

    /**
     * Return the first element child with the specified qualified name.
     *
     * @param parent
     * @param q
     * @return
     */
    public static Element getFirstChildWithName(Element parent, QName q) {
        String ns = q.getNamespaceURI();
        String lp = q.getLocalPart();
        return getFirstChildWithName(parent, ns, lp);
    }

    /**
     * Return the first element child with the specified qualified name.
     *
     * @param parent
     * @param ns
     * @param lp
     * @return
     */
    public static Element getFirstChildWithName(Element parent, String ns, String lp) {
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element) {
                Element e = (Element) n;
                String ens = (e.getNamespaceURI() == null) ? "" : e.getNamespaceURI();
                if (ns.equals(ens) && lp.equals(e.getLocalName())) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Return child elements with specified name.
     *
     * @param parent
     * @param ns
     * @param localName
     * @return
     */
    public static List<Element> getChildrenWithName(Element parent, String ns, String localName) {
        List<Element> r = new ArrayList<Element>();
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element) {
                Element e = (Element) n;
                String eNs = (e.getNamespaceURI() == null) ? "" : e.getNamespaceURI();
                if (ns.equals(eNs) && localName.equals(e.getLocalName())) {
                    r.add(e);
                }
            }
        }
        return r;
    }

    /**
     * Get the first child of the specified type.
     *
     * @param parent
     * @param type
     * @return
     */
    public static Node getChild(Node parent, int type) {
        Node n = parent.getFirstChild();
        while (n != null && type != n.getNodeType()) {
            n = n.getNextSibling();
        }
        if (n == null) {
            return null;
        }
        return n;
    }

    public static List<Node> getChildList(Node parent, int type) {
        List<Node> list = new ArrayList<>();
        Node n = parent.getFirstChild();
        while (n != null) {
            if (type == n.getNodeType()) {
                list.add(n);
            }
            n = n.getNextSibling();
        }
        return list;
    }

    /**
     * Get the next sibling with the same name and type
     */
    public static Node getNext(Node current) {
        String name = current.getNodeName();
        int type = current.getNodeType();
        return getNext(current, name, type);
    }

    /**
     * Return the next sibling with a given name and type
     */
    public static Node getNext(Node current, String name, int type) {
        Node first = current.getNextSibling();
        if (first == null) {
            return null;
        }

        for (Node node = first; node != null; node = node.getNextSibling()) {

            if (type >= 0 && node.getNodeType() != type) {
                continue;
            }

            if (name == null) {
                return node;
            }
            if (name.equals(node.getNodeName())) {
                return node;
            }
        }
        return null;
    }

    public static class NullResolver implements EntityResolver {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    }

    /**
     * Read XML as DOM.
     */
    public static Document readXml(InputStream is) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new NullResolver());

        // db.setErrorHandler( new MyErrorHandler());

        return db.parse(is);
    }

    public static Document readXml(Reader is) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new NullResolver());

        // db.setErrorHandler( new MyErrorHandler());
        InputSource ips = new InputSource(is);
        return db.parse(ips);
    }

    public static Document readXml(File xmlF) throws SAXException, IOException, ParserConfigurationException {
        if (!xmlF.exists()) {
            return null;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        //dbf.setCoalescing(true);
        //dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new NullResolver());

        // db.setErrorHandler( new MyErrorHandler());

        Document doc = db.parse(xmlF);
        return doc;
    }

    public static Document readXml(StreamSource is) throws SAXException, IOException, ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        db.setEntityResolver(new NullResolver());

        // db.setErrorHandler( new MyErrorHandler());
        InputSource is2 = new InputSource();
        is2.setSystemId(is.getSystemId());
        is2.setByteStream(is.getInputStream());
        is2.setCharacterStream(is.getReader());

        return db.parse(is2);
    }

    public static void writeXml(Node n, OutputStream os) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        // identity
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(n), new StreamResult(os));
    }

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

    public static DocumentBuilder createDocumentBuilder() {
        try {
            return FACTORY.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Couldn't find a DOM parser.", e);
        }
    }

    public static Document createDocument() {
        try {
            return getBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Couldn't find a DOM parser.", e);
        }
    }

    public static String getPrefixRecursive(Element el, String ns) {
        String prefix = getPrefix(el, ns);
        if (prefix == null && el.getParentNode() instanceof Element) {
            prefix = getPrefixRecursive((Element) el.getParentNode(), ns);
        }
        return prefix;
    }

    public static String getPrefix(Element el, String ns) {
        NamedNodeMap atts = el.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            Node node = atts.item(i);
            String name = node.getNodeName();
            if (ns.equals(node.getNodeValue()) && (name != null && (XMLNAMESPACE.equals(name) || name.startsWith(XMLNAMESPACE + ":")))) {
                return node.getPrefix();
            }
        }
        return null;
    }

    /**
     * Get all prefixes defined, up to the root, for a namespace URI.
     *
     * @param element
     * @param namespaceUri
     * @param prefixes
     */
    public static void getPrefixesRecursive(Element element, String namespaceUri, List<String> prefixes) {
        getPrefixes(element, namespaceUri, prefixes);
        Node parent = element.getParentNode();
        if (parent instanceof Element) {
            getPrefixesRecursive((Element) parent, namespaceUri, prefixes);
        }
    }

    /**
     * Get all prefixes defined on this element for the specified namespace.
     *
     * @param element
     * @param namespaceUri
     * @param prefixes
     */
    public static void getPrefixes(Element element, String namespaceUri, List<String> prefixes) {
        NamedNodeMap atts = element.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            Node node = atts.item(i);
            String name = node.getNodeName();
            if (namespaceUri.equals(node.getNodeValue()) && (name != null && (XMLNAMESPACE.equals(name) || name.startsWith(XMLNAMESPACE + ":")))) {
                prefixes.add(node.getPrefix());
            }
        }
    }

    public static String createNamespace(Element el, String ns) {
        String p = "ns1";
        int i = 1;
        while (getPrefix(el, ns) != null) {
            p = "ns" + i;
            i++;
        }
        addNamespacePrefix(el, ns, p);
        return p;
    }

    /**
     * Starting from a node, find the namespace declaration for a prefix. for a matching namespace
     * declaration.
     *
     * @param node         search up from here to search for namespace definitions
     * @param searchPrefix the prefix we are searching for
     * @return the namespace if found.
     */
    public static String getNamespace(Node node, String searchPrefix) {

        Element el;
        while (!(node instanceof Element)) {
            node = node.getParentNode();
        }
        el = (Element) node;

        NamedNodeMap atts = el.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            Node currentAttribute = atts.item(i);
            String currentLocalName = currentAttribute.getLocalName();
            String currentPrefix = currentAttribute.getPrefix();
            if (searchPrefix.equals(currentLocalName) && XMLNAMESPACE.equals(currentPrefix)) {
                return currentAttribute.getNodeValue();
            } else if (isEmpty(searchPrefix) && XMLNAMESPACE.equals(currentLocalName) && isEmpty(currentPrefix)) {
                return currentAttribute.getNodeValue();
            }
        }

        Node parent = el.getParentNode();
        if (parent instanceof Element) {
            return getNamespace(parent, searchPrefix);
        }

        return null;
    }

    public static List<Element> findAllElementsByTagNameNS(Element elem, String nameSpaceURI, String localName) {
        List<Element> ret = new LinkedList<Element>();
        findAllElementsByTagNameNS(elem, nameSpaceURI, localName, ret);
        return ret;
    }

    private static void findAllElementsByTagNameNS(Element el, String nameSpaceURI, String localName, List<Element> elementList) {

        if (localName.equals(el.getLocalName()) && nameSpaceURI.contains(el.getNamespaceURI())) {
            elementList.add(el);
        }
        Element elem = getFirstElement(el);
        while (elem != null) {
            findAllElementsByTagNameNS(elem, nameSpaceURI, localName, elementList);
            elem = getNextElement(elem);
        }
    }

    public static List<Element> findAllElementsByTagName(Element elem, String tagName) {
        List<Element> ret = new LinkedList<Element>();
        findAllElementsByTagName(elem, tagName, ret);
        return ret;
    }

    private static void findAllElementsByTagName(Element el, String tagName, List<Element> elementList) {

        if (tagName.equals(el.getTagName())) {
            elementList.add(el);
        }
        Element elem = getFirstElement(el);
        while (elem != null) {
            findAllElementsByTagName(elem, tagName, elementList);
            elem = getNextElement(elem);
        }
    }

    /**
     * Set a namespace/prefix on an element if it is not set already. First off, it searches for the element
     * for the prefix associated with the specified namespace. If the prefix isn't null, then this is
     * returned. Otherwise, it creates a new attribute using the namespace/prefix passed as parameters.
     *
     * @param element
     * @param namespace
     * @param prefix
     * @return the prefix associated with the set namespace
     */
    public static String setNamespace(Element element, String namespace, String prefix) {
        String pre = getPrefixRecursive(element, namespace);
        if (pre != null) {
            return pre;
        }
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:" + prefix, namespace);
        return prefix;
    }

    /**
     * Add a namespace prefix definition to an element.
     *
     * @param element
     * @param namespaceUri
     * @param prefix
     */
    public static void addNamespacePrefix(Element element, String namespaceUri, String prefix) {
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:" + prefix, namespaceUri);
    }

    protected static boolean isEmpty(String str) {
        if (str != null) {
            int len = str.length();
            for (int x = 0; x < len; ++x) {
                if (str.charAt(x) > ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Element> getAllChild(Node node) {
        List<Element> elements = new ArrayList<>();
        if (node != null) {
            Element element = null;
            if (node instanceof Document) {
                element = ((Document) node).getDocumentElement();
            } else if (node instanceof Element) {
                element = (Element) node;
            }
            elements.add(element);
            getChilds(element, elements);
        }
        return elements;
    }

    private static List<Element> getChilds(Element parent, List<Element> elements) {
        if (parent.hasChildNodes()) {
            NodeList nodes = parent.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(0).getNodeType() == Element.ELEMENT_NODE) {
                    Element child = (Element) (nodes.item(i));
                    elements.add(child);
                    getChilds(child, elements);
                }
            }
        }
        return elements;
    }
}
