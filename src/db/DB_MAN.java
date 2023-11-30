package db;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.*;
import java.io.*;

public class DB_MAN {
    String strDriver = "com.mysql.cj.jdbc.Driver";
    String strURL = "jdbc:mysql://43.201.45.183:3306/JAVA?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    String strUser = "root";
    String strPWD = "fbrudfhr14";
    
    Connection DB_con;
    Statement DB_stmt;
    ResultSet DB_rs;
    
    public void dbOpen() throws SQLException {
        try {
            Class.forName(strDriver);
            DB_con = DriverManager.getConnection(strURL, strUser, strPWD);
            DB_stmt = DB_con.createStatement();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver Load Failed");
        }
    }
    
    public void dbClose() throws SQLException {
        if (DB_stmt != null) DB_stmt.close();
        if (DB_con != null) DB_con.close();
    }
    
     // PreparedStatement를 생성하고 반환하는 메서드
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return DB_con.prepareStatement(sql);
    }
    
    // SQL 쿼리 실행을 위한 Statement를 가져오는 메서드
    public Statement getStatement() {
        return DB_stmt;
    }
}
