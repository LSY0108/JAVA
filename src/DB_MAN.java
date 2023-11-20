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
    
    public void dbOpen() throws IOException {
        try{
            Class.forName(strDriver);
            DB_con = DriverManager.getConnection(strURL, strUser, strPWD); //연동
            DB_stmt = DB_con.createStatement();    //객체 생성
        }catch(Exception e){
            System.err.println("SQLException: " + e.getMessage());
        }
    }
    
    public void dbClose() throws IOException{
        try{
            DB_stmt.close();
            DB_con.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
    }
}
