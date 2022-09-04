package com.lifementos.webcrawler.crawling.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class RecruitmentNotice {
    private String company;
    private String title;
    private String content;
    private LocalDateTime collectedAt;
}
