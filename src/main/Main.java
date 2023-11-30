package main;

import db.DB_MAN;
import db.DB_MAN;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Main extends javax.swing.JFrame {
    DB_MAN DBM = new DB_MAN();
    private String userId;
    
    private Calendar cal;
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private JDialog memoListDialog = null;
    
    // 메모 데이터를 저장하는 HashMap입니다.
    private HashMap<String, ArrayList<MemoInfo>> memoData = new HashMap<>();
    
    public Main(String userId) {
        this.userId = userId;
        initComponents();
        
        
        setTitle("Calendar");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 프레임을 화면의 중앙에 위치시킴
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cal = new GregorianCalendar();
        monthLabel = new JLabel();
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cal.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cal.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // 달력 상단 패널 설정
        JPanel headerPanel = new JPanel();
        headerPanel.add(prevButton);
        headerPanel.add(monthLabel);
        headerPanel.add(nextButton);
        // 여기에 배경색을 설정하는 코드를 추가합니다.
        headerPanel.setBackground(new Color(244, 243, 243)); 
        
        // 달력 패널 초기화
        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5)); // 7 columns for days of the week
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        
        for (String day : days) {
            calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        add(headerPanel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);

        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();
        calendarPanel.setBackground(new Color(244, 243, 243)); 
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        monthLabel.setText(String.format("%tB %d", cal, year));

        Calendar tempCal = new GregorianCalendar(year, month, 1);
        int startDay = tempCal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // 날짜별 패널 생성 및 이벤트 리스너 설정
        for (int i = 1; i <= daysInMonth; i++) {
            final int day = i;
            JPanel datePanel = new JPanel();
            datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
            datePanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            datePanel.setBackground(new Color(252, 251, 251));
            datePanel.setOpaque(true);

            JLabel dayLabel = new JLabel(Integer.toString(day));
            dayLabel.setFont(new Font("Serif", Font.BOLD, 16));
            dayLabel.setForeground(new Color(5, 5, 5));
            dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dayLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            datePanel.add(dayLabel);

            // 날짜별로 저장된 메모의 색상 띠를 가져옵니다.
            ArrayList<MemoInfo> memos = getMemosForDate(day, month, year);
            if (!memos.isEmpty()) {
                datePanel.add(Box.createVerticalStrut(1));
                for (MemoInfo memo : memos) {
                    JPanel colorBar = new JPanel();
                    colorBar.setBackground(memo.getColor());
                    colorBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
                    colorBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                    datePanel.add(colorBar);
                    datePanel.add(Box.createVerticalStrut(2));
                }
            }

            // 날짜 패널에 마우스 리스너 추가
            datePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ArrayList<MemoInfo> memos = getMemosForDate(day, month, year);
                    if (memos.isEmpty()) {
                        displayMemoEditor(null, day, month, year);
                    } else {
                        displayMemoList(memos, day, month, year);
                    }
                }
            });

            calendarPanel.add(datePanel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
    
    // 메모 목록 표시 메서드
    private void displayMemoList(ArrayList<MemoInfo> memos, int day, int month, int year) {
        if (memoListDialog == null) {
            memoListDialog = new JDialog(this, "메모 목록", false);
            memoListDialog.setLayout(new BorderLayout());
            memoListDialog.setSize(300, 400);
            memoListDialog.setLocationRelativeTo(this);
        } else {
            memoListDialog.getContentPane().removeAll(); // 내용을 모두 제거합니다.
        }
        
        JDialog memoListDialog = new JDialog(this, "메모 목록", true);
        memoListDialog.setLayout(new BorderLayout());
        memoListDialog.setSize(300, 400);
        memoListDialog.setLocationRelativeTo(this);

        // JList와 커스텀 렌더러를 사용합니다.
        JList<MemoInfo> memoList = new JList<>(new Vector<>(memos));
        memoList.setCellRenderer(new MemoListCellRenderer());

        memoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                MemoInfo selectedMemo = memoList.getSelectedValue();
                if (selectedMemo != null) {
                    displayMemoDetails(selectedMemo, day, month, year);
                }
            }
        });

        memoListDialog.add(new JScrollPane(memoList), BorderLayout.CENTER);

        JButton addButton = new JButton("일정 추가하기");
        addButton.addActionListener(e -> displayMemoEditor(null, day, month, year));
        memoListDialog.add(addButton, BorderLayout.SOUTH);

        memoListDialog.setVisible(true);
    }
    
    // 메모 목록을 위한 커스텀 렌더러
    class MemoListCellRenderer extends JPanel implements ListCellRenderer<MemoInfo> {
        private JLabel titleLabel;
        private JPanel colorPanel;

        public MemoListCellRenderer() {
            setLayout(new BorderLayout());
            titleLabel = new JLabel();
            titleLabel.setOpaque(true);
            titleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 텍스트 주변의 여백 설정

            colorPanel = new JPanel();
            colorPanel.setPreferredSize(new Dimension(10, 10)); // 색상 띠의 크기 설정

            add(colorPanel, BorderLayout.WEST);
            add(titleLabel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends MemoInfo> list, MemoInfo memo, int index, boolean isSelected, boolean cellHasFocus) {
            titleLabel.setText(memo.getTitle());
            titleLabel.setBackground(isSelected ? new Color(237, 245, 251) : Color.WHITE); // 선택된 항목에 대한 배경색 설정
            titleLabel.setForeground(Color.BLACK);

            colorPanel.setBackground(memo.getColor()); // 메모의 색상 띠 설정

            // 선택 상태일 때의 디자인
            if (isSelected) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 120, 215), 2), // 테두리 색상과 두께 설정
                    BorderFactory.createEmptyBorder(5, 5, 5, 5))); // 내부 여백 설정
            } else {
                setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7)); // 내부 여백 설정
            }

            return this;
        }
    }

    // 메모 상세보기 및 편집 메서드
    private void displayMemoDetails(MemoInfo memo, int day, int month, int year) {
        JDialog memoDetailsDialog = new JDialog(this, "메모 상세보기", true);
        memoDetailsDialog.setLayout(new BorderLayout(10, 10)); // 여백을 추가합니다.
        memoDetailsDialog.setSize(350, 200);
        memoDetailsDialog.setLocationRelativeTo(this);

        // 타이틀과 내용에 적용할 폰트와 색상을 정의합니다.
        Font titleFont = new Font("Serif", Font.BOLD, 20);
        Font contentFont = new Font("SansSerif", Font.PLAIN, 16);
        Color backgroundColor = new Color(255, 255, 255);
        Color textColor = new Color(108, 108, 108);

        // 타이틀 레이블
        JLabel titleLabel = new JLabel("제목: " + memo.getTitle());
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setBackground(backgroundColor);
        titleLabel.setOpaque(true);

        // 내용 레이블
        JLabel contentLabel = new JLabel("<html><body style='width: 200px'>" + memo.getContent() + "</body></html>");
        contentLabel.setFont(contentFont);
        contentLabel.setForeground(textColor);
        contentLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentLabel.setBackground(backgroundColor);
        contentLabel.setOpaque(true);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);

        JButton backButton = createCustomButton("뒤로가기");
        backButton.addActionListener(e -> memoDetailsDialog.dispose());

        JButton editButton = createCustomButton("수정하기");
        editButton.addActionListener(e -> {
            memoDetailsDialog.dispose();
            displayMemoEditor(memo, day, month, year);
        });

        JButton deleteButton = createCustomButton("삭제하기");
        deleteButton.addActionListener(e -> {
            // 삭제 로직 구현 필요
        });

        buttonPanel.add(backButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        memoDetailsDialog.add(titleLabel, BorderLayout.NORTH);
        memoDetailsDialog.add(contentLabel, BorderLayout.CENTER);
        memoDetailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        memoDetailsDialog.setVisible(true);
    }
    
    // 커스텀 버튼을 생성하는 메서드
    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(new Color(232, 240, 254));
        button.setForeground(new Color(100, 100, 100));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }
    
    // 메모 편집기 표시 메서드
    private void displayMemoEditor(MemoInfo memo, int day, int month, int year) {
        JDialog memoEditorDialog = new JDialog(this, "메모 편집기", true);
        memoEditorDialog.setLayout(new BorderLayout());
        memoEditorDialog.setSize(300, 400);
        memoEditorDialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField();
        JTextArea contentArea = new JTextArea(10, 20);
        JButton colorButton = new JButton("색상 선택");
        final Color[] chosenColor = new Color[]{ Color.WHITE }; // 색상을 저장하기 위한 배열

        if (memo != null) {
            titleField.setText(memo.getTitle());
            contentArea.setText(memo.getContent());
            chosenColor[0] = memo.getColor(); // 기존 메모의 색상을 가져옵니다.
            colorButton.setBackground(chosenColor[0]);
        }

        colorButton.addActionListener(e -> {
            chosenColor[0] = JColorChooser.showDialog(memoEditorDialog, "색상 선택", chosenColor[0]);
            colorButton.setBackground(chosenColor[0]);
        });

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            String content = contentArea.getText();
            saveMemo(day, month, year, title, content, chosenColor[0]);
            memoEditorDialog.dispose();
            updateCalendar();
        });
        buttonPanel.add(saveButton);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(titleField, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        inputPanel.add(colorButton, BorderLayout.SOUTH);

        memoEditorDialog.add(inputPanel, BorderLayout.CENTER);
        memoEditorDialog.add(buttonPanel, BorderLayout.SOUTH);

        memoEditorDialog.setVisible(true);
    }
    
    // EventDialog 클래스
    class EventDialog extends JDialog {
        private JTextField titleField; // 제목 입력 필드
        private JTextArea contentArea; // 내용 입력 영역
        private JPanel scheduleListPanel; // 일정 목록 패널
        private Color chosenColor; // 사용자가 선택한 색상을 저장할 필드
        private int day, month, year;
        
        public EventDialog(JFrame parent, int day, int month, int year, boolean isAddingNewEvent) {
            //super(parent, "일정 관리: " + day + "/" + (month + 1) + "/" + year, true);
            super(parent, "일정 관리: " + year + "년 " + (month + 1) + "월 " + day + "일 ", true);
            this.day = day;
            this.month = month;
            this.year = year;
            
            getContentPane().setBackground(new Color(244, 243, 243));
            setLayout(new BorderLayout());

            // 제목과 내용 입력 영역을 초기화하지만, 먼저 숨깁니다.
            titleField = new JTextField();
            contentArea = new JTextArea(10, 30);
            titleField.setVisible(false);
            contentArea.setVisible(false);

            // 일정 목록 패널을 초기화합니다.
            scheduleListPanel = new JPanel();
            scheduleListPanel.setLayout(new BoxLayout(scheduleListPanel, BoxLayout.Y_AXIS));
            //scheduleListPanel.setBackground(Color.WHITE);
            add(scheduleListPanel, BorderLayout.CENTER);

            // 저장된 일정을 리스트에 표시합니다.
            ArrayList<MemoInfo> memos = getMemosForDate(day, month, year);
            for (MemoInfo memo : memos) {
                JButton memoButton = new JButton(memo.getTitle());
                memoButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, memo.getContent());
                });
                scheduleListPanel.add(memoButton);
            }

            JPanel buttonPanel = new JPanel();

            JButton addButton = new JButton("일정 추가하기");
            addButton.addActionListener(e -> {
                // 일정 추가를 위한 입력 필드와 텍스트 영역을 표시합니다.
                titleField.setVisible(true);
                contentArea.setVisible(true);
                scheduleListPanel.setVisible(false); // 일정 목록을 숨깁니다.
                addButton.setVisible(false); // 추가 버튼을 숨깁니다.
                pack(); // 대화상자 크기를 조정합니다.
            });
            buttonPanel.add(addButton);

            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(e -> {
                String title = titleField.getText();
                String content = contentArea.getText();
                saveMemo(day, month, year, title, content, chosenColor);
                dispose();
                ((Main)parent).updateCalendar();
            });
            buttonPanel.add(saveButton);

            JButton deleteButton = new JButton("삭제");
            // 삭제 로직은 구현해야 합니다.
            buttonPanel.add(deleteButton);

            JButton colorButton = new JButton("색상 선택");
            colorButton.addActionListener(e -> {
                chosenColor = JColorChooser.showDialog(EventDialog.this, "색상 선택", Color.WHITE);
                colorButton.setBackground(chosenColor);
            });
            buttonPanel.add(colorButton);

            add(titleField, BorderLayout.NORTH);
            add(new JScrollPane(contentArea), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(parent);
        }
    }
        // 메모와 색상을 저장하는 메서드의 예시입니다.
        private void saveMemo(int day, int month, int year, String title, String content, Color color) {
            String dateKey = year + "-" + String.format("%02d", (month+1)) + "-" + String.format("%02d", day);
            ArrayList<MemoInfo> memos = memoData.getOrDefault(dateKey, new ArrayList<MemoInfo>());
            memos.add(new MemoInfo(title, content, color));
            memoData.put(dateKey, memos);
            // 메모 저장 후 메모 목록을 업데이트합니다.
            displayMemoList(getMemosForDate(day, month, year), day, month, year);
        }
        
        // 날짜별로 저장된 메모 정보를 가져오는 메서드를 수정합니다.
        private ArrayList<MemoInfo> getMemosForDate(int day, int month, int year) {
            String dateKey = year + "-" + String.format("%02d", (month+1)) + "-" + String.format("%02d", day);
            return memoData.getOrDefault(dateKey, new ArrayList<MemoInfo>());
        }
        
        // 메모 정보를 저장하는 클래스입니다.
        class MemoInfo {
            private String title; // 메모 제목
            private String content; // 메모 내용
            private Color color; // 메모 색상

            public MemoInfo(String title, String content, Color color) {
                this.title = title;
                this.content = content;
                this.color = color;
            }

            public String getTitle() {
                return title;
            }

            public String getContent() {
                return content;
            }

            public Color getColor() {
                return color;
            }

        }
    public Main() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
