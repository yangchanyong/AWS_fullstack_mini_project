package team1.service.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import team1.service.BookService;
import team1.service.MemberService;
import team1.service.PaymentService;
import team1.vo.*;

public class PaymentServiceImpl implements PaymentService {
	private static final PaymentService paymentService = new PaymentServiceImpl();

	private static final BookService bookService = BookServiceImpl.getInstance();
	private static List<Payment> paymentList;
	private int pIndex = 1;

	{
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("payment.ser"))){
			paymentList = (List<Payment>)ois.readObject();
			Payment LastPayment = null;
			LastPayment = paymentList.get(paymentList.size() - 1);

			if(LastPayment != null){
				pIndex = LastPayment.getPid() + 1;
			}

			Customer c1 = new Customer(1, "ycy", "1234","양찬용", "bnocc@naver.com", "010-1234-1234", "인천광역시", "테스트 주소1");
			Customer c2 = new Customer(2, "bhs", "1234","방한솔", "hsnachos@naver.com", "010-4321-4321", "광주광역시", "테스트 주소2");
			Customer c3 = new Customer(3, "ceg", "1234","천은경", "ceg@naver.com", "010-1243-3412", "서울특별시", "테스트 주소3");

			Book b1 = new Book("레시피보다 중요한 100가지 요리 비결", "《레시피보다 중요한 100가지 요리 비결》은 요리의 기본 ‘비결’을 과학원리와 일러스트와 함께 알기 쉽게 풀어쓴 요리책이다. 이 책에서는 모두 100가지의 요리 비결을 크게 13개 부분으로 나누어 알려준다. 13개 부분은 ‘야채과 과일에 관한 비결’, ‘육류에 관한 비결’, ‘해산물에 관한 비결’, ‘계란에 관한 비결’, ‘밥, 빵, 면류에 관한 비결’, ‘밑준비에 관한 비결’, ‘조리 전반의 기본 비결’, 등으로 순서대로가 아니더라도 가장 자신이", new String[] {""}, "숨쉬는책공장", "1186452129", 16000, 30, 0, false, false);
			Book b2 = new Book("진짜 기본 요리책", "기본 레시피 306개를 수록한 『진짜 기본 요리책』. ‘왕초보’를 위한 요리책을 만들기 위해 실제 요리 초보 100명을 선발하여 함께 메뉴를 골라 구성한 것으로, 따라하는 요리잡지 월간 수퍼레시피 테스트키친팀이 검증한 기본 메뉴 306가지, 응용방법 56가지 총 360여 개의 레시피가 담겨 있다. 재료 손질법을 사진과 함께 알려줌은 물론, 분량과 불의 세기, 조리시간까지 정확히 제시하며, 기호에 따라 선택할 수 있도록 다양한 양념 옵션", new String[] {"월간 수퍼레시피"}, "레시피팩토리", "8996347272", 15800, 1, 0, false, false);

			Order o1 = new Order(b1, 1);
			Order o2 = new Order(b1, 1);

			c1.getCart().addOrder(o1);
			c1.getCart().addOrder(o2);



		} catch (FileNotFoundException e){
			// 최초 실행시 파일이 없을 경우 기본 결제 리스트 데이터로 초기화

			paymentList = new ArrayList<>();

			Customer c1 = new Customer(1, "ycy", "1234","양찬용", "bnocc@naver.com", "010-1234-1234", "인천광역시", "테스트 주소1");
			Customer c2 = new Customer(2, "bhs", "1234","방한솔", "hsnachos@naver.com", "010-4321-4321", "광주광역시", "테스트 주소2");
			Customer c3 = new Customer(3, "ceg", "1234","천은경", "ceg@naver.com", "010-1243-3412", "서울특별시", "테스트 주소3");

			Book b1 = new Book("레시피보다 중요한 100가지 요리 비결", "《레시피보다 중요한 100가지 요리 비결》은 요리의 기본 ‘비결’을 과학원리와 일러스트와 함께 알기 쉽게 풀어쓴 요리책이다. 이 책에서는 모두 100가지의 요리 비결을 크게 13개 부분으로 나누어 알려준다. 13개 부분은 ‘야채과 과일에 관한 비결’, ‘육류에 관한 비결’, ‘해산물에 관한 비결’, ‘계란에 관한 비결’, ‘밥, 빵, 면류에 관한 비결’, ‘밑준비에 관한 비결’, ‘조리 전반의 기본 비결’, 등으로 순서대로가 아니더라도 가장 자신이", new String[] {""}, "숨쉬는책공장", "1186452129", 16000, 30, 0, false, false);
			Book b2 = new Book("진짜 기본 요리책", "기본 레시피 306개를 수록한 『진짜 기본 요리책』. ‘왕초보’를 위한 요리책을 만들기 위해 실제 요리 초보 100명을 선발하여 함께 메뉴를 골라 구성한 것으로, 따라하는 요리잡지 월간 수퍼레시피 테스트키친팀이 검증한 기본 메뉴 306가지, 응용방법 56가지 총 360여 개의 레시피가 담겨 있다. 재료 손질법을 사진과 함께 알려줌은 물론, 분량과 불의 세기, 조리시간까지 정확히 제시하며, 기호에 따라 선택할 수 있도록 다양한 양념 옵션", new String[] {"월간 수퍼레시피"}, "레시피팩토리", "8996347272", 15800, 1, 0, false, false);

			Order o1 = new Order(b1, 1);
			Order o2 = new Order(b1, 1);

			c1.getCart().addOrder(o1);
			c1.getCart().addOrder(o2);

			Payment p = new Payment(pIndex, c1, c1.getCart().getOrderList(), "테스트 주소", PaymentStatus.PENDING, new Date());

			paymentList.add(p);

			//Payment payment()
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	// private List<Payment> payments = new ArrayList<Payment>();


	private PaymentServiceImpl() {}

	public static PaymentService getInstance() {
		return paymentService;
	}

	public static List<Payment> getPaymentList(){
		return paymentList;
	}

	/**
	 * @param loginUser, address
	 * 입력한다(구매한다)
	 * @author 방한솔
	 * @since 23/02/08
	 */
	@Override
	public void purchase(Customer loginUser, String address) {
		if(loginUser == null)
		{
			System.err.println("고객정보 정보가 존재하지 않습니다.");
			return;
		}

		String inputAddress;

		if(address.equals("0")){
			inputAddress = loginUser.getAddress();
		} else {
			inputAddress = address;
		}

		for(Order o : loginUser.getCart().getOrderList()) {
			if(o.getQuantity() > o.getBook().getStock())
				System.out.println("책의 재고 숫자가 부족합니다.");
				return;
		}

		//Payment p = new Payment(inputAddress);

		Payment p = new Payment(1, loginUser, loginUser.getCart().getOrderList(), inputAddress, PaymentStatus.PENDING, new Date());

		for(Order o : loginUser.getCart().getOrderList()) {
			Book orderBook = o.getBook();

			bookService.setBookStock(orderBook.getIsbn(), orderBook.getStock() - o.getQuantity());

			//orderBook.setStock(orderBook.getStock() - o.getQuantity());
		}

		loginUser.getCart().getOrderList().clear();

		paymentList.add(p);
		save();
	}

	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * @param paymentId
	 * @return 기능 설명
	 * 조회한다(주문내역 조회한다)
	 */
	@Override
	public Payment findBy(int paymentId) {
		for(Payment p : paymentList) {
			if(paymentId == p.getPid()) {
				display(p);
				return p;
			}
		}
		return null;
	}


	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * @param paymentId
	 * @return 기능 설명
	 * 삭제한다(환불처리, 실제 삭제 X)
	 */
	@Override
	public void refund(Customer customer, int paymentId) {
		Payment p = findBy(paymentId);
		if(p == null) {
			System.err.println("주문정보가 존재하지 않습니다!");
		}

		for(Order o : customer.getCart().getOrderList()) {
			Book orderBook = o.getBook();
			orderBook.setStock(orderBook.getStock() + o.getQuantity());
		}
		
		// p.getBookList() : 주문한 책 리스트
		// bookStock : 전체 책 리스트
			/*
		for(Book b : p.getBookList()) {
			for(int i = 0 ; i < bookStocks.size() ; i++) {
				//if(b.getbId() == bookStocks.get(i).getbId()) {
					Book updateBook = bookStocks.get(i);
					// 재고 수정
					// updateBook.setStock(paymentId);
					bookStocks.set(i, updateBook);
				//}
			}
		}
*/
		save();
	}

	/**
	 * 결제 상세 내용을 콘솔에 출력한다.
	 * @author 방한솔
	 * @since 23/02/08
	 * @param p
	 * 메서드 생성
	 *
	 * @since 23/02/12
	 * 주문자, 주문 상세내역 추가 및 메서드 수정
	 */
	@Override
	public void display(Payment p) {
		System.out.println("=============================");
		System.out.println("주문 번호 : " + p.getPid());
		System.out.println("주문자 : " + p.getCustomer().getName());
		System.out.println("구매날짜 : " + p.getRegDate());
		for(Order o : p.getOrderList()) {
			System.out.println("제목 : " + o.getBook().getTitle());
			System.out.println("출판사 : " + o.getBook().getPublisher());
			System.out.println("가격 : " + o.getBook().getPrice());
			System.out.println("수량 : " + o.getQuantity());
		}
		System.out.println("총합 : " + p.getTotalPrice());
		System.out.println("=============================");
		
	}

	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * 전체 결제 리스트를 출력한다.
	 */
	@Override
	public void list() {
		for (Payment payment : paymentList) {
			display(payment);
		}
	}

	@Override
	public void list(PaymentStatus paymentStatus){
		for (Payment payment : paymentList) {
			if(payment.getStatus() == paymentStatus)
				display(payment);
		}
	}

	@Override
	public void list(Customer customer) {
		for (Payment payment : paymentList) {
			if(payment.getCustomer().getId().equals(customer.getId()))
				display(payment);
		}
	}

	private void save() {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("payment.ser"))){
			oos.writeObject(paymentList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
