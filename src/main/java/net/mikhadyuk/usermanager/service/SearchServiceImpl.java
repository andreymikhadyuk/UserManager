package net.mikhadyuk.usermanager.service;

import net.mikhadyuk.usermanager.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    public final static String BELCHIP_LINK = "http://belchip.by/";
    private Elements catItems;
    private int currentCatItemIndex;

    public final static String CHIPDIP_LINK = "https://www.ru-chipdip.by";
    private Elements productGroups;
    private int currentProductGroupIndex;
    private Document currentProductGroupPage;

    @Override
    public void initBelchip(String query) {
        try {
            query.replaceAll(" ", "+");
            query = URLEncoder.encode(query, "utf-8");
            Document document = null;
            for (int i = 0; i < 500; i++) {
                try {
                    document = Jsoup.connect("http://belchip.by/search/?query=" + query).get();
                    break;
                } catch (IOException e) {
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e1) {
//                    }
                }
            }
            catItems = document.getElementsByClass("cat-item");
            if (catItems.size() == 0) {
                document = Jsoup.connect("http://belchip.by/search_fuzzy/?query=" + query).get();
                catItems = document.getElementsByClass("cat-item");
            }
            currentCatItemIndex = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getNextBelchipProducts() {
        List<Product> products = new ArrayList<>();

        if (currentCatItemIndex >= catItems.size()) {
            return products;
        }

        int newCurrentCatItemIndex = currentCatItemIndex + 10;
        for (int i = currentCatItemIndex; i < newCurrentCatItemIndex && i < catItems.size(); i++) {
            Element catItem = catItems.get(i);
            Product product = new Product();
            Elements links = catItem.select("a[href]");
            Element priceElement = catItem.getElementsByClass("butt-add").first();
            product.setImageUrl(BELCHIP_LINK + links.get(0).attr("href"));
            product.setUrl(BELCHIP_LINK + links.get(2).attr("href"));
            product.setName(links.get(2).text());
            if (priceElement.getElementsByClass("denoPrice").size() != 0) {
                Element denoPrice = priceElement.getElementsByClass("denoPrice").first();
                product.setPrice(denoPrice.text());
            } else {
                product.setPrice("on request");
            }
            products.add(product);
        }

        currentCatItemIndex = newCurrentCatItemIndex;

        return products;
    }

    @Override
    public void initChipdip(String request) {
        try {
            request.replaceAll(" ", "+");
            request = URLEncoder.encode(request, "utf-8");
            Document document = null;
            for (int i = 0; i < 500; i++) {
                try {
                    document = Jsoup.connect("https://www.ru-chipdip.by/search?searchtext=" + request).get();
                    break;
                } catch (IOException e) {
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e1) {
//                    }
                }
            }
            Elements ulElements = document.getElementsByClass("serp__group-col");
            productGroups = ulElements.select("a[href]");
            currentProductGroupIndex = 0;
            currentProductGroupPage = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getNextChipdipProducts() {
        if (currentProductGroupIndex >= productGroups.size()) {
            return new ArrayList<>();
        }

        return getProductsFromCurrentProductGroup();
    }

    private List<Product> getProductsFromCurrentProductGroup() {
        List<Product> products = new ArrayList<>();
        Document groupPage = null;

        if (currentProductGroupPage == null) {
            Element currentProductGroup = productGroups.get(currentProductGroupIndex);
            for (int i = 0; i < 500; i++) {
                try {
                    groupPage = Jsoup.connect(CHIPDIP_LINK + currentProductGroup.attr("href")).get();
                    break;
                } catch (IOException e) {
                    //e.printStackTrace();
                    //return products;
                }
            }
        } else {
            groupPage = currentProductGroupPage;
        }

        if (groupPage.getElementsByClass("with-hover").size() != 0) {
            hoverClassSearch(groupPage, products);
        } else {
            itemClassSearch(groupPage, products);
        }

        return products;
    }

    private void itemClassSearch(Document groupPage, List<Product> products) {
        Elements itemContentElements = groupPage.getElementsByClass("item__content");
        for (Element itemContentElement : itemContentElements) {
            Element aTag = itemContentElement.select("a[href]").get(0);
            Element priceElement = itemContentElement.getElementsByClass("price").get(0);
            Product product = new Product();

            Element imageTag = aTag.getElementsByTag("img").get(0);

            product.setName(imageTag.attr("alt"));
            product.setUrl(CHIPDIP_LINK + aTag.attr("href"));
            product.setImageUrl(imageTag.attr("src"));
            product.setPrice(priceElement.text());
            products.add(product);
        }

        currentProductGroupPage = getNextGroupPage(groupPage);
    }

    private void hoverClassSearch(Document groupPage, List<Product> products) {
        Elements withHoverElements = groupPage.getElementsByClass("with-hover");
        for (Element withHoverElement : withHoverElements) {
            Element aTag = withHoverElement.getElementsByClass("name").get(0)
                    .select("a[href]").get(0);
            Element priceElement = withHoverElement.getElementsByClass("price_mr").get(0);
            Product product = new Product();
            Elements imageWrappers = withHoverElement.getElementsByClass("img-wrapper");
            if (imageWrappers.size() != 0) {
                Element imageTag = imageWrappers.get(0).getElementsByTag("img").get(0);
                product.setImageUrl(imageTag.attr("src"));
            } else {
                product.setImageUrl("/resources/images/noimage-1.png");
            }
            product.setName(aTag.text());
            product.setUrl(CHIPDIP_LINK + aTag.attr("href"));
            product.setPrice(priceElement.text());
            products.add(product);
        }

        currentProductGroupPage = getNextGroupPage(groupPage);
    }

    private Document getNextGroupPage(Document currentGroupPage) {
        if (currentGroupPage.getElementsByClass("pager").size() != 0) {
            Element rightElement = currentGroupPage.getElementsByClass("right").get(0);
            Elements aTagsInRightElement = rightElement.select("a[href]");
            if (aTagsInRightElement.size() != 0) {
                for (int i = 0; i < 500; i++) {
                    try {
                        return Jsoup.connect(CHIPDIP_LINK + aTagsInRightElement.get(0).attr("href")).get();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
                return null;
            }
        }
        currentProductGroupIndex++;
        return null;
    }
}
