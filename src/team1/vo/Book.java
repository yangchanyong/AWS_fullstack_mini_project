package team1.vo;

import java.io.Serializable;
import java.util.Arrays;

import team1.service.impl.BookServiceImpl;

public class Book implements Serializable  {

	String publisher; // 출판사
	String title; // 제목
	String contents; // 내용
	String[] authors; //작가
	String isbn; // 책 코드 (재고 달라도 동일 책이면 동일 코드) 
	int price; // 가격
	int stock; //재고
		
	//베스트 셀러 / 추천 도서 / 등록 및 대기 상태
	int status; // 상태 (0 = 정식등록, 1 = 대기중, 2 = 요청 반려)
	Boolean picks; // 추천도서
	Boolean bestSellers; // 베스트셀러
	
	
	

	public Book() {
		// TODO Auto-generated constructor stub
	}
	
	// 모든 필드 생성자
	public Book(String title, String contents, String[] authors, String publisher, String isbn, int price, int stock,
			int status, Boolean picks, Boolean bestSellers) {
		super();
		this.publisher = publisher;
		this.title = title;
		this.contents = contents;
		this.authors = authors;
		this.isbn = isbn;
		this.price = price;
		this.stock = stock;
		this.status = status;
		this.picks = picks;
		this.bestSellers = bestSellers;
	}
	
	// 출판사의 책 등록용 생성자
	public Book(String title, String contents, String[] authors, String publisher, String isbn, int price, int stock) {
		super();
		this.publisher = publisher;
		this.title = title;
		this.contents = contents;
		this.authors = authors;
		this.isbn = isbn;
		this.price = price;
		this.stock = stock;
		this.status = 1;
	}	
	
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String[] getAuthors() {
		return authors;
	}
	public void setAuthors(String[] authors) {
		this.authors = authors;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean getPicks() {
		return picks;
	}

	public void setPicks(Boolean picks) {
		this.picks = picks;
	}

	public Boolean getBestSellers() {
		return bestSellers;
	}

	public void setBestSellers(Boolean bestSellers) {
		this.bestSellers = bestSellers;
	}


	//관리자 모드 일반 조회 형태
	public String toString() {
		
		String bookStatus = null;
		if(status == 0) bookStatus = "정식 등록";
		else if(status == 1) bookStatus = "대기 중";
		else bookStatus = "요청 반려";
		
		return "("+ bookStatus + ") isbn코드: "+ isbn + ", 출판사: " + publisher + " < " + title 
				+ " >\n                저자: " + Arrays.toString(authors) + ", 가격: " + price + ", 수량: " + stock ;
	}
	
	
	//수정모드 조회 형태
	public String modifyDp() {

		String bookStatus = null;
		if(status == 0) bookStatus = "정식 등록";
		else if(status == 1) bookStatus = "대기 중";
		else bookStatus = "요청 반려";
		
		String con;
		if(contents == "") con = "없음"; 
		else con = contents;
			
		return "isbn: " + isbn 
				+ "/ 제목: " + title 
				+ "/ 저자: " + Arrays.toString(authors) 
				+ "/ 출판사: " + publisher 
				+ "/ 가격: " + price 
				+ "/ 재고: " + stock 
				+ "/ 상태: " + status + "(" + bookStatus + ")"
				+ "\n설명: " + con;
	}
	
	

	
	/**
	 * @author 천은경
	 * @since 23/02/07
	 * @param
	 * @return Book(title, publisher, authors, contents, price, stock)
	 * 검색 시 책 출력 형태 설정
	 */
	public void display(Book book) {

		System.out.println("【"+title+"】 ");
		printContents(book, 70);
		System.out.print(publisher + " /");
		System.out.print(" "+ Arrays.toString(authors));
		System.out.print(" ┃ 가격: "+price+"원 ┃");
		System.out.print(" 남은 수량: "+ stock + "권 ┃");
		System.out.println(" isbn 코드: "+ isbn + " ┃");
		System.out.println();
	}
	

	
	/**
	 * @author 천은경
	 * @since 23/02/12 + 수정 23/02/14
	 * @param int length, int size, int row
	 * @return 
	 * str을 (size자)씩 row줄로 끊기
	 */
	public void stringCut(String str, int size, int row) {
		int length = str.length();
		for(int i = 0 ; i <= row - 1 ; i++) {
			if(i == length/size) {
				System.out.println(str.substring(i * size, length));
				break;
			}
		System.out.println(str.substring(i * size, (i + 1) * size));
		}
	}
	
	
	/**
	 * @author 천은경
	 * @since 23/02/12 + 수정 23/02/14
	 * @param Book book, int size
	 * @return 
	 * 책 설명 한 줄(size자)씩 끊어서 출력
	 */
	public void printContents(Book book, int size) {
		String contents = book.getContents();
		stringCut(contents, size, 2);		
	}
	
//	public void printTitle(String title) {
//		if(title.contains("(")) {
//			System.out.println(title.split("\\(")[0]);
//			System.out.println(title.substring(title.indexOf("(")));
//		}
//		else if(title.contains(":")) {
//			System.out.println(title.split(":")[0]);
//			System.out.println(title.substring(title.indexOf(":")));
//		}
//		else stringCut(title, 20, 2);
//	}

	
//	 오늘의 베스트셀러	
	public void bestDisplay() {	
		
		System.out.print("┌───────────────");
		System.out.print("Today Best");
		System.out.println("───────────────┐");
		System.out.print("▶ ");
		stringCut(title, 22, 2);
		System.out.print(" " + publisher + " ");
		System.out.println(Arrays.toString(authors) +" ");
		System.out.println("└────────────────────────────────────────┘");
	}


		
//		오늘의 추천도서
	public void pickDisplay() {
		System.out.print("┌────────────────");
		System.out.print("MD Picks");
		System.out.println("────────────────┐");
		System.out.println("▶ ");
		stringCut(title, 22, 2);
		System.out.print(" "+publisher +" ");
		System.out.println(Arrays.toString(authors) +" ");
		System.out.println("└────────────────────────────────────────┘");
	}

	
}	
