package com.withJ.controller.admin;

import com.withJ.service.admin.AdminMemberService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * <p>관리자 로그인을 처리하는 컨트롤러<br>
 * 관리자 모드로 로그인 요청시 아이디와 비밀번호를 입력하면 내용에 따라 메서드를 호출함</p>
 * <p>해당 컨트롤러에서 처리 가능한 경로
 * 1. /admin_login_form을 요청 받으면 /admin/main.jsp 관리자 로그인 화면으로 이동
 * 2. /admin_login 요청 받으면 /action/admin/product/list 로 이동
 * 3. /admin_logout 요청 받으면 세션 초기화와 함께 /adminIndex로 이동하여 관리자 로그인 페이지로 이동
 * </p>
 *
 * @author limjeajeong
 */
@WebServlet("/action/admin/loginCheck/*")
public class AdminLoginCheckController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminMemberService adminMemberService = AdminMemberService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(path, request, response);
    }

    private void pathMapping(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (path) {
            case "/admin_login_form": adminLoginForm(request, response); break;
            case "/admin_login": adminLogin(request, response); break;
            case "/admin_logout": adminLogout(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void adminLoginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/main.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/admin/loginCheck/admin_login_form";
        String workerId = request.getParameter("workerId").trim();
        String workerPwd = request.getParameter("workerPwd").trim();

        Log.printVariable("workerId", workerId);
        Log.printVariable("workerPwd", workerPwd);

        String msg = adminMemberService.workerLogCheck(workerId, workerPwd);

        if (isLoginSuccess(msg)) {
            Log.print("Login Success");
            HttpSession session = request.getSession();
            session.setAttribute("workerId", workerId);
            url = "/action/admin/product/list";
        } else {
            Log.print("Login Fail");
        }

        request.setAttribute("message", msg);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private boolean isLoginSuccess(String msg) {
        return msg.equals("로그인 성공.");
    }

    private void adminLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/admin/loginCheck/admin_login_form";

        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            session.invalidate();
            request.setAttribute("message", "");
        }

        request.getRequestDispatcher(url).forward(request, response);
    }
}
