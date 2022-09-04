package com.lifementos.webcrawler.parse;

import java.util.Arrays;
import java.util.List;

public class NaverConstants {

    public static final List<String> jobPositionSeparator = Arrays.asList("역할", "");
    public static final List<String> requiredSkillsSeparator = Arrays.asList("자격요건", "Required Skills", "지원자격", "[필요역량]", "담당 업무");
    public static final List<String> preferredSkillsSeparator = Arrays.asList("우대사항", "Preferred Skills", "우대 사항", "[우대사항]");
    public static final List<String> requiredExperienceSeparator = Arrays.asList("필요한 역량", "");
}
