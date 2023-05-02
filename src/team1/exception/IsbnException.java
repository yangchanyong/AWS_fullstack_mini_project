package team1.exception;

public class IsbnException extends RuntimeException {

	public IsbnException() {}
	
	public IsbnException(String element) {
		super("10자리 이상의 isbn코드를 입력하세요");
	}
}
