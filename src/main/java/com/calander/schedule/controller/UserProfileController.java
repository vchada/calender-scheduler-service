package com.calander.schedule.controller;

import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.UserProfileEntity;
import com.calander.schedule.repo.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserProfileController
{

    @Autowired
    UserProfileRepo userProfileRepo;

    @PostMapping(value = "/saveUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse persistUser(@RequestBody final UserProfileEntity userProfileEntity) {
         userProfileRepo.save(userProfileEntity);
         return  StatusResponse.builder().message("USER_PERSISTED_SUCCESSFULLY").build();
    }

    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserProfileEntity> getAllUsers()
    {
        return (List<UserProfileEntity>) userProfileRepo.findAll();
    }

    @PutMapping(value = "/updateUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse updateUser(@RequestBody final UserProfileEntity userProfileEntity) {
         UserProfileEntity userProfile = userProfileRepo.findById(userProfileEntity.getUserId()).get();
         if(null!=userProfile)
         {
             userProfile.setUserName(userProfileEntity.getUserName());
             userProfile.setRole(userProfileEntity.getRole());
             userProfile.setDescription(userProfileEntity.getDescription());
             userProfileRepo.save(userProfile);
             //userProfile.setLastModifiedDateAndTime((Date) Calendar.getInstance().getTime());
         }
        return  StatusResponse.builder().message("USER_UPDATED_SUCCESSFULLY").build();
    }

    @DeleteMapping(value = "/deleteUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse deleteUser(@RequestBody final UserProfileEntity userProfileEntity) {
        userProfileRepo.delete(userProfileEntity);
        return  StatusResponse.builder().message("USER_DELETED_SUCCESSFULLY").build();
    }
}
