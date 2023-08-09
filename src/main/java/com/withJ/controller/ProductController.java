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
 * <p>사용자의 상품 관련 요청을 처리하는 역할을 담당<br>
 * /action/Product 이후의 경로를 통해 각각 알맞는 메서드를 호출하도록 구성함</p>
 * <p>이 컨트롤러에서 처리 가능한 경로<br>
 * 1. /detail<br>
 * 2. /catagory<br>
 * 3. /new<br>
 * 4. /best<br>
 * </p>
 * @author leeseungjun
 * 
 */
@WebServlet("/action/product/*")
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ProductService 인스턴스
    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(request, response, path);
    }

    // 경로에 따라 요청을 처리하는 메소드
    private void pathMapping(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        switch (path) {
            case "/detail": detail(request, response); break;
            case "/catagory": catagory(request, response); break;
            case "/new": newProductList(request, response); break;
            case "/best": bestProductList(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    // detail 요청을 처리하는 메소드
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/product/productDetail.jsp";
        String pseq = request.getParameter("pseq").trim();

        ProductVO productVO = productService.getProduct(pseq);

        request.setAttribute("productVO", productVO);
        request.getRequestDispatcher(url).forward(request, response);
    }
    // catagory 요청을 처리하는 메소드
    private void catagory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/product/productKind.jsp";
        String kind = request.getParameter("kind").trim();

        List<ProductVO> productKindList = productService.listKindProduct(kind);

        request.setAttribute("productKindList", productKindList);
        request.getRequestDispatcher(url).forward(request, response);
    }
	
    // new 요청을 처리하는 메소드
    private void newProductList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/product/newProductList.jsp";

        List<ProductVO> newProductList = productService.getNewProductList();

        request.setAttribute("newProductList", newProductList);
        request.getRequestDispatcher(url).forward(request, response);
    }
    // best요청을 처리하는 메소드
    private void bestProductList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/product/bestProductList.jsp";

        List<ProductVO> bestProductList = productService.getBestProductList();

        request.setAttribute("bestProductList", bestProductList);
        request.getRequestDispatcher(url).forward(request, response);
    }
}
