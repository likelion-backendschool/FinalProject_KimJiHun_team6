package com.ll.exam.final__2022_10_08.app.mybook.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.mybook.entity.MyBook;
import com.ll.exam.final__2022_10_08.app.mybook.repository.MyBookRepository;
import com.ll.exam.final__2022_10_08.app.order.entity.OrderItem;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyBookService {
	private final MyBookRepository myBookRepository;

	public void applyItem(Member buyer, List<OrderItem> orderItems) {
		for (OrderItem orderItem : orderItems) {
			saveItem(buyer, orderItem.getProduct());
		}
	}

	public void saveItem(Member buyer, Product product) {
		MyBook myBook = MyBook.builder()
			.member(buyer)
			.product(product)
			.build();
		myBookRepository.save(myBook);
	}
}
