package it.distributedsystems.model.ejb;

import it.distributedsystems.model.dao.Product;

import java.util.List;

public interface Cart {
   // public void init();
    public boolean addToCart(Product p);
    public boolean removeFromCart(Product p);
    public boolean confirm();
    public List<Product> getPorductsInCart();
}
