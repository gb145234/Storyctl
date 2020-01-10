//package fscut.manager.demo.util;
//
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.internal.SessionFactoryImpl;
//import org.hibernate.search.FullTextSession;
//import org.hibernate.search.Search;
//import org.hibernate.search.hcore.impl.HibernateSessionFactoryService;
//import org.hibernate.search.jpa.FullTextEntityManager;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//
//public class IndexUtils {
//    public static void initFullTextIndex() throws Exception {
//        SessionFactory sessionFactory = null;
//        Session currentSession = sessionFactory.getCurrentSession();
//        FullTextSession fullTextSession = Search.getFullTextSession(currentSession);
//        fullTextSession.createIndexer().startAndWait();
//    }
//}
