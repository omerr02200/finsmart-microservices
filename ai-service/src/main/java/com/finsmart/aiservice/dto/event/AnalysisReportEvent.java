package com.finsmart.analysisservice.dto.event;

import java.math.BigDecimal;

public record AnalysisReportEvent (
        String userId,
        String category,
        BigDecimal totalAmount,
        String analysisDetail
) {

}
