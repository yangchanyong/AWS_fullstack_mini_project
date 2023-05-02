package team1.service;

import team1.vo.Member;

public interface MemberService {
	
	
	//가입한다
	Member register(int x);
		
	//로그인
	Member login();
	
	int checkRange(int num);
	int checkRange(int num, int start, int end);
	Member getLoginUser();
	void logout();

	//	String checkIsbn(String isbn) {
//	if (isbn.length() < 10) {
//		throw new IsbnException(isbn);
//	}
//	return isbn;
//}




}
