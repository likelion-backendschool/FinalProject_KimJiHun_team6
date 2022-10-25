package com.ll.exam.final__2022_10_08.app.base.initData;

import java.util.List;

import com.ll.exam.final__2022_10_08.app.cart.entity.CartItem;
import com.ll.exam.final__2022_10_08.app.cart.service.CartService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import com.ll.exam.final__2022_10_08.app.order.service.OrderService;
import com.ll.exam.final__2022_10_08.app.post.service.PostService;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import com.ll.exam.final__2022_10_08.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProdInitData implements InitDataBefore{
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService
    ) {
        return args -> {
            if (initDataDone) {
                return;
            }
            initDataDone = true;
            before(memberService, postService, productService, cartService, orderService);
        };
    }
}
