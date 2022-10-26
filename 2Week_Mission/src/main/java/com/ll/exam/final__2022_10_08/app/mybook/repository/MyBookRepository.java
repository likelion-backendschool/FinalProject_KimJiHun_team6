package com.ll.exam.final__2022_10_08.app.mybook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.exam.final__2022_10_08.app.mybook.entity.MyBook;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
}
