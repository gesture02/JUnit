package com.cos.book.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 단위 테스트 (db관련된 bean이 IoC에 등록)
 * Replace.Any : 가짜 db로 테스트
 * Replace.None : 실제 db로 테스트(통합테스트에서 사용)
 */

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)
@DataJpaTest // Repository들을 다 IoC에 등록해줌
public class BookRepositoryUnitTest {
	
	@Autowired
	private BookRepository bookRepository;
}
