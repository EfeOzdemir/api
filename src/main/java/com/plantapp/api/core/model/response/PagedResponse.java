package com.plantapp.api.core.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
}
