package team1.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import team1.exception.BookRangeException;
import team1.exception.IsbnException;
import team1.exception.RequiredInputException;
import team1.service.BookService;
import team1.utils.BookStoreUtils;
import team1.vo.Book;
import team1.vo.Member;

public class BookServiceImpl implements BookService, Serializable {
	// 싱글톤
	private static BookService bookService = new BookServiceImpl();

	public static BookService getInstance() {
		return bookService;
	}

	private BookServiceImpl() {
	}

	// 서점에 등록된 모든 책 List
	public List<Book> books = new ArrayList<>();

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 예외처리
	 */
	String checkIsbn(String isbn) {
		if (isbn.length() < 10) {
			throw new IsbnException(isbn);
		}
		return isbn;
	}

	int checkRange(int num, int start) {
		if (num < start) {
			throw new BookRangeException(start);
		}
		return num;
	}

	int checkRange(int num, int start, int end) {
		if (num < start || num > end) {
			throw new BookRangeException(start, end);
		}
		return num;
	}

	String checkInput(String element, String name) {
		if (element.replace(" ", "").isEmpty()) {
			throw new RequiredInputException(name);
		}
		return element;
	}

	/**
	 * @author 천은경
	 * @since 23/02/07 + 23/02/09 수정
	 * @param Book
	 * @return Book 출판사의 새로운 책 등록 요청
	 */
	@Override
	public void callReg() {

		System.out.println("등록 요청할 책의 정보를 입력하세요");

		String isbn = checkIsbn(BookStoreUtils.nextLine2("isbn코드 : "));

		if (findBy(isbn) != null) {
			System.err.println("이미 요청된 책입니다");
//		이미 등록된 책일 경우 등록을 불가하게 하고, 각 출판사별로 등록한 책 정보 화면에서 수정할 수 있도록
			return;
		}
		System.err.println("*은 필수 입력 항목입니다");
		String title = BookStoreUtils.nextLine2("*책 제목 : ");
		String publisher = BookStoreUtils.nextLine2("*출판사 : ");
		String authors = BookStoreUtils.nextLine2("*작가명 : ");
		String contents = BookStoreUtils.nextLine2("책 설명 : ");
		int price = checkRange(BookStoreUtils.nextInt2("*가격 : "), 0, 1000000);
		int stock = checkRange(BookStoreUtils.nextInt2("*수량 : "), 0);

		Book book = new Book(title, contents, new String[] { authors }, publisher, isbn, price, stock);
		books.add(book);

		save();

		System.err.println("<" + title + ">  등록 요청되었습니다.");
		System.out.println("서점의 승인 후 정식 등록됩니다. 이용해 주셔서 감사합니다.");

	}

