package it.distributedsystems.model.ejb;

import it.distributedsystems.model.dao.Product;

public interface Cart {

    public boolean addToCart(Product p);
    public boolean removeFromCart(Product p);
    public boolean confirm();
}
