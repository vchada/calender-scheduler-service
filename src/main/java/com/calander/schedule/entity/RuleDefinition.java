package com.calander.schedule.entity;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Month;

import javax.persistence.*;

import com.calander.schedule.beans.Status;
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
	private Integer id;
	
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
	@Lob
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

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "YEAR")
	private String year;

	@Column(name = "DISPLAY_NAME")
	private String displayName;

	@Column(name = "IS_ACTIVE")
	@Enumerated(EnumType.STRING)
	private Status isActive;

}
