package com.ll.exam.final__2022_10_08.app.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.exam.final__2022_10_08.app.cash.entity.CashLog;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}