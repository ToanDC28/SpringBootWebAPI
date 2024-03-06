package com.example.UserRepo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestWrapper {
    private PageRequestDto searchRequest;
    private UserDto userDto;
}
