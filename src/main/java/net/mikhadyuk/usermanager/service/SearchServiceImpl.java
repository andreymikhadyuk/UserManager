package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    public final static String BELCHIP_LING = "http://belchip.by/";

    @Override
    public List<Product> getRequestedProductsFromBelchip(String request) {
        List<Product> products = new ArrayList<>();

        String queryUrl = "http://belchip.by/search_fuzzy/?query=";
        String searchQuery = request.replaceAll(" ", "+");

        try
        {
            searchQuery = URLEncoder.encode(searchQuery, "utf-8");
            Document doc = Jsoup.connect(queryUrl + searchQuery).get();
            Elements divElem = doc.getElementsByClass("cat-item");
            for (Element div : divElem) {
                Product product = new Product();
                Elements links = div.select("a[href]");
                product.setImageUrl(BELCHIP_LING + links.get(0).attr("href"));
                product.setUrl(BELCHIP_LING + links.get(2).attr("href"));
                product.setName(links.get(2).text());
                products.add(product);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return products;
    }
}
