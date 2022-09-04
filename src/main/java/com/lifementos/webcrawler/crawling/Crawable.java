package com.lifementos.webcrawler.crawling;

import com.lifementos.webcrawler.crawling.domain.RecruitmentNotice;

import java.util.List;

public interface Crawable {

    public List<RecruitmentNotice> searchJobDescription() throws CrawlingException;
}
