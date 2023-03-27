package com.alexmisko.service;

import com.alexmisko.pojo.Search;

public interface ElasticSearchService {
    void addSearch(Search search);
}