package com.withJ.service.admin;

import com.withJ.dao.QnaDAO;
import com.withJ.dto.QnaVO;

import java.util.List;

/**
 * 관리자가 Q&A와 관련된 서비스를 처리하는 곳
 *
 * @author kimjunyoung
 */
public class AdminQnaService {

    private static final AdminQnaService INSTANCE = new AdminQnaService();

    private final QnaDAO qnaDAO;

    private AdminQnaService() {
        qnaDAO = QnaDAO.getInstance();
    }

    public static AdminQnaService getInstance() {
        return INSTANCE;
    }

    /**
     * 번호에 따라 Q&A정보를 얻는 역할
     *
     * @param qseq Q&A 번호
     * @return Qna 정보 전체
     * @author kimjunyoung
     */
    public QnaVO getQna(String qseq) {
        return qnaDAO.getQna(Integer.parseInt(qseq));
    }

    /**
     * Q&A의 모든 목록을 가져오는 역할
     *
     * @return qnaList Q&A를 모아놓은 리스트를 가져오는 메서드
     * @author kimjunyoung
     */
    public List<QnaVO> getList() {
        return qnaDAO.listAllQna();
    }

    /**
     * Q&A에 답장을 달아서 저장하여 Q&A의 접근을 막는 역할
     *
     * @author kimjunyoung
     */
    public void saveList(String qseq, String reply) {
        QnaVO qnaVO = new QnaVO();
        qnaVO.setQseq(Integer.parseInt(qseq));
        qnaVO.setReply(reply);

        qnaDAO.updateQna(qnaVO);
    }
}
