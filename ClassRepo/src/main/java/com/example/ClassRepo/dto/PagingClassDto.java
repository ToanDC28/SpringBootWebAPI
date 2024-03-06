package com.example.ClassRepo.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PagingClassDto {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "createDate"; // Mặc định sắp xếp theo trường "createdBy"
    private String keyword;
    private String searchType;


    public Pageable getPageable() {
        Integer page = Objects.nonNull(pageNo) ? pageNo : 0;
        Integer size = Objects.nonNull(pageSize) ? pageSize : 100;

        Sort.Direction sortDirection = Objects.nonNull(sort) ? sort : Sort.Direction.ASC;
        String sortBy = Objects.nonNull(sortByColumn) ? sortByColumn : "createDate";

        // Kiểm tra xem trường sortBy có thuộc các trường "name", "dob" và "id" hay không
        if (!Arrays.asList("className", "classCode").contains(sortBy)) {
            sortBy = "createDate"; // Nếu không, sắp xếp theo trường "createdBy" mặc định
        }

        return PageRequest.of(page, size, sortDirection, sortBy);
    }

}
