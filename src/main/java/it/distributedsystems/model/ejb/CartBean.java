package it.distributedsystems.model.ejb;

import it.distributedsystems.model.dao.Product;

import javax.ejb.Local;
import javax.ejb.Stateful;
import java.util.ArrayList;
import java.util.List;


@Local
@Stateful
public class CartBean implements Cart {

    List<Product> myCart = new ArrayList<Product>();
    @Override
    public boolean addToCart(Product p) {
        try{
            return   myCart.add(p);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean removeFromCart(Product p) {
        try{
           return myCart.remove(p);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean confirm() {
            //save on db
        return true;
    }
}
