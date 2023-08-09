package com.withJ.controller.admin;

import com.withJ.dto.QnaVO;
import com.withJ.service.admin.AdminQnaService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>관리자가 Q&A을 관리하는 컨트롤러</p>
 * 1. /list: Q&A 목록을 보여주는 역할<br>
 * 2. /detail: Q&A 상세정보를 보여주는 역할<br>
 * 3. /resave: Q&A 응답을 한 것을 나타내는 역할<br>
 *
 * @author kimjunyoung
 */
@WebServlet("/action/admin/qna/*")
public class AdminQnaController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminQnaService qnaService = AdminQnaService.getInstance();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(request, response, path);
    }

    private void pathMapping(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        switch (path) {
            case "/list": list(request, response); break;
            case "/detail": detail(request, response); break;
            case "/resave": resave(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/qna/qnaList.jsp";

        List<QnaVO> qnaList = qnaService.getList();

        request.setAttribute("qnaList", qnaList);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/qna/qnaDetail.jsp";
        String qseq = request.getParameter("qseq").trim();

        QnaVO qnaVO = qnaService.getQna(qseq);

        request.setAttribute("qnaVO", qnaVO);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void resave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String url = "/shopping_complete/action/admin/qna/list";
        String qseq = request.getParameter("qseq").trim();
        String reply = request.getParameter("reply").trim();

        qnaService.saveList(qseq, reply);

        response.sendRedirect(url);
    }
}
