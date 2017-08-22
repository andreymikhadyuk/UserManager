package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.model.Product;

import java.util.List;

public interface SearchService {
    List<Product> getRequestedProductsFromBelchip(String request);
}
