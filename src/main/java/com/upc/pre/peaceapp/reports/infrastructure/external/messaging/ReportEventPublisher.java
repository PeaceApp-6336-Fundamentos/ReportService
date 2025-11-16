package com.upc.pre.peaceapp.reports.infrastructure.external.messaging;

import com.upc.pre.peaceapp.reports.domain.events.ReportApprovedEvent;
import com.upc.pre.peaceapp.reports.domain.events.ReportCreatedEvent;
import com.upc.pre.peaceapp.reports.domain.events.ReportDeletedEvent;
import com.upc.pre.peaceapp.reports.domain.events.ReportRejectedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReportEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.exchange.report}")
    private String exchange;

    @Value("${app.broker.routing-key.report.created}")
    private String createdRoutingKey;

    @Value("${app.broker.routing-key.report.approved}")
    private String approvedRoutingKey;

    @Value("${app.broker.routing-key.report.rejected}")
    private String rejectedRoutingKey;

    @Value("${app.broker.routing-key.report.deleted}")
    private String deletedRoutingKey;

    public ReportEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // -----------------------------
    // EVENTS
    // -----------------------------

    public void publishReportCreated(ReportCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, createdRoutingKey, event);
        System.out.println("📘 ReportCreatedEvent sent: " + event.getReportId());
    }

    public void publishReportApproved(ReportApprovedEvent event) {
        rabbitTemplate.convertAndSend(exchange, approvedRoutingKey, event);
        System.out.println("📗 ReportApprovedEvent sent: " + event.getReportId());
    }

    public void publishReportRejected(ReportRejectedEvent event) {
        rabbitTemplate.convertAndSend(exchange, rejectedRoutingKey, event);
        System.out.println("📕 ReportRejectedEvent sent: " + event.getReportId());
    }

    public void publishReportDeleted(ReportDeletedEvent event) {
        rabbitTemplate.convertAndSend(exchange, deletedRoutingKey, event);
        System.out.println("🗑️ ReportDeletedEvent sent: " + event.getReportId());
    }
}
