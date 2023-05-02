package team1.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

	/*
	 * 결제 클래스
	 * @author 방한솔
	 * @since 23/02/07
	 * 클래스 최초 생성
	 *
	 * @since 23/02/08
	 * 결제 번호, 결제한 책 리스트, 결제회원 정보, 결제 총합금액 변수 추가
	 *
	 * @since 23/02/12
	 * 주소, 주문 상태값, 결제날짜 추가
	 * 상품정보 리스트 -> 주문 내용 리스트, 회원 변수 타입 수정
	 * 결제 총합 변수 -> 메서드로 수정
	 */
public class Payment implements Serializable {
	private int pid; // 주문id
	private Customer customer; // 주문한 회원의 정보
	private List<Order> orderList = new ArrayList<Order>(); // 상품정보 리스트
	private String address; // 주소 정보
	private PaymentStatus status; // 주문 상태 값(주문 승인 대기, 주문 완료, 주문 취소(환불))
	private Date regDate; // 결제날짜

	/*public Payment() {
		super();
	}*/

		public Payment(int pid, Customer customer, List<Order> orderList, String address, PaymentStatus status, Date regDate) {
			this.pid = pid;
			this.customer = customer;
			this.orderList = orderList;
			this.address = address;
			this.status = status;
			this.regDate = regDate;
		}

		public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	/**
	 * @author 방한솔
	 * @since 23/02/12
	 * @return result
	 * 주문 리스트에서 전체 결제금액을 계산한다.
	 */
	public int getTotalPrice(){
		int result = 0;

		for(Order o : orderList){
			result += o.getBook().getPrice() * o.getQuantity();
		}

		return result;
	}

}
