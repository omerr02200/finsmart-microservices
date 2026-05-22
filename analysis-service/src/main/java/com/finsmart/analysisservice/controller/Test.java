package com.finsmart.analysisservice.restcontroller;

import com.finsmart.analysisservice.dto.event.AnalysisReportEvent;
import com.finsmart.analysisservice.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/analysis/sendToAi")
@RequiredArgsConstructor
public class Test {
    
    private final KafkaProducerService producerService;

    @GetMapping
    public void  sendToAiService(AnalysisReportEvent event)
    {
        producerService.sendToAiService(event);
    }
}
