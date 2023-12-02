package db;

import db.DB_MAN;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jdk.internal.org.jline.terminal.TerminalBuilder;
import main.Main;

public class NewJFrame extends javax.swing.JFrame {
    // DB_MAN 클래스의 인스턴스를 생성하여 데이터베이스 연결 관리를 수행
    DB_MAN DBM = new DB_MAN();
    String strSQL = "select * from user";
    private String captchaText;

    
    // 1. CardLayout 인스턴스 생성 및 JFrame의 content pane에 설정
    private CardLayout cardLayout;
    
    public NewJFrame() {
        initComponents();
        
        generateCaptcha(lb_cap);
        generateCaptcha(lb_cap1);
        // 프레임의 크기를 고정하고 싶은 경우
        setSize(900, 700); // 가로 600, 세로 400 픽셀로 설정
        setResizable(false); // 크기 조정 불가능하게 설정
        // 프레임을 화면의 중앙에 위치시킴
        setLocationRelativeTo(null);
        
        // CardLayout 초기화 및 적용
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);
        
        // 로그인 및 회원가입 버튼을 포함하는 초기 패널 생성
        JPanel initialPanel = new JPanel(new GridBagLayout());
        // GridBagConstraints 생성
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 여백 설정

        // btn_login 버튼 추가
        gbc.gridx = 0; // X 좌표
        gbc.gridy = 0; // Y 좌표
        initialPanel.add(btn_login, gbc);

        // btn_sign 버튼 추가
        gbc.gridx = 1; // X 좌표
        gbc.gridy = 0; // Y 좌표 (같은 행)
        initialPanel.add(btn_sign, gbc);

        // 초기 패널을 CardLayout에 추가
        getContentPane().add(initialPanel, "InitialPanel");
        // 패널을 CardLayout에 추가
        getContentPane().add(loginPanel, "LoginPanel");
        getContentPane().add(signPanel, "SignPanel");

        // 첫 화면을 초기 패널로 설정
        cardLayout.show(getContentPane(), "InitialPanel");
        
