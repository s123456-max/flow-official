package com.alexmisko.service;

import java.util.List;

import com.alexmisko.pojo.Search;

public interface SearchService {

    void addSearch(Search search);

    Search getSearch(String keyword);

    List<Search> getAllSearch(String keyword);

    void deleteAllSearch();
    
}