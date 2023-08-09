package com.withJ.controller.admin;

import com.withJ.dto.OrderVO;
import com.withJ.service.admin.AdminOrderService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>관리자가 사용자의 주문목록을 확인한 후 승인을 처리하는 컨트롤러</p>
 * <p>해당 컨트롤러에서 처리 가능한 경로<br>
 * 1. /admin_order_list 요청 받으면 /admin/order/orderList.jsp 이동하여 사용자의 주문목록을 확인함<br>
 * 2. /admin_order_save를 요청받으면 /admin_order_list를 호출하여 사용자가 주문한 내용을 승인함<br>
 * </p>
 *
 * @author limjeajeong
 */
@WebServlet("/action/admin/orders/*")
public class AdminOrderController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminOrderService adminOrderService = AdminOrderService.getInstance();

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
            case "/admin_order_list": adminOrderList(request, response); break;
            case "/admin_order_save": adminOrderSave(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void adminOrderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/order/orderList.jsp";
        String key = Objects.requireNonNullElse(request.getParameter("key"), "");

        Log.printVariable("key", key);

        List<OrderVO> orderList = adminOrderService.orderlist(key);

        request.setAttribute("orderList", orderList);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void adminOrderSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/action/admin/orders/admin_order_list";
        String[] resultArr = request.getParameterValues("result");

        Log.printVariable("resultArr", Arrays.toString(resultArr));

        adminOrderService.orderSave(resultArr);

        request.getRequestDispatcher(url).forward(request, response);
    }
}
