package team1.exception;

@SuppressWarnings("serial")
public class MyrangeException extends RuntimeException {
	
	public MyrangeException() {}
	
	public MyrangeException(int start, int end) {
		super(start + "~" + end + "사이의 값을 입력하세요");
	}

}
