package by.lab3.server.service;

import by.lab3.server.dao.DaoFactory;
import by.lab3.server.model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public User createUser(NodeList nodes) {
        int id = 0;
        String first = "";
        String last = "";

        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                String text = nodes.item(i).getTextContent();
                switch (nodes.item(i).getNodeName()) {
                    case "id" -> id = Integer.parseInt(text);
                    case "firstName" -> first = text;
                    case "lastName" -> last = text;
                    default -> throw new IllegalArgumentException("User doesn't exist");
                }
            }
        }

        return new User(id, first, last);
    }

    public Element createNode(Document doc, User user) {
        Element e = doc.createElement("case");
        Element id = doc.createElement("id");
        Element first = doc.createElement("firstName");
        Element last = doc.createElement("lastName");

        id.appendChild(doc.createTextNode(String.valueOf(user.getId())));
        first.appendChild(doc.createTextNode(user.getFirstName()));
        last.appendChild(doc.createTextNode(user.getLastName()));

        e.appendChild(id);
        e.appendChild(first);
        e.appendChild(last);

        return e;
    }

    public List<User> getAll() {
        return DaoFactory.getInstance().getCaseDao().getAll();
    }

    public boolean containsUser(int id) {
        return DaoFactory.getInstance().getCaseDao().contains(id);
    }

    public void editUser(int id, String firstName, String lastName) {
        DaoFactory.getInstance().getCaseDao().setById(id, new User(0, firstName, lastName));
    }

    public void addUser(String firstName, String lastName) {
        DaoFactory.getInstance().getCaseDao().add(new User(0, firstName, lastName));
    }
}
