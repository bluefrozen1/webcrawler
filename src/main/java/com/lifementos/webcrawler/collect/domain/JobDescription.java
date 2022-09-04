package com.lifementos.webcrawler.collect.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Document(indexName = "job_description")
@Builder
public class JobDescription {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String company;

    @Field(type = FieldType.Text)
    private String jobTitle;

    @Field(type = FieldType.Text)
    private String jobDescription;

    @Field(type = FieldType.Text, fielddata = true)
    private String jobPosition;

    @Field(type = FieldType.Text, fielddata = true)
    private String requiredSkills;

    @Field(type = FieldType.Text, fielddata = true)
    private String preferredSkills;

    @Field(type = FieldType.Text, fielddata = true)
    private String requiredExperience;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @Field(type = FieldType.Date, format = DateFormat.basic_date)
    private LocalDate createdAt;
}
