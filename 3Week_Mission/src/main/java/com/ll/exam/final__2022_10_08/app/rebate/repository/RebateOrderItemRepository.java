package com.ll.exam.final__2022_10_08.app.rebate.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.exam.final__2022_10_08.app.rebate.entity.RebateOrderItem;

public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem, Long> {
	Optional<RebateOrderItem> findByOrderItemId(long orderItemId);

	List<RebateOrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate);
}
