package team1.service;

import java.util.List;

import team1.vo.Book;
import team1.vo.Customer;
import team1.vo.Payment;
import team1.vo.PaymentStatus;

// 결제 서비스
public interface PaymentService {

	/**
	 * @param loginUser
	 * @return 기능 설명
	 * 입력한다(구매한다)
	 * @author 방한솔
	 * @since 23/02/08
	 */
	void purchase(Customer loginUser, String address);
	
	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * @param paymentId
	 * @return 기능 설명
	 * 조회한다(주문내역 조회한다)
	 */
	public Payment findBy(int paymentId);
	
	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * @param customer, paymentId
	 * @return 기능 설명
	 * 삭제한다(환불처리, 실제 삭제 X)
	 */
	void refund(Customer customer, int paymentId);
	
	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * @param p
	 * @return 기능 설명
	 * 결제 내용을 콘솔에 출력한다.
	 */
	void display(Payment p);
	
	/**
	 * @author 방한솔
	 * @since 23/02/08
	 * @return 기능 설명
	 * 전체 결제 리스트를 출력한다.
	 */
	void list();

	public void list(PaymentStatus paymentStatus);

	public void list(Customer customer);
}
