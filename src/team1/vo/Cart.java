package team1.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 장바구니 클래스, 서비스의 역할을 해야함

/**
 * @author 방한솔
 * @since 23/02/08
 * 
 */
public class Cart implements Serializable {
	// Sample Code
	//List<Product> cart = new ArrayList<Product>();

	List<Order> orderList = new ArrayList<>();

	public List<Order> getOrderList() {
		return orderList;
	}

	/**
	 * @author 방한솔
	 * @since 23/02/14
	 * @param order
	 * 주문 리스트에 주문을 추가한다.
	 */
	public void addOrder(Order order) {
		// 담을 상품을 선택하세요 >> prodId >> Product
		if(order != null){
			orderList.add(order);
		}
	}

	/**
	 * @author 방한솔
	 * @since 23/02/14
	 * @param bookId, quantity
	 * 주문의 수량을 수정한다.
	 */
	public void updateOrder(String bookId, int quantity){
		int index = findIdx(bookId);

		if(index != -1){
			Order o = orderList.get(index);
			o.setQuantity(quantity);
			orderList.set(index, o);
		}
	}

	/**
	 * @author 방한솔
	 * @since 23/02/14
	 * 주문 리스트에서 주문을 삭제한다.
	 */
	public void removeOrder(String bookId) {
		Order findOrder = findByBookId(bookId);

		if(findOrder != null){
			orderList.remove(findOrder);
		}
	}

	/**
	 * @author 방한솔
	 * @since 23/02/12
	 * 장바구니의 주문 목록을 출력한다(요약정보)
	 */
	public void display() {
		// System.out.println(cart);
		System.out.println("==================================================================");
		if(orderList.size() == 0){
			System.out.println("장바구니에 상품이 존재하지 않습니다.");
			System.out.println("==================================================================");
			return;

		}

		for(Order o : orderList){
			o.display();
		}

		System.out.println("총 합 = " + getTotalPrice());
		System.out.println("==================================================================");
	}

	/**
	 * @author 방한솔
	 * @since 23/02/14
	 * @param bookId
	 * @return result
	 * 입력한 bookId와 일치하는 주문정보를 반환한다.
	 */
	public Order findByBookId(String bookId){
		Order result = null;

		for(Order o : orderList){
			if(o.getBook().getIsbn().equals(bookId)){
				result = o;
				break;
			}
		}
		return result;
	}

	/**
	 * @author 방한솔
	 * @since 23/02/14
	 * @param bookId
	 * @return index
	 * 입력한 bookId와 일치하는 주문정보의 index를 반환한다.
	 */
	public int findIdx(String bookId){
		int result = -1;

		for(int i = 0 ; i < orderList.size() ; i++){
			Order o = orderList.get(i);

			if(o.getBook().getIsbn().equals(bookId)){
				result = i;
				break;
			}
		}

		return result;
	}


	public int getTotalPrice(){
		int result = 0;

		for(Order o : orderList){
			result += o.getBook().getPrice() * o.getQuantity();
		}

		return result;
	}
}
