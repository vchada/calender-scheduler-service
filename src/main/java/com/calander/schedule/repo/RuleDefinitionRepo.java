package com.calander.schedule.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.calander.schedule.entity.RuleDefinition;

import java.util.List;

@Repository
public interface RuleDefinitionRepo extends CrudRepository<RuleDefinition, Long>{
	
	RuleDefinition findByHolidayType(String holidayType);

	List<RuleDefinition> findByIsActive(int isActive);

}
