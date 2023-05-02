package team1.service;

import java.util.List;

import team1.vo.Book;

// 책 정보 관련 서비스
public interface BookService {
	

	/**
	 * @author 천은경
	 * @since 23/02/07 + 23/02/09 수정
	 * @param Book
	 * @return Book 출판사의 새로운 책 등록 요청
	 */
	void callReg();
	
	/**
	 * @author 천은경
	 * @since 23/02/07 + 23/02/09 수정
	 * @param
	 * @return Book 출판사에게 요청받은 책을 서점에 등록
	 */
	void bookReg();
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param 
	 * @return 추천도서 설정
	 */
	public void setPicks();
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param 
	 * @return 베스트셀러 설정
	 */
	public void setBest();
		
	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param String isbn
	 * @return books에서 isbn이 입력값과 같은 Book, 없으면 null
	 * 모든 책에서 정보에 맞는 책 찾기
	 */
	public Book findBy(String isbn);
	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param String isbn, int status
	 * @return books에서 element(isbn or publisher), status가 입력값과 같은 Book, 없으면 null
	 */
	public Book findBy(String isbn, int status);
	
	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param int status
	 * @return books에서 status가 동일한 책 리스트
	 */
	public List<Book> findList(int status);
	/**
	 * @author 천은경
	 * @since 23/02/13
	 * @param String publisher
	 * @return books에서 publisher가 동일한 책 리스트
	 */
	public List<Book> findList(String publisher);
	
	/**
	 * @author 천은경
	 * @since 23/02/10 + 수정 23/02/11
	 * @param List<Book> page, int size, String level
	 * @return sear의 페이징 작업. (Member level에 따라 다른 출력 형태로), 선택 book return
	 */
	public void paging(List<Book> page, int size, String level);	
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 베스트셀러 리스트 출력
	 */
	public void bestSellersList();
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 베스트셀러 중 랜덤 1권 출력
	 */
	public void bestSellers();
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 추천도서 리스트 출력
	 */
	public void picksList();
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param
	 * @return 추천도서 중 랜덤 1권 출력
	 */
	public void picks();
	
	/**
	 * @author 천은경
	 * @since 23/02/07 + 수정 23/02/10 + 수정 23/02/11
	 * @param
	 * @return 등록된 책 중 title에 search와 일치하는 부분이 있는 Book 일치 목록 조회(검색) : 제목별
	 */
	void searchTitle();
	
	/**
	 * @author 천은경
	 * @since 23/02/07 + 수정 23/02/10
	 * @param
	 * @return 등록된 책 중 author에 search와 일치하는 부분이 있는 Book 일치 목록 조회(검색) : 작가별
	 */
	void searchAuthors();
	
	/**
	 * @author 천은경
	 * @since 23/02/14
	 * @param 
	 * @return 등록된 책 중 isbn과 일치하는 Book 조회(검색)
	 */
	public void searchIsbn();
	
	/**
	 * @author 천은경
	 * @since 23/02/08 + 수정 23/02/10 + 수정 23/02/13
	 * @param String publisher
	 * @return 해당 출판사의 책 목록 조회
	 */
	void publisherList(String publisher);
	
	/**
	 * @author 천은경
	 * @since 23/02/07 + 수정 23/02/10
	 * @param int status
	 * @return books에서 조건에 맞는 book 페이징 된 조회 (관리자 모드) (status : 0 = 정식등록 / 1 = 대기중 / 2 = 요청반려)
	 */
	void bookList(int status);
	
	/**
	 * @author 천은경
	 * @since 23/02/12 + 수정 23/02/13
	 * @param
	 * @return 기능 설명 출판사 또는 관리자의 책 정보 수정
	 */
	void bookModify();
	
	/**
	 * @author 천은경
	 * @since 23/02/11
	 * @param
	 * @return books의 Book 삭제 관리자가 isbn코드를 입력하여 등록된 책 폐기
	 */
	void bookDelete(int member);
	
	/**
	 * @author 천은경
	 * @since 23/02/08 + 수정 23/02/11
	 * @param
	 * @return books에서 status가 1인 Book 중 삭제 관리자가 승인 대기 책 리스트 중 요청 반려
	 */
	void reject();



	public void setBookStock(String isbn, int quantity);
	
	
}
