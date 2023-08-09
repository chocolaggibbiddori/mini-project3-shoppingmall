package com.withJ.service;

import com.withJ.dao.CartDAO;
import com.withJ.dto.CartVO;

import java.util.List;

/**
 * 장바구니와 관련된 비즈니스 로직을 처리하는 서비스
 *
 * @author kangdonghee
 */
public class CartService {

    private static final CartService INSTANCE = new CartService();

    private final CartDAO cartDAO;

    private CartService() {
        cartDAO = CartDAO.getInstance();
    }

    public static CartService getInstance() {
        return INSTANCE;
    }

    /**
     * 사용자가 선택한 상품들을 장바구니에서 삭제한다.
     *
     * @param cseqArr 장바구니에서 사용자가 체크한 상품들이 담긴 배열
     * @author kangdonghee
     */
    public void deleteOptions(String[] cseqArr) {
        for (String cseq : cseqArr) {
            cartDAO.deleteCart(Integer.parseInt(cseq));
        }
    }

    /**
     * 사용자가 선택한 상품을 장바구니에 담는다.
     *
     * @param userId   사용자 아이디
     * @param pseq     상품 번호
     * @param quantity 상품 수량
     * @author kangdonghee
     */
    public void insertOption(String userId, int pseq, int quantity) {
        CartVO cartVO = new CartVO();
        cartVO.setId(userId);
        cartVO.setPseq(pseq);
        cartVO.setQuantity(quantity);

        cartDAO.insertCart(cartVO);
    }

    /**
     * 사용자의 장바구니 목록을 반환한다.
     *
     * @param userId 사용자 아이디
     * @return 사용자의 장바구니 목록
     * @author kangdonghee
     */
    public List<CartVO> getCartList(String userId) {
        return cartDAO.listCart(userId);
    }

    /**
     * 장바구니 내 상품들의 총 가격을 계산한다.
     *
     * @param cartList 상품들의 총 가격을 계산할 장바구니 목록
     * @return 장바구니에 담긴 상품들의 총 가격
     * @author kangdonghee
     */
    public int getTotalPrice(List<CartVO> cartList) {
        int totalPrice = 0;

        for (CartVO cartVO : cartList) {
            totalPrice += cartVO.getPrice2() * cartVO.getQuantity();
        }

        return totalPrice;
    }
}
