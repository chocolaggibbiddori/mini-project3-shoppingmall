package com.withJ.controller;

import com.withJ.dto.ProductVO;
import com.withJ.service.ProductService;
import com.withJ.util.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>인덱스 요청을 처리하는 컨트롤러<br>
 * "/action/index" 요청을 받으면 초기 화면(index.jsp)으로 보냄</p>
 *
 * @author kangdonghee
 */
@WebServlet("/action/index")
public class IndexController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/index.jsp";
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        List<ProductVO> newProductList = productService.getNewProductList();
        List<ProductVO> bestProductList = productService.getBestProductList();

        request.setAttribute("newProductList", newProductList);
        request.setAttribute("bestProductList", bestProductList);
        request.getRequestDispatcher(url).forward(request, response);
    }
}
