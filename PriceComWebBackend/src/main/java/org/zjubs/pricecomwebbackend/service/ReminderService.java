package org.zjubs.pricecomwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.zjubs.pricecomwebbackend.entity.User;
import org.zjubs.pricecomwebbackend.mapper.UserMapper;
import org.zjubs.pricecomwebbackend.mapper.UsingMapper;
import org.zjubs.pricecomwebbackend.query.Remainder;
import org.zjubs.pricecomwebbackend.utils.EmailUtil;
import org.zjubs.pricecomwebbackend.utils.WphCrawlerUtil;

import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private UsingMapper usingMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailUtil emailUtil;

    @Scheduled(cron = "0 0 0 * * ?")
    public void remind() {
        try {
            List<Remainder> allReminder = usingMapper.getAllReminder();
            for (Remainder reminder: allReminder) {
                if (reminder.getFrom().equals("wph")) {
                    String detailUrl = reminder.getDetailUrl();
                    Double newPrice = WphCrawlerUtil.getSingleItemPrice(detailUrl);
                    if (newPrice != null) {
                        Double lastPrice = reminder.getPrice();
                        if (lastPrice > newPrice) {
                            String description = reminder.getDescription();
                            Integer userId = reminder.getUserId();
                            User user = userMapper.getUserById(userId);
                            String email = user.getEmail();
                            emailUtil.sendRemainderEmail(email,description,lastPrice,newPrice,detailUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
