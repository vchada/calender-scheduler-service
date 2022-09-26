package com.calander.schedule.controller;

import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.User;
import com.calander.schedule.entity.UserProfileEntity;
import com.calander.schedule.repo.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
        userProfileEntity.setPassword(String.valueOf(userProfileEntity.getPassword().hashCode()));
         userProfileRepo.save(userProfileEntity);
         return  StatusResponse.builder().message("USER_PERSISTED_SUCCESSFULLY").build();
    }

    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserProfileEntity> getAllUsers()
    {
        return (List<UserProfileEntity>) userProfileRepo.findAll();
    }

    @GetMapping(value = "/getUserByID/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileEntity getUserById(@PathVariable final String id)
    {
        UserProfileEntity retVal;
        if(userProfileRepo.findById(id).isPresent())
        {
             retVal = userProfileRepo.findById(id).get();
             retVal.setPassword(String.valueOf(retVal.getPassword().hashCode()));
             return retVal;
        }
        else
            return null;
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

    @PostMapping(value = "/validateUser",  consumes = MediaType.APPLICATION_JSON_VALUE,
                                            produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean getAllUsers(@RequestBody final User user)
    {
        boolean retVal = false;
        Optional<UserProfileEntity> userProfile = userProfileRepo.findById(user.getUserID());
        if(userProfile.isPresent())
        {
            if(userProfile.get().getUserId().equalsIgnoreCase(user.getUserID())
            && userProfile.get().getPassword().equalsIgnoreCase(String.valueOf(user.getPassword().hashCode())))
            {
                retVal = true;
            }
        }
        return retVal;
    }

    @PutMapping(value = "/updatePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse updateUser(@RequestBody final User user) {
        UserProfileEntity userProfile = userProfileRepo.findById(user.getUserID()).get();
        if(null!=userProfile)
        {
            userProfile.setPassword(String.valueOf(user.getPassword().hashCode()));
            userProfileRepo.save(userProfile);
            //userProfile.setLastModifiedDateAndTime((Date) Calendar.getInstance().getTime());
        }
        return  StatusResponse.builder().message("USER_UPDATED_SUCCESSFULLY").build();
    }
}
