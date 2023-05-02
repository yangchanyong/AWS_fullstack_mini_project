package team1.service;

import team1.vo.Customer;
import team1.vo.Member;

public interface CustomerService extends MemberService {
		
	/**
	* @author 양찬용
	* @since 23/02/08
	* @param register
	* @return 기능 설명
	* 회원가입
	*/
	Customer register(int x);
	
	
	/**
	* @author 양찬용
	* @since 23/02/08
	* @param findById, findByPw
	* @return 기능 설명
	* id, pw 조회
	*/
	Customer findById(String id);
	Customer findByCode(int code);
	

	/**
	* @author 양찬용
	* @since 23/02/08
	* @param modify
	* @return 기능 설명
	* 회원 정보 수정
	*/
	void modify();

	/**
	* @author 양찬용
	* @since 23/02/08
	* @param remove
	* @return 기능 설명
	* 회원 탈퇴 
	*/
	void remove();
	
	/**
	* @author 양찬용
	* @since 23/02/09
	* @param login
	* @return 기능 설명
	* 회원 로그인 
	*/
//	Customer getLoginUser();	 
	String chekId(String id);
	
	//모든 구매자가 장바구니를 하나씩 가진다
	
	
}
