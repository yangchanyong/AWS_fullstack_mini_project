package team1.utils;

import java.util.Scanner;

public class BookStoreUtils {
	// 자주 사용하는 함수 관련 클래스

	
//	콘솔창 입력값 스캐너
	private static Scanner scanner = new Scanner(System.in);
	
	public static String nextLine(String msg) {
		System.out.println(msg);
		return scanner.nextLine();
	}

	public static String nextLine2(String msg) {
		System.out.print(msg);
		return scanner.nextLine();
	}
	
	public static int nextInt(String msg) {
		return Integer.parseInt(nextLine(msg));
	}
	
	public static int nextInt2(String msg) {
		return Integer.parseInt(nextLine2(msg));
	}
}
