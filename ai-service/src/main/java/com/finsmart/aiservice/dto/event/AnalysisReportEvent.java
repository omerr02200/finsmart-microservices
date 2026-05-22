package com.finsmart.aiservice.dto.event;

import java.math.BigDecimal;

public record AnalysisReportEvent (
        String userId,
        String category,
        BigDecimal totalAmount,
        String analysisDetail
) {

}