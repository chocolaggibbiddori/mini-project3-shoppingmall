package com.withJ.controller;

import com.withJ.dto.MemberVO;
import com.withJ.dto.QnaVO;
import com.withJ.service.QnaService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>사용자의 질문과 답변(Q&A) 관련 요청을 처리하는 역할을 담당<br>
 * /action/Qna 이후의 경로를 통해 각각 알맞는 메서드를 호출하도록 구성함</p>
 * <p>이 컨트롤러에서 처리 가능한 경로<br>
 * 1. /list<br>
 * 2. /view<br>
 * 3. /write<br>
 * 4. /writeform<br>
 * </p>
 * @author leeseungjun
 */

@WebServlet("/action/qna/*")
public class QnaController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final QnaService qnaService = QnaService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// 요청된 URL 경로를 검색
    	String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(request, response, path);
    }

    // 경로에 따라 요청을 처리하는 메소드
    private void pathMapping(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        switch (path) {
            case "/list": list(request, response); break;
            case "/view": view(request, response); break;
            case "/write": write(request, response); break;
            case "/writeForm": writeForm(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    // list 요청을 처리하는 메소드
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/qna/qnaList.jsp";
        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (Objects.isNull(loginUser)) {
            response.sendRedirect("/shopping_complete/action/member/loginForm");
            return;
        }

        List<QnaVO> qnaList = qnaService.getQnaList(loginUser.getId());

        request.setAttribute("qnaList", qnaList);
        request.getRequestDispatcher(url).forward(request, response);
    }
    
    // view 요청을 처리하는 메소드
    private void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/qna/qnaView.jsp";
        int qseq = Integer.parseInt(request.getParameter("qseq"));

        QnaVO qnaVO = qnaService.getQna(qseq);

        request.setAttribute("qnaVO", qnaVO);
        request.getRequestDispatcher(url).forward(request, response);
    }

    // write 요청을 처리하는 메소드
    private void write(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        QnaVO qnaVO = new QnaVO();
        qnaVO.setSubject(request.getParameter("subject"));
        qnaVO.setContent(request.getParameter("content"));

        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (Objects.isNull(loginUser)) {
            response.sendRedirect("/shopping_complete/action/member/loginForm");
            return;
        }

        qnaService.writeQna(qnaVO, loginUser.getId());

        response.sendRedirect("/shopping_complete/action/qna/list");
    }
    
    // writeForm 요청을 처리하는 메소드
    private void writeForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        String url = qnaService.getQnaWriteUrl(loginUser);

        request.getRequestDispatcher(url).forward(request, response);
    }
}
