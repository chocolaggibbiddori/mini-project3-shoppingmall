package com.withJ.service;

import com.withJ.dao.ProductDAO;
import com.withJ.dto.ProductVO;

import java.util.List;

/**
 * 상품과 관련된 비즈니스 로직을 처리하는 서비스
 * @author leeseungjun,kangdonghee
 */
public class ProductService {

    private static final ProductService INSTANCE = new ProductService();
   
    private final ProductDAO productDAO = ProductDAO.getInstance();

    private ProductService() {
	}
    
    /**
     * ProductService 클래스의 싱글턴 인스턴스 반환
     * @return ProductService 인스턴스
     * @author leeseungjun
     */
     
	public static ProductService getInstance() {
		return INSTANCE;
	}

	/**
     * 상품 시퀀스 번호에 따른 상품 정보를 반환
     * @param pseq 상품 시퀀스 
     * @return 해당 상품의 ProductVO 객체
     */
	
	public ProductVO getProduct(String pseq) {
		return productDAO.getProduct(pseq);
	}

	/**
     * 상품 종류에 따라 해당 종류의 상품 목록을 가져와 웹 페이지에 출력하는 로직
     * @param kind 상품 종류 
     * @return 해당 상품 종류의 ProductVO 목록
     * @author leeseungjun
     */
	
	public List<ProductVO> listKindProduct(String kind) {
		return productDAO.listKindProduct(kind);
	}

	 /**
     * 신상품의 목록을 반환하는 메서드
     * @return 신상품 목록 (List<ProductVO>)
     * @author kangdonghee
     */
    public List<ProductVO> getNewProductList() {
        return productDAO.listNewProduct();
    }

    /**
     * 인기 상품의 목록을 반환하는 메서드
     * @return 인기 상품의 목록
     * @author kangdonghee
     */
    public List<ProductVO> getBestProductList() {
        return productDAO.listBestProduct();
    }
}
