package com.example.UserRepo.service;

import com.example.UserRepo.entity.Permission;
import com.example.UserRepo.exceptions.ResourceNotFoundException;
import com.example.UserRepo.repository.PermissionRepository;
import com.example.UserRepo.request.PermissionRequest;
import com.example.UserRepo.reponses.ResponseObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class PermissionService {
    private static Logger logger = LogManager.getLogger(PermissionService.class);

    @Autowired
    private PermissionRepository permissionRepository;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

//    @Autowired
//    private Properties properties;

    public ResponseEntity<ResponseObject> setPermission(List<PermissionRequest> request) {
        List<Permission> list = new ArrayList<>();
        for (PermissionRequest p : request) {
            list.add(permissionRepository.findById(p.getPermissionID()).orElseThrow(
                    () -> new ResourceNotFoundException("Permission is not found")
            ));
        }
        for (Permission p : list) {
            try {
                logger.info("Set Permission");
                p.setSyllabus(p.getSyllabus());
                p.setTrainingProgram(p.getTrainingProgram());
                p.setClassManagement(p.getClassManagement());
                p.setLearningMaterial(p.getLearningMaterial());
                p.setUserManagement(p.getUserManagement());
                permissionRepository.save(p);
//                redisTemplate.delete(properties.getProperty("redis-permission"));
            } catch (Exception e) {
                logger.error(e + ": Fail to set permission.");
                return ResponseEntity.badRequest().body(new ResponseObject(1, "Failed to set permission", e.getMessage()));
            }
        }
        return ResponseEntity.ok(new ResponseObject(0, "Permission set successfully", null));
    }
}
