package com.upc.pre.peaceapp.reports.infrastructure.external.messaging;

import com.upc.pre.peaceapp.reports.domain.events.ReportDeletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class ReportEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.exchange.report}")
    private String exchange;

    @Value("${app.broker.routing-key.report.deleted}")
    private String routingKey;

    public ReportEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReportDeleted(ReportDeletedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        System.out.println("✅ ReportDeletedEvent sent to Message Broker: " + event.getReportId());
    }
}
