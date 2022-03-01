package com.calander.schedule.repo;

import com.calander.schedule.entity.CalendarSchedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalenderScheduleRepo extends CrudRepository<CalendarSchedule, Long> {
}
