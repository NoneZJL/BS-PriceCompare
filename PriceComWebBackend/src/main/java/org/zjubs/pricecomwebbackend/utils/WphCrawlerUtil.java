package org.zjubs.pricecomwebbackend.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zjubs.pricecomwebbackend.entity.Good;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WphCrawlerUtil {
    //    private static final String BASE_URL = "https://category.vip.com/suggest.php?keyword=";
    private static final String BASE_URL = "https://www.xiaomiyoupin.com/search?keyword=";
//    private static final String BASE_URL = "https://you.163.com/search?keyword=";

    public static List<Good> search(String name) {
        String url = BASE_URL + name;
        List<Good> goodList = new ArrayList<Good>();
        try {
            // 创建 EdgeOptions 对象
            EdgeOptions options = new EdgeOptions();
//            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
            options.addArguments("--headless=old"); // 无头模式
            WebDriver driver = new EdgeDriver(options);
            driver.get(url);
            Thread.sleep(5000);
            // 获取页面源代码
            String pageSource = driver.getPageSource();
            // 关闭 WebDriver
            driver.quit();
            Document document = Jsoup.parse(pageSource);
            Elements goodsItems = document.select(".pro-item");
            for (Element goodsItem : goodsItems) {
                // 提取商品名称
                String productName = goodsItem.select(".pro-name").attr("title");
                // 提取商品描述
//                String productDesc = goodsItem.select(".pro-desc").attr("title");
                // 提取商品价格
                String priceText = goodsItem.select(".pro-price .m-num").text();
                double price = Double.parseDouble(priceText);
                // 提取图片路径
                String imageUrl = goodsItem.select(".pro-img img").attr("src");
                if (imageUrl.isEmpty()) {
                    continue;
                }
                // 提取商品链接
                String productLink = goodsItem.attr("data-src");
//                System.out.println("-------------------------");
//                System.out.println("名称 = " + productName);
//                System.out.println("描述 = " + productDesc);
//                System.out.println("价格 = " + price);
//                System.out.println("图片路径 = " + imageUrl);
//                System.out.println("商品链接 = " + productLink);
                // 输出商品信息
                Good good = new Good();
                good.setQueryName(name);
                good.setDescription(productName);
                good.setImg(imageUrl);
                good.setDetailUrl(productLink);
                good.setPrice(price);
                goodList.add(good);
            }
            return goodList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Double getSingleItemPrice(String url) {
        try {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless=old"); // 无头模式
            WebDriver driver = new EdgeDriver(options);
            driver.get(url);
            Thread.sleep(5000);
            String pageSource = driver.getPageSource();
            driver.quit();
            Document document = Jsoup.parse(pageSource);
            Elements priceElements = document.select(".price .value");
            if (!priceElements.isEmpty()) {
                String price = priceElements.first().text();
                System.out.println("商品价格: " + price);
                return Double.valueOf(price);
            } else {
                System.out.println("未找到价格信息");
                return null;
            }
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
}
