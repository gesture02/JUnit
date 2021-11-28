package com.cos.book.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookService {
	//autowired
	private final BookRepository bookRepository;
	
	@Transactional // 서비스 함수가 종료될 때 commit할지 rollback할지 관리
	public Book save (Book book) {
		return bookRepository.save(book);
	}
	@Transactional(readOnly = true)//변경감지활성화 안됨, update시에 정합성 유지
	public Book findById (Long id) {
		return bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요"));
	}
	@Transactional(readOnly = true)//select시에는 모두 붙이는게 좋다.
	public List<Book> findAll () {
		return bookRepository.findAll();
	}
	@Transactional
	public Book update (Long id, Book book) {
		//더티체킹 update
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요")); //BookEntity 영속화
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());

		return bookEntity;
		//함수 종료 => 트랜잭션 종료 => 영속화 되어있는 데이터를 DB로 갱신(flush) => commit /// 더티체킹
	}
	@Transactional
	public String delete (Long id) {
		bookRepository.deleteById(id);//오류가 터지면 exception을 탐
		return "ok";
	}
}
