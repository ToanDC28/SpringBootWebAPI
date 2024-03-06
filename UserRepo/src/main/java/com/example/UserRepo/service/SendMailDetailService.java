package com.example.UserRepo.service;

import com.example.UserRepo.dto.PageRequestDto;
import com.example.UserRepo.entity.SendMailDetail;
import com.example.UserRepo.repository.SendMailInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendMailDetailService {
    @Autowired
    private SendMailInfoRepository sendMailInfoRepository;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    public Page<SendMailDetail> getAllEmailSend(PageRequestDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by(dto.getSort(), "id"));
        Page<SendMailDetail> list = sendMailInfoRepository.findAll(pageable);
//        redisTemplate.opsForValue().set("mail-detail", list);
        return new PageImpl<>(list.getContent(), pageable, list.getTotalElements());
    }
}
