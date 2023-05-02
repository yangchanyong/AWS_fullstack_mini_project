package team1.exception;

public class IdException extends RuntimeException {
	
	public IdException() {}
	public IdException(String id) {
		super("3자리 이상의 id를 입력하세요");
	}

}
