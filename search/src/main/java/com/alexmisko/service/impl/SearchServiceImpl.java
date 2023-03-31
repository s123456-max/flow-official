package com.alexmisko.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.pojo.Search;
import com.alexmisko.repository.SearchRepository;
import com.alexmisko.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService{
    @Autowired
    private SearchRepository searchRepository;

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
}
