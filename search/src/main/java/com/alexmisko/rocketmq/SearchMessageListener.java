package com.alexmisko.rocketmq;

import java.util.Date;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.feign.UserFeign;
import com.alexmisko.pojo.Search;
import com.alexmisko.service.SearchService;
import com.alexmisko.vo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "consumer-search", topic = "message_search")
public class SearchMessageListener implements RocketMQListener<Search>{

    @Autowired
    SearchService searchService;

    @Autowired
    UserFeign userFeign;
    
    @Override
    public void onMessage(Search search){
        log.info("message_search: [{}] [{}]", search, search.getUserId());
        User user = userFeign.getUser(search.getUserId()).getData();
        log.info("user: [{}]", user);
        search.setTags(search.getTags());
        search.setUsername(user.getUsername());
        search.setCreateTime(new Date());
        search.setUpdateTime(new Date());
        searchService.addSearch(search);
        log.info("添加成功！");
    }
}
