package br.com.voluntplus.volunteerservices.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ServiceReviewStatisticsProvider {

	Map<UUID, ServiceReviewStatistics> findByServiceIds(Set<UUID> serviceIds);
}
