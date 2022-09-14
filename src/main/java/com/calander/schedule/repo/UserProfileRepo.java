package com.calander.schedule.repo;

import com.calander.schedule.entity.CalendarSchedule;
import com.calander.schedule.entity.UserProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends CrudRepository<UserProfileEntity, String> {

}
