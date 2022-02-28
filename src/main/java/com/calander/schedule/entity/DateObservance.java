package com.calander.schedule.entity;

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

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "DATE_OBSERVANCE")
public class DateObservance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "holidayType")
	private String holidayType;
	
	@Column(name = "month")
	@Enumerated(EnumType.STRING)
	private Month month;
	
	@Column(name = "dayOfTheMonth")
	private int dayOfTheMonth;
	
	@Column(name = "dayOfTheWeek")
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfTheWeek;
	
	@Column(name = "weekOfTheMonth")
	private int weekOfTheMonth;

}
