package com.calander.schedule.entity;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "RULE_DEFINITION")
public class RuleDefinition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RULE_ID")
	private Long id;
	
	@Column(name = "HOLIDAY_TYPE")
	private String holidayType;
	
	@Column(name = "MONTH")
	@Enumerated(EnumType.STRING)
	private Month month;
	
	@Column(name = "DAY_OF_MONTH")
	private int dayOfTheMonth;
	
	@Column(name = "DAY_OF_WEEK")
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfTheWeek;
	
	@Column(name = "WEEK_OF_MONTH")
	private int weekOfTheMonth;

	@Column(name = "CUSTOM_DAYS")
	private String customDays;

	@Column(name = "CREATED_DT_TM")
	@CreationTimestamp
	private Date createdDateAndTime;

	@Column(name = "CREATED_USER")
	private String createdUser;

	@Column(name = "LAST_MODIFIED_DT_TM")
	@UpdateTimestamp
	private Date lastModifiedDateAndTime;

	@Column(name = "LAST_MODIFIED_USR")
	private String lastModifiedUser;

	@Column(name = "IS_ACTIVE")
	private int isActive;

}
