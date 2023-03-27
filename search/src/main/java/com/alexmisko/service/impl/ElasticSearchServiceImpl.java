package com.alexmisko.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.pojo.Search;
import com.alexmisko.repository.SearchRepository;
import com.alexmisko.service.ElasticSearchService;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService{
    @Autowired
    private SearchRepository searchRepository;

    public void addSearch(Search search){
        searchRepository.save(search);
    }

    public Search getSearch(String keyword){
        return searchRepository.findByTitleLike(keyword);
    }

    public void deleteAllSearch(){
        searchRepository.deleteAll();
    }
}
