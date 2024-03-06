package com.example.UserRepo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
public class PageRequestDto {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "createdAt"; // Mặc định sắp xếp theo trường "createdBy"
    private String keyword;
    private String searchType;

    public Pageable getPageable() {
        Integer page = Objects.nonNull(pageNo) ? pageNo : 0;
        Integer size = Objects.nonNull(pageSize) ? pageSize : 10;

        Sort.Direction sortDirection = Objects.nonNull(sort) ? sort : Sort.Direction.ASC;
        String sortBy = Objects.nonNull(sortByColumn) ? sortByColumn : "createdAt";

        // Kiểm tra xem trường sortBy có thuộc các trường "name", "dob" và "id" hay không
        if (!Arrays.asList("name", "dob", "id").contains(sortBy)) {
            sortBy = "createdAt"; // Nếu không, sắp xếp theo trường "createdBy" mặc định
        }

        return PageRequest.of(page, size, sortDirection, sortBy);
    }

}