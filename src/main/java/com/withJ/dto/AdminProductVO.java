package com.withJ.dto;

import java.util.List;

/**
 * 상품 정보를 한꺼번에 넘겨주기 위한 VO 클래스
 *
 * @author kimjunyoung
 */
public class AdminProductVO {

    private final List<ProductVO> productInfo;
    private final String paging;
    private final int productListSize;

    public AdminProductVO(List<ProductVO> productInfo, String paging, int productListSize) {
        this.productInfo = List.copyOf(productInfo);
        this.paging = paging;
        this.productListSize = productListSize;
    }

    public List<ProductVO> getProductInfo() {
        return productInfo;
    }

    public String getPaging() {
        return paging;
    }

    public int getProductListSize() {
        return productListSize;
    }
}
