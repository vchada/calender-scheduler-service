package com.calander.schedule.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.calander.schedule.entity.DateObservance;

@Repository
public interface DateObservanceRepo extends CrudRepository<DateObservance, Long>{
	
	DateObservance findByHolidayType(String holidayType);

}
