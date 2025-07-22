package com.henheang.commonapi.components;

import com.kosign.wepoint.common.domain.pointbatch.PointBatch;
import com.kosign.wepoint.common.domain.pointbatch.PointBatchRepository;
import com.kosign.wepoint.common.enums.PointStatus;
import com.kosign.wepoint.common.enums.SourceType;
import com.kosign.wepoint.common.logging.AppLogManager;
import com.kosign.wepoint.common.payload.queue.PointExpiryQueue;
import com.kosign.wepoint.common.rabbitmq.RabbitMQSender;
import com.kosign.wepoint.common.service.AuditLog.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExpiredPointSchedulerService {
    private final PointBatchRepository pointBatchRepository;
    private final RabbitMQSender rabbitMQSender;
    private final AuditLogService auditLogService;
    @Value("${expired-points.scheduler.enable:false}")
    private boolean enabled;

    @Scheduled(cron = "${expired-points.scheduler.cron}")
    public void calculateExpiredPoints(){
        if (!enabled) return;
        AppLogManager.info("[Point Scheduler] Starting expired points calculation");

        try {
            // Find all expired points
            List<PointBatch> expiredPoints = pointBatchRepository.findAllExpiredPoints(PointStatus.NORMAL);

            // Group by biller ID
            Map<String, List<PointBatch>> pointsByProductUserId = expiredPoints.stream()
                    .collect(Collectors.groupingBy(PointBatch::getWalletId));

            AppLogManager.info("[WePoint Scheduler] Found expired points for {} wallet", pointsByProductUserId.size());

            // Process each biller's points
            pointsByProductUserId.forEach((productUserId, points) -> {

                // Calculate regular points (PNT001)
                Long totalRegularPoints = points.stream()
                        .filter(p -> SourceType.POINT.equals(p.getSourceType()))
                        .mapToLong(PointBatch::getRemainingPoint)
                        .sum();

                // Calculate bonus points (PNT002)
                Long totalBonusPoints = points.stream()
                        .filter(p -> SourceType.BONUS.equals(p.getSourceType()))
                        .mapToLong(PointBatch::getRemainingPoint)
                        .sum();

                List<Long> batchIds = points.stream()
                        .map(PointBatch::getId)
                        .collect(Collectors.toList());

                PointExpiryQueue queue = PointExpiryQueue.builder()
                        .walletId(productUserId)
                        .totalRegularPoint(totalRegularPoints)
                        .totalBonusPoint(totalBonusPoints)
                        .expiredBatchIds(batchIds)
                        .expiryDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .build();

                // Send to RabbitMQ
                rabbitMQSender.convertAndCheckExpiredPoint(queue);
            });


        } catch (Exception e) {
            AppLogManager.error("[WePoint Scheduler] Error during point expiry processing", e);
        }
    }
}
