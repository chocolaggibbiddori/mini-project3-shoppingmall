package com.withJ.service;

import com.withJ.dao.CartDAO;
import com.withJ.dao.OrderDAO;
import com.withJ.dto.CartVO;
import com.withJ.dto.OrderVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 로그인 한 경우 주문 정보를 가져와서 주문 내역을 출력하는 서비스
 * @author leeseungjun
 */
public class OrderService {

    private static final OrderService INSTANCE = new OrderService();

    private final CartService cartService;
    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;

    private OrderService() {
        orderDAO = OrderDAO.getInstance();
        cartDAO = CartDAO.getInstance();
        cartService = CartService.getInstance();
    }

    /**
     * OrderService 클래스의 싱글톤 인스턴스를 반환
     * @return OrderService 인스턴스
     * @author leeseungjun
     */
   
    public static OrderService getInstance() {
        return INSTANCE;
    }

    /**
     * 사용자 ID로 주문 목록을 가져오는 메소드
     * @param userId 사용자 ID
     * @return 주문 목록(List<OrderVO>)
     * @author leeseungjun
     */
    
    public List<OrderVO> getOrderList(String userId) {
        // 주문 리스트 생성
        List<Integer> oseqList = orderDAO.selectSeqOrderIng(userId);
        List<OrderVO> orderList = new ArrayList<>();

        for (int oseq : oseqList) {
            List<OrderVO> orderListIng = orderDAO.listOrderById(userId, "%", oseq);
            OrderVO orderVO = orderListIng.get(0);

            if (orderListIng.size() == 1) orderVO.setPname(orderVO.getPname());
            else orderVO.setPname(orderVO.getPname() + " 외 " + (orderListIng.size() - 1) + "건");

            int totalPrice = getTotalPrice(orderListIng);

            orderVO.setPrice2(totalPrice);
            orderList.add(orderVO);
        }

        return orderList;
    }

    /**
     * 주문 상세 정보를 가져와 웹 페이지에 출력하는 메소드
     * @param id 사용자 ID
     * @param oseq 주문 시퀀스 번호
     * @return 주문 상세 정보 목록 (List<OrderVO>)
     * @author leeseungjun
     */
    public List<OrderVO> getOrderDetail(String id, int oseq) {
        return orderDAO.listOrderById(id, "%", oseq);
    }

    /**
     * 장바구니 목록을 조회하는 메소드
     * @param memberId 회원 ID
     * @return 해당 회원의 장바구니 목록 (List<CartVO>)
     * @author leeseungjun
     */
    public List<CartVO> getCartList(String memberId) {
        return cartDAO.listCart(memberId);
    }

    /**
     * 장바구니를 거치지 않고 즉시 구매하는 메소드
     * @param pseq 상품 시퀀스 번호
     * @param quantity 구매 수량
     * @param memberId 회원 ID
     * @return OrderList (List<CartVO>)
     * @author kimjunyoung
     */
    public List<CartVO> getCartListImm(int pseq, int quantity, String memberId) {
        cartService.insertOption(memberId, pseq, quantity);
        return cartDAO.listCartOne(memberId);
    }

    /**
     * 카트 목록에 있는 품목을 주문하는 메소드
     * @param cartList 카트 목록
     * @param userId 회원 ID
     * @author leeseungjun
     */
    public void insertOrder(List<CartVO> cartList, String userId) {
        orderDAO.insertOrder(cartList, userId);
    }

    /**
     * 사용자 전체 주문 목록을 반환하는 메소드
     * @param userId 회원 ID
     * @return 전체 주문 목록 (List<OrderVO>)
     * @author leeseungjun
     */    
    
    public List<OrderVO> listOrderAll(String userId) {
        return orderDAO.listOrderAll(userId);
    }

    /**
     * 주문 목록의 총 가격을 계산하는 메소드
     * @param  orderList 주문 목록 (List<OrderVO>)
     * @return 총 가격 (int)
     * @author leeseungjun
     */
    public int getTotalPrice(List<OrderVO> orderList) {
        int totalPrice = 0;
        
        for (OrderVO orderVO : orderList) {
            totalPrice += orderVO.getPrice2() * orderVO.getQuantity();
        }

        return totalPrice;
    }
}
