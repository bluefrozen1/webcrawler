package com.lifementos.webcrawler.parse;

import com.lifementos.webcrawler.MockJobDescription;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RecruitmentNoticeParserTest {

    @Test
    void parseTest() {

        //GIVEN


        for(String jobPositionSeparator : NaverConstants.requiredSkillsSeparator) {
            if(MockJobDescription.jobDescription.indexOf(jobPositionSeparator) > 0) {
                Pattern pattern = Pattern.compile(jobPositionSeparator + "(?<ya>[\\w\\W]+)\\\\# ");
                Matcher matcher = pattern.matcher(MockJobDescription.jobDescription);

                while(matcher.find()) {
                    System.out.println(jobPositionSeparator + " -> matcher! = " + matcher.group(1));
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
    }

}