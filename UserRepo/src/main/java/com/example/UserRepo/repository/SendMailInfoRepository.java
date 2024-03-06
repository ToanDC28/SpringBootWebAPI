package com.example.UserRepo.repository;

import com.example.UserRepo.entity.SendMailDetail;
import com.example.UserRepo.entity.User;
import com.example.UserRepo.enums.Action;
import com.example.UserRepo.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendMailInfoRepository extends JpaRepository<SendMailDetail, Integer> {

    Page<SendMailDetail> findAll(Pageable pageable);

    SendMailDetail findSendMailDetailByEmailAndData(String email, String data);

    SendMailDetail findSendMailDetailByEmailAndAction(String email, Action action);
}
