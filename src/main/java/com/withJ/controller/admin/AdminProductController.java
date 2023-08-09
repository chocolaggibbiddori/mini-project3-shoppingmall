package com.withJ.controller.admin;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.withJ.dto.AdminProductVO;
import com.withJ.dto.ProductVO;
import com.withJ.service.admin.AdminProductService;
import com.withJ.util.Log;

/**
 * <p>관리자가 상품을 관리하는 컨트롤러</p>
 * 1. /list: 상품 목록 보여주는 기능<br>
 * 2. /detail: 상품 상세정보 보여주는 기능<br>
 * 3. /update: 상품 정보 수정하는 기능<br>
 * 4. /update_form: 상품 정보 수정하는 창 가는 기능<br>
 * 5. /write: 상품 등록하는 기능<br>
 * 6. /write_form: 상품 등록하는 창 가는 기능<br>
 *
 * @author kimjunyoung
 */
@WebServlet("/action/admin/product/*")
public class AdminProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminProductService productService = AdminProductService.getInstance();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        Log.printClass(getClass());
        Log.printVariable("path", path);

        pathMapping(path, request, response);
    }

    private void pathMapping(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (path) {
            case "/list": list(request, response); break;
            case "/detail": detail(request, response); break;
            case "/update": update(request, response); break;
            case "/update_form": updateForm(request, response); break;
            case "/write": write(request, response); break;
            case "/write_form": writeForm(request, response); break;
            default: response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String url = "/admin/product/productList.jsp";
        String key = Objects.requireNonNullElse(request.getParameter("key"), "");
        String tpage = getTpage(request);

        Log.printVariable("key", key);

        AdminProductVO productInfo = productService.getProductList(tpage, key);
        List<ProductVO> productList = productInfo.getProductInfo();
        int productListSize = productInfo.getProductListSize();
        String paging = productInfo.getPaging();

        request.setAttribute("key", key);
        request.setAttribute("tpage", tpage);
        request.setAttribute("productList", productList);
        request.setAttribute("productListSize", productListSize);
        request.setAttribute("paging", paging);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private String getTpage(HttpServletRequest request) {
        String tpage = request.getParameter("tpage");

        if (Objects.isNull(tpage) || tpage.isBlank()) {
            tpage = "1";
        }

        return tpage;
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/product/productDetail.jsp";
        String pseq = request.getParameter("pseq").trim();
        String tpage = Objects.requireNonNullElse(request.getParameter("tpage"), "1");

        Map<String, Object> productDetail = productService.getProduct(pseq, tpage);

        request.setAttribute("productDetail", productDetail);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "/shopping_complete/action/admin/product/list";

        productService.updateProduct(getMulti(request));

        response.sendRedirect(url);
    }

    private void updateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String url = "/admin/product/productUpdate.jsp";
        String[] kindList = {"Heels", "Boots", "Sandals", "Sneakers", "Sale"};
        String pseq = request.getParameter("pseq").trim();
        String tpage = Objects.requireNonNullElse(request.getParameter("tpage"), "1");

        ProductVO productVO = productService.getProduct(pseq);

        request.setAttribute("tpage", tpage);
        request.setAttribute("kindList", kindList);
        request.setAttribute("productVO", productVO);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void write(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "/shopping_complete/action/admin/product/list";

        productService.insertProduct(getMulti(request));

        response.sendRedirect(url);
    }

    private void writeForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/admin/product/productWrite.jsp";
        String[] kindList = {"Heels", "Boots", "Sandals", "Sneakers", "Sale"};

        request.setAttribute("kindList", kindList);
        request.getRequestDispatcher(url).forward(request, response);
    }

    public MultipartRequest getMulti(HttpServletRequest request) {
        HttpSession session = request.getSession();

        int sizeLimit = 5 * 1024 * 1024;
        String savePath = "product_images";
        ServletContext context = session.getServletContext();
        String uploadFilePath = context.getRealPath(savePath);

        MultipartRequest multi = null;
        try {
            multi = new MultipartRequest(
                    request, // 1. 요청 객체
                    uploadFilePath, // 2. 업로드될 파일이 저장될 파일 경로명
                    sizeLimit, // 3. 업로드될 파일의 최대 크기(5Mb)
                    "UTF-8", // 4. 인코딩 타입 지정
                    new DefaultFileRenamePolicy() // 5. 덮어쓰기를 방지 위한 부분
            );
        } catch (IOException e) {
            e.printStackTrace();
        } // 이 시점을 기해 파일은 이미 저장이 되었다

        return multi;
    }
}
