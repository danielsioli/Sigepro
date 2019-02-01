/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;


//quando mudar para tomcat, voltar para a versão 305
/**
 *
 * @author Daniel
 */
public class DAO {

    private static DAO instance = null;
    private DAO backupDAO = null;
    private String context;
    private String dataSource;
    private boolean backup = false;

    private DAO(boolean backup) {
        if (!backup) {
            context = "java:comp/env/persistence/LogicalName";
            dataSource = "java:comp/env/DataSource";
            /*backupDAO = new DAO(true);
            backupDAO.setBackup(true);
            backupDAO.setContext("java:comp/env/persistence/LogicalNameBackup");
            backupDAO.setDataSource("java:comp/env/DataSourceBackup");*/
        }
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO(false);
        }
        return instance;
    }

    protected String getDataSource() {
        return dataSource;
    }

    protected void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    protected boolean isBackup() {
        return backup;
    }

    protected void setBackup(boolean backup) {
        this.backup = backup;
    }

    protected String getContext() {
        return context;
    }

    protected void setContext(String context) {
        this.context = context;
    }

    public <T> T find(Class<T> t, Object primaryKey) {
        T rows = null;
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup(context);
            rows = (T) em.find(t, primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            /*if (!isBackup()) {
                return backupDAO.find(t, primaryKey);
            } else {
                throw new RuntimeException(ex);
            }*/
        }
        return rows;
    }

    public <T> T find(Class<T> t, String primaryKey) {
        T rows = null;
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup(context);
            rows = (T) em.find(t, primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            /*if (!isBackup()) {
                return backupDAO.find(t, primaryKey);
            } else {
                throw new RuntimeException(ex);
            }*/
        }
        return rows;
    }

    public <T> T find(Class<T> t, int primaryKey) {
        T rows = null;
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup(context);
            rows = (T) em.find(t, primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            /*if (!isBackup()) {
                return backupDAO.find(t, primaryKey);
            } else {
                throw new RuntimeException(ex);
            }*/
        }
        return rows;
    }

    public <T> T find(Class<T> t, long primaryKey) {
        T rows = null;
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup(context);
            rows = (T) em.find(t, primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            /*if (!isBackup()) {
                return backupDAO.find(t, primaryKey);
            } else {
                throw new RuntimeException(ex);
            }*/
        }
        return rows;
    }

    public List<Object> findByUnnamedQuery(Class e, String unnamedQuery, Hashtable<String, Object> parameters) {
        List<Object> rows = new Vector<Object>();
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup(context);
            Query query = em.createQuery(unnamedQuery);
            Enumeration<String> keys = parameters.keys();
            Enumeration<Object> values = parameters.elements();
            while (keys.hasMoreElements()) {
                query.setParameter(keys.nextElement(), values.nextElement());
            }
            rows = (List<Object>) query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            /*if (!isBackup()) {
                return backupDAO.findByUnnamedQuery(e, unnamedQuery, parameters);
            } else {
                throw new RuntimeException(ex);
            }*/
        }
        return rows;
    }

    public <E> List<E> findByNamedQuery(Class<E> e, String namedQuery, Hashtable<String, String> parameters) {
        List<E> rows = new Vector<E>();
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup(context);
            Query query = em.createNamedQuery(namedQuery);
            Enumeration<String> keys = parameters.keys();
            Enumeration<String> values = parameters.elements();
            while (keys.hasMoreElements()) {
                query.setParameter(keys.nextElement(), values.nextElement());
            }
            rows = (List<E>) query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            /*if (!isBackup()) {
                return backupDAO.findByNamedQuery(e, namedQuery, parameters);
            } else {
                throw new RuntimeException(ex);
            }*/
        }
        return rows;
    }

    public void remove(Object[] entities) {
        try {
            Context ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/env/UserTransaction");
            utx.begin();
            EntityManager em = (EntityManager) ctx.lookup(context);
            for (int i = 0; i < entities.length; i++) {
                em.remove(em.contains(entities[i]) ? entities[i] : em.merge(entities[i]));
            }
            utx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            throw new RuntimeException(ex);
        }
    }

    public void persist(Object[] entities) {
        try {
            Context ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/env/UserTransaction");
            utx.begin();
            EntityManager em = (EntityManager) ctx.lookup(context);
            for (int i = 0; i < entities.length; i++) {
                em.persist(entities[i]);
            }
            utx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            throw new RuntimeException(ex);
        }
    }

    public void merge(Object[] entities) {
        try {
            Context ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/env/UserTransaction");
            utx.begin();
            EntityManager em = (EntityManager) ctx.lookup(context);
            for (int i = 0; i < entities.length; i++) {
                em.merge(entities[i]);
            }
            utx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            throw new RuntimeException(ex);
        }
    }

    public String getCryptedPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        String cryptedPassword = null;
        Connection connection;
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(dataSource);
            connection = ds.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT PASSWORD('" + rawPassword + "')");

            // Se não houver registros, retorna null
            if (!result.next()) {
                return null;
            }
            if (result.getMetaData().getColumnCount() != 1) {
                return null;
            }
            cryptedPassword = result.getString(1);

            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ex);
            if (!isBackup()) {
                return backupDAO.getCryptedPassword(rawPassword);
            } else {
                throw new RuntimeException(ex);
            }
        }
        return cryptedPassword;
    }
}
