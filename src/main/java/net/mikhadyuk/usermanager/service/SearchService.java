package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.model.Product;

import java.util.List;

public interface SearchService {
    void initBelchip(String request);
    List<Product> getNextBelchipProducts();

    void initChipdip(String request);
    List<Product> getNextChipdipProducts();
}
