package com.example.UserRepo.service;

import com.example.UserRepo.entity.Permission;
import com.example.UserRepo.entity.Role;
import com.example.UserRepo.enums.UserRole;
import com.example.UserRepo.exceptions.ResourceNotFoundException;
import com.example.UserRepo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Properties properties;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;


    public Object getAll(){
//        if(redisTemplate.hasKey(properties.getProperty("redis-permission"))){
//            return redisTemplate.opsForValue().get(properties.getProperty("redis-permission"));
//        }
        List<Role> list = roleRepository.findAll();
//        redisTemplate.opsForValue().set(properties.getProperty("redis-permission"), list);
        return list;
    }

    public Role getRoleByRoleName(UserRole roleName){
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(
                ()-> new ResourceNotFoundException("The Role is not found")
        );
    }

}
