/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author seung
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import nl.captcha.text.renderer.DefaultWordRenderer;

public class MainFrame extends javax.swing.JFrame {

    // DB_MAN 클래스의 인스턴스를 생성하여 데이터베이스 연결 관리를 수행
    DB_MAN DBM = new DB_MAN();
    String strSQL = "select * from user";
    // 캡차 생성 메서드
    
    
    public MainFrame() {
        initComponents();
        try{
            String strData = null;
            DBM.dbOpen();// 데이터베이스 연결을 열고,
            getDBData(strSQL); // SQL 쿼리를 실행하여 데이터를 가져온다.
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("MainForm.<init>()");
        }
    }
    
    // 데이터베이스에서 데이터를 가져와 JTextArea에 표시하는 메서드
    public final void getDBData(String strQuery) {
        String strOutput = "";

        try {
            // SQL 쿼리를 실행하고 결과를 DBM.DB_rs에 저장
            System.out.println(strQuery);
            DBM.DB_rs = DBM.DB_stmt.executeQuery(strQuery);
            
            while (DBM.DB_rs.next()) {
                strOutput = "";
                strOutput += DBM.DB_rs.getString("id") + "\t";
//                strOutput += DBM.DB_rs.getString("pw") + "\t";
//                strOutput += DBM.DB_rs.getString("name") + "\t";
//                strOutput += DBM.DB_rs.getString("birthd") + "\t";
            }
            DBM.DB_rs.close();
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_title = new javax.swing.JLabel();
        lbl_id = new javax.swing.JLabel();
        lbl_pw = new javax.swing.JLabel();
        lbl_pwcheck = new javax.swing.JLabel();
        lbl_name = new javax.swing.JLabel();
        lbl_birth = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        txt_name = new javax.swing.JTextField();
        txt_birth = new javax.swing.JTextField();
        lbl_hurdle1 = new javax.swing.JLabel();
        lbl_hurdle2 = new javax.swing.JLabel();
        btn_sign = new javax.swing.JButton();
        btn_idcheck = new javax.swing.JButton();
        lbl_hurdle3 = new javax.swing.JLabel();
        txt_pw = new javax.swing.JPasswordField();
        txt_pwcheck = new javax.swing.JPasswordField();
        lbl_hurdle5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbl_title.setText("회원가입");

        lbl_id.setText("아이디");

        lbl_pw.setText("비밀번호");

        lbl_pwcheck.setText("비밀번호 확인");

        lbl_name.setText("성명");

        lbl_birth.setText("생년월일");

        txt_birth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_birthKeyReleased(evt);
            }
        });

        lbl_hurdle1.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle1.setText("영어 대문자, 소문자, 숫자, 특수문자를 조합해주세요.");

        lbl_hurdle2.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle2.setText("8~15자 이상 입력");

        btn_sign.setText("회원가입");
        btn_sign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signActionPerformed(evt);
            }
        });

        btn_idcheck.setText("중복 확인");
        btn_idcheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_idcheckActionPerformed(evt);
            }
        });

        lbl_hurdle3.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle3.setText("비밀번호가 일치하지 않습니다.");

        txt_pw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pwActionPerformed(evt);
            }
        });
        txt_pw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pwKeyReleased(evt);
            }
        });

        txt_pwcheck.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pwcheckKeyReleased(evt);
            }
        });

        lbl_hurdle5.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle5.setText("8자리 입력");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(326, 326, 326)
                        .addComponent(lbl_title))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_id)
                                    .addComponent(lbl_pw)
                                    .addComponent(lbl_pwcheck))
                                .addGap(61, 61, 61)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(btn_idcheck))
                                    .addComponent(lbl_hurdle1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_hurdle2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_hurdle3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txt_pwcheck, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                        .addComponent(txt_pw, javax.swing.GroupLayout.Alignment.LEADING))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_name)
                                    .addComponent(lbl_birth))
                                .addGap(61, 61, 61)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_hurdle5, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(216, 216, 216)
                                .addComponent(btn_sign)))))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lbl_title)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_id)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_idcheck))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pw)
                    .addComponent(txt_pw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pwcheck)
                    .addComponent(txt_pwcheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(lbl_hurdle1)
                .addGap(18, 18, 18)
                .addComponent(lbl_hurdle2)
                .addGap(18, 18, 18)
                .addComponent(lbl_hurdle3)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_name)
                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_birth)
                    .addComponent(txt_birth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_hurdle5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                .addComponent(btn_sign)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_idcheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_idcheckActionPerformed
        String ID = txt_id.getText();
        strSQL = "SELECT id FROM user WHERE id = '" + ID + "'";
        
        if(ID.contains(" ")){
            JOptionPane.showMessageDialog(null,"띄어쓰기는 불가능합니다.");
            return;
        }

        try {
            DBM.dbOpen();
            DBM.DB_rs = DBM.DB_stmt.executeQuery(strSQL);
            
            // 만약 결과가 존재한다면 중복된 ID가 있다는 메시지를 표시
            if (DBM.DB_rs.next()) {
                JOptionPane.showMessageDialog(null, "이미 사용 중인 ID입니다. 다른 ID를 입력해주세요.");
            } else {
                JOptionPane.showMessageDialog(null, "사용 가능한 ID입니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_idcheckActionPerformed

    private void txt_pwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pwKeyReleased
        //String passwordPattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$";
        String passwordPattern = "^((?=.*[a-z])(?=.*\\d))";
        if(String.valueOf(txt_pw.getPassword()).length() >= 8){
            lbl_hurdle2.setForeground(new Color(0,204,102));
        }
        
        // 정규식 검사
        if (Pattern.matches(passwordPattern, String.valueOf(txt_pw.getPassword()))) {
            lbl_hurdle1.setForeground(new Color(0,204,102)); 
        } 
    }//GEN-LAST:event_txt_pwKeyReleased

    private void txt_pwcheckKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pwcheckKeyReleased
        if (String.valueOf(txt_pw.getPassword()).equals(String.valueOf(txt_pwcheck.getPassword())) ) {
            lbl_hurdle3.setForeground(new Color(0,204,102));    
        }
    }//GEN-LAST:event_txt_pwcheckKeyReleased

    private void btn_signActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_signActionPerformed
            strSQL = "Insert Into user Values (";
            strSQL += "'" + txt_id.getText() + "', ";
            strSQL += "'" + txt_pwcheck.getText() + "', ";
            strSQL += "'" + txt_name.getText() + "', ";
            strSQL += "'" + txt_birth.getText() + "')";

            try {
                DBM.dbOpen();
                DBM.DB_stmt.executeUpdate(strSQL);
                strSQL = "Select * From user";
                getDBData(strSQL);
                JOptionPane.showMessageDialog(null, "회원가입 완료");
                DBM.dbClose();
            } catch (Exception e) {
                System.out.println("SQLException: " + e.getMessage());
            }
        
    
    }//GEN-LAST:event_btn_signActionPerformed

    
    private void txt_birthKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_birthKeyReleased
        String birth = txt_birth.getText();
        
        if (birth.length() > 8) {
        // 입력이 8자리를 초과하는 경우, 입력을 무시하고 이전 상태로 되돌립니다.
            txt_birth.setText(birth.substring(0, 8));     
        }
  
        if(birth.length() == 8){
            lbl_hurdle5.setForeground(new Color(0,204,102));
        }
    }//GEN-LAST:event_txt_birthKeyReleased

    private void txt_pwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pwActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pwActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_idcheck;
    private javax.swing.JButton btn_sign;
    private javax.swing.JLabel lbl_birth;
    private javax.swing.JLabel lbl_hurdle1;
    private javax.swing.JLabel lbl_hurdle2;
    private javax.swing.JLabel lbl_hurdle3;
    private javax.swing.JLabel lbl_hurdle5;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_pw;
    private javax.swing.JLabel lbl_pwcheck;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JTextField txt_birth;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_name;
    private javax.swing.JPasswordField txt_pw;
    private javax.swing.JPasswordField txt_pwcheck;
    // End of variables declaration//GEN-END:variables
}
