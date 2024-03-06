package com.example.ClassRepo.request;

import com.example.ClassRepo.dto.ClassDto;
import com.example.ClassRepo.dto.PagingClassDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestWrapper {
    private PagingClassDto searchRequest;
    private ClassDto classDto;
}
