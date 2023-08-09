package com.withJ.controller.admin;

import com.withJ.dto.MemberVO;
import com.withJ.service.admin.AdminMemberService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>관리자 로그인하여 회원리스트를 확인하는 컨트롤러</p>
 * <p>해당 컨트롤러에서 처리 가능한 경로
 * /adminMemberList 요청 받으면 /admin/member/memberList.jsp 이동하여 가입한 사용자의 리스트 확인
 * </p>
 *
 * @author limjeajeong
 */
@WebServlet("/action/admin/memberList/*")
public class AdminMemberListController extends HttpServlet {

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
        if (path.equals("/admin_member_list")) adminMemberList(request, response);
        else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void adminMemberList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/member/memberList.jsp";
        String key = Objects.requireNonNullElse(request.getParameter("key"), "");

        Log.printVariable("key", key);

        List<MemberVO> memberList = adminMemberService.memberList(key);

        request.setAttribute("memberList", memberList);
        request.getRequestDispatcher(url).forward(request, response);
    }
}
