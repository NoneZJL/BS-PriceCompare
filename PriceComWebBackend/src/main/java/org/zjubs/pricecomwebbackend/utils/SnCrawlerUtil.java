package org.zjubs.pricecomwebbackend.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.zjubs.pricecomwebbackend.entity.Good;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnCrawlerUtil {
    private static final String BASE_SN_URL = "https://search.suning.com/";

    public static List<Good> search(String keyword) {
        String url = BASE_SN_URL + keyword + "/";
        try {
//            URL resource = SnCrawlerUtil.class.getClassLoader().getResource("msedgedriver.exe");
//            if (resource == null) {
//                throw new IllegalStateException("msedgedriver.exe not found in resources");
//            }
            // 设置 EdgeDriver 路径
//            System.setProperty("webdriver.edge.driver", resource.getPath());

            // 创建 EdgeOptions 对象
            EdgeOptions options = new EdgeOptions();
//            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
            options.addArguments("--headless=old"); // 无头模式
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");  // 解决某些沙箱问题
            options.addArguments("--disable-dev-shm-usage");  // 解决 Docker 中的内存问题

            // 创建 WebDriver 对象
            WebDriver driver = new EdgeDriver(options);

            // 打开页面
            driver.get(url);

            // 等待页面加载完成
            Thread.sleep(5000); // 等待5秒，确保页面加载完成

            // 获取页面源代码
            String pageSource = driver.getPageSource();

            // 关闭 WebDriver
            driver.quit();

            // 解析页面源代码
            Document document = Jsoup.parse(pageSource);
            return parseProducts(document, keyword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static List<Good> parseProducts(Document document, String queryName) {
        try {
            // 选择商品列表的容器
            Elements ul = document.getElementsByClass("general clearfix");
            Elements liList = ul.select("li");
            List<Good> goodList = new ArrayList<Good>();
//            int i = 0;
            for (Element li : liList) {
                // 获取商品名称
                String productName = li.select(".title-selling-point a").text();

                // 获取商品价格
                String productPrice = li.select(".def-price").text();
                if (productPrice == null || productPrice.isEmpty()) {
                    continue;
                }
                String price = extractNumber(productPrice);

                // 获取商品链接
                String productLink = li.select(".res-img a").attr("href");
                System.out.println(productLink);
                // 获取商品图片链接
                String productImage = li.select(".res-img img").attr("src");
                // 获取商品评价数量
                // String productReviews = li.select(".info-evaluate i").text();
                // 获取商品自营标识
                // String productSelf = li.select(".store-stock .zy").text();
                Good good = new Good();
                good.setQueryName(queryName);
                good.setDescription(productName);
                good.setImg(productImage);
                if (!price.isEmpty()) {
                    good.setPrice(Double.valueOf(price));
                } else {
                    good.setPrice(0.0); // 设置默认价格
                }
                good.setShopName(productName);
                good.setDetailUrl(productLink);
                goodList.add(good);
//                i++;
//                if (i == 16) {
//                    break;
//                }
            }
            return goodList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static String extractNumber(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.replaceAll("[^0-9.]", "");
    }

//    public static void search(String keyword) {
//        String url = BASE_SN_URL + keyword + "/";
//        try {
//            Document document = Jsoup.connect(url).get();
////            System.out.println(document);
//            Elements ul = document.getElementsByClass("general clearfix");
//            Elements liList = ul.select("li");
//            for (Element li : liList) {
//                System.out.println(li);
//                // 获取商品名称
//                String productName = li.select(".title-selling-point a").text();
//                // 获取商品价格
//                String productPrice = li.select(".def-price").text();
//                // 获取商品链接
//                String productLink = li.select(".res-img a").attr("href");
//                // 获取商品图片链接
//                String productImage = li.select(".res-img img").attr("src");
//                // 获取商品评价数量
//                String productReviews = li.select(".info-evaluate i").text();
//                // 获取商品自营标识
//                String productSelf = li.select(".store-stock .zy").text();
//                System.out.println("商品名称: " + productName);
//                System.out.println("商品价格: " + productPrice);
//                System.out.println("商品链接: " + productLink);
//                System.out.println("商品图片: " + productImage);
//                System.out.println("商品评价: " + productReviews);
//                System.out.println("商品自营: " + productSelf);
//                System.out.println("=============================");
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
