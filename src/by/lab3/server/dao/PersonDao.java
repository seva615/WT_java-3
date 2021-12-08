package by.lab3.server.dao;

import by.lab3.server.model.User;
import by.lab3.server.service.ServiceFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PersonDao {
    private static final PersonDao INSTANCE = new PersonDao();
    private static final String XML_PATH = "users.xml";

    private final ReadWriteLock lock;
    private final Map<Integer, User> persons;

    private PersonDao() {
        lock = new ReentrantReadWriteLock();
        persons = new HashMap<>();
        init();
    }

    private void init() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(XML_PATH));
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    User box = ServiceFactory.getInstance().getCaseService().createUser(node.getChildNodes());
                    persons.put(box.getId(), box);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ignored) {
        }
    }

    public static PersonDao getInstance() {
        return INSTANCE;
    }

    public boolean contains(int id) {
        return persons.containsKey(id);
    }

    public List<User> getAll() {
        try {
            lock.readLock().lock();
            return new ArrayList<>(persons.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void add(User box) {
        try {
            lock.writeLock().lock();
            box.setId(persons.keySet().stream().max(Comparator.comparingInt(a -> a)).get() + 1);
            persons.put(box.getId(), box);
            update();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setById(int id, User box) {
        try {
            lock.writeLock().lock();
            box.setId(id);
            persons.put(id, box);
            update();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void update() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element rootEle = doc.createElement("users");
            for(var _case : getAll()) {
                Element caseEle = ServiceFactory.getInstance().getCaseService().createNode(doc, _case);
                rootEle.appendChild(caseEle);
            }

            doc.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(XML_PATH)));

            } catch (IOException | TransformerException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
