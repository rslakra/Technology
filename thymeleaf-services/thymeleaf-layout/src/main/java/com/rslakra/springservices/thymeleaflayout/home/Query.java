package com.rslakra.springservices.thymeleaflayout.home;

import com.rslakra.appsuite.core.ToString;

import java.util.ArrayList;
import java.util.List;

public class Query {
    
    private String keyword;
    private List<String> results;
    
    /**
     * @param keyword
     * @param results
     */
    public Query(String keyword, List<String> results) {
        this.keyword = keyword;
        this.results = results;
    }
    
    /**
     * @param keyword
     */
    public Query(String keyword) {
        this(keyword, new ArrayList<>());
    }
    
    public Query() {
        this(null);
    }
    
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public List<String> getResults() {
        return results;
    }
    
    public void setResults(List<String> results) {
        this.results = results;
    }
    
    /**
     * @return
     */
    @Override
    public String toString() {
        return ToString.of(Query.class, true)
                .add("keyword", getKeyword())
                .add("results", getResults())
                .toString();
    }
    
}
