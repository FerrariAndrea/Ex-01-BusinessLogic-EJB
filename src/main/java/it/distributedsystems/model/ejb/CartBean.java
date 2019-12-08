package it.distributedsystems.model.ejb;

import it.distributedsystems.model.dao.DAOFactory;
import it.distributedsystems.model.dao.Product;
import it.distributedsystems.model.dao.Purchase;
import it.distributedsystems.model.dao.PurchaseDAO;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.faces.context.ExternalContext;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Local
@Stateful(name = "carrello")
public class CartBean implements Cart {

    private List<Product> myCart = new ArrayList<Product>();
    private int testingSession = 0;
    private String label="Your cart is void";
    //@EJB Purchase purchase;
    //@EJB EJB3PurchaseDAO purchaseDAO; <-------NON VA
    public CartBean(){  myCart = new ArrayList<Product>(); label="Your cart is void";}
/*
    @Override
    public void init() {
        myCart = new ArrayList<Product>();
    }
*/
    @Override
    public boolean addToCart(Product p) {
        testingSession++;
        System.out.println("--------------->Count of invoke: "+ testingSession);
        if(p!=null ){
            label="Add on cart fail, unknown item.";
        }
        try{
            if(myCart.contains(p)){
                System.out.println("--------------->myCart.contains(p)--<true>");
                label="Add on cart fail, product id: "+ p.getId()+ ", already on cart.";
                return false;
            }
            if(myCart.add(p) ){
                label="Product id: "+ p.getId() + " added on cart.";
                return true;
            }else{
                label="Add on cart fail, product id: "+ p.getId()+ ".";
                return true;
            }
        }catch (Exception e){
            System.out.println("WARNING-> addToCart error: "+ e.getMessage());
            if(p!=null ){ label="Add on cart fail, for product id: "+ p.getId();}else{
                label="Add on cart fail, unknown item.";
            }
            return false;
        }
    }

    @Override
    public boolean removeFromCart(Product p) {
        try{
           //return myCart.remove(p);
            return internalRemove(p);
        }catch (Exception e){
            System.out.println("WARNING-> removeFromCart error: "+ e.getMessage());
            label="Remove item from cart fail.";
            return false;
        }
    }

    private boolean internalRemove(Product p){
        int id=p.getId();
        for (int x=0;x< myCart.size();x++ ) {
            if(id==myCart.get(x).getId()){
                label="Item  "+myCart.remove(x).getId()+" removed from cart.";
                return true;
            }
        }
        label="No item found to remove.";
        return false;
    }



    @Override
    public boolean confirm() {
        Set<Product> set = myCart.stream().collect(Collectors.toSet());
        Purchase purchase = new Purchase();
        purchase.setProducts(set);
        try{
            InitialContext ic = new InitialContext();
            //java:global/distributed-systems-demo/distributed-systems-demo.war/purchaseDAO!it.distributedsystems.model.dao.PurchaseDAO
            //java:global/distributed-systems-demo/distributed-systems-demo.war/purchaseDAO
            PurchaseDAO  purchaseDAO = (PurchaseDAO) ic.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/purchaseDAO");
            purchaseDAO.insertPurchase(purchase);
        }catch (Exception ex){
            System.out.println("ERROR-> confirm error: "+ ex.getMessage());
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
    public String getLabel() {
        return label;
    }
}
