package team1.service.impl;

import team1.exception.*;
import team1.service.*;
import team1.utils.BookStoreUtils;
import team1.vo.*;

public class BookStoreServiceImpl implements BookStoreService {
	private static BookStoreService bookStoreService = new BookStoreServiceImpl();
	private static boolean runCheck = true;

//	AdminService adminService = AdminServiceImpl.getInstance();
//	PublisherService publisherService = PublisherServiceImpl.getInstance();
//	CustomerService customerService = CustomerServiceImpl.getInstance();

	MemberService memberService = MemberServiceImpl.getInstance();
	BookService bookService = BookServiceImpl.getInstance();

	PaymentService paymentService = PaymentServiceImpl.getInstance();

	private Member loginUser;
	private BookStoreServiceImpl(){}
	public static BookStoreService getInstance() {
		return bookStoreService;
	}

	@Override
	public void run() {
		while (runCheck){
			try {
				mainMenu();
			}
			catch (
					BookRangeException |
					BookStoreException |
					IdException |
					IsbnException |
					RequiredInputException |
					MyrangeException e
			) {
				System.err.println(e.getMessage());
			} catch(NumberFormatException e){
				System.err.println("숫자를 입력해주세요.");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	
	public void logo() {
		String logo = "     #####    ###    ####       ###    ######    ######  ###  ### \n" +
				"       ##     ###     ##        ###     ##  ##     ##     ###  #  \n" +
				"       ##     # ##    ##        # ##    ##   ##    ##     # ## #  \n" +
				"       ##    #  ##    ##       #  ##    ##   ##    ##     # ## #  \n" +
				"       ##   #######   ##   #  #######   ##   ##    ##     #  ###  \n" +
				"       ##   #    ##   ##  ##  #    ##   ##  ##     ##     #   ##  \n" +
				"    ## ##   ##   ### #######  ##   ### ######    ######  ###   ## \n" +
				"    ###\n";
		System.out.println(logo);
	}
	
	private void mainMenu() {
		logo();
		Member member = memberService.getLoginUser();
		String menuText = "";

		if(member == null){
			menuText += "1.추천도서\t2.도서검색\t3.회원가입\t4.로그인\t0. 종료";
		} else {
			System.out.println(member.getName() + "님 환영합니다.");
			if (member instanceof Customer) {
				menuText = "1.추천도서\t2.도서검색\t3.장바구니\t4.로그아웃";
			} else if (member instanceof Publisher) {
				menuText += "1.상품등록\t2.상품수정\t3.로그아웃";
			} else if (member instanceof Admin) {
				menuText = "1.상품 승인\t2.추천도서 수정\n3.주문관리\t4.로그아웃";
			}
		}

		int input = BookStoreUtils.nextInt(menuText);

		if (member instanceof Customer) {
			customerMenu(input);
		} else if (member instanceof Publisher) {
			publisherMenu(input);
		} else if (member instanceof Admin) {
			adminMenu(input);
		} else if(member == null){
			nonLoginMenu(input);
		}



	}

	void nonLoginMenu(int input){
		// 1.추천도서	2.도서검색	3.회원가입	4.로그인  0. 종료

		switch(input) {
			/*case 1:
				bookService.bestSellers();
				//bookService.bestSellersList();
				break;*/
			case 1:
				bookService.picksList();
				break;
			case 2:
				bookSearch();
				break;
			case 3:
					// register 수정 필요
				memberService.register(0);
				break;
			case 4:
				memberService.login();
				break;
			case 0 :
				runCheck = false;
				break;
			default:
				System.err.println("입력 에러");
		}
	}

	void customerMenu(int input){
		//	1.추천도서	2.도서검색	3.장바구니   4. 로그아웃
		switch(input){
			/*
			case 1:
				bookService.bestSellers();
				break;
				*/
			case 1:
				bookService.picksList();
				break;
			case 2:
				bookSearch();
				bookOrderMenu();
				break;
			case 3:
				memberService.getLoginUser().getCart().display();
				if(memberService.getLoginUser().getCart().getOrderList().size() > 0){
					cartMenu();
				}
				break;
			case 4:
				memberService.logout();
				break;
			default:
				System.err.println("입력 에러");
		}

	}

	void publisherMenu(int input){
		// 1.상품등록	2.상품수정 3.로그아웃
		try {
			switch (input) {
				case 1:
					bookService.callReg();
					break;
				case 2:
					bookService.bookModify();
					break;
				case 3:
					memberService.logout();
					break;
				default:
					System.err.println("입력 에러");
					break;
			}
		}
		catch (IsbnException e){
			System.err.println(e.getMessage());
		}
	}

	void adminMenu(int input){
//		1.상품 승인	2.추천도서 수정
//		3.주문관리 4. 로그아웃
		switch(input) {
			case 1:
				bookService.bookList(1);
				bookService.bookReg();
				break;
			case 2:
				bookService.setPicks();
				break;
			case 3:
				paymentService.list(PaymentStatus.PENDING);
				break;
			case 4:
				memberService.logout();
				break;
			default:
				System.err.println("입력에러");
		}
	}

	private void bookSearch(){
		//String menuText = "1.도서명 검색\t2.작가 검색\t3.도서번호 검색";
		String menuText = "1.도서명 검색\t2.작가 검색\t3.ISBN 검색";

		int input = BookStoreUtils.nextInt(menuText);

		switch(input) {
			case 1:
				// 도서명 검색
				bookService.searchTitle();
				break;
			case 2:
				// 작가 검색
				bookService.searchAuthors();
				break;
			case 3:
				// 도서 검색
				String isbn = BookStoreUtils.nextLine("검색할 isbn을 입력해주세요");
				Book findBook = bookService.findBy(isbn);
				findBook.display(findBook);
				break;
			default:
				System.out.println("에러");
		}
	}

	private void bookOrderMenu(){
		String menuText = "1.책 주문\t0.취소";

		int input = BookStoreUtils.nextInt(menuText);

		switch (input){
			case 1:
				addOrder();
				break;
			case 0:
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			default:
				System.err.println("입력 에러");
		}
	}

	private void cartMenu(){
		String menuText = "1.장바구니 결제\t2.장바구니 삭제\t0.취소";

		int input = BookStoreUtils.nextInt(menuText);

		switch (input){
			case 1:
				// 주소 입력 필요
				paymentService.purchase((Customer) memberService.getLoginUser(), "");
				memberService.getLoginUser().getCart().getOrderList().clear();

				System.out.println("장바구니의 상품이 결제가 되었습니다.");
				break;
			case 2:
				memberService.getLoginUser().getCart().getOrderList().clear();
				System.out.println("장바구니의 상품을 전체 삭제하였습니다.");
				break;
			case 0:
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			default:
				System.err.println("입력 에러");
		}
	}
	private void addOrder(){

		String isbn = BookStoreUtils.nextLine("장바구니에 넣을 책의 ISBN 번호를 입력해주세요");
		Book book = bookService.findBy(isbn);

		if(book == null){
			return;
		}

		int quantity = BookStoreUtils.nextInt("주문수량을 입력해주세요");
		Order newOrder = new Order(book, quantity);

		memberService.getLoginUser().getCart().addOrder(newOrder);
	}
}
