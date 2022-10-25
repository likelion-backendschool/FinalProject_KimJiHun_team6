package com.ll.exam.final__2022_10_08.app.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.exam.final__2022_10_08.app.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}