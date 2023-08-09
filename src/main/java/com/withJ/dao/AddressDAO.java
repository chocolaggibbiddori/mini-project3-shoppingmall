package com.withJ.dao;

import com.withJ.dto.AddressVO;
import com.withJ.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    private static final AddressDAO INSTANCE = new AddressDAO();

    private AddressDAO() {
    }

    public static AddressDAO getInstance() {
        return INSTANCE;
    }

    public List<AddressVO> selectAddressByDong(String dong) {
        List<AddressVO> list = new ArrayList<>();

        String sql = "select * from address where dong like '%'||?||'%'";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dong);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AddressVO addressVO = new AddressVO();
                addressVO.setzipNum(rs.getString("zip_num"));
                addressVO.setSido(rs.getString("sido"));
                addressVO.setGugun(rs.getString("gugun"));
                addressVO.setDong(rs.getString("dong"));
                addressVO.setzipCode(rs.getString("zip_code"));
                addressVO.setBunji(rs.getString("bunji"));

                list.add(addressVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(conn, pstmt, rs);
        }

        return list;
    }
}