        // btn_login 버튼에 액션 리스너 추가
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        // btn_sign 버튼에 액션 리스너 추가
        btn_sign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signActionPerformed(evt);
            }
        });
        
        try{
            String strData = null;
            DBM.dbOpen();// 데이터베이스 연결을 열고,
            getDBData(strSQL); // SQL 쿼리를 실행하여 데이터를 가져온다.
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("NewJFrame.<init>()");
        }
    }
    
    private void generateCaptcha(JLabel jbl) {
        captchaText = generateRandomText(6);
        jbl.setIcon(new ImageIcon(createCaptchaImage(captchaText)));
    }

    private String generateRandomText(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder text = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            text.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return text.toString();
    }

    private BufferedImage createCaptchaImage(String text) {
        BufferedImage image = new BufferedImage(120, 30, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 120, 30);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString(text, 10, 20);
        graphics.dispose();
        return image;
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
            }
            DBM.DB_rs.close();
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        signPanel = new javax.swing.JPanel();
        sign_pwcheck = new javax.swing.JPasswordField();
        lbl_hurdle4 = new javax.swing.JLabel();
        lbl_title1 = new javax.swing.JLabel();
        lbl_id1 = new javax.swing.JLabel();
        lbl_pw1 = new javax.swing.JLabel();
        lbl_hurdle1 = new javax.swing.JLabel();
        lbl_pwcheck1 = new javax.swing.JLabel();
        lbl_hurdle2 = new javax.swing.JLabel();
        lbl_name1 = new javax.swing.JLabel();
        btn_sign1 = new javax.swing.JButton();
        lbl_birth1 = new javax.swing.JLabel();
        btn_idcheck = new javax.swing.JButton();
        sign_id = new javax.swing.JTextField();
        lbl_hurdle3 = new javax.swing.JLabel();
        sign_name = new javax.swing.JTextField();
        sign_pw = new javax.swing.JPasswordField();
        sign_birth = new javax.swing.JTextField();
        btn_back2 = new javax.swing.JButton();
        lbl_cap = new javax.swing.JLabel();
        lb_cap = new javax.swing.JLabel();
        tf_cap = new javax.swing.JTextField();
        loginPanel = new javax.swing.JPanel();
        lbl_title = new javax.swing.JLabel();
        lbl_id = new javax.swing.JLabel();
        lbl_pw = new javax.swing.JLabel();
        login_id = new javax.swing.JTextField();
        login_pw = new javax.swing.JTextField();
        btn_login1 = new javax.swing.JButton();
        login_back = new javax.swing.JButton();
        lbl_cap1 = new javax.swing.JLabel();
        tf_cap1 = new javax.swing.JTextField();
        lb_cap1 = new javax.swing.JLabel();
        btn_login = new javax.swing.JButton();
        btn_sign = new javax.swing.JButton();

        signPanel.setBackground(new java.awt.Color(255, 255, 255));

        sign_pwcheck.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sign_pwcheckKeyReleased(evt);
            }
        });

        lbl_hurdle4.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle4.setText("8자리 입력");

        lbl_title1.setText("회원가입");

        lbl_id1.setText("아이디");

        lbl_pw1.setText("비밀번호");

        lbl_hurdle1.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle1.setText("영어 대문자, 소문자, 숫자, 특수문자를 조합해주세요.");

        lbl_pwcheck1.setText("비밀번호 확인");

        lbl_hurdle2.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle2.setText("8~15자 이상 입력");

        lbl_name1.setText("성명");

        btn_sign1.setText("회원가입");
        btn_sign1.setEnabled(false);
        btn_sign1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sign1ActionPerformed(evt);
            }
        });

        lbl_birth1.setText("생년월일");

        btn_idcheck.setText("중복 확인");
        btn_idcheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_idcheckActionPerformed(evt);
            }
        });

        lbl_hurdle3.setForeground(new java.awt.Color(255, 51, 51));
        lbl_hurdle3.setText("비밀번호가 일치해야합니다.");

        sign_pw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sign_pwActionPerformed(evt);
            }
        });
        sign_pw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sign_pwKeyReleased(evt);
            }
        });

        sign_birth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sign_birthKeyReleased(evt);
            }
        });

        btn_back2.setText("뒤로가기");
        btn_back2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back2ActionPerformed(evt);
            }
        });

        lbl_cap.setText("인증");

        javax.swing.GroupLayout signPanelLayout = new javax.swing.GroupLayout(signPanel);
        signPanel.setLayout(signPanelLayout);
        signPanelLayout.setHorizontalGroup(
            signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signPanelLayout.createSequentialGroup()
                .addContainerGap(209, Short.MAX_VALUE)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                        .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(signPanelLayout.createSequentialGroup()
                                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_id1)
                                    .addComponent(lbl_pw1)
                                    .addComponent(lbl_pwcheck1))
                                .addGap(61, 61, 61)
                                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_hurdle1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_hurdle2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_hurdle3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(signPanelLayout.createSequentialGroup()
                                        .addComponent(sign_id, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(btn_idcheck))
                                    .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(sign_pwcheck, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sign_pw, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(signPanelLayout.createSequentialGroup()
                                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_name1)
                                    .addComponent(lbl_birth1)
                                    .addComponent(lbl_cap))
                                .addGap(89, 89, 89)
                                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_hurdle4, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(sign_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sign_name, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lb_cap, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(169, 169, 169))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                        .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tf_cap, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_sign1))
                        .addGap(407, 407, 407))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btn_back2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_title1)
                .addGap(417, 417, 417))
        );
        signPanelLayout.setVerticalGroup(
            signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_title1)
                    .addComponent(btn_back2))
                .addGap(56, 56, 56)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_id1)
                    .addComponent(sign_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_idcheck))
                .addGap(28, 28, 28)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pw1)
                    .addComponent(sign_pw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pwcheck1)
                    .addComponent(sign_pwcheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(lbl_hurdle1)
                .addGap(18, 18, 18)
                .addComponent(lbl_hurdle2)
                .addGap(18, 18, 18)
                .addComponent(lbl_hurdle3)
                .addGap(15, 15, 15)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_name1)
                    .addComponent(sign_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_birth1)
                    .addComponent(sign_birth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_hurdle4)
                .addGap(27, 27, 27)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_cap)
                    .addComponent(lb_cap, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tf_cap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(btn_sign1)
                .addGap(30, 30, 30))
        );

        loginPanel.setBackground(new java.awt.Color(252, 252, 252));

        lbl_title.setText("로그인");

        lbl_id.setText("아이디");

        lbl_pw.setText("비밀번호");

        btn_login1.setText("로그인");
        btn_login1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_login1ActionPerformed(evt);
            }
        });

        login_back.setText("뒤로가기");
        login_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login_backActionPerformed(evt);
            }
        });

        lbl_cap1.setText("인증");

        tf_cap1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_cap1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(login_back)
                        .addGap(303, 303, 303)
                        .addComponent(lbl_title))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_id)
                            .addComponent(lbl_pw))
                        .addGap(76, 76, 76)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(login_pw, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(login_id, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                                .addComponent(lbl_cap1)
                                .addGap(89, 89, 89)
                                .addComponent(lb_cap1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                                .addComponent(tf_cap1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(100, 100, 100))))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(315, 315, 315)
                        .addComponent(btn_login1)))
                .addContainerGap(283, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_title)
                    .addComponent(login_back))
                .addGap(85, 85, 85)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_id)
                    .addComponent(login_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(108, 108, 108)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pw)
                    .addComponent(login_pw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_cap1)
                    .addComponent(lb_cap1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tf_cap1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(btn_login1)
                .addContainerGap(212, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        btn_login.setText("로그인");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        btn_sign.setText("회원가입");
        btn_sign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153)
                .addComponent(btn_sign)
                .addContainerGap(254, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_sign, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(272, 272, 272))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        cardLayout.show(getContentPane(), "LoginPanel");
    }//GEN-LAST:event_btn_loginActionPerformed

        
    private void btn_signActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_signActionPerformed
        cardLayout.show(getContentPane(), "SignPanel");
    }//GEN-LAST:event_btn_signActionPerformed

    private void btn_login1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_login1ActionPerformed
        // 사용자가 입력한 아이디와 비밀번호
        String userId = login_id.getText();
        String userPw = login_pw.getText();

        // 아이디와 비밀번호 필드가 비어 있는지 확인
        if(tf_cap1.getText().equals(captchaText)) { //캡챠 마지막 수정해야함 
            JOptionPane.showMessageDialog(this, "보안 문자가 틀립니다.!");
            generateCaptcha(lb_cap1);
        } else if(userId.isEmpty() || userPw.isEmpty()) {
            JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 입력해주세요.");
        } 
        else {
            // 데이터베이스에서 사용자 정보를 조회하는 SQL 쿼리
            String strSQL = "SELECT * FROM user WHERE id = '" + userId + "' AND pw = '" + userPw + "'";
            try {
                DBM.dbOpen();
                DBM.DB_rs = DBM.DB_stmt.executeQuery(strSQL);
                
                if(DBM.DB_rs.next()) { // 결과가 있으면 로그인 성공
                    JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다.");
                    // 여기에 로그인 성공 후의 로직을 추가하세요.
                    Main main = new Main(userId); // Main 객체 생성
                    main.setVisible(true); // NewJFrame을 표시합니다.
                    this.setVisible(false); // 로그인 창을 숨깁니다.
                    this.dispose(); // 로그인 창의 자원을 해제합니다.
                } else { // 결과가 없으면 로그인 실패
                    JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 일치하지 않습니다.");
                }

                DBM.DB_rs.close();
                DBM.dbClose();
            } catch (Exception e) {
                System.out.println("SQLException: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "로그인 처리 중 오류가 발생했습니다.");
            }
        }
    }//GEN-LAST:event_btn_login1ActionPerformed

    private void login_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_login_backActionPerformed
        cardLayout.show(getContentPane(), "InitialPanel");
    }//GEN-LAST:event_login_backActionPerformed

    private void btn_back2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back2ActionPerformed
        cardLayout.show(getContentPane(), "InitialPanel");
    }//GEN-LAST:event_btn_back2ActionPerformed

    private void sign_birthKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sign_birthKeyReleased
        String birth = sign_birth.getText();

        if (birth.length() > 8) {
            // 입력이 8자리를 초과하는 경우, 입력을 무시하고 이전 상태로 되돌립니다.
            sign_birth.setText(birth.substring(0, 8));
        }

        if(birth.length() == 8){
            lbl_hurdle4.setForeground(new Color(0,204,102));
        }
    }//GEN-LAST:event_sign_birthKeyReleased

    private void sign_pwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sign_pwKeyReleased
        String passwordPattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$";
        //String passwordPattern = "^((?=.*[a-z])(?=.*\\d))";
        if(String.valueOf(sign_pw.getPassword()).length() >= 8){
            lbl_hurdle2.setForeground(new Color(0,204,102));
        }else{
            lbl_hurdle2.setForeground(new Color(255,51,51));//빨강
        }

        // 정규식 검사
        if (Pattern.matches(passwordPattern, String.valueOf(sign_pw.getPassword()))) {
            lbl_hurdle1.setForeground(new Color(0,204,102));
        }else{
            lbl_hurdle1.setForeground(new Color(255,51,51));
        }
        
        if(String.valueOf(sign_pwcheck.getPassword()).equals(String.valueOf(sign_pw.getPassword()))){
            lbl_hurdle3.setForeground(new Color(0,204,102));
        }else{
            lbl_hurdle3.setForeground(new Color(255,51,51));
        }
    }//GEN-LAST:event_sign_pwKeyReleased

    private void sign_pwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sign_pwActionPerformed
        cardLayout.show(getContentPane(), "InitialPanel");
    }//GEN-LAST:event_sign_pwActionPerformed

    private void btn_idcheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_idcheckActionPerformed
        String ID = sign_id.getText();
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
                id_temp = sign_id.getText();
                btn_sign1.setEnabled(true);
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_idcheckActionPerformed

    private void btn_sign1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sign1ActionPerformed
        strSQL = "Insert Into user Values (";
        strSQL += "'" + sign_id.getText() + "', ";
        strSQL += "'" + sign_pwcheck.getText() + "', ";
        strSQL += "'" + sign_name.getText() + "', ";
        strSQL += "'" + sign_birth.getText() + "')";
        String passwordPattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$";
        
        if(sign_id.getText().isEmpty() || sign_birth.getText().isEmpty() || sign_name.getText().isEmpty() || sign_pw.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "모든 입력란을 채워주세요.");
            return;
        }
        else if (!tf_cap.getText().equals(captchaText)) {
            JOptionPane.showMessageDialog(this, "보안 문자가 틀립니다.");
            generateCaptcha(lb_cap);
            return;
        }
        else if(5 > sign_id.toString().length() &&  20 <= sign_id.toString().length()){
            JOptionPane.showMessageDialog(this, "아이디는 6~20자로 지정해주세요.");
            return;
        }
        else if(!id_temp.equals(sign_id.getText())){
            System.out.println("NewJFraWQEWQEQWEQWEWQwme.btn_sign1ActionPerformed() : " + id_temp + " sign : "+ sign_id.getText());
            JOptionPane.showMessageDialog(this, "중복확인을 다시해주세요");
            return;
        }
