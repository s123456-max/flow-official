package com.alexmisko.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.alexmisko.pojo.Search;

public interface SearchService {

    void addSearch(Search search);

    Search getSearch(String keyword);

    List<Search> getAllSearch(String keyword);

    void deleteAllSearch();

    List<Map<String, Object>> getHighLightSearch(String keyword, Integer pageNo, Integer pageSize);
    
}