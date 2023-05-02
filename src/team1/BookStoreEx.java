package team1;

import java.util.Scanner;

import team1.service.BookService;
import team1.service.BookStoreService;
import team1.service.MemberService;
import team1.service.impl.BookServiceImpl;
import team1.service.impl.BookStoreServiceImpl;
import team1.service.impl.MemberServiceImpl;

public class BookStoreEx {
	public static void main(String[] args) {
		
		
		BookStoreService bookStoreService = BookStoreServiceImpl.getInstance();
		bookStoreService.run();
	}
}
