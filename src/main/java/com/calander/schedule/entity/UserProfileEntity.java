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
@Table(name = "USER_PROFILE")
public class UserProfileEntity {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_DT_TM")
    @CreationTimestamp
    private Date createdDateAndTime;

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column(name = "LAST_MODIFIED_DT_TM")
    @UpdateTimestamp
    private Date lastModifiedDateAndTime;

    @Column(name = "LAST_MODIFIED_USER")
    private String lastModifiedUser;

    @Column(name = "PASSWORD")
    private String password;


}
