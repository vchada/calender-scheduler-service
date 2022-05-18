package com.calander.schedule.repo;

import com.calander.schedule.beans.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.calander.schedule.entity.RuleDefinition;

import java.util.List;

@Repository
public interface RuleDefinitionRepo extends CrudRepository<RuleDefinition, Integer>{

	List<RuleDefinition> findByHolidayType(String holidayType);

	List<RuleDefinition> findByIsActive(Status isActive);

}
