package by.lab3.server.dao;

public class DaoFactory {

    private static final DaoFactory INSTANCE = new DaoFactory();

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public PersonDao getCaseDao() {
        return PersonDao.getInstance();
    }

}
