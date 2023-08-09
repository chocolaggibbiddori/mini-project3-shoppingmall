package com.withJ.controller;

import com.withJ.dto.CartVO;
import com.withJ.dto.MemberVO;
import com.withJ.service.CartService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * <p>장바구니와 관련된 요청을 처리하는 컨트롤러<br>
 * /action/cart 이후의 경로를 통해 각각 알맞는 메서드를 호출하도록 구성함</p>
 * <p>이 컨트롤러에서 처리 가능한 경로<br>
 * 1. /delete<br>
 * 2. /insert<br>
 * 3. /list<br>
 * </p>
 *
 * @author kangdonghee
 */
@WebServlet("/action/cart/*")
public class CartController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CartService cartService = CartService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(path, request, response);
    }

    private void pathMapping(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (path) {
            case "/delete": delete(request, response); break;
            case "/insert": insert(request, response); break;
            case "/list": list(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/cart/list";
        String[] cseqArr = request.getParameterValues("cseq");

        cartService.deleteOptions(cseqArr);

        request.getRequestDispatcher(url).forward(request, response);
    }

    private void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "/shopping_complete/action/member/loginForm";
        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (nonNull(loginUser)) {
            url = "/shopping_complete/action/cart/list";
            String userId = loginUser.getId();
            int pseq = Integer.parseInt(request.getParameter("pseq"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            cartService.insertOption(userId, pseq, quantity);
        }

        response.sendRedirect(url);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/member/loginForm";
        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (nonNull(loginUser)) {
            url = "/mypage/cartList.jsp";
            List<CartVO> cartList = cartService.getCartList(loginUser.getId());
            int totalPrice = cartService.getTotalPrice(cartList);

            request.setAttribute("cartList", cartList);
            request.setAttribute("totalPrice", totalPrice);
        }

        request.getRequestDispatcher(url).forward(request, response);
    }
}