//        else if(!sign_pw.getPassword().equals(sign_pwcheck.getPassword()) || sign_pw.getPassword() != sign_pwcheck.getPassword()){
//            JOptionPane.showMessageDialog(this, "비밀번호가 다릅니다.");
//            return;
//        }
        else if(!Pattern.matches(passwordPattern, String.valueOf(sign_pw.getPassword()))) {
            JOptionPane.showMessageDialog(this, "비밀번호 양식을 확인해주세요.");
            return;
        }
        else{
            try {
                Random rand = new Random();
                DBM.dbOpen();
                DBM.DB_stmt.executeUpdate(strSQL);
                strSQL = "Select * From user";
                getDBData(strSQL);
                long randomNumber = (long) (rand.nextDouble() * 10000000000L);
                String random_Calendar_key = String.valueOf(randomNumber);
                strSQL = "INSERT INTO Calendar (id, calendar_name,calendar_key) " + " VALUES ('" + sign_id.getText() + "', '내 캘린더'" + "," + random_Calendar_key + ");";
                
                System.out.println(strSQL);
                DBM.DB_stmt.execute(strSQL);
                JOptionPane.showMessageDialog(null, "회원가입 완료");
                // 회원가입 성공 후, 초기 화면으로 돌아가기
                cardLayout.show(getContentPane(), "InitialPanel");
                DBM.dbClose();
            } catch (Exception e) {
                System.out.println("SQLException: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "회원가입 처리 중 오류가 발생했습니다.");
            }
        }
    }//GEN-LAST:event_btn_sign1ActionPerformed

    private void sign_pwcheckKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sign_pwcheckKeyReleased
        if (String.valueOf(sign_pw.getPassword()).equals(String.valueOf(sign_pwcheck.getPassword())) ) {
            lbl_hurdle3.setForeground(new Color(0,204,102));
        }else{
            lbl_hurdle3.setForeground(new Color(255,51,51));
        }
    }//GEN-LAST:event_sign_pwcheckKeyReleased

    private void tf_cap1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_cap1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_cap1ActionPerformed

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }
    private String id_temp;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back2;
    private javax.swing.JButton btn_idcheck;
    private javax.swing.JButton btn_login;
    private javax.swing.JButton btn_login1;
    private javax.swing.JButton btn_sign;
    private javax.swing.JButton btn_sign1;
    private javax.swing.JLabel lb_cap;
    private javax.swing.JLabel lb_cap1;
    private javax.swing.JLabel lbl_birth1;
    private javax.swing.JLabel lbl_cap;
    private javax.swing.JLabel lbl_cap1;
    private javax.swing.JLabel lbl_hurdle1;
    private javax.swing.JLabel lbl_hurdle2;
    private javax.swing.JLabel lbl_hurdle3;
    private javax.swing.JLabel lbl_hurdle4;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_id1;
    private javax.swing.JLabel lbl_name1;
    private javax.swing.JLabel lbl_pw;
    private javax.swing.JLabel lbl_pw1;
    private javax.swing.JLabel lbl_pwcheck1;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JLabel lbl_title1;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JButton login_back;
    private javax.swing.JTextField login_id;
    private javax.swing.JTextField login_pw;
    private javax.swing.JPanel signPanel;
    private javax.swing.JTextField sign_birth;
    private javax.swing.JTextField sign_id;
    private javax.swing.JTextField sign_name;
    private javax.swing.JPasswordField sign_pw;
    private javax.swing.JPasswordField sign_pwcheck;
    private javax.swing.JTextField tf_cap;
    private javax.swing.JTextField tf_cap1;
    // End of variables declaration//GEN-END:variables
}
