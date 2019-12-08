package it.distributedsystems.model.ejb;

import it.distributedsystems.model.dao.Product;

import javax.ejb.Local;
import javax.ejb.Stateful;
import java.util.ArrayList;
import java.util.List;


@Local
@Stateful(name = "carrello")
public class CartBean implements Cart {

    private List<Product> myCart = new ArrayList<Product>();
    private int testingSession = 0;
    public CartBean(){  myCart = new ArrayList<Product>();}
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
        try{
            if(myCart.contains(p)){
                System.out.println("--------------->myCart.contains(p)--<true>");
                return false;
            }
            return   myCart.add(p);
        }catch (Exception e){
            System.out.println("WARNING-> addToCart error: "+ e.getMessage());
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
            return false;
        }
    }

    private boolean internalRemove(Product p){
        int id=p.getId();
        for (int x=0;x< myCart.size();x++ ) {
            if(id==myCart.get(x).getId()){
                myCart.remove(x);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean confirm() {
            //save on db
        return true;
    }

    @Override
    public List<Product> getPorductsInCart() {
        return myCart;
    }
}
