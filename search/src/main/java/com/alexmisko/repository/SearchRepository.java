package com.alexmisko.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.alexmisko.pojo.Search;

public interface SearchRepository extends ElasticsearchRepository<Search, Long>{
    Search findByTitleLike(String keyword);
}
