package team1.vo;

import java.io.Serializable;

/**
 * 주문 클래스
 * @author 방한솔
 * @since 23/02/12
 * 클래스 최초생성
 */
public class Order implements Serializable {
    private Book book; // 주문하려는 책의 정보
    private int quantity; // 주문 수량

    /*public Order() {
    }*/

    public Order(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 주문 조회 리스트
     */
    public void display() {

        System.out.println("ISBN : " + book.getIsbn() + ", 제목 : " + book.getTitle() + ", 수량 : " + quantity);
    }

    @Override
    public String toString() {
        return "Order{" +
                "book=" + book +
                ", quantity=" + quantity +
                '}';
    }
}
