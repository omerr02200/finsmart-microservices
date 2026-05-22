package com.finsmart.analysisservice.controller;

import com.finsmart.analysisservice.dto.event.AnalysisReportEvent;
import com.finsmart.analysisservice.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/analysis/sendToAi")
@RequiredArgsConstructor
public class Test {
    
    private final KafkaProducerService producerService;

    @GetMapping
    public void  sendToAiService(@RequestBody AnalysisReportEvent event)
    {
        producerService.sendToAiService(event);
    }
}
