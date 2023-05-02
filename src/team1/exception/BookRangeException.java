package team1.exception;

import java.text.NumberFormat;

public class BookRangeException extends RuntimeException {

	public BookRangeException() {}

	public BookRangeException(int start) {
		super(start + " 이상의 값을 입력하세요");
	}
	
	public BookRangeException(int start, int end) {
		super(start + "~" + end + "사이의 가격으로 책정하세요");
	}
	
}
