package com.ll.exam.final__2022_10_08.app.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.exam.final__2022_10_08.app.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}