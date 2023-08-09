package com.withJ.service;

import com.withJ.dao.QnaDAO;
import com.withJ.dto.MemberVO;
import com.withJ.dto.QnaVO;

import java.util.List;
import java.util.Objects;

/**
 * QnA 질문 정보를 가져와 처리하는 서비스
 * @author leeseungjun
 */

public class QnaService {

    private static final QnaService INSTANCE = new QnaService();

    private final QnaDAO qnaDAO;

    private QnaService() {
        qnaDAO = QnaDAO.getInstance();
    }

    /**
     * 사용자의 로그인 여부를 확인하고, 로그인 된 경우 회원의 질문 목록(QnA) 정보를 가져와서 웹 페이지에 출력하는 로직
     * @return QnaService 인스턴스
     * @author leeseungjun
     */
    
    public static QnaService getInstance() {
        return INSTANCE;
    }

    /**
     * 주어진 사용자 ID에 대한 QnA 항목 목록
     * @param userId 사용자 ID 문자열
     * @return QnaVO 
     * @author leeseungjun 
     */
    
    public List<QnaVO> getQnaList(String userId) {
        return qnaDAO.listQna(userId);
    }

    /**
     * 로그인한 사용자의 특정 QnA 질문 정보를 가져와 웹 페이지에 출력하는 메서드
     * @param qseq QnA 시퀀스 번호
     * @return 해당 시퀀스 번호의 QnaVO 객체
     * @author leeseungjun
     */
    
    public QnaVO getQna(int qseq) {
        return qnaDAO.getQna(qseq);
    }

    /**
     * 로그인한 사용자의 QnA 질문 작성 기능을 처리하고 리다이렉션하는 메서드
     * @param qnaVO 생성할 QnA 질문에 대한 정보를 담은 QnaVO 객체
     * @param userid QnA를 작성할 사용자의 ID
     * @author leeseungjun
     */
    public void writeQna(QnaVO qnaVO, String userid) {
        qnaDAO.insertQna(qnaVO, userid);
    }

    /**
     * 로그인한 사용자에 대해 QnA 질문 작성 폼을 출력하는 메서드
     * @param loginUser 로그인한 사용자의 MemberVO 객체
     * @return QnA 작성 기능에 대해 표시할 URL 문자열
     * @author leeseungjun
     */
    public String getQnaWriteUrl(MemberVO loginUser) {
        if (Objects.isNull(loginUser)) {
            return "/shopping_complete/action/member/loginForm";
        }

        return "/qna/qnaWrite.jsp";
    }
}
