package com.cos.book.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.Service.BookService;
import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 1. 통합 테스트 (모든 bean을 IoC에 올리고 테스트)
 * WebEnvironment.MOCK : 실제 톰캣을 올리는게 아니라 다른(모의) 톰캣으로 테스트
 * WebEnvironment.RANDOM_POR : 실제 톰켓(실제 웹환경)으로 테스트
 * AutoConfigureMockMvc : MockMvc IoC에 등록
 * Transactional : 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback해주는 역할
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc; // 얘를 통해 테스트
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach//실행 전에 무조건 실행되는 것, 여기서 시행되기 전에 서로 독립적으로 만들어주면된다.
	public void init() {//ALTER TABLE book AUTO_INCREMENT = 1
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
	// BDDMockito 패턴 : given, when, then
	@Test
	public void save_test() throws Exception {
		log.info("save_test() 시작 =========================");
		Book book = new Book(null, "spring", "cos");
		
		//given (테스트를 하기 위한 준비)
		String content = new ObjectMapper().writeValueAsString(book); // object to json
		
		
		//when (테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				);
		
		//then (검증)
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("spring"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void findAll_test() throws Exception {
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "spring", "cos"));
		books.add(new Book(null, "react", "cos"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$.[0].title").value("spring"))
			.andDo(MockMvcResultHandlers.print());
			
	}
}
