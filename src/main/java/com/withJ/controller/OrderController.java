package com.withJ.controller;

import com.withJ.dto.CartVO;
import com.withJ.dto.MemberVO;
import com.withJ.dto.OrderVO;
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
import java.util.Objects;


/**
 * <p>주문 관련 요청을 처리하는 역할을 담당<br>
 * /action/order 이후의 경로를 통해 각각 알맞는 메서드를 호출하도록 구성함</p>
 * <p>이 컨트롤러에서 처리 가능한 경로<br>
 * 1. /all<br>
 * 2. /detail<br>
 * 3. /insert<br>
 * 4. /insertImm<br>
 * </p>
 * @author leeseungjun
 */
@WebServlet("/action/order/*")
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;	// OrderService 인스턴스 생성

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
    	throws ServletException, IOException {			// 요청 URL 경로 가져오기
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(request, response, path);	// 해당 매소드 출력
    }

    // 경로에 따른 요청 처리 메소드
    private void pathMapping(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        switch (path) {
            case "/all": all(request, response); break;
            case "/detail": detail(request, response); break;
            case "/insert": insert(request, response); break;
            case "/insertImm": insertImm(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private MemberVO getMemberVO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (MemberVO) session.getAttribute("loginUser");
    }

     // all 요청을 처리하는 메소드
    private void all(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/member/loginForm";
        MemberVO loginUser = getMemberVO(request);

        // 로그인 된 사용자의 경우, 관련 정보를 포함한 페이지
        if (Objects.nonNull(loginUser)) {
            url = "/mypage/orderList.jsp";

            List<OrderVO> orderList = orderService.listOrderAll(loginUser.getId());
            int totalPrice = orderService.getTotalPrice(orderList);

            request.setAttribute("orderList", orderList);
            request.setAttribute("totalPrice", totalPrice);
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    // detail 요청을 처리하는 메소드
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/member/loginForm";
        MemberVO loginUser = getMemberVO(request);

        if (Objects.nonNull(loginUser)) {
            url = "/mypage/orderDetail.jsp";

            int oseq = Integer.parseInt(request.getParameter("oseq"));
            List<OrderVO> orderList = orderService.getOrderDetail(loginUser.getId(), oseq);
            int totalPrice = orderService.getTotalPrice(orderList);

            request.setAttribute("orderDetail", orderList.get(0));
            request.setAttribute("orderList", orderList);
            request.setAttribute("totalPrice", totalPrice);
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    // insert 요청을 처리하는 메소드
    private void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "/shopping_complete/action/member/loginForm";
        MemberVO loginUser = getMemberVO(request);

        if (Objects.nonNull(loginUser)) {
            url = "/shopping_complete/action/member/myPage";
            List<CartVO> cartList = orderService.getCartList(loginUser.getId());
            orderService.insertOrder(cartList, loginUser.getId());
        }

        response.sendRedirect(url);
    }

    // insertImm 요청을 처리하는 메소드
    private void insertImm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "/shopping_complete/action/member/loginForm";
        MemberVO loginUser = getMemberVO(request);

        if (Objects.nonNull(loginUser)) {
            url = "/shopping_complete/action/member/myPage";
            int pseq = Integer.parseInt(request.getParameter("pseq"));
            int quantity = 1;

            List<CartVO> cartList = orderService.getCartListImm(pseq, quantity, loginUser.getId());
            orderService.insertOrder(cartList, loginUser.getId());
        }

        response.sendRedirect(url);
    }
}
