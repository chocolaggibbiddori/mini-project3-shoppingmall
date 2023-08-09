package com.withJ.util;

import java.sql.*;

public class DBManager {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String UID = "scott";
    private static final String PWD = "tiger";

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, UID, PWD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rset) {
        if (rset != null) {
            try {
                rset.close();
            } catch (SQLException ignored) {
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(Connection conn, Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