	/**
	 * @author 천은경
	 * @since 23/02/07 + 23/02/09 수정
	 * @param
	 * @return Book 출판사에게 요청받은 책을 서점에 등록
	 */
	@Override
	public void bookReg() {
		String isbn = checkIsbn(BookStoreUtils.nextLine("정식 등록할 책의 isbn코드를 입력하세요"));
		Book book = findBy(isbn, 1);
		if (book == null)
			return;

		System.out.println("<" + book.getTitle() + "> 책이 정식 등록 되었습니다");
		book.setStatus(0);
		save();
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 베스트셀러 설정
	 */
	public void setBest() {
		String isbn = checkIsbn(BookStoreUtils.nextLine("설정할 책의 isbn코드를 입력하세요"));
		Book book = findBy(isbn, 0);
		if (book == null)
			return;
		if (book.getBestSellers()) {
			book.setBestSellers(false);
			System.err.println("<" + book.getTitle() + "> 책의 베스트셀러 설정이 해제되었습니다");
			save();
			return;
		}
		book.setBestSellers(true);
		System.out.println("<" + book.getTitle() + "> 책이 베스트셀러로 설정 되었습니다");
		save();
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 추천도서 설정
	 */
	public void setPicks() {
		String isbn = checkIsbn(BookStoreUtils.nextLine("설정할 책의 isbn코드를 입력하세요"));
		Book book = findBy(isbn, 0);
		if (book == null)
			return;
		if (book.getBestSellers()) {
			book.setPicks(false);
			System.err.println("<" + book.getTitle() + "> 책의 추천도서 설정이 해제되었습니다");
			save();
			return;
		}
		book.setPicks(true);
		System.out.println("<" + book.getTitle() + "> 책이 추천도서로 설정되었습니다");
		save();
	}

	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param String isbn
	 * @return books에서 isbn이 입력값과 같은 Book, 없으면 null 모든 책에서 정보에 맞는 책 찾기
	 */
	@Override
	public Book findBy(String isbn) {
		Book book = null;
		for (Book b : books) {
			if (b.getIsbn().equals(isbn) || b.getPublisher().equals(isbn)) {
				book = b;
				break;
			}
		}
		return book;
	}

	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param String isbn, int status
	 * @return books에서 element(isbn or publisher), status가 입력값과 같은 Book, 없으면 null
	 */
	@Override // 오버로딩
	public Book findBy(String isbn, int status) {
		Book book = findBy(isbn);

		if (book == null || book.getStatus() != status) {
			System.out.println("'" + isbn + "' 코드와 일치하는 책이 없습니다");
			return null;
		}
		return book;
	}

	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param int status
	 * @return books에서 status가 동일한 책 리스트
	 */
	@Override
	public List<Book> findList(int status) {

		List<Book> list = new ArrayList<Book>();

		for (Book book : books) {
			if (book.getStatus() == status) {
				list.add(book);
			}
		}
		return list;
	}

	/**
	 * @author 천은경
	 * @since 23/02/13
	 * @param String publisher
	 * @return books에서 publisher가 동일한 책 리스트
	 */
	@Override // 오버로딩
	public List<Book> findList(String publisher) {

		List<Book> list = new ArrayList<Book>();

		for (Book book : books) {
			if (book.getPublisher().equals(publisher)) {
				list.add(book);
			}
		}
		return list;
	}

	/**
	 * @author 천은경
	 * @since 23/02/10 + 수정 23/02/11
	 * @param List<Book> page, int size, String level
	 * @return sear의 페이징 작업. (Member level에 따라 다른 출력 형태로), 선택 book return
	 */
	@Override
	public void paging(List<Book> page, int size, String level) {

		int maxNum = page.size();
		Book book = null;

		for (int n = 0; n < maxNum / size + 1; n++) {
			for (int i = n * size; i < (n + 1) * size; i++) {

				System.out.print(" " + (i + 1) + "  ");
				book = page.get(i);

				if (level == "1")
					book.display(book); // 일반 검색 모드
				else
					System.out.println(book); // 관리자 또는 출판사 모드

				if (i == maxNum - 1) {
					System.out.println("마지막 페이지");
					return;
				}
			}
			if (n < maxNum / size)
				BookStoreUtils.nextLine("다음 페이지");
		}
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param List<Book> list
	 * @return 베스트셀러 및 추천도서가 0인 경우 방지 출력용 list
	 */
	public List<Book> emptyList(List<Book> list, String message) {
		if (list.isEmpty()) {
			Book nonBook = new Book();
			nonBook.setTitle("");
			nonBook.setAuthors(new String[] { message });
			nonBook.setPublisher("");
			list.add(nonBook);
		}
		return list;
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 베스트셀러 리스트 출력
	 */
	@Override
	public void bestSellersList() {
		List<Book> list = new ArrayList<Book>();

		for (Book book : findList(0)) {
			if (book.getBestSellers())
				list.add(book);
		}
		list = emptyList(list, "베스트셀러가 없습니다");
		paging(list, 10, "0");
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 베스트셀러 중 랜덤 1권 출력
	 */
	@Override
	public void bestSellers() {

		List<Book> list = new ArrayList<Book>();
		Random random = new Random();

		for (Book book : findList(0)) {
			if (book.getBestSellers())
				list.add(book);
		}
		list = emptyList(list, "베스트셀러가 없습니다");
		int randomIndex = random.nextInt(list.size());
		list.get(randomIndex).bestDisplay();
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 추천도서 리스트 출력
	 */
	@Override
	public void picksList() {
		List<Book> list = new ArrayList<Book>();

		for (Book book : findList(0)) {
			if (book.getPicks())
				list.add(book);
		}
		list = emptyList(list, "추천도서가 없습니다");
		paging(list, 10, "1");
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 추천도서 중 랜덤 1권 출력
	 */
	@Override
	public void picks() {
		List<Book> list = new ArrayList<Book>();
		Random random = new Random();

		for (Book book : findList(0)) {
			if (book.getPicks()) {
				list.add(book);
			}
		}
		list = emptyList(list, "추천도서가 없습니다");
		int randomIndex = random.nextInt(list.size());
		list.get(randomIndex).pickDisplay();

	}

	/**
	 * @author 천은경
	 * @since 23/02/07 + 수정 23/02/10 + 수정 23/02/11
	 * @param
	 * @return 등록된 책 중 title에 search와 일치하는 부분이 있는 Book 일치 목록 조회(검색) : 제목별
	 */
	@Override
	public void searchTitle() {

		String search = BookStoreUtils.nextLine("검색할 책의 제목을 입력하세요");

		List<Book> sear = new ArrayList<Book>();

		// 공백 포함 문자 변수 작업
		search = search.replace(" ", "");

		for (Book book : findList(0)) {
			if (book.getTitle().replace(" ", "").contains(search))
				sear.add(book);
		}
		if (sear.isEmpty()) {
			System.err.println("'" + search + "' 에 대한 검색 결과가 없습니다");
			return;
		}
		paging(sear, 8, "1");
	}

	/**
	 * @author 천은경
	 * @since 23/02/07 + 수정 23/02/10
	 * @param
	 * @return 등록된 책 중 author에 search와 일치하는 부분이 있는 Book 일치 목록 조회(검색) : 작가별
	 */
	@Override
	public void searchAuthors() {

		String search = BookStoreUtils.nextLine("검색할 작가를 입력하세요");

		List<Book> sear = new ArrayList<>();

		// 공백 포함 문자 변수 작업
		search = search.replace(" ", "");

		for (Book book : findList(0)) {
			for (int j = 0; j < book.getAuthors().length; j++) {
				if (book.getAuthors()[j].replace(" ", "").contains(search))
					sear.add(book);
			}
		}
		if (sear.isEmpty()) {
			System.err.println("'" + search + "'에 대한 검색 결과가 없습니다");
			return;
		}
		paging(sear, 8, "1");
	}

	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 등록된 책 중 isbn과 일치하는 Book 조회(검색)
	 */
	@Override
	public void searchIsbn() {
		String isbn = BookStoreUtils.nextLine("검색할 Isbn코드를 입력하세요");
		Book book = findBy(isbn, 0);
		book.display(book);
	}

	/**
	 * @author 천은경
	 * @since 23/02/08 + 수정 23/02/10 + 수정 23/02/13
	 * @param String publisher
	 * @return 해당 출판사의 책 목록 조회
	 */
	@Override
	public void publisherList(String publisher) {

		if (findList(publisher).size() == 0) {
			System.err.println("'" + publisher + "' 출판사의 책이 없습니다");
			return;
		}
		paging(findList(publisher), 20, "0");
	}

	/**
	 * @author 천은경
	 * @since 23/02/07 + 수정 23/02/10
	 * @param int status
	 * @return books에서 조건에 맞는 book 페이징 된 조회 (관리자 모드) (status : 0 = 정식등록 / 1 = 대기중 /
	 *         2 = 요청반려)
	 */
	@Override
	public void bookList(int status) {

		if (findList(status).isEmpty()) {
			System.out.println("해당 조회 결과가 없습니다.");
			return;
		}
		paging(findList(status), 10, "0");

	}

	/**
	 * @author 천은경
	 * @since 23/02/12 + 수정 23/02/13
	 * @param
	 * @return 기능 설명 출판사 또는 관리자의 책 정보 수정
	 */
	public void bookModify() {

		String isbn = checkIsbn(BookStoreUtils.nextLine("수정할 책의 isbn코드를 입력하세요"));

		Book book = findBy(isbn);
		if (book == null) {
			System.out.println("'" + isbn + "' 코드와 일치하는 책이 없습니다");
			return;
		}
		System.out.println(book.modifyDp());
		System.out.println(" ▼ ");

		book.setTitle(BookStoreUtils.nextLine2("변경 제목: "));
		book.setPublisher(BookStoreUtils.nextLine2("변경 출판사: "));
		book.setAuthors(new String[] { BookStoreUtils.nextLine2("변경 저자: ") });
		book.setPrice(checkRange(BookStoreUtils.nextInt2("변경 가격: "), 0, 1000000));
		book.setStock(checkRange(BookStoreUtils.nextInt2("변경 수량: "), 0));
		book.setContents(BookStoreUtils.nextLine("변경 설명: "));

		save();
		System.out.println("변경되었습니다");
	}

	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param int member 
	 * @return books의 Book 삭제 관리자(0) or 출판사(1) 가 isbn코드를 입력하여 등록된 책 폐기
	 */
	@Override
	public void bookDelete(int member) {

		String isbn = checkIsbn(BookStoreUtils.nextLine("삭제할 책의 isbn코드를 입력하세요"));

		Book book = findBy(isbn, member);
		if (book == null)
			return;

		System.out.println("<" + book.getTitle() + "> 책이 삭제되었습니다");
		books.remove(book);
		save();

	}

	/**
	 * @author 천은경
	 * @since 23/02/08 + 수정 23/02/11
	 * @param
	 * @return books에서 status가 1인 Book 중 삭제 관리자가 승인 대기 책 리스트 중 요청 반려
	 */
	@Override
	public void reject() {

		String isbn = checkIsbn(BookStoreUtils.nextLine("요청 반려할 책의 isbn코드를 입력하세요"));

		Book book = findBy(isbn, 1);
		if (book == null)
			return;

		System.out.println("<" + book.getTitle() + "> 책의 요청을 반려하였습니다");
		findBy(isbn).setStatus(2);
		save();

	}

	/**
	 * @author 천은경
	 * @since 23/02/13
	 * @return 저장
	 */
	private void save() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("book.ser"))) {
			oos.writeObject(books);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBookStock(String isbn, int quantity){
		Book book = findBy(isbn);
		int index = books.indexOf(book);

		book.setStock(quantity);

		books.set(index, book);
	}

	// 더미데이터
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.ser"))) {
			books = (List<Book>) ois.readObject();

		} catch (FileNotFoundException e) {

			// 서점 책 전체 더미데이터
			books.add(new Book("레시피보다 중요한 100가지 요리 비결",
					"《레시피보다 중요한 100가지 요리 비결》은 요리의 기본 ‘비결’을 과학원리와 일러스트와 함께 알기 쉽게 풀어쓴 요리책이다. 이 책에서는 모두 100가지의 요리 비결을 크게 13개 부분으로 나누어 알려준다. 13개 부분은 ‘야채과 과일에 관한 비결’, ‘육류에 관한 비결’, ‘해산물에 관한 비결’, ‘계란에 관한 비결’, ‘밥, 빵, 면류에 관한 비결’, ‘밑준비에 관한 비결’, ‘조리 전반의 기본 비결’, 등으로 순서대로가 아니더라도 가장 자신이",
					new String[] { "" }, "숨쉬는책공장", "1186452129", 16000, 6, 0, true, true));
			books.add(new Book("진짜 기본 요리책",
					"기본 레시피 306개를 수록한 『진짜 기본 요리책』. ‘왕초보’를 위한 요리책을 만들기 위해 실제 요리 초보 100명을 선발하여 함께 메뉴를 골라 구성한 것으로, 따라하는 요리잡지 월간 수퍼레시피 테스트키친팀이 검증한 기본 메뉴 306가지, 응용방법 56가지 총 360여 개의 레시피가 담겨 있다. 재료 손질법을 사진과 함께 알려줌은 물론, 분량과 불의 세기, 조리시간까지 정확히 제시하며, 기호에 따라 선택할 수 있도록 다양한 양념 옵션",
					new String[] { "월간 수퍼레시피" }, "레시피팩토리", "8996347272", 15800, 8, 0, false, false));
			books.add(new Book("양념공식 요리법(엄마도 모르는)(양장본 HardCover)",
					"신미혜의 남다른 요리 비결 양념공식 요리법, 한식 요리의 품격을 지키면서 요리 시간이 짧아지는 즐거움, 그리고 우리집 요리사 엄마도 새로 배우는 양념공식 요리법『양념공식 요리법』. 엄마가 해준 음식은 입맛에 맞는다. 그런데 왜 우리는 엄마의 요리 맛을 흉내 내지 못할까? 엄마처럼 맛을 내려면 꼭 30년의 노하우가 쌓여야 하는 걸까? 우리 음식의 조리법은 대체로 주먹구구다. 갓 요리를 시작한 신참 주부에게 엄마가 내던 맛은 불가사의하기만 하다. 사람들",
					new String[] { "신미혜" }, "세종서적", "8984076112", 11000, 6, 0, false, false));
			books.add(new Book("Why? 역사 속 음식과 요리(초등과학학습만화 40)(양장본 HardCover)",
					"모든 음식에는 제각기 다른 역사가 담겨 있다. 이러한 배경을 알고 먹으면 평범해 보이던 음식도 어느새 특별해 보일 것이다. 《Why? 역사 속 음식과 요리》는 옛날부터 우리 민족이 즐겨 먹는 음식과 요리, 그 속에 담긴 이야기를 소개하고 있다. 인현 왕후를 미나리에, 장희빈을 장다리에 비유한 미나리요 이야기를 통해 역사적 인물과 사건을 알 수 있고, 굴비나 탕평채 등의 이름이 붙은 유래를 알아보는 과정에서 당시 시대상 등 다양한 역사 상식을 얻을 수",
					new String[] { "강주현" }, "예림당", "8930232817", 11000, 6, 0, false, false));
			books.add(new Book("요리스타 청 1: 천재 요리 소녀의 등장",
					"바르고 맛있는 음식들과 함께 건강하고 즐거운 식습관을 길러주는 『요리스타 청: 천재 요리 소녀의 등장』 제1권 《천재 요리 소녀의 등장》. 어린이들이 과학 정보를 알기 쉽게 접할 수 있도록 《어린이 과학동아》의 과학 전문 기자들이 직접 취재한, 요리와 관련된 다양한 과학 정보들을 담았다. 몸에 좋은 음식들을 맛있게 섭취하는 방법을 배울 수 있다.    자타가 공인하는 꽃미남 요리사 한울 앞에 나타난 소녀 청이. 음식의 냄새만 맡아도 재료를 알아내는 청이의",
					new String[] { "조재호" }, "주니어김영사", "8934965274", 10000, 2, 0, false, false));
			books.add(new Book("나만의 요리(천연조미료로 통하는)",
					"우리 몸과 통하는 요리를 소개하는 『천연조미료로 通하는 나만의 요리』. 집에서 쉽게 만들 수 있는 천연조미료와 요리를 알려준다. 간단한 방법으로 미리 만들어 놓은 천연조미료를 가지고 나만의 요리를 만들 수 있다. 요리연구가이자 인기 강사인 저자의 오랜 강의 경험과 열정을 바탕으로 현대인에게 꼭 필요한 요리, 요리하면서 기본적으로 알아야 할 점들을 다루었다.",
					new String[] { "권향자" }, "꿈꾸는사람들", "8994136959", 13800, 8, 0, false, false));
			books.add(new Book("5천만 국민요리(요안나의 제철 재료 밥상)",
					"파워블로거 요안나가 제안하는 제철 건강 밥상 『5천만 국민요리』. 4천만이 검색한 오늘의 요리로 많은 사랑을 받은 저자의 두 번째 요리책으로, 제철 음식으로 차려낸 사계절 건강 밥상을 소개한다. 제철 음식은 우리 몸이 원하는 영양소를 섭취하고 몸의 기운이 균형을 맞출 수 있도록 도와준다. 이 책에서는 자연의 기운을 담아 어머니의 손맛으로 요리한, 우리 몸에 맞춘 제철 음식 요리법을 알려준다. 우리 가족의 건강을 지키고 행복한 밥상",
					new String[] { "이혜영(요안나)" }, "경향미디어", "8965180163", 14500, 4, 0, false, false));
			books.add(new Book("특별한 요리: 튀김요리 냄비요리 구이요리(일식의 명인 안효주의)(양장본...",
					"이 책은 〈미스터 초밥왕〉에 실명 주인공으로 나와 화제를 모았던 요리가 안효주의 일식 요리 안내서다. 스테디셀러인 전작 〈이것이 일본요리다〉과는 다르게, 일식 튀김ㆍ냄비ㆍ구이요리의 진수를 선보인다. 각 요리는 풍부한 사진을 바탕으로 그 과정을 쉽게 설명했다. [양장본]",
					new String[] { "안효주" }, "여백미디어", "8958660457", 20000, 2, 0, false, false));
			books.add(new Book("친정엄마네 레시피",
					"엄마가 해주시던 ‘그 맛’ 내는 요리비법『친정엄마네 레시피』. 이 책은 단순히 레시피만 알려주는 것이 아니라 살림 초보들이 요리할 때 놓치기 쉬운 포인트와 비법, 재료 다루기 등의 요리 지식을 친정엄마의 마음으로 담아 냈다. 콩나물 무침, 애호박볶음, 달걀찜처럼 흔한 재료로 만드는 만만한 반찬과 제철 재료 반찬, 된장찌개, 무국 등 기본 국물요리. 냉장고에 넣어두면 괜히 뿌듯한 장조림 등 밑반찬, 일 년에 한두 번이지만 꼭 해보고 싶은 갈비찜, 삼계탕 등",
					new String[] { "중앙M&B 편집부" }, "중앙M&B", "8964561570", 12000, 4, 0, false, false));
			books.add(new Book("고급서양요리(NCS기반)", "▶ 서양요리에 관한 내용을 담은 전문서적입니다.", new String[] { "서민석" }, "백산출판사",
					"1189740192", 33000, 8, 0, false, false));
			books.add(new Book("5분 편의점 요리",
					"『5분 편의점 요리』는 누구나 쉽게 가정에서 요리할 수 있는 초간단 요리법을 소개한 책이다. 밥 먹을 시간도 없는 사람들을 위해 5분, 10분 만에 만들어 먹을 수 있는 스피트 레시피를 엄선하여 소개했다. 기본적으로 가지고 있는 양념과 편의점에서 쉽게 구할 수 있는 기본 재료만으로 한 끼를 해결할 수 있는 현실적인 요리와 요리법을 친절하게 알려준다. 이를 위해 최소한의 재료와 과정, 양념으로 모든 요리를 새롭게 개발했으며, 한 번에 눈으로 볼 수 있는",
					new String[] { "이재건" }, "길벗", "8966186645", 15500, 4, 0, false, false));
			books.add(new Book("내 남자의 요리책",
					"『내 남자의 요리책』은 요리에 대해 전혀 문외한인 ‘남자’가 쉽게 요리를 시작할 수 있도록 도와주는 책이다. 남부럽지 않은 반찬과, 한 끼 식사를 위한 요리, 여행지에서 활용할 수 있는 요리, 술안주나 누군가를 위로할 때 좋은 요리 등 상황에 따라 다양한 메뉴를 구성하여 알려준다. 현실에 맞게 가까운 마트에서 반 조리 제품이나 밑 손질이 되어 있는 재료를 구입하여 세 단계 안에 간편 요리가 가능하다.  ‘레시피’에 중점을 둔 것이 아니라 간편하게 요리",
					new String[] { "권향자" }, "M&Kitchen", "8992947526", 12000, 4, 0, false, false));
			books.add(new Book("아토피 요리혁명: 약죽편(먹으면서 치료하는)(아토피 요리혁명 시리즈 2)",
					"[먹으면서 치료하는 아토피 요리혁명: 약죽편]은 아토피를 치료하는 한의사 한명화가 아토피를 치료하는데 도움을 주는 약죽 요리법을 소개하고, 각 재료의 효능과 아토피에 좋은 음식, 약초 등을 담은 책이다. 이 책에서 저자는 다양한 재료들을 소개하고 그 재료들이 갖고 있는 효능과 각 재료가 어떠한 사람들에게 효과적인지 설명해주어 자신에게 도움이 되는 약죽을 선택하여 먹을 수 있도록 하고 있다.",
					new String[] { "한명화" }, "열린시대사", "8996947237", 15000, 10, 0, false, false));
			books.add(new Book("4천만이 좋아하는 오늘의 면 요리",
					"네이버 파워 블로거 ‘요안나’가 알려주는 쉽고 맛있는 면 요리 레시피 『4천만이 좋아하는 오늘의 면 요리』. 기존의 베스트셀러 요리책을 언제 어디서나 쉽게 펼쳐볼 수 있는 콤팩트한 사이즈로 재구성한 「베스트 핸디북」 시리즈의 하나로, 사이즈를 줄이면서 가격도 낮추었다. 이 책에는 국수와 우동부터 파스타와 별미면까지 다양한 면 요리 144가지를 담았다. 네이버에서 사람들이 많이 검색한 메뉴들 중에서 면 요리 메뉴만을 선별하고, 여기에 저자만의 특별 메뉴",
					new String[] { "이혜영(요안나)" }, "중앙북스", "8927803116", 3900, 9, 0, false, false));
			books.add(new Book("이것이 일본요리다", "", new String[] { "안효주" }, "여백미디어", "898580457X", 40000, 2, 0, false,
					false));
			books.add(new Book("냉동보관 요리법(간편하게 냉동해서 쉽게 요리하는)(리빙 라이프 3)",
					"간단하고 맛있는 냉동보관 요리법을 소개하는 책 『냉동보관 요리법』. 생활의 기술을 담은 「리빙 라이프」 시리즈의 세 번째 책으로, 냉동보관의 테크닉과 냉동보관 식재료를 이용한 식단을 담았다. 장보는 시간과 조리 시간을 단축시켜주는 냉동보관 요리법은 음식 낭비를 줄여주고 다양한 요리들로 식탁을 풍요롭게 만든다. 일본의 유명 요리연구가이자 영양관리사인 저자는 영양학을 기초로 하여, 누구나 쉽게 가정 요리를 만들 수 있는 냉동보관 요리법을 제안한다. 식재료에",
					new String[] { "이와사키 케이코" }, "북웨이", "8994291253", 6000, 6, 0, false, false));
			books.add(new Book("진짜 캠핑요리",
					"『진짜 캠핑요리』는 10년 이상 미국과 한국에서 가족과 캠핑을 즐긴 저자 홍신애가 실전에서 얻은 노하우를 바탕으로 150가지 캠핑요리를 선보인다. 일상에서 구하기 쉬운 값싼 해산물과 채소류, 육류 등을 활용해 에피타이저, 바비큐 &amp; 일품요리, 간식 &amp; 별식, 밥 &amp; 찌개 식사류, 디저트까지 다양하면서도 먹고 싶은 캠핑요리만을 엄선했다. 1% 절대리스트를 비롯해 캠핑요리 맛있게 잘 만드는 압축된 노하우 다섯 가지, 왕초보도 따라할",
					new String[] { "홍신애" }, "스토리블라썸", "8996439762", 14500, 4, 0, false, false));
			books.add(new Book("누굴 줄까 1: 요리",
					"감사하는 마음을 배우는 선물 스티커북 『누굴 줄까』제1권 요리 편. 이 책은 오늘 하루 아이가 요리사가 되어 빈 케이크와 빈 피자, 빈 도시락에다 실 사진으로 된 스티커를 이용해 정성껏 꾸미도록 구성되어 있다. 스티커를 자유자재로 붙여 요리를 만들고, 편지지를 꾸며 마음을 담고, 봄이와 요미가 떠나는 선물여행 그림에 색칠공부까지 할 수 있도록 쉽고 체계적으로 만들어졌다. 이 책은 아이들이 직접 선물할 대상을 정하고 감사하는 마음을 전할",
					new String[] { "이차랑" }, "코딱지", "8996751820", 9800, 8, 0, false, false));
			books.add(new Book("꿈을 요리하는 마법 카페",
					"『꿈을 요리하는 마법 카페』는 여행가, 작가, 강연가, 기업인, 콘텐츠 제작자, 다큐 감독, 작사가, 번역가 등 직업만 열 개가 넘는 꿈쟁이 김수영 작가의 첫 자기계발 동화. 2010년 출간한 베스트셀러 '멈추지 마, 다시 꿈부터 써봐' 외 다수의 저작 활동을 통해 50만 명의 독자들을 꿈 부자로 만든 김수영 작가가 이번 책 '꿈을 요리하는 마법카페'에서는 무한한 가능성을 가지고 있지만 꿈이 가난한 어린이들을 위해 꿈을 이루어 가는 과정을 환상적이고",
					new String[] { "김수영" }, "위즈덤하우스", "8960863408", 12800, 8, 0, false, false));
			books.add(new Book("양념공식(요리책 없이 요리하는)", "", new String[] { "한명숙" }, "랜덤하우스코리아", "8925531275", 11000, 6, 0,
					false, false));
			books.add(new Book("서양요리실무", "▶ 이 책은 서양요리실무를 다룬 이론서입니다.", new String[] { "김옥란", " 이동근", " 배성일", " 한석만" },
					"백산출판사", "1189740451", 28000, 9, 0, false, false));
			books.add(new Book("이욱정 PD의 요리인류키친(양장본 HardCover)",
					"《이욱정 PD의 요리인류 키친》은 다큐멘터리 [요리인류]와 [누들로드]에 미처 담지 못한 이야기, 그 남겨진 재료를 엮어서 만든 책이다. 훌륭한 셰프는 ‘이런 게 요리 재료가 될까’ 하고 버려지는 재료로 최고의 요리를 해내는데, 이욱정 역시 그들처럼 남겨진 재료로 멋진 요리를 만들고 싶었다고 한다.",
					new String[] { "이욱정" }, "예담", "8959139823", 15800, 8, 0, false, false));
			books.add(new Book("700만이 뽑은 초간단 인생 요리 120",
					"에서 한국인이 가장 사랑하는 반찬 레시피를 공개한 ‘만개의 레시피’가 뜨거운 호응에 힘입어 이번에는 ‘진짜 쉽고 간단하게 차리는 근사한 식탁’을 이야기합니다. 《700만이 뽑은 인생 반찬 120》은 출간 즉시 모든 온·오프라인 서점 요리책 베스트셀러 1위가 되어 이미 16쇄를 찍었고, 지금까지도 스테디셀러로 많은 사랑을 받고 있습니다. 이어서 정성스럽게 내놓은 두 번째 책, 《700만이 뽑은 초간단 인생 요리》에는 시간은 없지만 맛있는 집밥은 먹고 싶은 바쁜",
					new String[] { "만개의 레시피" }, "만개의레시피", "1196437017", 15800, 8, 0, false, false));
			books.add(new Book("간편하고 맛있는 프라이팬 요리 86",
					"프라이팬은 볶음, 구이, 조림 등에 많이 사용되는 조리 기구다. 하지만 재료가 눌어붙거나 타기 쉬워 기름을 둘러 요리하는 것이 일반적이기에, 건강이 염려된다.『간편하고 맛있는 프라이팬 요리 86』은 기름을 뺀 조리를 원하는 사람들을 위한 책이다. 프라이팬 하나로 구이, 볶음, 조림, 찜, 면 요리까지 기름 없이도 만들 수 있는 건강 레시피를 소개한 책으로, '기름이 필요 없기 때문에' 더 간편하고 더 맛있는 프라이팬 요리 86가지를 담아냈다.  이 책",
					new String[] { "이자와 유미코" }, "이보라이프", "1195314027", 11000, 6, 0, false, false));
			books.add(new Book("읽으면서 바로 써먹는 어린이 수수께끼: 공포 특급",
					"빙빙 돌려 알 듯 모를 듯 헷갈리는 수수께끼 엉뚱한 상상으로 말장난처럼 재미있고! 비유와 은유로 말 공부가 저절로! 검은머리귀신의 저주에 걸린 도시와 아이들 그 저주를 풀기 위한 찹이와 친구들의 수수께끼 대모험 오싹오싹, 한여름 밤의 공포 특급이 시작됩니다.",
					new String[] { "한날" }, "파란정원", "1158682441", 13000, 8, 0, false, false));
			books.add(new Book("어린이 급수 한자: 6,7,8급",
					"생김새도 복잡하고 글자마다 다른 뜻을 가진 한자는 어린이뿐 아니라 어른들도 부담스럽고 어렵다는 생각을 갖습니다. 하지만 우리말 속 한자어는 우리가 생각하는 것보다 아주 큰 비중을 차지하고 있는 것이 사실입니다. 그래서 잘 모르는 단어라도 한자를 많이 알면 대강의 뜻을 유추할 수 있어 문해력을 높이는 데 큰 도움이 됩니다. 하지만 한자를 한 글자 한 글자 따로 익히는 것은 어려운 일입니다. 또 익힌다고 해도 돌아서면 금방 잊어버려 속상하기만 합니다. 그래서",
					new String[] { "" }, "파란정원", "1158682476", 13000, 8, 0, false, false));
			books.add(new Book("긴긴밤",
					"『긴긴밤』은 우리의 삶이 촘촘하게 연결되어 있음을 보여 준다. 다리가 튼튼한 코끼리가 다리가 불편한 코끼리의 기댈 곳이 되어 주는 것처럼, 자연에서 살아가는 게 서툰 노든을 아내가 도와준 것처럼, 윔보가 오른쪽 눈이 보이지 않는 치쿠를 위해 항상 치쿠의 오른쪽에 서 있었던 것처럼, 앙가부가 노든의 이야기를 듣고 또 들어 준 것처럼, 작지만 위대한 사랑의 연대를 보여 준다._송수연(아동문학평론가)  세상에 마지막 하나 남은 코뿔소가 된다면, 소중",
					new String[] { "루리" }, "문학동네", "8954677150", 11500, 4, 0, true, false));
			books.add(new Book("세금 내는 아이들",
					"맛있는 음식을 먹거나 아플 때 치료를 받기 위해 또는 마음에 드는 물건을 사기 위해서…… 우리 일상의 모든 것이 ‘돈’과 관련이 있지만, 이상하게도 우리의 교육에서는 아이들에게 돈에 대해서 직접적으로 말하기를 꺼리곤 합니다. 이런 사회 분위기에서 아이들은 돈의 흐름과 개념에 대해 명확히 알지 못한 채 돈에 서툰 어른으로 성장하고 말죠. 돈을 어떻게 관리해야 하는지, 세금은 어떤 것이 있는지, 신용등급은 왜 관리해야 하는지와 같은 살아가는 데 있어",
					new String[] { "옥효진" }, "한국경제신문", "8947547263", 14000, 11, 0, false, false));
			books.add(new Book("최재천의 동물대탐험 1: 비글호의 푸른 유령",
					"못한 아이들이 과연 자연과 지구를 보호하고 사랑하는 어른이 될 수 있을까? 《최재천의 동물대탐험》 시리즈는 자연과 아이들의 연결을 꿈꾸는 책이다. 아이들이 자연의 넓은 품에서 경험하고 느끼고 사랑하길 바라는 마음을 담은 최재천표 어린이 생물학 동화 시리즈의 첫 권이 출간되었다.  팬데믹과 기후 위기로 지구가 몸살을 앓고 있다. 자연과 함께 살아가는 방법을 강구해야만 하는 때다. 자연과의 공생은 다음 세대를 살아갈 우리의 아이들에게는 더 긴급하고 간절한",
					new String[] { "황혜영" }, "다산어린이", "1130694267", 15000, 10, 0, false, false));
			books.add(new Book("어린이를 위한 역사의 쓸모 1: 선사시대~남북국 시대",
					"랜선 제자만 600만 명인 대한민국 대표 역사 강사 최태성의 《어린이를 위한 역사의 쓸모》. 이 책은 역사적 사실을 학습하도록 하는 책이 아니라, 역사를 통해 현재의 문제를 해결하도록 돕는 책이라서 특별합니다.  삶에 관한 질문은 어른들만 던지는 것이 아닙니다. 어린 시절에도 우리는 수많은 고민을 하며 살아갑니다. 그렇지만 어린이들이 만족할 만한 대답을 줄 수 있는 어른은 그리 많지 않습니다. 이때 아득한 시간 동안 쌓인 무수한 사건과 인물의 기록을",
					new String[] { "최태성" }, "다산어린이", "1130692663", 15000, 10, 0, false, false));
			books.add(new Book("어린이 과학동아(2022년 9월1일자)(17호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202", 11000,
					2, 0, false, false));
			books.add(new Book("어린이 과학동아(2022년 8월15일자)(16호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202", 11000,
					2, 0, false, false));
			books.add(new Book("어린이를 위한 역사의 쓸모 1: 선사시대~남북국 시대",
					"랜선 제자만 600만 명인 대한민국 대표 역사 강사 최태성의 《어린이를 위한 역사의 쓸모》. 이 책은 역사적 사실을 학습하도록 하는 책이 아니라, 역사를 통해 현재의 문제를 해결하도록 돕는 책이라서 특별합니다.  삶에 관한 질문은 어른들만 던지는 것이 아닙니다. 어린 시절에도 우리는 수많은 고민을 하며 살아갑니다. 그렇지만 어린이들이 만족할 만한 대답을 줄 수 있는 어른은 그리 많지 않습니다. 이때 아득한 시간 동안 쌓인 무수한 사건과 인물의 기록을",
					new String[] { "최태성" }, "다산어린이", "1130692663", 15000, 10, 0, false, false));
			books.add(new Book("어린이 과학동아 (2021년10월15일자)(20호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202",
					11000, 6, 0, false, false));
			books.add(new Book("읽으면서 바로 써먹는 어린이 속담",
					"여러분은 속담에 대해 얼마나 정확하게 알고 있나요? 속담을 많이 아는 만큼 어휘력이 좋아지는 건 당연한 말씀. 하지만 잘못 알고 있는 속담도 있고, 미처 몰랐던 속담도 있어요. 은유와 비유가 가득 담긴 속담, 제대로 알아야겠지요? 초등학생이라면 꼭 알아야 할 속담을 엄선해서 추려 인기 웹툰 작가의 그림으로 쉽고 재미있게 뜻을 설명함으로써 읽기만 해도 속담의 의미를 이해하고 실생활에 활용할 수 있어요. 속담과 친해지는 아주 쉬운 방법, 시작해볼까요",
					new String[] { "한날" }, "파란정원", "1158681259", 12000, 4, 0, false, false));
			books.add(new Book("어린이 과학동아 (2021년9월15일자)(18호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202", 11000,
					2, 0, false, false));
			books.add(new Book("어린이 과학동아(2021년 11월15일자)(22호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202",
					11000, 6, 0, false, false));
			books.add(new Book("어린이 한국사 퀴즈 2",
					"우리가 당연하게 누리는 자유와 평화는 절대 당연한 것이 아니에요. 나라와 민족을 위해 투쟁하고 희생한 많은 분이 있었기에 가능한 일이지요. 현재는 과거가 되고, 미래는 현재가 됩니다. 바로 이것이 현재를 충실히 살아야 하는 이유예요. 내가 역사의 주인공임을 잊지 마세요!",
					new String[] { "한날" }, "파란정원", "1158682301", 12000, 4, 0, false, false));
			books.add(new Book("어린이를 위한 역사의 쓸모 2: 고려 시대 - 조선 전기",
					"랜선 제자만 600만 명인 대한민국 대표 역사 강사 최태성의 《어린이를 위한 역사의 쓸모》. 《어린이를 위한 역사의 쓸모》 2권에는 고려와 조선 시대에 살았던 많은 사람들의 이야기가 담겨 있습니다. 어린 시절에도 우리는 수많은 고민을 하며 살아갑니다. 그렇지만 어린이들은 너무 많이 배우다 보니 생각할 시간을 갖지 못하는 경우가 많습니다. 이때 역사는 어린이들에게 생각할 공간을 마련해 주는 역할을 합니다. 아득한 시간 속에 살아왔던 수많은 인물과 대화",
					new String[] { "최태성" }, "다산어린이", "1130693651", 15000, 10, 0, false, false));
			books.add(new Book("어린이 과학동아(2022년 7월1일자)(13호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202", 11000,
					2, 0, false, false));
			books.add(new Book("요술 연필 페니 올림픽 사수 작전(좋은책어린이문고 13)", "", new String[] { "에일린 오헬리" }, "좋은책어린이",
					"8959775169", 9000, 1, 0, false, false));
			books.add(new Book("어린이 과학동아(2021년10월1일자) (19호)", "", new String[] { "편집부" }, "동아사이언스", "1739361202", 11000,
					2, 0, false, false));
			books.add(new Book("이토록 평범한 미래",
					"작가 김연수가 짧지 않은 침묵을 깨고 신작 소설집 『이토록 평범한 미래』를 출간한다. 『사월의 미, 칠월의 솔』(2013) 이후 9년 만에 펴내는 여섯번째 소설집이다. 그전까지 2~4년 간격으로 꾸준히 소설집을 펴내며 ‘다작 작가’로 알려져온 그에게 지난 9년은 “바뀌어야 한다는 내적인 욕구”가 강하게 작동하는 동시에 “외적으로도 바뀔 수밖에 없는 일들이 벌어진”(특별 소책자 『어텐션 북』 수록 인터뷰에서) 시간이었다. 안팎으로 변화를 추동하는 일",
					new String[] { "김연수" }, "문학동네", "8954680003", 14000, 11, 0, false, false));
			books.add(new Book("하얼빈(양장본 Hardcover)",
					"‘우리 시대 최고의 문장가’ ‘작가들의 작가’로 일컬어지는 소설가 김훈의 신작 장편소설 『하얼빈』이 출간되었다. 『하얼빈』은 김훈이 작가로 활동하는 내내 인생 과업으로 삼아왔던 특별한 작품이다. 작가는 청년 시절부터 안중근의 짧고 강렬했던 생애를 소설로 쓰려는 구상을 품고 있었고, 안중근의 움직임이 뿜어내는 에너지를 글로 감당하기 위해 오랜 시간을 들여 ‘인간 안중근’을 깊이 이해해나갔다. 그리고 2022년 여름, 치열하고 절박한 집필 끝에 드디어 그",
					new String[] { "김훈" }, "문학동네", "895469991X", 16000, 6, 0, false, false));
			books.add(new Book("작별인사",
					"김영하가 『살인자의 기억법』 이후 9 년 만에 내놓는 장편소설 『작별인사』는 그리 멀지 않은 미래를 배경으로, 별안간 삶이 송두리째 뒤흔들린 한 소년의 여정을 좇는다. 유명한 IT 기업의 연구원인 아버지와 쾌적하고 평화롭게 살아가던 철이는 어느날 갑자기 수용소로 끌려가 난생처음 날것의 감정으로 가득한 혼돈의 세계에 맞닥뜨리게 되면서 정신적, 신체적 위기에 직면한다. 동시에 자신처럼 사회에서 배제된 자들을 만나 처음으로 생생한 소속감을 느끼고 따뜻한",
					new String[] { "김영하" }, "복복서가", "1191114228", 14000, 11, 0, false, false));
			books.add(new Book("아버지의 해방일지",
					"김유정문학상 심훈문학대상 이효석문학상 등을 수상하며 문학성을 두루 입증받은 ‘리얼리스트’ 정지아가 무려 32년 만에 장편소설을 발표했다. 써내는 작품마다 삶의 현존을 정확하게 묘사하며 독자와 평단의 찬사를 받아온 작가는 이번에 역사의 상흔과 가족의 사랑을 엮어낸 대작을 선보임으로써 선 굵은 서사에 목마른 독자들에게 한모금 청량음료 같은 해갈을 선사한다. 탁월한 언어적 세공으로 “한국소설의 새로운 화법을 제시”(문학평론가 정홍수)하기를 거듭해온 정지아는",
					new String[] { "정지아" }, "창비", "8936438832", 15000, 10, 0, false, false));
			books.add(new Book("불편한 편의점 2(단풍 에디션)",
					"출간 후 1년이 넘도록 독자의 사랑을 받으며 베스트셀러 상위권을 지키고 있는 소설, 김호연 작가의 『불편한 편의점』이 그 두 번째 이야기로 다시 찾아왔다. 서울역 노숙인 독고 씨가 편의점의 야간 알바로 일하면서 시작되는 1편의 이야기는 예측불허의 웃음과 따스한 온기로 잔잔한 감동을 선사했다. 『불편한 편의점 2』는 전편의 위트와 속 깊은 시선을 이어가며 더욱 진득한 이야기로 독자를 끌어당긴다.  소설은 1편의 시간으로부터 1년 반이 흐른 여름날의",
					new String[] { "김호연" }, "나무옆의자", "116157137X", 14000, 11, 0, false, false));
			books.add(new Book("파친코 2",
					"한 세기에 걸친 재일조선인 가족의 이야기를 그린 세계적 베스트셀러, 이민진 작가의 장편소설 《파친코》가 새롭게 출간되었다. 《파친코》는 재미교포 1.5세대인 이민진 작가가 30년에 달하는 세월에 걸쳐 집필한 대하소설로, 2017년 출간되어 《뉴욕타임스》 베스트셀러에 올랐다. 현재까지 전 세계 33개국에 번역 수출되었으며, BBC, 아마존 등 75개 이상의 주요 매체의 ‘올해의 책’으로 선정되었을 뿐 아니라 전미도서상 최종 후보에 이름을 올리며 평단과",
					new String[] { "이민진" }, "인플루엔셜", "1168340543", 15800, 8, 0, false, false));
			books.add(new Book("파친코 1",
					"“내게 ‘한국인’은 이야기의 주인공이 될 가치가 있는 이들이다. 나는 가능한 한 오래 한국인 이야기를 쓰고 싶다.” - ‘한국 독자들에게’ 중에서  4대에 걸친 재일조선인 가족의 이야기를 그린 세계적 베스트셀러, 이민진 작가의 장편소설 《파친코》가 새롭게 출간되었다. 《파친코》는 재미교포 1.5세대인 이민진 작가가 30년에 달하는 세월에 걸쳐 집필한 대하소설로, 2017년 출간되어 《뉴욕타임스》 베스트셀러에 올랐다. 현재까지 전 세계 33개국에 번역 수출되었으며",
					new String[] { "이민진" }, "인플루엔셜", "1168340519", 15800, 8, 0, false, false));
			books.add(new Book("작은 땅의 야수들",
					"하는 것은 어쩌면 필연적인 일이었을 것이다.  폭넓은 서사와 호흡을 보여준다는 점에서 톨스토이의 작품을 연상케 하고, 일제강점기에 한국인이 겪었던 뒤틀린 운명을 그려낸다는 점에서 동시대를 배경으로 하는 『파친코』도 떠오른다. 대하소설을 좋아하는 독자, 절절한 사랑 이야기를 좋아하는 독자는 물론, 성별과 세대를 아울러 널리 읽힐 대작이다. 「기생충」을 시작으로 「파친코」까지 K-콘텐츠가 전 세계의 사랑을 받는 가운데 영어로 먼저 쓰인 ‘우리 이야기’를 본국",
					new String[] { "김주혜" }, "다산책방", "1130693929", 18000, 2, 0, false, false));
			books.add(new Book("어서 오세요, 휴남동 서점입니다 (여름 숲 에디션)",
					"작은 상처와 희망을 가진 사람들이 휴남동 서점이라는 공간을 안식처로 삼아 함께 살아가는 법을 배운다. 『어서 오세요, 휴남동 서점입니다』는 우리가 잃어버린 채 살고 있지만 사는 데 꼭 필요한 것들이 가득한 책이다. 배려와 친절, 거리를 지킬 줄 아는 사람들끼리의 우정과 느슨한 연대, 진솔하고 깊이 있는 대화 등. 출간 즉시 전자책 TOP 10 베스트셀러에 오르며 수많은 독자의 찬사를 받은 소설이 독자들의 강력한 요청으로 마침내 종이책으로 다시 태어났다",
					new String[] { "황보름" }, "클레이하우스", "119737714X", 15000, 10, 0, false, false));
			books.add(new Book("불편한 편의점(40만부 기념 벚꽃 에디션)",
					"누적 판매 40만부 돌파, 2022년 가장 사랑받는 소설 ★★★전 서점 종합베스트 1위, 2021 올해의 책, ★★★국립중앙도서관 사서추천도서, 해외 6개국 판권 수출  김호연 작가의 장편소설 『불편한 편의점』이 누적 판매 40만부 돌파를 기념하여 벚꽃 에디션으로 새 단장 했습니다. 2021년 4월에 출간되어 전 연령층의 폭넓은 공감을 얻으며 소설 읽기 바람을 일으킨 『불편한 편의점』의 열기는 지금도 현재진행형입니다. “읽는 내내 마음이 따뜻하고 먹먹",
					new String[] { "김호연" }, "나무옆의자", "1161571183", 14000, 11, 0, false, false));
			books.add(new Book("연금술사",
					"1987년 출간이후 전세계 120여 개국에서 변역되어 2,000만 부가 넘는 판매량을 기록한 파울로 코엘료의 『연금술사』. 청년 산티아고가 만물에 깃들인 영혼의 언어들을 하나하나 배워가는 과정을 그리고 있다. 마음의 목소리에 귀를 기울이는 것이 얼마나 중요한지를 증언하고, 진정 자기 자신의 꿈과 대면하고자 하는 모든 이들을 축복하는 희망과 환희의 메시지를 담은 작품이다.  신부가 되기 위해 라틴어, 스페인어, 신학을 공부한 산티아고는 어느 날 부모",
					new String[] { "파울로 코엘료" }, "문학동네", "8982814477", 12000, 4, 0, false, false));
			books.add(new Book("눈먼 자들의 도시(탄생 100주년 기념 스페셜 에디션)",
					"에디션은 많은 독자들이 요청해왔던 초판 버전의 표지로 리뉴얼하여 새롭게 단장했다.  『눈먼 자들의 도시』는 주제 사라마구의 이름을 널리 알려준 대표적인 작품으로, 한 도시 전체에 ‘실명’이라는 전염병이 퍼지며 이야기가 시작된다. 이 소설은 시간적 공간적 배경이 확실하지 않으며, 등장하는 인물들의 이름 또한 따로 없는 것이 특징이다. 여기서 중요한 것은 이름이 아니라 ‘눈이 멀었다’는 사실 그 자체다. 작품 속의 인간들은 물질적 소유에 눈이 멀었을 뿐만 아니라 그",
					new String[] { "주제 사라마구" }, "해냄출판사", "8973374931", 18800, 8, 0, false, false));
			books.add(new Book("나미야 잡화점의 기적",
					"이 이야기에는 살인 사건도 민완 형사도 없다. 범죄자의 컴컴한 악의 대신 인간 내면에 잠재한 선의에 대한 믿음이 있고, 모든 세대를 뭉클한 감동에 빠뜨리는 기적에 대한 완벽한 구성이 있다. - 옮긴이 양윤옥",
					new String[] { "히가시노 게이고" }, "현대문학", "8972756199", 14800, 8, 0, true, true));
			books.add(new Book("베로니카 죽기로 결심하다(양장본 HardCover)",
					"스물네 살의 베로니카는 원하는 것은 모두 가지고 있는 듯하다. 젊음, 아름다움, 매력적인 남자친구들, 만족스런 직업, 그리고 사랑하는 가족. 하지만 그녀에게는 뭔가 부족한 게 있다. 마음이 너무나 공허하여 그 무엇으로도 채울 수 없을 것 같다. 1997년 11월 21일, 베로니카는 죽기로 결심하는데……. 피에트라 강가에서 나는 울었네에 이은 '그리고 일곱 번째 날' 3부작 시리즈 중 두 번째 작품이다.",
					new String[] { "파울로 코엘료" }, "문학동네", "8982817425", 11500, 4, 0, false, false));
			books.add(new Book("모순",
					"양귀자 소설의 힘을 보여준 베스트셀러 『모순』. 1998년에 초판이 출간된 이후 132쇄를 찍으며 여전히 많은 사랑을 받고 있는 작품을, 오래도록 소장할 수 있는 양장본으로 새롭게 선보인다. 스물다섯 살 미혼여성 안진진을 통해 모순으로 가득한 우리의 인생을 들여다본다. 작가 특유의 섬세한 문장들로 여러 인물들의 삶을 생생하게 그려내고 있다.    시장에서 내복을 팔고 있는 억척스런 어머니와 행방불명 상태로 떠돌다 가끔씩 귀가하는 아버지, 조폭의 보스",
					new String[] { "양귀자" }, "쓰다", "8998441012", 13000, 8, 0, false, false));
			books.add(new Book("가녀장의 시대",
					"매일 한 편씩 이메일로 독자들에게 글을 보내는 〈일간 이슬아〉로 그 어떤 등단 절차나 시스템의 승인 없이도 독자와 직거래를 트며 우리 시대의 대표 에세이스트로 자리잡은 작가 이슬아, 그가 첫 장편소설을 발표했다. 제목은 ‘가녀장의 시대’. 〈일간 이슬아〉에서 이 소설이 연재되는 동안 이슬아 작가가 만든 ‘가녀장’이란 말은 SNS와 신문칼럼에 회자되며 화제를 불러일으켰다.  이 소설은 가부장도 가모장도 아닌 가녀장이 주인공인 이야기이다. 할아버지가",
					new String[] { "이슬아" }, "이야기장수", "8954688799", 15000, 10, 0, false, false));
			books.add(new Book("밝은 밤",
					"공감을 불러일으키는 이야기와 서정적이며 사려 깊은 문장, 그리고 그 안에 자리한 뜨거운 문제의식으로 등단 이후 줄곧 폭넓은 독자의 지지와 문학적 조명을 두루 받고 있는 작가 최은영의 첫 장편소설. ‘문화계 프로가 뽑은 차세대 주목할 작가’(동아일보) ‘2016, 2018 소설가들이 뽑은 올해의 소설’(교보문고 주관) ‘독자들이 뽑은 한국문학의 미래가 될 젊은 작가’(예스24) 등 차세대 한국소설을 이끌 작가를 논할 때면 분야를 막론하고 많은 사람들",
					new String[] { "최은영" }, "문학동네", "8954681174", 14500, 4, 0, false, false));
			books.add(new Book("달러구트 꿈 백화점(레인보우 에디션)",
					"신참 직원 ‘페니’, 꿈을 만드는 제작자 ‘아가넵 코코’, 그리고 베일에 둘러싸인 비고 마이어스…등이 등장한다. 《달러구트 꿈 백화점》은 ‘무의식에서만 존재하는 꿈을 정말 사고 팔 수 있을까?’라는 기발한 질문에 답을 찾아가며, 꿈을 만드는 사람, 파는 사람, 사는 사람의 비밀스런 에피소드를 담고 있는 판타지 소설이다. 텀블벅 펀딩 1812% 달성, 전자책 출간 즉시 베스트셀러 1위를 3주간 기록하며 수많은 독자들의 요청으로 종이책으로 출간하게 되었다",
					new String[] { "이미예" }, "팩토리나인", "1165341905", 13800, 8, 0, true, false));
			books.add(new Book("아이 엠 넘버 포 1: 로리언에서 온 그와의 운명적 만남(로리언레거시 1)",
					"로리언 행성에서 온 소년의 이야기 『아이 엠 넘버 포』. 자신의 목숨과 행성의 운명을 짊어진 채 지구로 온 로리언 가드들의 이야기를 그린 「로리언 레거시」 시리즈의 첫 번째 책이다. 어린 시절에 지구로 와 이제 막 로리언 인 특유의 능력이 발현되는 시기에 있는 특별한 소년. 그는 비범한 능력이 발현된 것을 기뻐하는 동시에, 평범한 사람으로 살아갈 수 없다는 사실에 낙담한다. 그럼에도 소중한 사람들을 위해 자신의 모든 것을 거는 용기를 내는데…. 환상",
					new String[] { "피타커스 로어" }, "세계사", "8933830464", 12800, 8, 0, false, false));
			books.add(new Book("7년의 밤",
					"동안 아버지와 아들에게 일어난 이야기 『7년의 밤』. 제1회 세계청소년문학상 수상작 〈내 인생의 스프링 캠프〉와 제5회 세계문학상 수상작 〈내 심장을 쏴라〉의 작가 정유정. 그녀가 수상 이후 오랜 시간 준비하여 야심차게 내놓은 소설이다. 크게 두 파트로 나뉘어 있는 이 작품은 액자 소설 형태를 취하고 있다. 살인마의 아들이라는 굴레를 쓰고 떠돌던 아들이 아버지의 사형집행 소식을 듣는다. 아버지의 죽음은 7년 전 그날 밤으로 아들을 데려가고, 아들은 아직 그날",
					new String[] { "정유정" }, "은행나무", "8956604991", 14500, 4, 0, false, false));
			books.add(new Book("소설 무소유: 법정스님 이야기",
					"특유의 불교적 사유를 바탕으로 문학작품과 산문을 써온 작가 정찬주가 무소유의 삶을 몸소 실천하고 가신 법정스님의 소박하면서도 위대한 삶을 소설화한『소설 무소유』. 법정스님이 태어나 출가하고, 수행하고, 입적하기까지의 모든 행적이 섬세하면서도 담백한 문체 속에 고스란히 담겨 있다. 땅끝 마을 가난한 시골 소년이 탐욕과 무지의 세속을 벗어나 무아와 무소유의 삶을 이루는 과정을 통해 스님의 따스한 마음과 무소의 뿔처럼 살아오신 수행의 여정을 만날 수 있다",
					new String[] { "정찬주" }, "열림원", "8970636552", 15000, 10, 0, false, false));
			books.add(new Book("사라진 소녀들의 숲",
					"한국의 역사와 문화에 바탕을 둔 작품 분위기, 탄탄한 서사 속에 치밀한 미스터리 장치를 가미한 필력으로 한국이 아닌 세계에서 먼저 이름을 알린 작가 허주은의 장편소설 『사라진 소녀들의 숲』이 미디어창비에서 출간되었다. 작가는 이번 작품의 배경에 한국인들에게도 생소한 역사, 조선 세종 대까지 존재했던 공녀(貢女) 제도를 앉혀놓는다. 이에 얽힌 제주 한 마을의 비극, 그 비극에 긴박하게 연결된 가족사, 나아가 가부장 시대 조선 여성들의 삶을 다층적으로",
					new String[] { "허주은" }, "미디어창비", "1191248917", 17000, 13, 0, false, false));
			books.add(new Book("우리가 빛의 속도로 갈 수 없다면",
					"바이오센서를 만드는 과학도에서 이제는 소설을 쓰는 작가 김초엽. 어디에도 없는 그러나 어딘가에 있을 것 같은 상상의 세계를 특유의 분위기로 손에 잡힐 듯 그려내며, 정상과 비정상, 성공과 실패, 주류와 비주류의 경계를 끊임없이 질문해온 그의 첫 소설집 『우리가 빛의 속도로 갈 수 없다면』.    《관내분실》로 2017년 제2회 한국과학문학상 중단편부문 대상을, 《우리가 빛의 속도로 갈 수 없다면》으로 가작을 동시에 받으며 작품 활동을 시작한 저자의",
					new String[] { "김초엽" }, "허블", "1190090015", 14000, 11, 0, false, false));
			books.add(new Book("11분",
					"성(性)과 사랑이 가져다주는 '내면의 빛'을 이야기하는 소설. 『연금술사』의 작가 파울로 코엘료의 신작으로, 한 처녀의 성 입문과정을 통해 몸과 마음의 화해, 영적 자기 발견을 내밀하게 표현한 작품이다. 사랑은 오직 고통을 줄 뿐이라 믿는 브라질 처녀 마리아는 일자리와 모험을 찾아 제네바로 떠나고, 그곳에서 진정한 사랑의 의미를 깨닫게 해줄 젊은 화가를 만난다. 실제 인물에게서 영감을 받아 쓴 이 이야기는 강하고 거침이 없다. 저자는 시적 에스프리와",
					new String[] { "파울로 코엘료" }, "문학동네", "8982818227", 12000, 4, 0, false, false));
			books.add(new Book("베어타운",
					"《오베라는 남자》로 전 세계를 사로잡은 프레드릭 배크만이 모두의 가슴을 울리는 새로운 이야기 『베어타운』. 일자리도, 미래도 없이 막다른 곳에 내몰린 소도시, 베어타운을 배경으로 공동체를 하나로 엮는 희망과 그 공동체를 갈기갈기 찢어놓는 비밀, 대의를 위해 잡음을 모른척하려는 이기심과 대의에 반하는 선택을 하는 한 개인의 용기를 생생하게 그려낸 작품이다.  온 마을이 아이스하키에 매달리는 베어타운은 과거의 영광도 하키로 이루었고, 몰락도 하키에서",
					new String[] { "프레드릭 배크만" }, "다산책방", "1130616657", 15800, 8, 0, false, false));
			books.add(new Book("지구 끝의 온실",
					"이미 폭넓은 독자층을 형성하며 열렬한 사랑을 받고 있는 김초엽 작가는 더스트로 멸망한 이후의 세계를 첫 장편소설의 무대로 삼았다. 그는 지난해 말 플랫폼 연재를 통해 발표한 이야기를 반년이 훌쩍 넘는 시간 동안 수정하면서 한층 더 무르익도록 만들었다. 그리하여 장 구성부터 세부적인 장면은 물론 문장들까지 완전히 새롭게 탄생한 『지구 끝의 온실』이 2021년 8월 드디어 독자들을 만난다.  『지구 끝의 온실』은 자이언트북스의 네 번째 도서이다. 김중혁의",
					new String[] { "김초엽" }, "자이언트북스", "1191824004", 15000, 10, 0, false, false));
			books.add(new Book("크리스마스 타일",
					"수많은 독자에게 사랑받는 작가 김금희가 데뷔 13년 만에 첫번째 연작소설을 선보인다. 크리스마스를 배경으로 한 명랑하고 아름다운 이야기를 반짝이는 일곱편의 소설 속에 담아냈다. 조금씩 연결되어 있는 인물들의 각기 다른 크리스마스 이야기를 담은 이 연작소설에는 쿠바에서 보낸 크리스마스에 작은 기적을 만난 방송작가 은하, 사랑에 대해 함께 이야기한 밤들이 모두 특별했음을 깨닫는 영화학도 한가을, 아홉살의 크리스마스에 처음 만난 남자애와 스무살까지 이어온",
					new String[] { "김금희" }, "창비", "8936438891", 15000, 10, 0, false, false));
			books.add(new Book("빅 엔젤의 마지막 토요일",
					"루이스 알베르토 우레아의『빅 엔젤의 마지막 토요일』이 다산책방에서 출간되었다. 루이스 알베르토 우레아는 시, 소설, 수필 등 다양한 분야를 넘나드는 작품 활동으로 펜포크너상, 에드거상, 라난 문학상 등을 수상하고 퓰리처상 최종 후보에 오르며 필력을 인정받은 작가로, 그의 장편소설이 국내에 소개되는 건 이번이 처음이다. 작가가 형의 마지막 생일 파티에 영감을 받아 쓴 『빅 엔젤의 마지막 토요일』은 암 선고를 받은 70세 노인 빅 엔젤의 마지막 생일 파티",
					new String[] { "루이스 알베르토 우레아" }, "다산책방", "1130627586", 15800, 8, 0, false, false));
			books.add(new Book("버터",
					"사진이었다. 일반적으로 생각하는 ‘꽃뱀’의 이미지가 아니었던 것이다. 피해 남성들은 이 여자가 사기를 칠 것이라는 의심은 조금도 하지 않았다고 한다. 이 사건의 범인으로 지목된 기지마 가나에는 2017년 사형선고를 받고 현재 도쿄 구치소에 수감중이다. 옥중 생활 중에도 블로그를 운영하고 결혼을 하는 등 화제를 만들어냈다. ‘음식 소설’로 유명한 유즈키 아사코는 사건 자체보다 범인이 요리 블로그를 운영했고, 요리교실에 다녔다는 사실에 주목하며 소설 『버터』를 집필한다",
					new String[] { "유즈키 아사코" }, "이봄", "1190582481", 17800, 8, 0, false, false));
			books.add(new Book("달러구트 꿈 백화점 2(레인보우 에디션)",
					"어느덧 페니가 달러구트 꿈 백화점에서 일한 지도 1년이 넘었다. 재고가 부족한 꿈을 관리하고, 꿈값 창고에서 감정으로 가득 찬 병을 옮기고, 프런트의 수많은 눈꺼풀 저울을 관리하는 일에 능숙해진 페니는 자신감이 넘친다. 게다가 꿈 산업 종사자로 인정을 받아야만 드나들 수 있는 ‘컴퍼니 구역’에도 가게 된 페니는 기쁜 마음을 감출 수 없다. 하지만 그곳에서 페니를 기다리고 있는 건, 꿈에 대한 불만을 털어놓는 사람들로 가득한 ‘민원관리국’이었다. 설상가상",
					new String[] { "이미예" }, "팩토리나인", "116534372X", 13800, 8, 0, false, false));
			books.add(new Book("소년이 온다",
					"한국인 최초 맨부커상 수상작가 한강의 여섯 번째 장편소설 『소년이 온다』. 1980년 5월 18일부터 열흘간 있었던 광주민주화운동 당시의 상황과 그 이후 남겨진 사람들의 이야기를 들려주는 소설이다. 2013년 11월부터 2014년 1월까지 창비문학블로그 ‘창문’에서 연재했던 작품으로 지금까지의 작품세계를 한 단계 끌어올렸다는 평가를 받았다. 철저한 고증과 취재를 통해 저자 특유의 정교하고도 밀도 있는 문장으로 계엄군에 맞서 싸우다 죽음을 맞게 된 중학생",
					new String[] { "한강" }, "창비", "8936434128", 14000, 11, 0, false, false));
			books.add(new Book("죽이고 싶은 아이",
					"주목받아 온 이꽃님 작가가 결말을 예측할 수 없는, 놀랍도록 흡인력 있는 작품으로 돌아왔다. 『죽이고 싶은 아이』는 한 여고생의 죽음이라는, 결코 평범하지 않은 이야기를 통해 독자들에게 진실과 믿음에 관한 이야기를 건넨다.    소설의 주인공인 주연과 서은은 둘도 없는 단짝 친구다. 두 사람이 크게 싸운 어느 날, 학교 건물 뒤 공터에서 서은이 시체로 발견되고 가장 유력한 용의자로 주연이 체포된다. 그런데 어찌 된 일인지 주연은 그날의 일이 도무지 기억",
					new String[] { "이꽃님" }, "우리학교", "1190337754", 12500, 4, 0, false, false));
			books.add(new Book("달콤한 복수 주식회사",
					"베스트셀러 작가 요나스 요나손의 장편소설 『달콤한 복수 주식회사』가 열린책들에서 출간되었다. 요나손은 4편의 소설로 전 세계에서 1천6백만 부 이상 판매되며 세계적으로 열풍을 일으킨 작가다. 다섯 번째 작품인 이 책 역시 출간되자마자 유럽 전역에서 베스트셀러 목록에 올랐으며, 독일에서는 한 달 만에 책이 매진되기도 했다. 요나손 특유의 문체와 말맛을 그대로 살리기로 정평이 난 전문 번역가 임호경이 번역을 맡았다.  스웨덴 스톡홀름에 사는 빅토르는",
					new String[] { "요나스 요나손" }, "열린책들", "8932921431", 15800, 8, 0, false, false));
			books.add(new Book("구의 증명",
					"젊은 감성을 위한 테이크아웃 소설 시리즈 「은행나무 노벨라」 제7권 『구의 증명』. 도서출판 은행나무에서 200자 원고지 300매~400매 분량으로 한두 시간이면 읽을 수 있을 만큼 속도감 있고 날렵하며 트렌드에 민감한 젊은 독자들을 대상으로 한 형식과 스타일을 콘셉트로 한 작품들을 선보인다.    『구의 증명』은 사랑하는 연인의 갑작스러운 죽음 이후 겪게 되는 상실과 애도의 과정을 통해 삶의 의미 혹은 죽음의 의미를 되묻는 작품이다. 저자는 퇴색하지",
					new String[] { "최진영" }, "은행나무", "8956608555", 8000, 2, 0, false, false));
			books.add(new Book("세계를 건너 너에게 갈게",
					"*드라마 및 영화 제작 중 *일본, 대만, 태국 판권 수출 *2019 양주시 올해의 책 | 2019 안동시 올해의 책 | 2020 천안 올해의 책  산 자와 죽은 자 사이에 시공간을 건너뛰며 이어지는 편지 형식의 서사와 따뜻하고 아름다운 결말. 이 작품이 품은 감동이 독자들에게 온전히 건네질 수 있기를 기원한다._심사평(김진경, 유영진, 윤성희, 이금이)  “나에게. 아빠가 쓰라고 해서 쓰는 거야.” 첫 문장으로 시작한 편지가 “세계를 건너 너",
					new String[] { "이꽃님" }, "문학동네", "895465021X", 12500, 4, 0, false, false));
			books.add(new Book("자기 앞의 생",
					"열네 살 소년 모모가 들려주는 신비롭고 경이로운 생의 비밀을 담은 에밀 아자르의 소설 『자기 앞의 생』. 1980년 의문의 권총 자살로 생을 마감한 프랑스 작가 로맹 가리가 에밀 아자르라는 필명으로 출간한 두 번째 소설이다. 어린 소년 모모의 슬프지만 아름다운 성장 이야기를 담고 있다. 악동 같지만 순수한 어린 주인공 모모를 통해 이 세상 누구도 눈길을 주지 않는 밑바닥 삶을 살아가는 불행한 사람들의 슬픔과 고독과 사랑을 그리고 있다.    저자는",
					new String[] { "로맹 가리(에밀 아자르)" }, "문학동네", "8982816631", 13000, 8, 0, false, false));
			books.add(new Book("재수사 1",
					"부대》 《우리의 소원은 전쟁》 《한국이 싫어서》……. 날카로운 지성과 거침없는 상상력, 속도감 있는 문장으로 발표하는 작품마다 우리 삶과 연관된 가장 사실적인 순간을 포착해온, 그야말로 장르불문의 올라운더 소설가 장강명의 신작 장편소설 《재수사》가 은행나무출판사에서 출간된다. 6년 만의 장편소설이다. 강력범죄수사대 소속 형사 연지혜가 22년 전 발생한 신촌 여대생 살인사건을 재수사하며 벌어지는 일을 다룬 이 소설은, 치밀한 취재로 만들어낸 생생한 현장감, 서사",
					new String[] { "장강명" }, "은행나무", "1167372018", 16000, 6, 0, false, false));
			books.add(new Book("완전한 행복",
					"《내 인생의 스프링 캠프》 《내 심장을 쏴라》 《7년의 밤》 《28》 《종의 기원》 《진이, 지니》. 발표하는 작품마다 독자들의 열광적인 지지를 받으며 한국문학의 대체불가한 작가로 자리매김한 정유정이 신작 《완전한 행복》으로 돌아왔다. 500여 쪽을 꽉 채운 압도적인 서사와 적재적소를 타격하는 속도감 있는 문장, 치밀하고 정교하게 쌓아올린 플롯과 독자의 눈에 작열하는 생생한 묘사로 정유정만의 스타일을 가감 없이 보여주는 한편, 더 완숙해진 서스펜스",
					new String[] { "정유정" }, "은행나무", "1167370287", 15800, 8, 0, false, false));
			books.add(new Book("창문 넘어 도망친 100세 노인",
					"데뷔작으로 전 유럽 서점가를 강타한 스웨덴의 작가 요나스 요나손의 장편소설 『창문 넘어 도망친 100세 노인』. 100세 생일날 슬리퍼 바람으로 양로원의 창문을 넘어 탈출한 알란이 우연히 갱단의 돈가방을 손에 넣고 자신을 추적하는 무리를 피해 도망 길에 나서며 벌어지는 이야기를 담은 작품이다. 기자와 PD로 오랜 세월 일해 온 저자의 늦깎이 데뷔작으로 1905년 스웨덴의 한 시골 마을에서 태어난 노인이 살아온 백 년의 세월을 코믹하고도 유쾌하게 그려",
					new String[] { "요나스 요나손" }, "열린책들", "8932916195", 16800, 8, 0, false, false));
			books.add(new Book("순례 주택",
					"한국어린이도서상, IBBY 어너리스트 수상작가 유은실의 신작 청소년 소설『순례 주택』. 코믹 발랄한 캐릭터 설정과, 순례 주택을 둘러싼 한바탕 대소동은 기발하면서도 유쾌하다. 약간은 막 가는 수림이네 네 식구가 쫄딱 망한 뒤, 돌아가신 외할버지의 옛 여자친구의 빌라‘순례 주택’으로 이사 들어가면서 벌어지는 이야기이다. 솔직하지 못한 엄마, 누군가에게 얹혀사는 데 일가견 있는 아빠, 라면은 끓일 줄 모르고 컵라면에 물만 겨우 부을 줄 아는 고등학생 언니",
					new String[] { "유은실" }, "비룡소", "8949123495", 13000, 8, 0, false, false));
			books.add(new Book("책들의 부엌 (인사이드 에디션)",
					"50쇄 기념 인사이드 에디션 Inside Edition은 안에서 바라보는 소양리의 풍경도 느껴보셨으면 하는 마음을 담아 소양리 북스 키친 내부의 따뜻한 분위기를 담았습니다. 맑은 공기, 편안한 휴식, 그리고 맛있는 책 한 권과 함께, 책들의 부엌에서 잠시 쉬어가세요.  스타트업을 창업해 몇 년간 앞만 보며 달려왔던 주인공 유진, 우연히 찾아간 소양리에서 마법에 걸리듯 북 카페를 열기로 마음먹고 서울 생활을 미련 없이 정리한다. 입맛에 맞는 음식",
					new String[] { "김지혜" }, "팩토리나인", "116534520X", 14500, 4, 0, false, false));
			books.add(new Book("행성 1(양장본 HardCover)",
					"베스트셀러 작가 베르나르 베르베르의 신작 장편소설 『행성』(전2권)이 프랑스 문학 전문 번역가 전미연의 번역으로 열린책들에서 출간되었다. 코로나 바이러스가 전 세계에 맹위를 떨치던 2020년 프랑스에서 발표된 이 작품에는 그 영향이 짙게 깔려 있으며, 베르베르의 전작들에 비해 디스토피아 성격이 강하다. 같은 해 봄 발표한 초단편소설 「호모 콘피누스」에서 지하에 격리된 신인류를 묘사했던 베르베르는 『행성』에서는 땅에 발을 딛지 않고 고층 빌딩에 숨어 사는",
					new String[] { "베르나르 베르베르" }, "열린책들", "8932922365", 16800, 8, 0, true, false));
			books.add(new Book("함수형 자바스크립트",
					"새롭고 올바른 자바스크립트 프로그래밍 기법『함수형 자바스크립트』. 이 책은 Underscore.js 라이브러리를 이용해 아름답고 안전하고 직관적이고 테스트하기 쉬운 함수형 자바스크립트 코드를 구현하는 방법을 설명한다. 함수형 프로그램 기법을 배우려는 자바스크립트 프로그래머와 자바스크립트를 배우려는 함수형 프로그래머에게 유용하도록 구성하였다.",
					new String[] { "마이클 포거스" }, "한빛미디어", "8968480796", 22000, 4, 0, false, false));
			books.add(new Book("가장 빨리 만나는 자바 8",
					"《가장 빨리 만나는 자바 8》는 2014년 4월 출시된 자바 8(Java SE 8, JDK 8)의 주요 기능을 주제별로 정리해서 알려준다. 자바 세상을 완전히 변화시킬 람다 표현식과 스트림 API, 함수형 프로그래밍의 개념을 비중있게 다룬다. 또한 스윙 GUI를 대체하는 JavaFX와 새롭게 추가된 날짜/시간/캘린더 API, 자바 병렬 처리 향상점. JVM용 자바스크립트 엔진 Nashorn에 대해 소개하고 있다.",
					new String[] { "카이 호스트만" }, "길벗", "8966187277", 17800, 8, 0, false, false));
			books.add(
					new Book("자바(속전속결)", "", new String[] { "박용우" }, "영진닷컴", "893143328X", 18000, 2, 0, false, false));
			books.add(new Book("공통수학(자바)", "", new String[] { "남기수 외" }, "영인기획", "8988519132", 15000, 10, 0, false,
					false));
			books.add(new Book("자바스크립트 핵심 가이드(더글라스 크락포드의)",
					"야후의 선임 자바스크립트 아키텍트 더글라스 크락포드의 『자바스크립트 핵심 가이드』. 자바스크립트를 우연히 접했거나 호기심이 생겨 탐험하고 싶어하는 프로그래머를 위해 저술된 것이다.  이 책은 놀라울 정도로 강력한 언어인 자바스크립트에 대한 핵심적인 안내서다. 자바스크립트가 제공하는 여러 가지 기능을 보여준 다음, 그것을 조합하여 사용하는 방법을 찾을 수 있도록 인도한다.  자바스크립트를 우수한 객체지향 언어로 만들 수 있는 장점에 대해서도 다루는 것",
					new String[] { "더글라스 크락포드" }, "한빛미디어", "8979145985", 22000, 4, 0, false, false));
			books.add(new Book("수학 1(자바)", "", new String[] { "김대희 외" }, "영인기획", "8988519248", 12000, 4, 0, false,
					false));
			books.add(new Book("자바(Java) 1학년", "▶ 이 책은 JAVA를 다룬 이론서입니다. JAVA의 기초적이고 전반적인 내용을 학습할 수 있습니다.",
					new String[] { "모리 요시나오" }, "성안당", "8931555695", 17000, 13, 0, false, false));
			books.add(new Book("자바(핵심)(7판)", "▶ 이 책은 자바를 다룬 이론서입니다. 자바의 기초적이고 전반적인 내용을 학습할 수 있도록 구성했습니다.",
					new String[] { "Horstmann Cay S." }, "한티미디어", "8964211839", 35000, 10, 0, false, false));
			books.add(new Book("자바(예제로배우는)", "", new String[] { "김주호 외" }, "기전연구사", "8933604227", 20000, 2, 0, true,
					false));
			books.add(new Book("수학 2 평가(자바)", "", new String[] { "박현민 외" }, "영인기획", "8988519639", 7500, 4, 0, false,
					false));
			books.add(new Book("수학 평가 10-나(자바)", "", new String[] { "전성은 외" }, "영인기획", "8955350163", 5500, 4, 0, false,
					false));
			books.add(new Book("기본수학 10-나(자바)", "", new String[] { "전성은 외" }, "영인기획", "8955350244", 10000, 2, 0, false,
					false));
			books.add(new Book("기본수학 10-가(자바)", "", new String[] { "전성은 외" }, "영인기획", "8955350236", 10000, 2, 0, false,
					false));
			books.add(new Book("수학 1(자바)(북킹 400)", "", new String[] { "왕규채" }, "영인기획", "8988519256", 8500, 4, 0, false,
					false));
			books.add(new Book("자바(ALT+X시리즈 16)", "", new String[] { "신명기" }, "성안당", "8931543271", 4000, 11, 0, false,
					false));
			books.add(new Book("수리영역 수학 2(자바)", "", new String[] { "전성은 외" }, "영인기획", "8988519892", 8500, 4, 0, false,
					false));
			books.add(new Book("공통수학(자바)(북킹 400)", "", new String[] { "왕규채" }, "영인기획", "8988519221", 10000, 2, 0, false,
					false));
			books.add(new Book("자바(JAVA)로 기술한 자료구조 알고리즘 및 응용",
					"컴퓨터 공학 전공서. 이 책은 자바프로그램의 구조와 자바컴파일러와 가상 머신, 성능분석, 점근적 표기법, 성능 측정, 자료구조, 스택, 큐, 스킵 리스트와 해싱, 이진 및 기타 트리, 토너먼트 트리 등으로 구성되었다.",
					new String[] { "Sartaj Sahni" }, "상조사", "8937904071", 35000, 10, 0, false, false));
			books.add(new Book("자바 트러블슈팅",
					"건강한 서비스를 위한 scouter 활용법은 물론, 그 밖의 시스템 장애 극복을 위한 다양한 도구 사용법을 배운다!  기술이 아무리 발전하고 뛰어난 개발자가 있더라도 사람이 만든 프로그램은 언제든 장애가 발생할 수 있습니다. 그럼에도 상당수의 개발자나 시스템 운영자는 트러블슈팅에 대해 교육받을 기회가 많지 않습니다. 그래서 빠르게 장애를 파악하고 분석하여 장애 상황을 피하고 재발을 방지하는 데 작은 도움이 되고자 이 책을 준비하였습니다.",
					new String[] { "이상민" }, "제이펍", "1188621823", 14000, 11, 0, false, false));
			books.add(new Book("명품 자바 에센셜",
					"[명품 자바 에센셜]은 자바 언어에 대한 쉬운 설명과 의미 있는 예제를 도입하여 이론이 프로그래밍으로 이어지게 하고, 이해도 높은 삽화와 그림으로 본문에 쉽게 몰입할 수 있도록 한 자바 입문서이다. 책에는 이론이 자연스럽게 프로그래밍으로 이어지는 예제와 핵심을 점검할 수 있는 CHECK TIME 문제로 이론과 코딩을 한 번에 학습할 수 있도록 했고, 각 장의 요약에는 빈칸을 채우는 재미를 더하였다.",
					new String[] { "황기태" }, "생능출판", "8970508198", 26000, 6, 0, false, false));
			books.add(new Book("공통수학 수학 1(자바)(북킹 400)", "", new String[] { "전성은 외" }, "영인기획", "8988519647", 10000, 2, 0,
					false, false));
			books.add(new Book("자바 8 인 액션",
					"『자바 8 인 액션』은 함수형 언어의 영감을 받아 자바 8에 추가된 람다, 스트림, 함수형 프로그래밍, 병렬화와 공유 가변 데이터, 동작 파라미터화 등의 주요 기능을 자바 개발자에게 알려준다. 책은 실전에 유용한 코드로 람다를 설명한다. 그리고 새로운 스트림 API를 설명하며 기존의 컬렉션 기반 코드를 스트림 API로 쉽게 이해하고 유지보수할 수 있는 코드로 개선하는 방법을 살펴본다. 또한 디폴트 메서드, Optional, CompletableFuture, 새로운",
					new String[] { "라울-게이브리얼 우르마", " 마리오 푸스코", " 앨런 마이크로프트" }, "한빛미디어", "8968481792", 28000, 2, 0,
					false, false));
			books.add(new Book("자바 프로그래밍(비전공자를 위한)",
					"[비전공자를 위한 자바 프로그래밍]은 영어라는 언어와 프로그래밍 언어의 공통점에 착안하여 이 책에서는 자바 코드를 설명할 때 영어를 번역하듯 설명하고, 주요 단어를 영어 단어 외우듯 공부함으로써 영작하는 것처럼 자바 프로그래밍을 할 수 있도록 구성한 책이다. 자바를 조금 더 친숙하게 공부할 수 있도록 자바의 주요 개념을 일상생활에 빗대고, 좀 더 쉽게 이해할 수 있도록 그림을 곁들여 설명하였다.",
					new String[] { "강희은" }, "한빛미디어", "8968488150", 16000, 6, 0, true, false));
			books.add(new Book("Do it! 첫 코딩 with 자바",
					"코딩이 막막한 보통 사람들을 위해 태어난 책! 초등 고학년부터 대학생, 어르신까지 남녀노소 누구나 ‘코딩할 줄 아는 사람’이 되도록 도와주는 책입니다. 디지털 시대에 알아야 할 프로그래밍의 52가지 핵심 개념을 99가지 비유와 그림으로 풀어내 국내 최초 ‘문과식’ 입문서라고도 불립니다.  뿐만 아니라 코딩 초보자가 쉽게 무너지는 부분인 프로그램 설치, 예제파일 다운로드도 없습니다! ‘엘리스’ 플랫폼에 접속해 회원가입하면 누구나 무료로 코딩 실습",
					new String[] { "정동균" }, "이지스퍼블리싱", "1163031186", 14000, 11, 0, false, false));
			books.add(new Book("자바 웹 프로그래밍(프로젝트로 배우는)(IT CookBook 155)",
					"『자바 웹 프로그래밍』는 자바 프로그래밍 초보자나 경험이 있는 사람들 모두에게 적합하게 구성된 책이다. 변화하고 있는 웹 개발 환경에 맞도록 자바, JSP를 기본으로 웹 개발에 필요한 기술을 설명하며 기본실습과 응용실습으로 나누어 배운 내용을 확실하게 이해하고 활용할 수 있도록 구성하였다.",
					new String[] { "황희정" }, "한빛아카데미", "8998756684", 26000, 6, 0, false, false));
			books.add(new Book("쉽게 배우는 자바 프로그래밍(IT CookBook 218)",
					"『쉽게 배우는 자바 프로그래밍』은 객체 지향의 핵심과 자바의 현대적 기능을 충실히 다루면서도 초보자가 쉽게 학습할 수 있게 구성했다. 시각화 도구를 활용한 개념 설명과 군더더기 없는 핵심 코드를 통해 개념과 구현을 한 흐름으로 학습할 수 있으며 ‘기초 체력을 다지는 예제 → 셀프 테스트 → 생각을 논리적으로 정리하며 한 단계씩 풀어 가는 도전 과제 → 스토리가 가미된 흥미로운 프로그래밍 문제’ 등을 통해 프로그래밍 실력을 차근차근 끌어올릴 수 있다",
					new String[] { "우종정" }, "한빛아카데미", "1156643295", 27000, 13, 0, false, false));

			// 승인 대기 중 책 더미데이터
			books.add(new Book("체리새우: 비밀글입니다(30만 부 리커버 특별판)",
					"담겼다. 푸릇푸릇하고 청량한 색감으로 새봄 새 학기의 설렘이 고스란히 전해진다. 등나무 벤치에 앉은 주인공 다현이는 혼자임에도 밝은 미소를 띠고 있다. 오롯이 혼자 설 수 있게 되고서야 누군가와 함께할 때도 자기 자신을 잃지 않을 수 있다는 소설의 메시지가 떠오르는 그림이다. 관계의 피로함에서 벗어나 스스로를 있는 그대로 사랑하게 된 다현이의 더욱 산뜻해진 오늘을 만나 보자.  어떤 친구가 말했다. 우리 모두는 나무들처럼 혼자라고. 좋은 친구는 서로에게 햇살",
					new String[] { "황영미" }, "문학동네", "8954654754", 11500, 20, 1, false, false));
			books.add(new Book("난설헌",
					"제1회 혼불문학상 수상작으로 선정된 최문희의 소설 『난설헌』. 16세기 조선 중기의 천재 여류시인 허난설헌의 삶을 그린 작품이다. 77세의 여성 소설가는 난설헌의 삶과 내면을 꼼꼼하게 풀어내며, 각 장면을 한 편의 세밀화처럼 표현했다. 어린 초희는 자유로운 가풍 속에서 성장하며 당대의 시인으로 꼽혔던 이달에게 시를 배운다. 여성이 존중받을 수 없었던 시대였지만 아버지 허엽과 오빠 허봉은 그녀를 귀한 존재로 여겼고, 초희는 천재적인 재능을 발휘하며 주변",
					new String[] { "최문희" }, "다산책방", "8963706850", 13000, 8, 1, false, false));
			books.add(new Book("오백 년째 열다섯",
					"위즈덤하우스의 청소년 문학 시리즈 ‘텍스트 T’의 첫 권으로 김혜정 작가의 신작 장편소설 『오백 년째 열다섯』이 출간되었다. 단군 신화와 우리 옛이야기에서 탄생한 야호족과 호랑족의 참신한 세계관, 두 족속이 최초 구슬을 두고 벌이는 구슬 전쟁이라는 흥미진진한 스토리, 그리고 오백 년을 열다섯으로 살아온 여자아이라는 독보적인 캐릭터가 더해져 전 세대가 읽을 수 있는 몰입감 넘치는 한국형 판타지가 탄생했다. 또한 '오늘의 만화상' 『연의 편지』로 사랑받았던",
					new String[] { "김혜정" }, "위즈덤하우스", "116812106X", 12500, 1, 1, false, false));
			books.add(new Book("쇼코의 미소",
					"최은영의 첫 소설집 『쇼코의 미소』. 2013년 겨울 《작가세계》 신인상에 중편소설 《쇼코의 미소》가 당선되어 등단, 그 작품으로 다음해 젊은작가상을 수상하며 작품활동을 시작한 최은영이 써내려간 7편의 작품을 수록한 소설집이다. 사람의 마음이 흘러갈 수 있는 정밀한 물매를 만들어냄으로써, 우리들을 바로 그 ‘사람의 자리’로 이끄는 작품들을 만나볼 수 있다.  서로 다른 국적과 언어를 가진 두 인물이 만나 성장의 문턱을 통과해가는 과정을 그려낸 표제작",
					new String[] { "최은영" }, "문학동네", "8954641636", 12000, 4, 1, false, false));
			books.add(new Book("공허한 십자가",
					"히가시노 게이고 역대 최고의 문제작 『공허한 십자가』. 살인과 형벌, 속죄, 사형 제도의 존속, 생명의 소중함 등 결코 가볍지만은 않은 주제를 다룬 이 작품은 속죄에 관한 이야기를 담고 있다. 숨 쉴 수 없을 만큼의 긴박한 전개, 자세하게 그려낸 주인공의 심정을 살펴보며 독자들이 책을 읽는 것에서 나아가 체험하게 해준다. 이를 통해 사형 제도와 속죄에 대해 깊이 생각하게 만드는 수작이다.    어느 날, 프리라이터 하마오카 사요코가 누군가에 의해",
					new String[] { "히가시노 게이고" }, "자음과모음", "8957078150", 13800, 5, 1, false, false));
			books.add(new Book("세 얼간이(영상 소설)(양장본 HardCover)",
					"발리우드의 흥행 기록을 갈아치운 영화 세 얼간이를 책으로 옮긴 『영상 소설 세 얼간이』. 영화의 시나리오를 그대로 살리면서 일류 공학도들의 우정과 사랑, 미래에 대한 고민과 방황을 담아냈다. MIT, UC버클리에 이어 세계 공과대학 3위를 차지한 인도 최고의 공과대학 IIT에서 벌어지는 비뚤어진 천재들의 반란을 유쾌하게 그리고 있다. 라이언, 알록, 하리는 스스로를 얼간이라 부르며 학생들에 대한 관심 없이 오로지 평점과 점수만으로 평가",
					new String[] { "황승윤", " 라지쿠마르 히라니", " 비두 비노드 쇼프라", " 애브히짓 조쉬" }, "북스퀘어", "8994136703", 5000, 10, 1,
					false, false));
			books.add(new Book("곰탕 1: 미래에서 온 살인자",
					"영화 《헬로우 고스트》 《슬로우 비디오》의 김영탁 감독이 쓴 첫 장편소설 『곰탕』 제1권. 카카오페이지에서 연재되어 50만 독자들이 열광했던 스릴러 소설로, 몇 번의 쓰나미 이후 안전한 윗동네와 언제 죽을지 모르는 아랫동네로 나뉜 2063년의 부산에서 2019년의 부산으로 시간여행을 하게 된 한 남자의 이야기를 담고 있다. 유토피아를 꿈꾸는 자들의 간절함이 빚은 잔혹극 같지만 한편, 미래를 향한 절망 짙은 작가의 디스토피아적 세계관이 생생하게 압도하는",
					new String[] { "김영탁" }, "아르테(arte)", "8950973758", 13000, 8, 1, false, false));
			books.add(new Book("이것이 자바다",
					"『이것이 자바다』은 15년 이상 자바 언어를 교육해온 자바 전문강사의 노하우를 아낌 없이 담아낸 자바 입문서이다. 자바 입문자를 배려한 친절한 설명과 배려로 1장에 풀인원 설치 방법을 제공하여 쉽게 학습환경을 구축할 수 있다. 또한 중급 개발자로 나아가기 위한 람다식(14장), JavaFX(17장), NIO(18~19장) 수록되어 있으며 각 챕터마다 확인문제 제공. 풀이와 답은 인터넷 강의에서 친절한 해설을 통해 알려준다.",
					new String[] { "신용권" }, "한빛미디어", "8968481474", 30000, 2, 1, false, false));
			books.add(new Book("모던 자바스크립트 Deep Dive",
					"웹페이지의 단순한 보조 기능을 처리하기 위한 제한적인 용도로 태어난 자바스크립트는 과도하다고 느껴질 만큼 친절한 프로그래밍 언어입니다. 이러한 자바스크립트의 특징은 편리한 경우도 있지만 내부 동작을 이해하기 어렵게 만들기도 합니다.    하지만 자바스크립트는 더 이상 제한적인 용도의 프로그래밍 언어가 아닙니다. 오랜 변화를 거쳐 이제 자바스크립트는 프런트엔드와 백엔드 영역의 프로그래밍 언어로 사용할 수 있는 명실상부한 범용 애플리케이션 개발 언어로",
					new String[] { "이웅모" }, "위키북스", "1158392230", 45000, 10, 1, false, false));
			books.add(new Book("자바를 다루는 기술",
					"『자바를 다루는 기술』은 자바 언어의 기초 문법을 친절하고 자세하게 설명한다. 객체 지향 프로그래밍 개념은 물론, 자바의 자료구조, 제네릭(generics), 리플렉션(reflection) 등 고급 응용 기법들을 다양한 예제를 통해 익힐 수 있도록 구성하였다. 또한 저자의 실무 경험 속에서 얻은 노하우와 팁들을 제시하고, 오픈 소스 라이브러리 응용법 등을 통해 실무 적응력을 높여 독자들이 다양한 개발 현장에서 자바 프로젝트를 어려움 없이 수행할 수 있도록",
					new String[] { "김병부" }, "길벗", "8966185525", 29000, 1, 1, false, false));

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		save();
	}

}
