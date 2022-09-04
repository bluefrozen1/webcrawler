package com.lifementos.webcrawler.collect;

import com.lifementos.webcrawler.collect.domain.JobDescription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDescriptionRepository extends ElasticsearchRepository<JobDescription, String> {

}

