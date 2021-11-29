package com.cos.book.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1. 통합 테스트 (모든 bean을 IoC에 올리고 테스트)
 * WebEnvironment.MOCK : 실제 톰캣을 올리는게 아니라 다른(모의) 톰캣으로 테스트
 * WebEnvironment.RANDOM_POR : 실제 톰켓(실제 웹환경)으로 테스트
 * AutoConfigureMockMvc : MockMvc IoC에 등록
 * Transactional : 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback해주는 역할
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc; // 얘를 통해 테스트
	
	
}
