package com.withJ.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.oreilly.servlet.MultipartRequest;
import com.withJ.dao.ProductDAO;
import com.withJ.dto.AdminProductVO;
import com.withJ.dto.ProductVO;

/**
 * 관리자가 상품 정보와 관련된 서비스를 처리하는 곳
 *
 * @author kimjunyoung
 */
public class AdminProductService {

    private static final AdminProductService INSTANCE = new AdminProductService();

    private final ProductDAO productDAO;

    private AdminProductService() {
        productDAO = ProductDAO.getInstance();
    }

    public static AdminProductService getInstance() {
        return INSTANCE;
    }

    /**
     * 상품 번호에 따른 상품 정보 가져오기
     *
     * @param tpage page 번호
     * @param pseq 상품 번호
     * @return productDetails 상품 상세정보
     * @author kimjunyoung
     */
    public Map<String, Object> getProduct(String pseq, String tpage) {
        ProductVO productVO = productDAO.getProduct(pseq);
        String[] kindList = {"0", "Heels", "Boots", "Sandals", "Sneakers", "Sale"};
        int index = Integer.parseInt(productVO.getKind().trim());

        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("productVO", productVO);
        productDetails.put("kind", kindList[index]);
        productDetails.put("tpage", tpage);

        return productDetails;
    }

    /**
     * 상품 목록들을 나열하고 페이징 매기기
     *
     * @param tpage: page 번호
     * @param key: 상품명 검색 시 상품명
     * @return productList 상품 목록
     * @author kimjunyoung
     */
    public AdminProductVO getProductList(String tpage, String key) {
        List<ProductVO> productInfo = productDAO.listProduct(Integer.parseInt(tpage), key);
        int productListSize = productInfo.size();
        String paging = productDAO.pageNumber(Integer.parseInt(tpage), key);

        return new AdminProductVO(productInfo, paging, productListSize);
    }

    /**
     * 수정해서 만든 새로운 파일에서 정보를 가져와 원래있던 상품정보를 바꿈
     *
     * @param multi: 업로드한 파일의 정보
     * @author kimjunyoung
     */
    public void updateProduct(MultipartRequest multi) {
        String useyn = Objects.requireNonNullElse(multi.getParameter("useyn"), "n");
        String bestyn = Objects.requireNonNullElse(multi.getParameter("bestyn"), "n");

        ProductVO productVO = new ProductVO();
        productVO.setPseq(Integer.parseInt(multi.getParameter("pseq")));
        productVO.setKind(multi.getParameter("kind"));
        productVO.setName(multi.getParameter("name"));
        productVO.setPrice1(Integer.parseInt(multi.getParameter("price1")));
        productVO.setPrice2(Integer.parseInt(multi.getParameter("price2")));
        productVO.setPrice3(Integer.parseInt(multi.getParameter("price2")) - Integer.parseInt(multi.getParameter("price1")));
        productVO.setContent(multi.getParameter("content"));
        productVO.setUseyn(useyn);
        productVO.setBestyn(bestyn);

        if (multi.getFilesystemName("image") == null) productVO.setImage(multi.getParameter("nonmakeImg"));
        else productVO.setImage(multi.getFilesystemName("image"));

        productDAO.updateProduct(productVO);
    }

    /**
     * 수정할 때 상품 정보를 가져와 조회
     *
     * @param pseq 상품 번호
     * @return productInfo 상품 정보
     * @author kimjunyoung
     */
    public ProductVO getProduct(String pseq) {
        return productDAO.getProduct(pseq);
    }

    /**
     * 상품 등록하는 역할
     *
     * @param multi 등록하는 상품 여러 정보
     * @author kimjunyoung
     */
    public void insertProduct(MultipartRequest multi) {
        ProductVO productVO = new ProductVO();
        productVO.setKind(multi.getParameter("kind"));
        productVO.setName(multi.getParameter("name"));
        productVO.setPrice1(Integer.parseInt(multi.getParameter("price1")));
        productVO.setPrice2(Integer.parseInt(multi.getParameter("price2")));
        productVO.setPrice3(Integer.parseInt(multi.getParameter("price2")) - Integer.parseInt(multi.getParameter("price1")));
        productVO.setContent(multi.getParameter("content"));
        productVO.setImage(multi.getFilesystemName("image"));

        productDAO.insertProduct(productVO);
    }
}
