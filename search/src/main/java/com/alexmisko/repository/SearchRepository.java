package com.alexmisko.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.alexmisko.pojo.Search;

public interface SearchRepository extends ElasticsearchRepository<Search, Long>{
    Search findByTagsLike(String keyword);
    List<Search> findAllByTagsLike(String keyword);
}
