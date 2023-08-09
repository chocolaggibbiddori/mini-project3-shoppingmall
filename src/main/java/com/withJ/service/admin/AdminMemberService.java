package com.withJ.service.admin;

import com.withJ.dao.MemberDAO;
import com.withJ.dao.WorkerDAO;
import com.withJ.dto.MemberVO;

import java.util.List;

/**
 * 
 * 관리자의 로그인 성공여부 확인 및 회원가입한 사용자 리스트와 관련된 로직을 처리하는 서비스
 * 
 * @author limjeajeong
 *
 */
public class AdminMemberService {
	
	private static final AdminMemberService INSTANCE = new AdminMemberService();
	
	private final MemberDAO memberDAO;
	private final WorkerDAO workerDAO;
	
	private AdminMemberService() {
		memberDAO = MemberDAO.getInstance();
		workerDAO = WorkerDAO.getInstance();
	}
	
	public static AdminMemberService getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 관리자 로그인 화면에서 DB에 입력된 관리자의 아이디와 비밀번호를 확인한다.
	 * 
	 * @param workerId 관리자 아이디
	 * @param workerPw 관리자 비밀번호
	 * @return 입력한 아이디와 비밀번호에 따른 메세지 호출 
	 * @author limjeajeong
	 */
	public String workerLogCheck(String workerId, String workerPw) {
		int result = workerDAO.workerCheck(workerId, workerPw);
		return getMessage(result);
	}
	
	/**
	 * 관리자가 로그인을 시도했을때  DB에서 ID와 PW를 확인하여 로그인 성공 유무에 따른 메세지를 가져온다.
	 * 
	 * @param result 아래의 로그인 성공여부에 따른 결과값
	 * @return msg 로그인 성공 (result가 1인 경우) / 비밀번호 오류(result가 0인 경우)/ 아이디가 존재하지 않을떄(result가 -1인 경우)
	 * @author limjeajeong
	 */
	private String getMessage(int result) {
		String msg = "";
		
		if (loginSuccess(result)) { // 로그인 성공
			msg = "로그인 성공.";
		} else if (loginFailMismatchPassword(result)) {
			msg = "비밀번호를 확인하세요.";
		} else if (loginFailNoExistId(result)) {
			msg = "아이디를 확인하세요.";
		}
		
		return msg;
	}
	
	/**
	 * @param result 로그인 성공시 결과값
	 * @return 로그인 성공시 result가 1이면 true의 결과값을 받음
	 * @author limjeajeong
	 */
	private boolean loginSuccess(int result) {
		return result == 1;
	}
	
	/**
	 * @param result 비밀번호 틀렸을 때 결과값
	 * @return 비밀번호 오류시 result가 0이며 false의 결과값을 받음
	 * @author limjeajeong
	 */
	private boolean loginFailMismatchPassword(int result) {
		return result == 0;
	}
	
	/**
	 * @param result 아이디가 존재하지 않았을 때의 결과값
	 * @return 아이디가 존재하지 않을 경우에 result가 -1이며 false의 결과값을 받음.
	 * @author limjeajeong
	 */
	private boolean loginFailNoExistId(int result) {
		return result == -1;
	}
	
	/**
	 * 회원가입한 사용자의 이름을 기준으로 담은 정보들을 list로 가져온다.
	 * 
	 * @param key 사용자의 이름
	 * @return 사용자의 이름을 기준으로 가입된 목록
	 * @author limjeajeong
	 */
	public List<MemberVO> memberList(String key) {
		return memberDAO.listMember(key);
	}
}