package com.withJ.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.withJ.dto.CartVO;
import com.withJ.dto.OrderVO;
import com.withJ.util.DBManager;

public class OrderDAO {

	private static final OrderDAO INSTANCE = new OrderDAO();

	private OrderDAO() {
	}

	public static OrderDAO getInstance() {
		return INSTANCE;
	}

	// 사용자가 주문
	public int insertOrder(List<CartVO> cartList, String id) {
		int maxOseq = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs;

		try {
			conn = DBManager.getConnection();

			String selectMaxOseq = "select max(oseq) from orders";
			pstmt = conn.prepareStatement(selectMaxOseq);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				maxOseq = rs.getInt(1);
			}
			pstmt.close();

			String insertOrder = "insert into orders(oseq, id) values(" + "orders_seq.nextval, ?)";
			pstmt = conn.prepareStatement(insertOrder);
			pstmt.setString(1, id);
			pstmt.executeUpdate();

			for (CartVO cartVO : cartList) {
				insertOrderDetail(cartVO, maxOseq);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		return maxOseq;
	}

	public void insertOrderDetail(CartVO cartVO, int maxOseq) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();

			String insertOrderDetail = "insert into order_detail(odseq, oseq, "
					+ "pseq, quantity) values(order_detail_seq.nextval, ?, ?, ?)";
			pstmt = conn.prepareStatement(insertOrderDetail);
			pstmt.setInt(1, maxOseq);
			pstmt.setInt(2, cartVO.getPseq());
			pstmt.setInt(3, cartVO.getQuantity());
			pstmt.executeUpdate();
			pstmt.close();

			String updateCartResult = "update cart set result=2 where cseq=?";
			pstmt = conn.prepareStatement(updateCartResult);
			pstmt.setInt(1, cartVO.getCseq());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}

	// 사용자가 주문 내역 검색
	public List<OrderVO> listOrderById(String id, String result, int oseq) {
		List<OrderVO> orderList = new ArrayList<>();
		String sql = "select * from order_view where id=? " + "and result like '%'||?||'%' and oseq=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, result);
			pstmt.setInt(3, oseq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderVO orderVO = new OrderVO();
				orderVO.setOdseq(rs.getInt("ODSEQ"));
				orderVO.setOseq(rs.getInt("OSEQ"));
				orderVO.setId(rs.getString("ID"));
				orderVO.setIndate(rs.getTimestamp("INDATE"));
				orderVO.setMname(rs.getString("MNAME"));
				orderVO.setZipNum(rs.getString("ZIP_NUM"));
				orderVO.setAddress(rs.getString("ADDRESS"));
				orderVO.setPhone(rs.getString("PHONE"));
				orderVO.setPseq(rs.getInt("PSEQ"));
				orderVO.setQuantity(rs.getInt("QUANTITY"));
				orderVO.setPname(rs.getString("PNAME"));
				orderVO.setPrice2(rs.getInt("PRICE2"));
				orderVO.setResult(rs.getString("RESULT"));
				orderList.add(orderVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return orderList;
	}
	/**
	 * 
	 * @param id
	 * @param result
	 * @param oseq
	 * @return
	 */
	public List<OrderVO> listOrderAll(String id) {
		List<OrderVO> orderList = new ArrayList<>();
		String sql = "select * from order_view where id=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderVO orderVO = new OrderVO();
				orderVO.setOdseq(rs.getInt("ODSEQ"));
				orderVO.setOseq(rs.getInt("OSEQ"));
				orderVO.setId(rs.getString("ID"));
				orderVO.setIndate(rs.getTimestamp("INDATE"));
				orderVO.setMname(rs.getString("MNAME"));
				orderVO.setZipNum(rs.getString("ZIP_NUM"));
				orderVO.setAddress(rs.getString("ADDRESS"));
				orderVO.setPhone(rs.getString("PHONE"));
				orderVO.setPseq(rs.getInt("PSEQ"));
				orderVO.setQuantity(rs.getInt("QUANTITY"));
				orderVO.setPname(rs.getString("PNAME"));
				orderVO.setPrice2(rs.getInt("PRICE2"));
				orderVO.setResult(rs.getString("RESULT"));
				orderList.add(orderVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return orderList;
	}

	// 현재 진행 중인 주문 내역만 조회
	public List<Integer> selectSeqOrderIng(String id) {
		List<Integer> oseqList = new ArrayList<>();
		String sql = "select distinct oseq from order_view " + "where id=? and result='1' order by oseq desc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBManager.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				oseqList.add(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return oseqList;
	}

	/*
	 * * 관리자 모드에서 사용되는 메소드 * *
	 */
	public List<OrderVO> listOrder(String member_name) {
		List<OrderVO> orderList = new ArrayList<>();
		String sql = "select * from order_view where mname like '%'||?||'%' " + "order by result, oseq desc";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (Objects.equals(member_name, "")) {
				pstmt.setString(1, "%");
			} else {
				pstmt.setString(1, member_name);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderVO orderVO = new OrderVO();
				orderVO.setOdseq(rs.getInt("ODSEQ"));
				orderVO.setOseq(rs.getInt("OSEQ"));
				orderVO.setId(rs.getString("ID"));
				orderVO.setPseq(rs.getInt("PSEQ"));
				orderVO.setMname(rs.getString("MNAME"));
				orderVO.setPname(rs.getString("PNAME"));
				orderVO.setQuantity(rs.getInt("QUANTITY"));
				orderVO.setZipNum(rs.getString("ZIP_NUM"));
				orderVO.setAddress(rs.getString("ADDRESS"));
				orderVO.setPhone(rs.getString("PHONE"));
				orderVO.setIndate(rs.getTimestamp("INDATE"));
				orderVO.setPrice2(rs.getInt("PRICE2"));
				orderVO.setResult(rs.getString("RESULT"));
				orderList.add(orderVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return orderList;
	}

	public void updateOrderResult(String oseq) {
		String sql = "update order_detail set result='2' where odseq=?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oseq);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}
}
