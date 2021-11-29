package com.cos.book.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

//2. 유닛 테스트(Controller 관련 로직만 띄우기 : Filter, ControllerAdvice 등)
@WebMvcTest //JUnit5에서는 WebMvcTest안에 @ExtendWith(SptringExtension.class) 포함 //이 파일을 스프링 환경으로 확장하고싶을 때 붙임
public class BookControllerUnitTest {

}
