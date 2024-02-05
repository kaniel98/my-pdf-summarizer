package com.personal.pdfsummarizer.external.service;

import com.personal.pdfsummarizer.external.models.request.ExternalSummaryRequest;
import com.personal.pdfsummarizer.external.models.response.ExternalSummaryResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "external-service-llm", url = "${external-service.url}")
public interface ExternalService {
    @RequestMapping(method = RequestMethod.POST, value = "", produces = "application/json")
    Mono<ExternalSummaryResponse> generateSummary(@RequestBody ExternalSummaryRequest request);

}