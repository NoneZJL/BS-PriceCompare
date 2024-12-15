package org.zjubs.pricecomwebbackend.CrawerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zjubs.pricecomwebbackend.entity.Good;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.service.JdService;
import org.zjubs.pricecomwebbackend.utils.JDCrawlerUtil;
import org.zjubs.pricecomwebbackend.utils.SnCrawlerUtil;
import org.zjubs.pricecomwebbackend.utils.WphCrawlerUtil;

import java.util.List;

@SpringBootTest
public class JsoupTest {
    @Autowired
    private JdService jdService;


    @Test
    public void testGetUrl() {
        ApiResult apiResult = jdService.searchAndInsert("餐巾纸");
        if (apiResult.ok) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }

    @Test
    public void snTest() {
        List<Good> search = SnCrawlerUtil.search("电脑");
        System.out.println(search);
    }

    @Test
    public void wphTest() {
        List<Good> search = WphCrawlerUtil.search("手机");
        System.out.println(search);
    }

    @Test
    public void singleTest() {
        String url = "https://www.xiaomiyoupin.com/detail?gid=153253";
        Double price = WphCrawlerUtil.getSingleItemPrice(url);
        System.out.println("获取的价格为 " + price + " 元");
    }

    @Test
    public void singleGoodTest() {
        JDCrawlerUtil.getSingleGoodPrice("https://item.jd.com/100115333286.html");
    }
}
