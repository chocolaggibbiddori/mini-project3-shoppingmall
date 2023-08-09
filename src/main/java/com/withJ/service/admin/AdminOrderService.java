package com.withJ.service.admin;

import com.withJ.dao.OrderDAO;
import com.withJ.dto.OrderVO;

import java.util.List;

/**
 * 사용자가 주문한 내역을 확인하고 입금완료를 처리하는 로직의 서비스
 * @author limjeajeong
 */
public class AdminOrderService {

    private static final AdminOrderService Instance = new AdminOrderService();

    private final OrderDAO orderDAO;

    private AdminOrderService() {
        orderDAO = OrderDAO.getInstance();
    }

    public static AdminOrderService getInstance() {
        return Instance;
    }

    /**
     * 관리자가 사용자의 주문 목록을 확인한다.
     * 
     * @param key 사용자 이름
     * @return 사용자 이름을 통해 주문 목록울 가져옴
     * @author limjeajeong
     */
    public List<OrderVO> orderlist(String key) {
        return orderDAO.listOrder(key);
    }


    /**
     * 관리자가 사용자의 주문내역을 승인한다. 
     * 
     * @param resultArr 주문리스트에서 관리자가 체크한 사용자의 주문목록이 담긴 배열
     * @author limjeajeong
     */
    public void orderSave(String[] resultArr) {
        for (String oseq : resultArr) {
            orderDAO.updateOrderResult(oseq);
        }
    }
}
