package com.rslakra.springservices.thymeleaflayout.home.entity;

import java.util.List;
import java.util.StringJoiner;

public class Query {

    private String keyword;
    private List<String> results;

    public Query() {
    }

    public Query(String keyword, List<String> results) {
        this.keyword = keyword;
        this.results = results;
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
        return new StringJoiner(",")
                .add("Query <keyword=" + getKeyword())
                .add(", results=" + getResults())
                .add(">")
                .toString();
    }

}
