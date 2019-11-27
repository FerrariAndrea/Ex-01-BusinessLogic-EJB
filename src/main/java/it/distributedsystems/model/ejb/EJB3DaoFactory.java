package it.distributedsystems.model.ejb;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;

import it.distributedsystems.model.dao.*;
import org.apache.log4j.Logger;
import org.jboss.system.server.ServerInfo;

public class EJB3DaoFactory extends DAOFactory {
    private static Logger logger = Logger.getLogger("DAOFactory");

    public EJB3DaoFactory() {


    }

    private static InitialContext getInitialContext() throws Exception {
        Hashtable props = getInitialContextProperties();
        return new InitialContext(props);
    }

    private static Hashtable getInitialContextProperties() {
        Hashtable props = new Hashtable();
        //props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        //props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
       // props.put("java.naming.provider.url", "127.0.0.1:1099"); //(new ServerInfo()).getHostAddress()  --- 127.0.0.1 --
        props.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL,"http-remoting://localhost:1099");
        return props;
    }

    public CustomerDAO getCustomerDAO() {
        try {
            InitialContext context = getInitialContext();
            //CustomerDAO result = (CustomerDAO)context.lookup("distributed-systems-demo/EJB3CustomerDAO/local");
            CustomerDAO result = (CustomerDAO)context.lookup(generatePathContext("Customer",true));
            return result;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3CustomerDAO", var3);
            return null;
        }
    }

    public PurchaseDAO getPurchaseDAO() {
        try {
            InitialContext context = getInitialContext();
            //PurchaseDAO result = (PurchaseDAO)context.lookup("distributed-systems-demo/EJB3PurchaseDAO/local");
            PurchaseDAO result = (PurchaseDAO)context.lookup(generatePathContext("Purchase",false));

            return result;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3PurchaseDAO", var3);
            return null;
        }
    }

    public ProductDAO getProductDAO() {
        try {
            InitialContext context = getInitialContext();
           // ProductDAO result = (ProductDAO)context.lookup("distributed-systems-demo/EJB3ProductDAO/local");
            ProductDAO result = (ProductDAO)context.lookup(generatePathContext("Product",true));
            return result;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3ProductDAO", var3);
            return null;
        }
    }


    public ProducerDAO getProducerDAO() {
        try {
            InitialContext context = getInitialContext();
           // ProducerDAO result = (ProducerDAO)context.lookup("distributed-systems-demo/EJB3ProducerDAO/local");
            ProducerDAO result = (ProducerDAO)context.lookup(generatePathContext("Producer",true));

            return result;
        } catch (Exception var3) {
            logger.error("Error looking up EJB3ProducerDAO", var3);
            return null;
        }
    }

    private String generatePathContext(String ClassName,boolean local){
        if(local){
            return "java:global/distributed-systems-demo/distributed-systems-demo.war/EJB3"+ClassName+"DAO!it.distributedsystems.model.dao."+ClassName+"DAO";
        }else{
            return "ejb:distributed-systems-demo/distributed-systems-demo.war/EJB3"+ClassName+"DAO!it.distributedsystems.model.dao."+ClassName+"DAO";
        }
    }
}
