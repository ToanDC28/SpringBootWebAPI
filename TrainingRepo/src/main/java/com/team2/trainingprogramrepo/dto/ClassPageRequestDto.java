package com.team2.trainingprogramrepo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
public class ClassPageRequestDto {
    private Integer pageNo = 0;
    private Integer pageSize = 100;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "createDate"; // Default sorted by "createdBy"
    private String keyword;
    private String searchType;

    public Pageable getPageable() {
        Integer page = Objects.nonNull(pageNo) ? pageNo : 0;
        Integer size = Objects.nonNull(pageSize) ? pageSize : 100;

        Sort.Direction sortDirection = Objects.nonNull(sort) ? sort : Sort.Direction.ASC;
        String sortBy = Objects.nonNull(sortByColumn) ? sortByColumn : "createDate";

        if (!Arrays.asList("className", "classCode").contains(sortBy)) {
            sortBy = "createDate";
        }

        return PageRequest.of(page, size, sortDirection, sortBy);
    }
}
