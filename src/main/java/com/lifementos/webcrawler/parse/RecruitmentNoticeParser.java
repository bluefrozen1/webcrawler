package com.lifementos.webcrawler.parse;

import com.lifementos.webcrawler.collect.domain.JobDescription;
import com.lifementos.webcrawler.crawling.domain.RecruitmentNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class RecruitmentNoticeParser {

    public JobDescription toJobDescription(RecruitmentNotice recruitmentNotice) {

        String content = recruitmentNotice.getContent();

        for(String jobPositionSeparator : NaverConstants.jobPositionSeparator) {
            if(content.indexOf(jobPositionSeparator) > 0) {
                Pattern pattern = Pattern.compile(jobPositionSeparator + "(.*?)\n");
                Matcher matcher = pattern.matcher(content);

                while(matcher.find()) {
                    log.info("matcher! = " + matcher.group(1));
                    if(matcher.group(1) == null) {
                        break;
                    }
                }
            }
        }

        String jobPosition = "";
        String requiredSkills = "";
        String preferredSkills = "";
        String requiredExperience = "";






        return JobDescription.builder()
                .id(recruitmentNotice.getCompany() + "_" + recruitmentNotice.getTitle())
                .jobTitle(recruitmentNotice.getTitle())
                .jobDescription(recruitmentNotice.getContent())
                .jobPosition(jobPosition)
                .requiredSkills(requiredSkills)
                .preferredSkills(preferredSkills)
                .requiredExperience(requiredExperience)
                .createdAt(recruitmentNotice.getCollectedAt().toLocalDate()).build();
    }
}
