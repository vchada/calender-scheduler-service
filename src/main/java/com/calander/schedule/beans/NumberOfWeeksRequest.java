package com.calander.schedule.beans;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class NumberOfWeeksRequest {
    int year;
    Set<Integer> months;
}
