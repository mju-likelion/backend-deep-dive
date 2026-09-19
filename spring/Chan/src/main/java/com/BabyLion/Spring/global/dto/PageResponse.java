package com.BabyLion.Spring.global.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> contents;
    private long totalElement;
    private int totalPage;
    private int number;
    private int size;
    private boolean last;

    public PageResponse(List<T> contents, long totalElement, int totalPage, int number, int size, boolean last) {
        this.contents = contents;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
        this.number = number;
        this.size = size;
        this.last = last;
    }

    public static <T> PageResponse<T> from(Page<T> page){
        return new PageResponse<T>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isLast());
    }
}
