package com.alexmisko.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alexmisko.pojo.Search;
import com.alexmisko.service.SearchService;
import com.alexmisko.vo.Result;

@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("search/user")
    public Result<Search> getSearch(String keyword){
        return Result.success(searchService.getSearch(keyword));
    }

    @GetMapping("search/all/user")
    public Result<List<Search>> getAllSearch(String keyword){
        return Result.success(searchService.getAllSearch(keyword));
    }

    @DeleteMapping("search/admin")
    public Result<Search> deleteAllSearch(){
        searchService.deleteAllSearch();
        return Result.success("删除成功！");
    }
}
