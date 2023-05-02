package team1.service;

import team1.vo.Admin;

public interface AdminService extends MemberService {
	
	// 관리자 로그인기능
	Admin findById(String id);
//	Admin getLoginUser();
	
	
	// publisher 회원승인
	
	// 요청받은 책을 조회한다(승인요청 들어온 책 조회 / 모든책 + bId(서점index)까지)
	
	// 요청받은 책을 승인한다(입력한다 -> List<Book> Books 로 들어옴)
	
	// 요청받은 책을 수정한다
	
	// 요청받은 책을 삭제한다
}
