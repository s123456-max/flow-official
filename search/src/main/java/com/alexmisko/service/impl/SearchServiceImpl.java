package com.alexmisko.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.alexmisko.feign.UserInfoFeign;
import com.alexmisko.pojo.Search;
import com.alexmisko.repository.SearchRepository;
import com.alexmisko.service.SearchService;
import com.alexmisko.vo.Result;
import com.alexmisko.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService{

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private UserInfoFeign userInfoFeign;


    public void addSearch(Search search){
        searchRepository.save(search);
    }

    public Search getSearch(String keyword){
        return searchRepository.findByTagsLike(keyword);
    }

    public List<Search> getAllSearch(String keyword){
        return searchRepository.findAllByTagsLike(keyword);
    }

    public void deleteAllSearch(){
        searchRepository.deleteAll();
    }

    @Override
    public List<Map<String, Object>> getHighLightSearch(String keyword, Integer pageNo, Integer pageSize){
        SearchRequest searchRequest = new SearchRequest("search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo - 1);
        sourceBuilder.size(pageSize);
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "description", "tags", "username");
        sourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(sourceBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 高亮显示
        String[] array = {"description", "tags", "username"};
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String key : array){
            highlightBuilder.fields().add(new HighlightBuilder.Field(key));
        }
        // 多字段高亮将这个置为false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        // 执行搜索
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for(SearchHit hit : searchResponse.getHits()){
            // 处理高亮字段
            Map<String, HighlightField> highLightBuilderFields = hit.getHighlightFields();
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            log.info("sourceMap: [{}]", sourceMap);
            // 增加用户信息
            if (sourceMap.get("userId") != null){
                Result<UserInfo> result = userInfoFeign.getUserInfo(Long.valueOf((sourceMap.get("userId")).toString()));
                sourceMap.put("userInfo", result.getData());
            } 
            for(String key : array){
                HighlightField field = highLightBuilderFields.get(key);
                log.info("field: [{}]", field);
                if (field != null){
                    Text[] fragments = field.fragments();
                    String str = Arrays.toString(fragments);
                    str = str.substring(1, str.length() - 1);
                    log.info("key and str: [{}] [{}]", key, str);
                    sourceMap.put(key, str);
                    // 标记命中字段
                    sourceMap.put("hit", key);
                }
            }
            arrayList.add(sourceMap);
        }
        return arrayList; 
    }
}
