package team1.service;

import team1.vo.Member;
import team1.vo.Publisher;

public interface PublisherService extends MemberService {

	// 회원가입
	Member register(int x);
	
	
	
	Publisher findById(String id);
//	Publisher getloginUser();
	// 회원정보수정
	void modify();
	// 회원탈퇴
	void remove();
	
	// 승인요청한다(입력한다)
	
	// 조회한다(본인이 승인 요청한 것 / 승인된 것)
	
	// 수정요청한다
	
	// 삭제요청한다
}


