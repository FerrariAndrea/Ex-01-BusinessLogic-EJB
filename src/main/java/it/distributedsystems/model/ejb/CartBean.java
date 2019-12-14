package it.distributedsystems.model.ejb;

import it.distributedsystems.model.dao.*;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateful;

import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
    import java.util.stream.Collectors;


@Local
@Stateful(name = "carrello")
public class CartBean implements Cart {
   @Resource
   InitialContext  ctx;
    private List<Product> myCart = new ArrayList<Product>();
    private Customer myCustomer=null;
    private int testingSession = 0;
    private String label="Your cart is void";
   // private QueueSession session =null;
   // private QueueSender sender = null;
    //@EJB Purchase purchase;
    //@EJB EJB3PurchaseDAO purchaseDAO; <-------NON VA
    public CartBean() {
        myCart = new ArrayList<Product>();
        label="Your cart is void";

        try {
         //  InitialContext  ctx = new InitialContext();
           /*
            Queue queue = (Queue) ctx.lookup("/queue/Logging");
            QueueConnectionFactory factory =(QueueConnectionFactory) ctx.lookup("ConnectionFactory");
            QueueConnection connection = null;
            connection = factory.createQueueConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            sender = session.createSender(queue);

            */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
/*
    @Override
    public void init() {
        myCart = new ArrayList<Product>();
    }
*/
    @Override
    public boolean addToCart(Product p) {
        testingSession++;
        setLabel("--------------->Count of invoke: "+ testingSession);
       //System.out.println("--------------->Count of invoke: "+ testingSession);
        if(p!=null ){
            setLabel("Add on cart fail, unknown item.");
        }
        try{
            if(myCart.contains(p)){
                System.out.println("--------------->myCart.contains(p)--<true>");
               setLabel("Add on cart fail, product id: "+ p.getId()+ ", already on cart.");
                return false;
            }
            if(myCart.add(p) ){
                setLabel("Product id: "+ p.getId() + " added on cart.");
                return true;
            }else{
                setLabel("Add on cart fail, product id: "+ p.getId()+ ".");
                return true;
            }
        }catch (Exception e){
            System.out.println("WARNING-> addToCart error: "+ e.getMessage());
            if(p!=null ){  setLabel("Add on cart fail, for product id: "+ p.getId());}else{
                setLabel("Add on cart fail, unknown item.");
            }
            return false;
        }
    }

    @Override
    public boolean removeFromCart(Product p) {
        try{
           //return myCart.remove(p);
            setLabel("Remove "+p.getName());
            return internalRemove(p);
        }catch (Exception e){
            System.out.println("WARNING-> removeFromCart error: "+ e.getMessage());
            setLabel("Remove item from cart fail.");
            return false;
        }
    }

    private boolean internalRemove(Product p){
        int id=p.getId();
        for (int x=0;x< myCart.size();x++ ) {
            if(id==myCart.get(x).getId()){
                setLabel("Item  "+myCart.remove(x).getId()+" removed from cart.");
                return true;
            }
        }
        setLabel("No item found to remove.");
        return false;
    }



    @Override
    public boolean confirm() {
        Set<Product> set = myCart.stream().collect(Collectors.toSet());
        Purchase purchase = new Purchase();
        purchase.setProducts(set);
        purchase.setCustomer(myCustomer);
        try{
            InitialContext ic = new InitialContext();
            //java:global/distributed-systems-demo/distributed-systems-demo.war/purchaseDAO!it.distributedsystems.model.dao.PurchaseDAO
            //java:global/distributed-systems-demo/distributed-systems-demo.war/purchaseDAO
            PurchaseDAO  purchaseDAO = (PurchaseDAO) ic.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/purchaseDAO");
            purchaseDAO.insertPurchase(purchase);
            setLabel("Purchase confirmed.");
        }catch (Exception ex){
            System.out.println("ERROR-> confirm error: "+ ex.getMessage());
            setLabel("Purchase failed.");
            return false;
        }
       // purchaseDAO.insertPurchase(purchase);
        return true;
    }

    @Override
    public List<Product> getPorductsInCart() {

        return myCart;
    }

    @Override
    public void setCostumer(Customer c) {
        setLabel("Customer set to "+ c.getName());
        myCustomer=c;
    }

    @Override
    public String getLabel() {
        return label;
    }

    private void setLabel(String str){
        label = str;
       // if(session !=null && sender!=null){
    /*
            try {
                ObjectMessage objectMessage = session.createObjectMessage(str);
                sender.send(objectMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }


     */
       // }

    }
}
