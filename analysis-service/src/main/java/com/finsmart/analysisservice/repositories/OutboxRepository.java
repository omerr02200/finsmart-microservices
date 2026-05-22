package com.finsmart.analysisservice.repositories;

import com.finsmart.analysisservice.entities.Outbox;
import com.finsmart.analysisservice.entities.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
    List<Outbox> findByStatus(OutboxStatus outboxStatus);
}