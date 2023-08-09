package com.withJ.controller;

import com.withJ.dto.AddressVO;
import com.withJ.dto.MemberVO;
import com.withJ.dto.OrderVO;
import com.withJ.service.MemberService;
import com.withJ.service.OrderService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * <p>유저와 관련된 요청을 처리하는 컨트롤러<br>
 * /action/member 이후의 경로를 통해 각각 알맞는 메서드를 호출하도록 구성함</p>
 * <p>이 컨트롤러에서 처리 가능한 경로<br>
 * 1. /findZipNum<br>
 * 2. /idCheck<br>
 * 3. /join<br>
 * 4. /joinForm<br>
 * 5. /login<br>
 * 6. /loginForm<br>
 * 7. /logout<br>
 * 8. /myPage<br>
 * </p>
 *
 * @author kangdonghee
 */
@WebServlet("/action/member/*")
public class MemberController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MemberService memberService = MemberService.getInstance();
    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(path, request, response);
    }

    private void pathMapping(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (path) {
            case "/contract": contract(request, response); break;
            case "/findZipNum": findZipNum(request, response); break;
            case "/idCheck": idCheck(request, response); break;
            case "/join": join(request, response); break;
            case "/joinForm": joinForm(request, response); break;
            case "/login": login(request, response); break;
            case "/loginForm": loginForm(request, response); break;
            case "/logout": logout(request, response); break;
            case "/myPage": myPage(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void contract(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/member/contract.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void findZipNum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String url = "/member/findZipNum.jsp";
        String dong = request.getParameter("dong");

        if (isValidDong(dong)) {
            List<AddressVO> addressList = memberService.getAddressListByDong(dong.trim());
            request.setAttribute("addressList", addressList);
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    private boolean isValidDong(String dong) {
        return dong != null && !dong.trim().equals("");
    }

    private void idCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/member/idcheck.jsp";

        String id = request.getParameter("id").trim();
        int message = memberService.duplicateCheckId(id);

        request.setAttribute("message", message);
        request.setAttribute("id", id);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String url = "/member/login.jsp";
        HttpSession session = request.getSession();

        String id = request.getParameter("id");
        MemberVO memberVO = makeMemberVO(id, request);
        memberService.joinMember(memberVO);

        session.setAttribute("id", id);

        request.getRequestDispatcher(url).forward(request, response);
    }

    private MemberVO makeMemberVO(String id, HttpServletRequest request) {
        MemberVO memberVO = new MemberVO();
        memberVO.setId(id);
        memberVO.setPwd(request.getParameter("pwd"));
        memberVO.setName(request.getParameter("name"));
        memberVO.setEmail(request.getParameter("email"));
        memberVO.setZipNum(request.getParameter("zipNum"));
        memberVO.setAddress(request.getParameter("addr1") + request.getParameter("addr2"));
        memberVO.setPhone(request.getParameter("phone"));

        return memberVO;
    }

    private void joinForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String url = "/member/join.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String url = "/member/login_fail.jsp";
        HttpSession session = request.getSession();

        String id = request.getParameter("id");
        String pwd = request.getParameter("pwd");

        if (memberService.isValidMember(id, pwd)) {
            MemberVO memberVO = memberService.getMemberById(id);
            session.removeAttribute("id");
            session.setAttribute("loginUser", memberVO);
            url = "/action/index";
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    private void loginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String url = "/member/login.jsp";
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String url = "/action/index";

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    private void myPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/member/loginForm";
        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser != null) {
            url = "/mypage/mypage.jsp";
            List<OrderVO> orderList = orderService.getOrderList(loginUser.getId());

            request.setAttribute("title", "진행 중인 주문 내역");
            request.setAttribute("orderList", orderList);
        }

        request.getRequestDispatcher(url).forward(request, response);
    }
}
