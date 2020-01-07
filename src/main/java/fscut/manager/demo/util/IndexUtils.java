package fscut.manager.demo.util;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.hcore.impl.HibernateSessionFactoryService;

public class IndexUtils {
    public static void initFullTextIndex() throws Exception {
        SessionFactory
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();
    }
}
