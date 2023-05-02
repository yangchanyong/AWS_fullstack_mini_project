package team1.exception;

public class RequiredInputException extends RuntimeException {
	
	public RequiredInputException() {}
	
	public RequiredInputException(String element) {
		super(element + "은(는) 필수 입력 항목입니다");
	}
	
}
