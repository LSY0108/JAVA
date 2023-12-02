package main;

import db.DB_MAN;
import db.DB_MAN;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Main extends javax.swing.JFrame {
    DB_MAN DBM = new DB_MAN();
    private String userId;
    private int calendar_key = -1;
    
    private Calendar cal;
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private JDialog memoListDialog = null;
    
    // 메뉴 바 필드
    private JMenuBar menuBar;
    private JMenu calendarMenu;
    private JMenuItem MyCalendar;
    private JMenuItem ShareCalendar;
    
    private JMenu settingMenu;
    private JMenuItem CalendarSetting;
    private JMenuItem MyInfoSetting;
    private JMenuItem addCalendar;
    
    
    // Color 객체를 HEX 색상 코드 문자열로 변환하는 메서드
    private String colorToString(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
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
                updateCalendar("내 캘린더");
            }
        });

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cal.add(Calendar.MONTH, 1);
                updateCalendar(" 내 캘린더");
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

        updateCalendar("내 캘린더");
        // 메뉴 바 초기화
        initializeMenuBar();
    }

    private void loadUserCalendars(String userId) {
        List<String> calendarNames = getUserCalendars(userId);

        // 메뉴 항목 초기화 전에 기존 항목을 지웁니다.
        calendarMenu.removeAll();

        // 데이터베이스에서 가져온 캘린더 이름으로 메뉴 항목을 생성합니다.
        for(String calendarName : calendarNames){
            JMenuItem calendarItem = new JMenuItem(calendarName);
            calendarItem.addActionListener(e -> selectCalendar(calendarName));
            calendarMenu.add(calendarItem);
        }
    }
    
    private List<String> getUserCalendars(String userId) {
        String sql = "SELECT calendar_name FROM Calendar WHERE id = ?";
        List<String> calendarNames = new ArrayList<>();

        try {
            DBM.dbOpen();
            PreparedStatement pstmt = DBM.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                calendarNames.add(rs.getString("calendar_name"));
            }
            DBM.dbClose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendarNames;
    }
    
    private void selectCalendar(String calendarName) {
        // 선택한 캘린더의 `calendar_key`를 검색합니다.
        String sql = "SELECT calendar_key FROM Calendar WHERE id = ? AND calendar_name = ?";
        try {
            DBM.dbOpen();
            PreparedStatement pstmt = DBM.prepareStatement(sql);
            pstmt.setString(1, this.userId);
            pstmt.setString(2, calendarName);
            System.out.println("sql : " + pstmt);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.calendar_key = rs.getInt("calendar_key");
                updateCalendar(calendarName); // 캘린더를 업데이트합니다.
            } else {
                JOptionPane.showMessageDialog(this, "선택한 캘린더가 존재하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
            DBM.dbClose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "캘린더 선택 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeMenuBar() {
        menuBar = new JMenuBar();
    
        // 캘린더 메뉴
        calendarMenu = new JMenu("캘린더");
        menuBar.add(calendarMenu); // 캘린더 메뉴를 메뉴 바에 추가
        
       // 설정 메뉴
        settingMenu = new JMenu("설정");
        CalendarSetting = new JMenuItem("캘린더 설정");
        MyInfoSetting = new JMenuItem("내 정보 보기");
        addCalendar = new JMenuItem("캘린더 추가");

        // 설정 메뉴 아이템에 액션 리스너 추가
        CalendarSetting.addActionListener(e -> CalendarSetting());
        MyInfoSetting.addActionListener(e -> MyInfoSetting());
        addCalendar.addActionListener(e -> addCalendar());

        // 설정 메뉴에 메뉴 아이템 추가
        settingMenu.add(CalendarSetting);
        settingMenu.add(MyInfoSetting);
        settingMenu.add(addCalendar);

        // 메뉴 바에 설정 메뉴 추가
        menuBar.add(settingMenu);

        // 메뉴 바를 프레임에 추가
        setJMenuBar(menuBar);

        // 사용자 캘린더 불러오기 (사용자가 로그인 할 때 호출)
        loadUserCalendars(this.userId);
    }

    private void MyCalendar() {
        // 사용자 캘린더 불러오기
        loadUserCalendars(this.userId);
    }

    private void ShareCalendar() {
        // 공유캘린더 로직 구현
    }
    
    private void CalendarSetting() {
        // 캘린더 설정 대화상자에 필요한 컴포넌트를 초기화합니다.
        JDialog calendarSettingDialog = new JDialog(this, "캘린더 설정", true);
        calendarSettingDialog.setLayout(new FlowLayout());

        JLabel keyLabel = new JLabel("선택한 캘린더 키: " + this.calendar_key);
        JButton deleteButton = new JButton("삭제");

        // 삭제 버튼 액션 리스너
        deleteButton.addActionListener(e -> {
                deleteCalendar(this.calendar_key); // 캘린더 삭제 메서드 호출
                calendarSettingDialog.dispose(); // 설정 대화상자 닫기
        });

        calendarSettingDialog.add(keyLabel);
        calendarSettingDialog.add(deleteButton);

        calendarSettingDialog.pack();
        calendarSettingDialog.setLocationRelativeTo(this);
        calendarSettingDialog.setVisible(true);
    }
    
    private void MyInfoSetting() {
        // 나의 정보 설정 로직 구현
    }
    
    private void addCalendar() {
        // 캘린더 이름을 입력받는 대화상자를 생성
        String calendarName = JOptionPane.showInputDialog(this, "새 캘린더 이름을 입력하세요:");

        // 사용자가 취소를 누르지 않고 이름을 입력했다면 데이터베이스에 저장
        if (calendarName != null && !calendarName.trim().isEmpty()) {
            saveNewCalendar(this.userId, calendarName);
        }
    }
    
    private void saveNewCalendar(String userId, String calendarName) {
        String sqlInsert = "INSERT INTO Calendar (id, calendar_name) VALUES (?, ?)";
        String sqlSelectKey = "SELECT calendar_key FROM Calendar WHERE id = ? AND calendar_name = ?";

        try {
            DBM.dbOpen();
            // 새 캘린더를 데이터베이스에 추가합니다.
            PreparedStatement pstmtInsert = DBM.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, userId);
            pstmtInsert.setString(2, calendarName);
            pstmtInsert.executeUpdate();

            // 새로 추가한 캘린더의 `calendar_key`를 검색합니다.
            PreparedStatement pstmtSelectKey = DBM.prepareStatement(sqlSelectKey);
            pstmtSelectKey.setString(1, userId);
            pstmtSelectKey.setString(2, calendarName);
            ResultSet rs = pstmtSelectKey.executeQuery();

            if (rs.next()) {
                int newCalendarKey = rs.getInt("calendar_key");
                // 새 캘린더를 선택하고 화면에 표시합니다.
                this.calendar_key = newCalendarKey;
                updateCalendar(calendarName); // 캘린더를 업데이트합니다.
            }
            DBM.dbClose();

            // 캘린더 메뉴를 업데이트합니다.
            loadUserCalendars(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "캘린더 추가에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private int getMyCalendarKey(String userId, String cal_name) {
        String sql = "SELECT calendar_key FROM Calendar WHERE id = ? AND calendar_name = ?";
        

        try {
            DBM.dbOpen();
            PreparedStatement pstmt = DBM.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, cal_name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                calendar_key = rs.getInt("calendar_key");
            }

            DBM.dbClose();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return calendar_key;
    }
    
    private void deleteCalendar(int calendarKey) {
           // 사용자에게 캘린더 삭제 확인을 요청합니다.
          int response = JOptionPane.showConfirmDialog(this, "캘린더를 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);

          // '예'를 선택한 경우에만 삭제를 진행합니다.
          if (response == JOptionPane.YES_OPTION) {
              String sql = "DELETE FROM Calendar WHERE calendar_key = ?";

              try {
                  DBM.dbOpen();
                  PreparedStatement pstmt = DBM.prepareStatement(sql);
                  pstmt.setInt(1, calendarKey);
                  int result = pstmt.executeUpdate();
                  DBM.dbClose();

                  if (result > 0) {
                      // 삭제 성공 메시지를 표시합니다.
                      JOptionPane.showMessageDialog(this, "캘린더가 삭제되었습니다.");

                      // 삭제된 캘린더를 메뉴에서 제거합니다.
                      loadUserCalendars(this.userId);

                      // '내 캘린더' 뷰로 돌아갑니다.
                      updateCalendar("내 캘린더"); // 캘린더 화면을 갱신합니다.
                  } else {
                      // 삭제 실패 메시지를 표시합니다.
                      JOptionPane.showMessageDialog(this, "캘린더 삭제에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                  }
              } catch (SQLException e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(this, "캘린더 삭제 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
              }
          }
    }
    
    private void updateCalendar(String cal_name) {
        calendarPanel.removeAll();
        calendarPanel.setBackground(new Color(244, 243, 243)); 
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        monthLabel.setText(String.format("%tB %d", cal, year));
        
        // 현재 로그인한 사용자의 '내 캘린더'에 해당하는 calendar_key를 가져옵니다.
        calendar_key = getMyCalendarKey(userId,cal_name);
        System.out.println("새로 바뀌는 구역 c_key" + calendar_key);
        
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
            ArrayList<MemoInfo> memos = loadMemosFromDB(this.calendar_key, day, month, year);
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
                    ArrayList<MemoInfo> memos = loadMemosFromDB(calendar_key);
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
        // 기존에 열려있는 메모 목록 창을 닫습니다.
        if (memoListDialog != null) {
            memoListDialog.dispose();
        }

        // 새로운 메모 목록 창을 생성합니다.
        memoListDialog = new JDialog(this, "메모 목록", true);
        memoListDialog.setTitle(String.format("메모 목록 - %d년 %d월 %d일", year, month + 1, day));
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
                    displayMemoDetails(calendar_key, selectedMemo, day, month, year);
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

    private String cal_keyToCal_name(int calendar_key){
        String sql = "SELECT ? FROM Calendar WHERE calendar_key = ?";
        String c_key = null;
            try {
                DBM.dbOpen();
                PreparedStatement pstmt = DBM.prepareStatement(sql);
                pstmt.setString(1, "calendar_name");
                pstmt.setInt(2, calendar_key);

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    c_key = rs.getString("calendar_name");
                }

                DBM.dbClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return c_key;
    }
    // 메모 상세보기 및 편집 메서드
    private void displayMemoDetails(int calendar_key, MemoInfo memo, int day, int month, int year) {
        JDialog memoDetailsDialog = new JDialog(this, "메모 상세보기", true);
        memoDetailsDialog.setTitle(String.format("메모 상세보기 - %d년 %d월 %d일", year, month + 1, day));
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
            deleteMemo(memo.getSchedule_key());
            memoDetailsDialog.dispose();
            updateCalendar(cal_keyToCal_name(calendar_key));
            if (memoListDialog != null) {
                memoListDialog.dispose();
            }
            displayMemoList(loadMemosFromDB(calendar_key), day, month, year);
        });

        buttonPanel.add(backButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        memoDetailsDialog.add(titleLabel, BorderLayout.NORTH);
        memoDetailsDialog.add(contentLabel, BorderLayout.CENTER);
        memoDetailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        memoDetailsDialog.setVisible(true);
    }
    
    // 메모를 데이터베이스에서 삭제하는 메서드
    private void deleteMemo(int scheduleKey) {
        String sql = "DELETE FROM Schedule WHERE schedule_key = ?";
        try {
            DBM.dbOpen();
            PreparedStatement pstmt = DBM.prepareStatement(sql);
            pstmt.setInt(1, scheduleKey);
            pstmt.executeUpdate();
            DBM.dbClose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        memoEditorDialog.setTitle(String.format("메모 편집기 - %d년 %d월 %d일", year, month + 1, day));
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
            int scheduleKey = memo != null ? memo.getSchedule_key() : -1;
            Color color = chosenColor[0];

            MemoInfo updatedMemo = new MemoInfo(scheduleKey, title, content, color);
            saveMemo(userId, day, month, year, updatedMemo);

            memoEditorDialog.dispose();
            updateCalendar(cal_keyToCal_name(calendar_key));

            // 메모 목록을 업데이트합니다.
            ArrayList<MemoInfo> updatedMemos = loadMemosFromDB(calendar_key);
            displayMemoList(updatedMemos, day, month, year);
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
            ArrayList<MemoInfo> memos = loadMemosFromDB(calendar_key);
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
                // MemoInfo 객체를 생성
                MemoInfo newMemo = new MemoInfo(-1, title, content, chosenColor); // 새 메모의 경우 schedule_key는 -1 또는 다른 유효하지 않은 값으로 설정
                saveMemo(userId, day, month, year, newMemo); // 수정된 saveMemo 메서드 호출
                dispose();
                ((Main)parent).updateCalendar(cal_keyToCal_name(calendar_key));
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
        private void saveMemo(String userId, int day, int month, int year, MemoInfo memo) {
            String formattedDate = String.format("%d-%02d-%02d", year, month + 1, day);
            String sql;

            // schedule_key가 존재하는지 확인하여 쿼리를 결정합니다.
            if (memo.getSchedule_key() > 0) {
                // 메모 업데이트
                sql = "UPDATE Schedule SET title = ?, content = ?, color = ? WHERE schedule_key = ?";
            } else {
                // 새 메모 추가
                sql = "INSERT INTO Schedule (id, date, title, content, color, calendar_key) VALUES (?, ?, ?, ?, ?, ?)";
            }

            try {
                DBM.dbOpen();
                PreparedStatement pstmt = DBM.prepareStatement(sql);

                if (memo.getSchedule_key() > 0) {
                    // 업데이트 쿼리의 경우
                    pstmt.setString(1, memo.getTitle());
                    pstmt.setString(2, memo.getContent());
                    pstmt.setString(3, colorToString(memo.getColor()));
                    pstmt.setInt(4, memo.getSchedule_key());
                } else {
                    // 새 일정 추가 쿼리의 경우
                    pstmt.setString(1, userId);
                    pstmt.setString(2, formattedDate);
                    pstmt.setString(3, memo.getTitle());
                    pstmt.setString(4, memo.getContent());
                    pstmt.setString(5, colorToString(memo.getColor()));
                    pstmt.setInt(6, calendar_key);
                }

                pstmt.executeUpdate();
                DBM.dbClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // HEX 색상 코드 문자열을 Color 객체로 변환하는 메서드
        private Color stringToColor(String colorStr) {
            return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
        }
        
        // 메모 정보를 가져오는 메서드
        private ArrayList<MemoInfo> loadMemosFromDB(int calendarKey) {
            ArrayList<MemoInfo> memos = new ArrayList<>();
            String sql = "SELECT schedule_key, title, content, color FROM Schedule WHERE calendar_key = ?";

            try {
                DBM.dbOpen();
                PreparedStatement pstmt = DBM.prepareStatement(sql);
                pstmt.setInt(1, calendarKey);

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int schedule_key = rs.getInt("schedule_key");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    Color color = stringToColor(rs.getString("color"));
                    memos.add(new MemoInfo(schedule_key, title, content, color));
                }

                DBM.dbClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return memos;
        }
        
        // 새로운 오버로드된 메서드
        private ArrayList<MemoInfo> loadMemosFromDB(int calendarKey, int day, int month, int year) {
            ArrayList<MemoInfo> memos = new ArrayList<>();
            String formattedDate = String.format("%d-%02d-%02d", year, month + 1, day);
            String sql = "SELECT schedule_key, title, content, color FROM Schedule WHERE calendar_key = ? AND date = ?";

            try {
                DBM.dbOpen();
                PreparedStatement pstmt = DBM.prepareStatement(sql);
                pstmt.setInt(1, calendarKey);
                pstmt.setString(2, formattedDate);

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int schedule_key = rs.getInt("schedule_key");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    Color color = stringToColor(rs.getString("color"));
                    memos.add(new MemoInfo(schedule_key, title, content, color));
                }

                DBM.dbClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return memos;
        }
        
        private void updateMemo(int scheduleKey, String title, String content, Color color) {
            // 날짜 형식을 'YYYY-MM-DD'로 변환합니다.
            String sql = "UPDATE Schedule SET title = ?, content = ?, color = ? WHERE schedule_key = ?";
            try {
                DBM.dbOpen();
                PreparedStatement pstmt = DBM.prepareStatement(sql);

                // 파라미터 설정
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setString(3, colorToString(color));
                pstmt.setInt(4, scheduleKey);

                pstmt.executeUpdate(); // SQL 쿼리 실행
                DBM.dbClose();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // 메모 정보를 저장하는 클래스입니다.
        class MemoInfo {
            private int schedule_key;
            private String title; // 메모 제목
            private String content; // 메모 내용
            private Color color; // 메모 색상

            public MemoInfo(int schedule_key, String title, String content, Color color) {
                this.schedule_key = schedule_key;
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
            
            public int getSchedule_key() {
                return schedule_key;
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
