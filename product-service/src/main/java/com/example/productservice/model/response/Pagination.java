package com.example.productservice.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class Pagination {

    private long totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;

//    public Pagination(long totalElements, int currentPage, int pageSize, int totalPages) {
//        this.totalElements = totalElements;
//        this.currentPage = currentPage;
//        this.pageSize = pageSize;
//        this.totalPages = totalPages;
//    }

    public Pagination(Page<?> page) {
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
    }
}
