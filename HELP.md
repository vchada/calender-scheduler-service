# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

create table CALENDAR_SCHEDULE
(
    ID                  int auto_increment
        primary key,
    NAME                varchar(255) null,
    RULE_IDS            varchar(500) null,
    CREATED_DT_TM       datetime     null,
    CREATED_USER        varchar(255) null,
    LAST_MODIFIED_USR   varchar(300) null,
    LAST_MODIFIED_DT_TM datetime     null,
    IS_ACTIVE           tinyint(1)   null
);

create table RULE_DEFINITION
(
    RULE_ID             int auto_increment
        primary key,
    RULE_NAME           varchar(255)  null,
    HOLIDAY_TYPE        varchar(255)  null,
    MONTH               varchar(25)   null,
    DAY_OF_MONTH        int           null,
    DAY_OF_WEEK         varchar(25)   null,
    WEEK_OF_MONTH       int           null,
    CUSTOM_DAYS         varchar(1600) null,
    CREATED_DT_TM       datetime      null,
    CREATED_USER        varchar(255)  null,
    LAST_MODIFIED_DT_TM datetime      null,
    LAST_MODIFIED_USR   varchar(255)  null,
    IS_ACTIVE           tinyint(1)    null
);

