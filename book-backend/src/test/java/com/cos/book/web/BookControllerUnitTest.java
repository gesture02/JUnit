package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//2. 유닛 테스트(Controller 관련 로직만 띄우기 : Filter, ControllerAdvice 등)

@Slf4j
@WebMvcTest //JUnit5에서는 WebMvcTest안에 @ExtendWith(SptringExtension.class) 포함 //이 파일을 스프링 환경으로 확장하고싶을 때 붙임
public class BookControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean // BookService가 IoC 환경에 등록됨 (가짜 service : 진짜를 쓰면 repository랑 다 연결되야함)
	private BookService bookService;
	
	// BDDMockito 패턴 : given, when, then
	@Test
	public void save_test() throws Exception {
		log.info("save_test() 시작 =========================");
		Book book = new Book(null, "spring", "cos");
		
		//given (테스트를 하기 위한 준비)
		String content = new ObjectMapper().writeValueAsString(book); // object to json
		when(bookService.save(book)).thenReturn(new Book(1L, "spring", "cos"));
		
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
		books.add(new Book(1L, "spring", "cos"));
		books.add(new Book(2L, "react", "cos"));
		
		when(bookService.findAll()).thenReturn(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				);
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$.[0].title").value("spring"))
			.andDo(MockMvcResultHandlers.print());
			
	}
	
	@Test
	public void findById_test() throws Exception {
		// given
		Long id = 1L;
		when(bookService.findById(id)).thenReturn(new Book(1L, "spring", "cos"));
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("spring"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_test() throws Exception {
		//given
		
		Long id = 1L;
		Book book= new Book(null, "aaa", "cos");
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookService.update(id, book)).thenReturn(new Book(1L, "aaa","cos"));
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
				
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("aaa"))
			.andDo(MockMvcResultHandlers.print());
			
	}
	
	@Test
	public void delete_test() throws Exception {
		//given
		Long id = 1L;
		when(bookService.delete(id)).thenReturn("ok");
		
		//when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id)
				.accept(MediaType.TEXT_PLAIN));
				
		//then
		resultAction
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		//응답받는게 text면 이렇게 해야함
		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		assertEquals("ok",result);
	}
}
