package com.test.myproject.core.dto;

public class ReqQueryParam {
    public int page;
    public int size;
    public String sort;

    public ReqQueryParam(int page, int size, String sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
}
