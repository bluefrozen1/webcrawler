package com.lifementos.webcrawler.collect;

import com.lifementos.webcrawler.collect.domain.JobDescription;
import com.lifementos.webcrawler.crawling.CrawlingException;
import com.lifementos.webcrawler.crawling.impl.NaverCrawler;
import com.lifementos.webcrawler.crawling.domain.RecruitmentNotice;
import com.lifementos.webcrawler.parse.RecruitmentNoticeParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class JobDescriptionCollector {

    private final NaverCrawler naverCrawler;
    private final ElasticsearchRepository elasticsearchRepository;
    private final RecruitmentNoticeParser recruitmentNoticeParser;

    public JobDescriptionCollector(
            NaverCrawler naverCrawler,
            ElasticsearchRepository elasticsearchRepository,
            RecruitmentNoticeParser recruitmentNoticeParser
    ) {
        this.naverCrawler = naverCrawler;
        this.elasticsearchRepository = elasticsearchRepository;
        this.recruitmentNoticeParser = recruitmentNoticeParser;
    }

    @PostConstruct
    public void collect() {

        List<RecruitmentNotice> recruitmentNoticess = null;
        try {
            recruitmentNoticess = naverCrawler.searchJobDescription();
        } catch (CrawlingException e) {
            log.error("Crawling error", e);
        }

        if(!CollectionUtils.isEmpty(recruitmentNoticess)) {
            for(RecruitmentNotice recruitmentNotice : recruitmentNoticess) {
                JobDescription jd = recruitmentNoticeParser.toJobDescription(recruitmentNotice);
                elasticsearchRepository.save(jd);
            }
        }
    }
}
