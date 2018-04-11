/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.lafassoftware;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author Timothy Opiyo
 */
public class FrameSignIn extends javax.swing.JFrame {
    
    String select = null;
    String workID;
    
    public static String clientId1, fnameTable, lnameTable, caseStatus1, assignedTo, email1;
    
    Connection con = null;
    Connection cons = null;
    Statement statement = null;
    String use1=null;
    public static String usernameProf = null;
    public static String profAc1 = null;
    public static String profAc2 = null;
    public static String ProfEmp = null;
    public static String profEmail = null;
    public static String areaOf = null;
    public static String dateHired = null;
    public static String profPhone = null;
    public static String profNatid = null;
    public static String profDept = null;
    
    
    String clientId = "", fname = "", lname = "", caseStatus = "", workerId = "";
    String[] columnNames = {"Client ID", "First Name", "Second Name", "Case status", "Assigned to"};
    DefaultTableModel model = new DefaultTableModel();
    
    String reportOne = "", reportByOne = "", reportDateOne = "";
    String[] reportColumns = {"Report ID", "By", "Created"};
    DefaultTableModel reportTable = new DefaultTableModel();
    
    public static String eventIDmouse, eventNameMouse, EventDateMouse, eventCreatorMouse;
    String eventIDone = "", eventOne = "", eventDate = "", eventBy = "";
    String[] eventColumns = {"Event ID", "Event Name", "Date of Event", "Created By"};
    DefaultTableModel eventTableList = new DefaultTableModel();
    
    public static String schId1, schName1, schDate1, scchLocation1;
    String scheduleOne = "", scheduleName = "", scheduleDate = "", scheduleWhere = "";
    String[] schedduleColumns = {"Schedule ID", "Name", "Date", "Location"};
    DefaultTableModel schedulelist = new DefaultTableModel();
    
    String unpaidID = "", unpaidDate = "", unpaidMethodPay = "", unpaidTotal = "";
    String[] unpaidColumns = {"Invoice ID", "Invoice date", "Payment Method", "total"};
    DefaultTableModel unpaidlist = new DefaultTableModel();
    
    String paidID = "", paidDate = "", paidMethodPay = "", paidTotal = "";
    String[] paidColumns = {"Invoice ID", "Invoice date", "Payment Method", "total"};
    DefaultTableModel paidlist = new DefaultTableModel();
    
    String jtreevar;

    /**
     * Creates new form NewJFrame
     *
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    public FrameSignIn() throws ParseException, IOException {
        initComponents();
        setLocationRelativeTo(null);
        setdate();
        DisplayClient();
        DisplayReportList();
        DisplayEventList();
        DisplaySchedule();
        DisplayUnpaid();
        DisplayPaid();
        ProfileInfo();
        loadEventDashboard();
        reportDashboardInfo();
       
        labelUser.setText(LawFirmHome.usernameLabel);
        labelWorkid.setText((LawFirmHome.userId));
        txtEmpId.setText(LawFirmHome.userId);
        
        Date format = new SimpleDateFormat("MM-DD-YYYY").parse("01-01-2015");
        dateChooserFiling.setMinSelectableDate(format);
        Date date = new Date();
        dateChooserFiling.setMaxSelectableDate(date);
        Date schedule = new Date();
        dateChooserSchedule.setMinSelectableDate(schedule);
        
        spinFieldHours.setMinimum(1);
        spinFieldHours.setMaximum(8);
        
        Date dateevents = new Date();
        dateChooserEvents.setMinSelectableDate(dateevents);
        dateChooserEvents.setBackground(Color.white);
        
        Date dateInvoice = new Date();
        jDateChooser_invoice.setMaxSelectableDate(dateInvoice);
        
        //profile contents under profile button
        labelProfPending.setText(LawFirmHome.pendingCase);
        labelProfClientsAsc.setText(LawFirmHome.assignedNo);
        labelProfDisposed.setText(LawFirmHome.disposedCase);
        lblSessionId.setText(LawFirmHome.sessID1);
        
        //dashboard contents under schedule
        lblSchTitle.setText(LawFirmHome.schName);
        lblSchPro.setText(LawFirmHome.schPriority);
        lblDueDate.setText(LawFirmHome.schDate);
        
        //add application icon
        String imagepath = "/my/lafassoftware/ScalesForToolbar.png";
        InputStream imgstream = FrameSignIn.class.getResourceAsStream(imagepath);
        BufferedImage myImg = ImageIO.read(imgstream);
        this.setIconImage(myImg);
        
        //dashboard content under clients
        lblAssignedclients.setText(LawFirmHome.assignedNo);
        lblAllclients.setText(LawFirmHome.alClient);
        
    }
    
    private String ObtainWorkIDFromDB(String user) {
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery("SELECT emp_id from employee where lname ='" + user + "'");
            if (rs.next()) {
                workID = rs.getString("emp_id");
                labelWorkid.setText(workID);
                
                System.err.println("Employee ID" + rs.getString("emp_id"));
                
            }
        } catch (ClassNotFoundException | SQLException | HeadlessException e) {
        }
        return workID;
    }
    
    public void setdate() {
        ActionListener actiondate = (ActionEvent e) -> {
            java.util.Date mydate = new Date();
            timeLabel.setText(mydate.getHours() + ":" + mydate.getMinutes() + ":" + mydate.getSeconds());
        };
        new javax.swing.Timer(1000, actiondate).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupClientGender = new javax.swing.ButtonGroup();
        buttonGroupPaidUnpaid = new javax.swing.ButtonGroup();
        signInMainPanel = new javax.swing.JPanel();
        btnSignOutUser = new javax.swing.JButton();
        panelHolder = new javax.swing.JPanel();
        panelCard = new javax.swing.JPanel();
        panelDashboard = new javax.swing.JPanel();
        dateChooserUser = new com.toedter.calendar.JDayChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel62 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel63 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        btnViewAllEvents = new javax.swing.JButton();
        btnViewClientList = new javax.swing.JButton();
        btnViewAllSchedules = new javax.swing.JButton();
        btnManageReports = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        lblSchTitle = new javax.swing.JLabel();
        lblSchPro = new javax.swing.JLabel();
        lblDueDate = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        lblAssignedclients = new javax.swing.JLabel();
        lblAllclients = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        lblEvtCategory = new javax.swing.JLabel();
        lblEvtDate = new javax.swing.JLabel();
        lblEvtLocation = new javax.swing.JLabel();
        lblEvtName = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        lblDashLatestrep = new javax.swing.JLabel();
        lblDashNoReports = new javax.swing.JLabel();
        lblDashDoc = new javax.swing.JLabel();
        panelCreate = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txtEventName = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        comboEventCategory = new javax.swing.JComboBox<>();
        jLabel57 = new javax.swing.JLabel();
        dateChooserEvents = new com.toedter.calendar.JDateChooser();
        jLabel59 = new javax.swing.JLabel();
        spinFieldHours = new com.toedter.components.JSpinField();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtAreaDescription = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableEventList = new javax.swing.JTable();
        jLabel61 = new javax.swing.JLabel();
        btnCreateEvent = new javax.swing.JButton();
        btnDiscardEvent = new javax.swing.JButton();
        btnViewEvent = new javax.swing.JButton();
        comboEventLocation = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtreeDocuments = new javax.swing.JTree();
        openFile = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        panelClients = new javax.swing.JPanel();
        tabbedPaneClients = new javax.swing.JTabbedPane();
        panelCurrentClients = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableClient = new javax.swing.JTable();
        btnViewClientDetails = new javax.swing.JButton();
        btnSearchClient = new javax.swing.JButton();
        searchClient = new javax.swing.JTextField();
        btnShowAllClients = new javax.swing.JButton();
        panelAddClients = new javax.swing.JPanel();
        btnClientSave = new javax.swing.JButton();
        btnClientClear = new javax.swing.JButton();
        panelClientPsn = new javax.swing.JPanel();
        clientID = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        phoneNo = new javax.swing.JTextField();
        txtClientMail = new javax.swing.JTextField();
        clientLname = new javax.swing.JTextField();
        clentAddresField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        clientFname = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        rdPreferNotSay = new javax.swing.JRadioButton();
        rdOther = new javax.swing.JRadioButton();
        rdMale = new javax.swing.JRadioButton();
        rdFemale = new javax.swing.JRadioButton();
        panelAddClientsOtherDetails = new javax.swing.JPanel();
        jComboBoxCasetype = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        txtEmpId = new javax.swing.JTextField();
        txtCaseID = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dateChooserFiling = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        comboCaseStatus = new javax.swing.JComboBox<>();
        jLabel58 = new javax.swing.JLabel();
        txtField_invoiceId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        panelBilling = new javax.swing.JPanel();
        panelBillHome = new javax.swing.JPanel();
        btnReports = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnUnpaid = new javax.swing.JButton();
        btnAddClientBill = new javax.swing.JButton();
        btnReciepts = new javax.swing.JButton();
        btnNewInvoice = new javax.swing.JButton();
        btnBillTrends = new javax.swing.JButton();
        panelAddInvoice = new javax.swing.JPanel();
        btnAddInvoiceBack = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        panelInvoiceConfirmClient = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtInvoiceCaseType = new javax.swing.JTextField();
        txtInvoiceClientLname = new javax.swing.JTextField();
        txtInvoiceAssigneedTo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtInvoiceCaseStatus = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtInvoiceClientFname = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtInvoiceClientId = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        panelInvoiceDetails = new javax.swing.JPanel();
        txtDiscount = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtInvoiceID = new javax.swing.JTextField();
        txtRatingAmount = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtHoursWorked = new javax.swing.JTextField();
        txtFilingFee = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jDateChooser_invoice = new com.toedter.calendar.JDateChooser();
        panelTotalInvoice = new javax.swing.JPanel();
        txtInvoiceTotal = new javax.swing.JTextField();
        comboPaymentMethod = new javax.swing.JComboBox<>();
        radioNotYet = new javax.swing.JRadioButton();
        radioPaid = new javax.swing.JRadioButton();
        jButton27 = new javax.swing.JButton();
        btnDiscardInvoice = new javax.swing.JButton();
        btnViewReceipt = new javax.swing.JButton();
        btnSaveInvoice = new javax.swing.JButton();
        panelUnpaid = new javax.swing.JPanel();
        btnUnpaidBack = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableUnpaid = new javax.swing.JTable();
        txtSearchUnpaid = new javax.swing.JTextField();
        btnSearchUnpaid = new javax.swing.JButton();
        btnUnpaidRefresh = new javax.swing.JButton();
        panelReports = new javax.swing.JPanel();
        btnReportsBack = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tableReport = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        btnOpenReports = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        btnDiscardReport = new javax.swing.JButton();
        btnCreateDocument = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        reportConent = new javax.swing.JTextArea();
        titleReport = new javax.swing.JTextField();
        txtDocName = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        panelPrevious = new javax.swing.JPanel();
        btnPreviousBack = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablePaidInvoices = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        btnSearchPreviousInvoice = new javax.swing.JButton();
        btnRefreshPaid = new javax.swing.JButton();
        panelReciepts = new javax.swing.JPanel();
        txtRecieiptSearch = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnRecieptsBack = new javax.swing.JButton();
        panelTrends = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        btnTrendsBack = new javax.swing.JButton();
        panelSchedule = new javax.swing.JPanel();
        txtScheduleName = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        comboTask = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableSchedule = new javax.swing.JTable();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        comboPriority = new javax.swing.JComboBox<>();
        dateChooserSchedule = new com.toedter.calendar.JDateChooser();
        jLabel51 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtTaskDesc = new javax.swing.JTextArea();
        jLabel52 = new javax.swing.JLabel();
        btnCreateSchedule = new javax.swing.JButton();
        btnDiscardSchedule = new javax.swing.JButton();
        btnUpdateSchedule = new javax.swing.JButton();
        comboScheduleLocation = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        panelProfile = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        btnEditProfile = new javax.swing.JButton();
        usernameProfile = new javax.swing.JLabel();
        labelProfDisposed = new javax.swing.JLabel();
        labelProfClientsAsc = new javax.swing.JLabel();
        labelProfPending = new javax.swing.JLabel();
        labelProfEmail = new javax.swing.JLabel();
        labelProfEmpId = new javax.swing.JLabel();
        labelProfAcName = new javax.swing.JLabel();
        labelProfSpecialization = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        labelProfDate = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblSessionId = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        lblProfNatId = new javax.swing.JLabel();
        lblProfPhone = new javax.swing.JLabel();
        lblProfDept = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        btnDash = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnClients = new javax.swing.JButton();
        btnBill = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnSchedule = new javax.swing.JButton();
        btnHelp2 = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        btnProfile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        labelUser = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelWorkid = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LAFUS USER PAGE");
        setResizable(false);

        signInMainPanel.setBackground(new java.awt.Color(0, 204, 204));

        btnSignOutUser.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnSignOutUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Exit Sign_20px.png"))); // NOI18N
        btnSignOutUser.setText("Sign out");
        btnSignOutUser.setBorder(null);
        btnSignOutUser.setContentAreaFilled(false);
        btnSignOutUser.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/exitrollover.png"))); // NOI18N
        btnSignOutUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSignOutUserMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSignOutUserMouseExited(evt);
            }
        });
        btnSignOutUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignOutUserActionPerformed(evt);
            }
        });

        panelHolder.setBackground(new java.awt.Color(0, 24, 34));

        panelCard.setBackground(new java.awt.Color(0, 121, 129));
        panelCard.setLayout(new java.awt.CardLayout());

        panelDashboard.setBackground(new java.awt.Color(0, 121, 129));

        dateChooserUser.setBackground(new java.awt.Color(0, 121, 129));
        dateChooserUser.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Events");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Calender");

        jLabel14.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("CLIENTS");

        jLabel62.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("REPORTS");

        jLabel63.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("SCHEDULES");

        btnViewAllEvents.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        btnViewAllEvents.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAllEvents.setText("View all events");
        btnViewAllEvents.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnViewAllEvents.setContentAreaFilled(false);
        btnViewAllEvents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewAllEventsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewAllEventsMouseExited(evt);
            }
        });
        btnViewAllEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllEventsActionPerformed(evt);
            }
        });

        btnViewClientList.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        btnViewClientList.setForeground(new java.awt.Color(255, 255, 255));
        btnViewClientList.setText("View client list");
        btnViewClientList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnViewClientList.setContentAreaFilled(false);
        btnViewClientList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewClientListMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewClientListMouseExited(evt);
            }
        });
        btnViewClientList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewClientListActionPerformed(evt);
            }
        });

        btnViewAllSchedules.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        btnViewAllSchedules.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAllSchedules.setText("View all schedules");
        btnViewAllSchedules.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnViewAllSchedules.setContentAreaFilled(false);
        btnViewAllSchedules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewAllSchedulesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewAllSchedulesMouseExited(evt);
            }
        });
        btnViewAllSchedules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllSchedulesActionPerformed(evt);
            }
        });

        btnManageReports.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        btnManageReports.setForeground(new java.awt.Color(255, 255, 255));
        btnManageReports.setText("Manage reports");
        btnManageReports.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnManageReports.setContentAreaFilled(false);
        btnManageReports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnManageReportsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnManageReportsMouseExited(evt);
            }
        });
        btnManageReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageReportsActionPerformed(evt);
            }
        });

        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Your last schedule");

        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Title :");

        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Priority :");

        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Due date :");

        lblSchTitle.setForeground(new java.awt.Color(255, 255, 255));

        lblSchPro.setForeground(new java.awt.Color(255, 255, 255));

        lblDueDate.setForeground(new java.awt.Color(255, 255, 255));

        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Number of clients assigned to you :");

        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("All clients :");

        lblAssignedclients.setForeground(new java.awt.Color(255, 255, 255));

        lblAllclients.setForeground(new java.awt.Color(255, 255, 255));

        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Event Location :");

        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Event name :");

        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Event category :");

        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Event date :");

        lblEvtCategory.setForeground(new java.awt.Color(255, 255, 255));

        lblEvtDate.setForeground(new java.awt.Color(255, 255, 255));

        lblEvtLocation.setForeground(new java.awt.Color(255, 255, 255));

        lblEvtName.setForeground(new java.awt.Color(255, 255, 255));

        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Total number of reports :");

        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Latest Report :");

        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Document name :");

        lblDashLatestrep.setForeground(new java.awt.Color(255, 255, 255));

        lblDashNoReports.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelDashboardLayout = new javax.swing.GroupLayout(panelDashboard);
        panelDashboard.setLayout(panelDashboardLayout);
        panelDashboardLayout.setHorizontalGroup(
            panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDashboardLayout.createSequentialGroup()
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addComponent(jLabel80)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblEvtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addComponent(jLabel79)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEvtCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel74)
                                    .addComponent(jLabel73))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblAssignedclients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblAllclients, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addComponent(jLabel77)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEvtLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDashboardLayout.createSequentialGroup()
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnViewClientList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnViewAllSchedules, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnViewAllEvents, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDashboardLayout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelDashboardLayout.createSequentialGroup()
                                        .addComponent(jLabel68)
                                        .addGap(0, 202, Short.MAX_VALUE))
                                    .addGroup(panelDashboardLayout.createSequentialGroup()
                                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel69)
                                            .addComponent(jLabel70)
                                            .addComponent(jLabel71))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSchTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblSchPro, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                                            .addComponent(lblDueDate, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDashboardLayout.createSequentialGroup()
                                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel63, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDashboardLayout.createSequentialGroup()
                                .addComponent(jLabel78)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEvtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)))
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel62)
                            .addComponent(dateChooserUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator12))
                        .addComponent(btnManageReports, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDashboardLayout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDashNoReports, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel76)
                    .addGroup(panelDashboardLayout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDashDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblDashLatestrep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        panelDashboardLayout.setVerticalGroup(
            panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDashboardLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelDashboardLayout.createSequentialGroup()
                        .addComponent(dateChooserUser, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(panelDashboardLayout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel78)
                            .addComponent(lblEvtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel77)
                            .addComponent(lblEvtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel80)
                                    .addComponent(lblEvtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel79))
                            .addGroup(panelDashboardLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblEvtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewAllEvents, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(lblAssignedclients, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAllclients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnViewClientList, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)))
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel75)
                        .addComponent(lblDashNoReports, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel69)
                        .addComponent(lblSchTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel76))
                .addGap(8, 8, 8)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSchPro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblDashLatestrep, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDashDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelDashboardLayout.createSequentialGroup()
                        .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel71)
                                .addComponent(lblDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel81))
                        .addGap(0, 3, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewAllSchedules, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnManageReports, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        panelCard.add(panelDashboard, "card2");

        jTabbedPane1.setBackground(new java.awt.Color(0, 204, 204));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel54.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel54.setText("Create new event");

        jLabel55.setText("Event name");

        jLabel56.setText("Event category");

        comboEventCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select category", "firm meeting", "educational event", "seminar socials", "co-sponsored soirees", "community initiative", "client appreciation", "other" }));

        jLabel57.setText("Date of event");

        jLabel59.setText("Event duration (Hours) :");

        jLabel60.setText("Short description of event");

        txtAreaDescription.setColumns(20);
        txtAreaDescription.setRows(5);
        jScrollPane7.setViewportView(txtAreaDescription);

        tableEventList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableEventList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEventListMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tableEventList);

        jLabel61.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel61.setText("Events list");

        btnCreateEvent.setText("Create");
        btnCreateEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateEventActionPerformed(evt);
            }
        });

        btnDiscardEvent.setText("Discard");
        btnDiscardEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscardEventActionPerformed(evt);
            }
        });

        btnViewEvent.setText("View event");
        btnViewEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewEventActionPerformed(evt);
            }
        });

        comboEventLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select location", "inside firm premises", "Baringo County", "Bomet County", "Bungoma County", "Busia County", "Elgeyo Marakwet County", "Embu County", "Garissa County", "Homa Bay County", "Isiolo County", "Kajiado County", "Kakamega County", "Kericho County", "Kiambu County", "Kilifi County", "Kirinyaga County", "Kisii County", "Kisumu County", "Kitui County", "Kwale County", "Laikipia County", "Lamu County", "Machakos County", "Makueni County", "Mandera County", "Meru County", "Migori County", "Marsabit County", "Mombasa County", "Muranga County", "Nairobi County", "Nakuru County", "Nandi County", "Narok County", "Nyamira County", "Nyandarua County", "Nyeri County", "Samburu County", "Siaya County", "Taita Taveta County", "Tana River County", "Tharaka Nithi County", "Trans Nzoia County", "Turkana County", "Uasin Gishu County", "Vihiga County", "Wajir County", "West Pokot County", "Outside Kenya", "Outside Africa" }));

        jButton1.setText("Refresh table");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel60)
                        .addGap(125, 125, 125))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboEventLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel54)
                                        .addComponent(jLabel55)
                                        .addComponent(txtEventName)
                                        .addComponent(jLabel56)
                                        .addComponent(comboEventCategory, 0, 251, Short.MAX_VALUE)
                                        .addComponent(jLabel57)
                                        .addComponent(dateChooserEvents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(btnCreateEvent)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnDiscardEvent)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel59)
                                        .addGap(18, 18, 18)
                                        .addComponent(spinFieldHours, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnViewEvent))
                    .addComponent(jLabel61))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEventName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboEventCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooserEvents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboEventLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinFieldHours, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreateEvent)
                    .addComponent(btnDiscardEvent)
                    .addComponent(btnViewEvent)
                    .addComponent(jButton1))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("New Event", jPanel3);

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jtreeDocuments.setModel(new FileSystemModel(new File("C:\\Users\\Timothy Opiyo\\Documents\\NetBeansProjects")));
        jtreeDocuments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtreeDocumentsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtreeDocuments);

        openFile.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        openFile.setText("Open File");
        openFile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        openFile.setContentAreaFilled(false);
        openFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openFileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openFileMouseExited(evt);
            }
        });
        openFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(149, 149, 149)
                .addComponent(openFile, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(307, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(openFile, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Browse documents", jPanel5);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("New document", jPanel4);

        javax.swing.GroupLayout panelCreateLayout = new javax.swing.GroupLayout(panelCreate);
        panelCreate.setLayout(panelCreateLayout);
        panelCreateLayout.setHorizontalGroup(
            panelCreateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        panelCreateLayout.setVerticalGroup(
            panelCreateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        panelCard.add(panelCreate, "card2");

        panelClients.setBackground(new java.awt.Color(0, 153, 153));

        panelCurrentClients.setBackground(new java.awt.Color(0, 153, 153));

        tableClient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClientMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableClient);

        btnViewClientDetails.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnViewClientDetails.setText("View");
        btnViewClientDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnViewClientDetails.setContentAreaFilled(false);
        btnViewClientDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewClientDetailsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewClientDetailsMouseExited(evt);
            }
        });
        btnViewClientDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewClientDetailsActionPerformed(evt);
            }
        });

        btnSearchClient.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnSearchClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/searchClient.png"))); // NOI18N
        btnSearchClient.setToolTipText("click to search");
        btnSearchClient.setBorder(null);
        btnSearchClient.setContentAreaFilled(false);
        btnSearchClient.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/rolloverSearchClient.png"))); // NOI18N
        btnSearchClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchClientMouseExited(evt);
            }
        });
        btnSearchClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchClientActionPerformed(evt);
            }
        });

        searchClient.setBackground(new java.awt.Color(51, 51, 51));
        searchClient.setForeground(new java.awt.Color(255, 255, 255));
        searchClient.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        searchClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchClientKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchClientKeyTyped(evt);
            }
        });

        btnShowAllClients.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnShowAllClients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/showall.png"))); // NOI18N
        btnShowAllClients.setText("Show all");
        btnShowAllClients.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnShowAllClients.setContentAreaFilled(false);
        btnShowAllClients.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/showallRollover.png"))); // NOI18N
        btnShowAllClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnShowAllClientsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnShowAllClientsMouseExited(evt);
            }
        });
        btnShowAllClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllClientsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCurrentClientsLayout = new javax.swing.GroupLayout(panelCurrentClients);
        panelCurrentClients.setLayout(panelCurrentClientsLayout);
        panelCurrentClientsLayout.setHorizontalGroup(
            panelCurrentClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCurrentClientsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCurrentClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnViewClientDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCurrentClientsLayout.createSequentialGroup()
                        .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchClient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnShowAllClients, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(109, 109, 109))
        );
        panelCurrentClientsLayout.setVerticalGroup(
            panelCurrentClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCurrentClientsLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(panelCurrentClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowAllClients, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnViewClientDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        tabbedPaneClients.addTab("Current clients", panelCurrentClients);

        panelAddClients.setBackground(new java.awt.Color(0, 153, 153));

        btnClientSave.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnClientSave.setText("Save");
        btnClientSave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnClientSave.setContentAreaFilled(false);
        btnClientSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClientSaveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClientSaveMouseExited(evt);
            }
        });
        btnClientSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientSaveActionPerformed(evt);
            }
        });

        btnClientClear.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnClientClear.setText("Clear");
        btnClientClear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnClientClear.setContentAreaFilled(false);
        btnClientClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClientClearMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClientClearMouseExited(evt);
            }
        });
        btnClientClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientClearActionPerformed(evt);
            }
        });

        panelClientPsn.setBackground(new java.awt.Color(0, 153, 153));
        panelClientPsn.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "CLIENT PERSONAL DETAILS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        panelClientPsn.setForeground(new java.awt.Color(255, 255, 255));

        clientID.setBackground(new java.awt.Color(51, 51, 51));
        clientID.setForeground(new java.awt.Color(255, 255, 255));

        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Email:");

        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Address:");

        phoneNo.setBackground(new java.awt.Color(51, 51, 51));
        phoneNo.setForeground(new java.awt.Color(255, 255, 255));

        txtClientMail.setBackground(new java.awt.Color(51, 51, 51));
        txtClientMail.setForeground(new java.awt.Color(255, 255, 255));

        clientLname.setBackground(new java.awt.Color(51, 51, 51));
        clientLname.setForeground(new java.awt.Color(255, 255, 255));

        clentAddresField.setBackground(new java.awt.Color(51, 51, 51));
        clentAddresField.setForeground(new java.awt.Color(255, 255, 255));
        clentAddresField.setToolTipText("postal code+address");
        clentAddresField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clentAddresFieldActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("First name :");

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Phone :");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Last name :");

        clientFname.setBackground(new java.awt.Color(51, 51, 51));
        clientFname.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("National ID:");

        jPanel8.setBackground(new java.awt.Color(0, 153, 153));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select gender", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        btnGroupClientGender.add(rdPreferNotSay);
        rdPreferNotSay.setForeground(new java.awt.Color(255, 255, 255));
        rdPreferNotSay.setText("Prefer not to say");

        btnGroupClientGender.add(rdOther);
        rdOther.setForeground(new java.awt.Color(255, 255, 255));
        rdOther.setText("Other");

        btnGroupClientGender.add(rdMale);
        rdMale.setForeground(new java.awt.Color(255, 255, 255));
        rdMale.setText("Male");

        btnGroupClientGender.add(rdFemale);
        rdFemale.setForeground(new java.awt.Color(255, 255, 255));
        rdFemale.setText("Female");
        rdFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdFemaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdFemale)
                    .addComponent(rdOther))
                .addGap(59, 59, 59)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdMale)
                    .addComponent(rdPreferNotSay))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdFemale)
                    .addComponent(rdMale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdOther)
                    .addComponent(rdPreferNotSay))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelClientPsnLayout = new javax.swing.GroupLayout(panelClientPsn);
        panelClientPsn.setLayout(panelClientPsnLayout);
        panelClientPsnLayout.setHorizontalGroup(
            panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientPsnLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClientPsnLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clientLname, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelClientPsnLayout.createSequentialGroup()
                        .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelClientPsnLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clientID, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelClientPsnLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelClientPsnLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtClientMail, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelClientPsnLayout.createSequentialGroup()
                                        .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel25)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(phoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelClientPsnLayout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clentAddresField, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(20, Short.MAX_VALUE))))
            .addGroup(panelClientPsnLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clientFname, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelClientPsnLayout.setVerticalGroup(
            panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientPsnLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientFname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clentAddresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(panelClientPsnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtClientMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(34, 34, 34)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAddClientsOtherDetails.setBackground(new java.awt.Color(0, 153, 153));
        panelAddClientsOtherDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "OTHER DETAILS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jComboBoxCasetype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select case type", "Civil", "Criminal" }));

        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Date of case filing:");

        txtEmpId.setEditable(false);
        txtEmpId.setBackground(new java.awt.Color(51, 51, 51));
        txtEmpId.setForeground(new java.awt.Color(255, 255, 255));
        txtEmpId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpIdActionPerformed(evt);
            }
        });

        txtCaseID.setBackground(new java.awt.Color(51, 51, 51));
        txtCaseID.setForeground(new java.awt.Color(255, 255, 255));

        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Case type :");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Case status :");

        dateChooserFiling.setBackground(new java.awt.Color(0, 153, 153));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Case No:");

        comboCaseStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select case status", "Pending", "Disposed" }));
        comboCaseStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCaseStatusActionPerformed(evt);
            }
        });

        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Invoice Id");

        txtField_invoiceId.setBackground(new java.awt.Color(51, 51, 51));
        txtField_invoiceId.setForeground(new java.awt.Color(255, 255, 255));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Assigned to:");

        javax.swing.GroupLayout panelAddClientsOtherDetailsLayout = new javax.swing.GroupLayout(panelAddClientsOtherDetails);
        panelAddClientsOtherDetails.setLayout(panelAddClientsOtherDetailsLayout);
        panelAddClientsOtherDetailsLayout.setHorizontalGroup(
            panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddClientsOtherDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddClientsOtherDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtField_invoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddClientsOtherDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddClientsOtherDetailsLayout.createSequentialGroup()
                        .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addComponent(jLabel24))
                        .addGap(18, 18, 18)
                        .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCaseID)
                            .addComponent(comboCaseStatus, 0, 161, Short.MAX_VALUE)
                            .addComponent(jComboBoxCasetype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel46)
                        .addComponent(dateChooserFiling, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(78, 78, 78))
        );
        panelAddClientsOtherDetailsLayout.setVerticalGroup(
            panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddClientsOtherDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(24, 24, 24)
                .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboCaseStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(40, 40, 40)
                .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCasetype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(41, 41, 41)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChooserFiling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAddClientsOtherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtField_invoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout panelAddClientsLayout = new javax.swing.GroupLayout(panelAddClients);
        panelAddClients.setLayout(panelAddClientsLayout);
        panelAddClientsLayout.setHorizontalGroup(
            panelAddClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddClientsLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(panelClientPsn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(panelAddClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelAddClientsLayout.createSequentialGroup()
                        .addComponent(btnClientClear, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClientSave, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelAddClientsOtherDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelAddClientsLayout.setVerticalGroup(
            panelAddClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddClientsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAddClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAddClientsLayout.createSequentialGroup()
                        .addComponent(panelAddClientsOtherDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(panelAddClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClientClear, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClientSave, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelClientPsn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        tabbedPaneClients.addTab("Add clients", panelAddClients);

        javax.swing.GroupLayout panelClientsLayout = new javax.swing.GroupLayout(panelClients);
        panelClients.setLayout(panelClientsLayout);
        panelClientsLayout.setHorizontalGroup(
            panelClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneClients, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
        );
        panelClientsLayout.setVerticalGroup(
            panelClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPaneClients)
        );

        panelCard.add(panelClients, "card2");

        panelBilling.setLayout(new java.awt.CardLayout());

        panelBillHome.setBackground(new java.awt.Color(0, 102, 102));

        btnReports.setBackground(new java.awt.Color(153, 153, 153));
        btnReports.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/reports.png"))); // NOI18N
        btnReports.setText("REPORTS  ");
        btnReports.setBorder(null);
        btnReports.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReports.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsActionPerformed(evt);
            }
        });

        btnPrevious.setBackground(new java.awt.Color(153, 153, 153));
        btnPrevious.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/previous reports.png"))); // NOI18N
        btnPrevious.setText("VIEW PREVIOUS INVOICES  ");
        btnPrevious.setBorder(null);
        btnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrevious.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnUnpaid.setBackground(new java.awt.Color(153, 153, 153));
        btnUnpaid.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnUnpaid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/unpaid invoices.png"))); // NOI18N
        btnUnpaid.setBorder(null);
        btnUnpaid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUnpaid.setLabel("UNPAID INVOICES");
        btnUnpaid.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnUnpaid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnpaidActionPerformed(evt);
            }
        });

        btnAddClientBill.setBackground(new java.awt.Color(153, 153, 153));
        btnAddClientBill.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnAddClientBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/addnew client.png"))); // NOI18N
        btnAddClientBill.setText("ADD NEW CLIENT  ");
        btnAddClientBill.setBorder(null);
        btnAddClientBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddClientBill.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnAddClientBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddClientBillActionPerformed(evt);
            }
        });

        btnReciepts.setBackground(new java.awt.Color(153, 153, 153));
        btnReciepts.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnReciepts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Receipt_100px.png"))); // NOI18N
        btnReciepts.setText("VIEW RECIEPTS  ");
        btnReciepts.setBorder(null);
        btnReciepts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReciepts.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnReciepts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecieptsActionPerformed(evt);
            }
        });

        btnNewInvoice.setBackground(new java.awt.Color(153, 153, 153));
        btnNewInvoice.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnNewInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/newInvoice_100px.png"))); // NOI18N
        btnNewInvoice.setText("CREATE NEW INVOICE  ");
        btnNewInvoice.setBorder(null);
        btnNewInvoice.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewInvoice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnNewInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewInvoiceActionPerformed(evt);
            }
        });

        btnBillTrends.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnBillTrends.setForeground(new java.awt.Color(255, 255, 255));
        btnBillTrends.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Line Chart_20px.png"))); // NOI18N
        btnBillTrends.setText("TRENDS");
        btnBillTrends.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 255, 255)));
        btnBillTrends.setContentAreaFilled(false);
        btnBillTrends.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBillTrendsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBillTrendsMouseExited(evt);
            }
        });
        btnBillTrends.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillTrendsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBillHomeLayout = new javax.swing.GroupLayout(panelBillHome);
        panelBillHome.setLayout(panelBillHomeLayout);
        panelBillHomeLayout.setHorizontalGroup(
            panelBillHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBillHomeLayout.createSequentialGroup()
                .addContainerGap(131, Short.MAX_VALUE)
                .addGroup(panelBillHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBillTrends, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBillHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBillHomeLayout.createSequentialGroup()
                            .addComponent(btnNewInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnReports, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBillHomeLayout.createSequentialGroup()
                            .addComponent(btnAddClientBill, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnReciepts, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15))
        );
        panelBillHomeLayout.setVerticalGroup(
            panelBillHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBillHomeLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelBillHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReports, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelBillHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddClientBill, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReciepts, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(btnBillTrends, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        panelBilling.add(panelBillHome, "card2");

        panelAddInvoice.setBackground(new java.awt.Color(0, 102, 102));
        panelAddInvoice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "ADD NEW INVOICE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        btnAddInvoiceBack.setText("Back");
        btnAddInvoiceBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddInvoiceBackActionPerformed(evt);
            }
        });

        jLabel28.setForeground(new java.awt.Color(255, 255, 255));

        panelInvoiceConfirmClient.setBackground(new java.awt.Color(0, 102, 102));
        panelInvoiceConfirmClient.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Confirm Client Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Enter client ID :");

        txtInvoiceCaseType.setEditable(false);

        txtInvoiceClientLname.setEditable(false);

        txtInvoiceAssigneedTo.setEditable(false);

        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Client First name :");

        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Case Type :");

        txtInvoiceCaseStatus.setEditable(false);

        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Assigned to :");

        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Case Status :");

        txtInvoiceClientFname.setEditable(false);

        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Client Last name :");

        txtInvoiceClientId.setToolTipText("enter client ID and search");
        txtInvoiceClientId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInvoiceClientIdKeyTyped(evt);
            }
        });

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/searchClient.png"))); // NOI18N
        jButton23.setContentAreaFilled(false);
        jButton23.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/rolloverSearchClient.png"))); // NOI18N
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInvoiceConfirmClientLayout = new javax.swing.GroupLayout(panelInvoiceConfirmClient);
        panelInvoiceConfirmClient.setLayout(panelInvoiceConfirmClientLayout);
        panelInvoiceConfirmClientLayout.setHorizontalGroup(
            panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInvoiceConfirmClientLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelInvoiceConfirmClientLayout.createSequentialGroup()
                        .addComponent(txtInvoiceCaseStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelInvoiceConfirmClientLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panelInvoiceConfirmClientLayout.createSequentialGroup()
                                .addComponent(txtInvoiceClientId)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addComponent(txtInvoiceClientFname, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtInvoiceClientLname, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtInvoiceAssigneedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtInvoiceCaseType, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelInvoiceConfirmClientLayout.setVerticalGroup(
            panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInvoiceConfirmClientLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtInvoiceClientId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceClientFname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceClientLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceAssigneedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceCaseType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceConfirmClientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceCaseStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(21, 21, 21))
        );

        panelInvoiceDetails.setBackground(new java.awt.Color(0, 102, 102));
        panelInvoiceDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Please add the remaining Invoice Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDiscountKeyTyped(evt);
            }
        });

        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Invoice ID :");

        txtInvoiceID.setEditable(false);

        txtRatingAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRatingAmountKeyTyped(evt);
            }
        });

        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Rating Amount/Hour :");

        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Invoice Date :");

        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Discount :");

        txtHoursWorked.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHoursWorkedKeyTyped(evt);
            }
        });

        txtFilingFee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFilingFeeKeyTyped(evt);
            }
        });

        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Filing fee :");

        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Hours worked on matter :");

        jDateChooser_invoice.setBackground(new java.awt.Color(0, 102, 102));
        jDateChooser_invoice.setMaxSelectableDate(new java.util.Date(253370757682000L));

        javax.swing.GroupLayout panelInvoiceDetailsLayout = new javax.swing.GroupLayout(panelInvoiceDetails);
        panelInvoiceDetails.setLayout(panelInvoiceDetailsLayout);
        panelInvoiceDetailsLayout.setHorizontalGroup(
            panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInvoiceDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelInvoiceDetailsLayout.createSequentialGroup()
                        .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel36)
                            .addComponent(jLabel35)
                            .addComponent(jLabel32)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtHoursWorked, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                .addComponent(txtRatingAmount)
                                .addComponent(txtDiscount)
                                .addComponent(txtFilingFee))
                            .addComponent(txtInvoiceID, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelInvoiceDetailsLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser_invoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        panelInvoiceDetailsLayout.setVerticalGroup(
            panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInvoiceDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceID)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser_invoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoursWorked, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRatingAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(18, 18, 18)
                .addGroup(panelInvoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilingFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addGap(22, 22, 22))
        );

        panelTotalInvoice.setBackground(new java.awt.Color(0, 102, 102));
        panelTotalInvoice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Totals", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        txtInvoiceTotal.setEditable(false);
        txtInvoiceTotal.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N

        comboPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select payment method", "Cash", "Credit card", "Wire", "MPESA" }));

        buttonGroupPaidUnpaid.add(radioNotYet);
        radioNotYet.setForeground(new java.awt.Color(255, 255, 255));
        radioNotYet.setText("Not yet");

        buttonGroupPaidUnpaid.add(radioPaid);
        radioPaid.setForeground(new java.awt.Color(255, 255, 255));
        radioPaid.setText("Paid");

        jButton27.setText("Calculate Total");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        btnDiscardInvoice.setText("Discard");
        btnDiscardInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscardInvoiceActionPerformed(evt);
            }
        });

        btnViewReceipt.setText("View Receipt");

        btnSaveInvoice.setText("Save");
        btnSaveInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveInvoiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTotalInvoiceLayout = new javax.swing.GroupLayout(panelTotalInvoice);
        panelTotalInvoice.setLayout(panelTotalInvoiceLayout);
        panelTotalInvoiceLayout.setHorizontalGroup(
            panelTotalInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalInvoiceLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTotalInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTotalInvoiceLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(radioPaid)
                        .addGap(34, 34, 34)
                        .addComponent(radioNotYet))
                    .addComponent(comboPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addGroup(panelTotalInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSaveInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewReceipt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDiscardInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelTotalInvoiceLayout.setVerticalGroup(
            panelTotalInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalInvoiceLayout.createSequentialGroup()
                .addComponent(comboPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTotalInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTotalInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioPaid)
                    .addComponent(radioNotYet))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTotalInvoiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSaveInvoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewReceipt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDiscardInvoice)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelAddInvoiceLayout = new javax.swing.GroupLayout(panelAddInvoice);
        panelAddInvoice.setLayout(panelAddInvoiceLayout);
        panelAddInvoiceLayout.setHorizontalGroup(
            panelAddInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelAddInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                        .addComponent(panelInvoiceConfirmClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelInvoiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                        .addComponent(panelTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                        .addComponent(btnAddInvoiceBack, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))))
            .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAddInvoiceLayout.setVerticalGroup(
            panelAddInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAddInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelInvoiceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelInvoiceConfirmClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addGroup(panelAddInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddInvoiceBack)
                        .addGap(20, 20, 20))
                    .addGroup(panelAddInvoiceLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(panelTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        panelBilling.add(panelAddInvoice, "card2");

        panelUnpaid.setBackground(new java.awt.Color(0, 102, 102));
        panelUnpaid.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "UNPAID INVOICES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        btnUnpaidBack.setText("Back");
        btnUnpaidBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnpaidBackActionPerformed(evt);
            }
        });

        tableUnpaid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tableUnpaid);

        txtSearchUnpaid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchUnpaidActionPerformed(evt);
            }
        });

        btnSearchUnpaid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/searchClient.png"))); // NOI18N
        btnSearchUnpaid.setContentAreaFilled(false);
        btnSearchUnpaid.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Search_20px.png"))); // NOI18N

        btnUnpaidRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/newFresh.png"))); // NOI18N
        btnUnpaidRefresh.setText("Refresh table");
        btnUnpaidRefresh.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Refresh_20px.png"))); // NOI18N

        javax.swing.GroupLayout panelUnpaidLayout = new javax.swing.GroupLayout(panelUnpaid);
        panelUnpaid.setLayout(panelUnpaidLayout);
        panelUnpaidLayout.setHorizontalGroup(
            panelUnpaidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUnpaidLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUnpaidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUnpaidLayout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUnpaidLayout.createSequentialGroup()
                        .addGap(0, 381, Short.MAX_VALUE)
                        .addComponent(txtSearchUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnUnpaidRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUnpaidLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUnpaidBack)
                .addContainerGap())
        );
        panelUnpaidLayout.setVerticalGroup(
            panelUnpaidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUnpaidLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUnpaidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUnpaidRefresh)
                    .addComponent(txtSearchUnpaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUnpaidBack)
                .addGap(15, 15, 15))
        );

        panelBilling.add(panelUnpaid, "card2");

        panelReports.setBackground(new java.awt.Color(0, 102, 102));
        panelReports.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "My reports", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        btnReportsBack.setText("Back");
        btnReportsBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsBackActionPerformed(evt);
            }
        });

        tableReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(tableReport);

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("List of Reports");

        btnOpenReports.setText("Refresh");
        btnOpenReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenReportsActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(0, 102, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Create new report", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Title of report");

        btnDiscardReport.setText("Discard");
        btnDiscardReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscardReportActionPerformed(evt);
            }
        });

        btnCreateDocument.setText("Create");
        btnCreateDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateDocumentActionPerformed(evt);
            }
        });

        reportConent.setColumns(20);
        reportConent.setRows(5);
        jScrollPane10.setViewportView(reportConent);

        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Document name :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel72)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDocName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCreateDocument, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDiscardReport, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(titleReport))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(titleReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDiscardReport)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCreateDocument)
                        .addComponent(txtDocName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel72)))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelReportsLayout = new javax.swing.GroupLayout(panelReports);
        panelReports.setLayout(panelReportsLayout);
        panelReportsLayout.setHorizontalGroup(
            panelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportsLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel15)
                        .addGap(0, 288, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReportsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReportsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                        .addGroup(panelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOpenReports, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(btnReportsBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelReportsLayout.setVerticalGroup(
            panelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenReports)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReportsBack)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelReportsLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelBilling.add(panelReports, "card2");

        panelPrevious.setBackground(new java.awt.Color(0, 102, 102));
        panelPrevious.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "PREVIOUS AND PAID INVOICES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        btnPreviousBack.setText("Back");
        btnPreviousBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousBackActionPerformed(evt);
            }
        });

        tablePaidInvoices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablePaidInvoices);

        btnSearchPreviousInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/searchClient.png"))); // NOI18N
        btnSearchPreviousInvoice.setContentAreaFilled(false);
        btnSearchPreviousInvoice.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Search_20px.png"))); // NOI18N

        btnRefreshPaid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/newFresh.png"))); // NOI18N
        btnRefreshPaid.setText("Refresh table");
        btnRefreshPaid.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Refresh_20px.png"))); // NOI18N

        javax.swing.GroupLayout panelPreviousLayout = new javax.swing.GroupLayout(panelPrevious);
        panelPrevious.setLayout(panelPreviousLayout);
        panelPreviousLayout.setHorizontalGroup(
            panelPreviousLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreviousLayout.createSequentialGroup()
                .addGap(300, 300, 300)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchPreviousInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefreshPaid)
                .addContainerGap(164, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPreviousLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPreviousLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPreviousLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPreviousBack)))
                .addContainerGap())
        );
        panelPreviousLayout.setVerticalGroup(
            panelPreviousLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPreviousLayout.createSequentialGroup()
                .addGroup(panelPreviousLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefreshPaid)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchPreviousInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPreviousBack)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBilling.add(panelPrevious, "card2");

        panelReciepts.setBackground(new java.awt.Color(0, 102, 102));
        panelReciepts.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "RECIEPTS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        panelReciepts.setForeground(new java.awt.Color(255, 255, 255));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/searchClient.png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Search_20px.png"))); // NOI18N

        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("Case status :");

        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("Case number :");

        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("Phone :");

        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("National :");

        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("Last name :");

        jLabel87.setForeground(new java.awt.Color(255, 255, 255));
        jLabel87.setText("First name :");

        jLabel88.setForeground(new java.awt.Color(255, 255, 255));
        jLabel88.setText("Invoice Id :");

        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setText("Assigned to :");

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Generate");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 204)));
        jButton4.setContentAreaFilled(false);

        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Discard");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 204)));
        jButton5.setContentAreaFilled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        btnRecieptsBack.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnRecieptsBack.setForeground(new java.awt.Color(255, 255, 255));
        btnRecieptsBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Backhome_20px.png"))); // NOI18N
        btnRecieptsBack.setText("Back");
        btnRecieptsBack.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnRecieptsBack.setContentAreaFilled(false);
        btnRecieptsBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRecieptsBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRecieptsBackMouseExited(evt);
            }
        });
        btnRecieptsBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecieptsBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRecieptsLayout = new javax.swing.GroupLayout(panelReciepts);
        panelReciepts.setLayout(panelRecieptsLayout);
        panelRecieptsLayout.setHorizontalGroup(
            panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRecieptsLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel88)
                    .addComponent(jLabel87)
                    .addComponent(jLabel86)
                    .addComponent(jLabel85)
                    .addComponent(jLabel84)
                    .addComponent(jLabel83)
                    .addComponent(jLabel82)
                    .addComponent(jLabel89))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRecieptsLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(txtRecieiptSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField2)
                        .addComponent(jTextField3)
                        .addComponent(jTextField4)
                        .addComponent(jTextField5)
                        .addComponent(jTextField6)
                        .addComponent(jTextField7)
                        .addComponent(jTextField8)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRecieptsLayout.createSequentialGroup()
                        .addComponent(btnRecieptsBack, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRecieptsLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))))
        );
        panelRecieptsLayout.setVerticalGroup(
            panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRecieptsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRecieptsLayout.createSequentialGroup()
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRecieiptSearch)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel88))
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel87))
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel86))
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel85))
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel84))
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel83))))
                .addGap(18, 18, 18)
                .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82))
                .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRecieptsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelRecieptsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel89))
                        .addContainerGap(41, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRecieptsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRecieptsBack, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );

        panelBilling.add(panelReciepts, "card2");

        panelTrends.setBackground(new java.awt.Color(0, 102, 102));

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("TRENDS");

        btnTrendsBack.setText("Back");
        btnTrendsBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrendsBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTrendsLayout = new javax.swing.GroupLayout(panelTrends);
        panelTrends.setLayout(panelTrendsLayout);
        panelTrendsLayout.setHorizontalGroup(
            panelTrendsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrendsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(808, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTrendsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTrendsBack)
                .addContainerGap())
        );
        panelTrendsLayout.setVerticalGroup(
            panelTrendsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrendsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 446, Short.MAX_VALUE)
                .addComponent(btnTrendsBack)
                .addContainerGap())
        );

        panelBilling.add(panelTrends, "card2");

        panelCard.add(panelBilling, "card2");

        jLabel21.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel21.setText("Create or modify your schedule");

        jLabel47.setText("Name of schedule");

        jLabel48.setText("Task type");

        comboTask.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select task", "represent client on criminal litigation", "represent client on civil litigation", "draw up legal documents", "advise on legal transactions", "counseling about legal options", "other" }));

        tableSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableSchedule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableScheduleMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableSchedule);

        jLabel49.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel49.setText("My schedules");

        jLabel50.setText("Priority of task");

        comboPriority.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "choose priority", "1 (mild importance)", "2 (important)", "3 (very important)" }));

        jLabel51.setText("Date to be done");

        txtTaskDesc.setColumns(20);
        txtTaskDesc.setRows(5);
        jScrollPane6.setViewportView(txtTaskDesc);

        jLabel52.setText("Describe task (not more than 50 caharacters)");

        btnCreateSchedule.setText("Create");
        btnCreateSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateScheduleActionPerformed(evt);
            }
        });

        btnDiscardSchedule.setText("Discard");
        btnDiscardSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiscardScheduleActionPerformed(evt);
            }
        });

        btnUpdateSchedule.setText("View");
        btnUpdateSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateScheduleActionPerformed(evt);
            }
        });

        comboScheduleLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "select location", "inside firm premises", "Baringo County", "Bomet County", "Bungoma County", "Busia County", "Elgeyo Marakwet County", "Embu County", "Garissa County", "Homa Bay County", "Isiolo County", "Kajiado County", "Kakamega County", "Kericho County", "Kiambu County", "Kilifi County", "Kirinyaga County", "Kisii County", "Kisumu County", "Kitui County", "Kwale County", "Laikipia County", "Lamu County", "Machakos County", "Makueni County", "Mandera County", "Meru County", "Migori County", "Marsabit County", "Mombasa County", "Muranga County", "Nairobi County", "Nakuru County", "Nandi County", "Narok County", "Nyamira County", "Nyandarua County", "Nyeri County", "Samburu County", "Siaya County", "Taita Taveta County", "Tana River County", "Tharaka Nithi County", "Trans Nzoia County", "Turkana County", "Uasin Gishu County", "Vihiga County", "Wajir County", "West Pokot County", "Outside Kenya", "Outside Africa" }));

        jButton2.setText("Refresh schedule list");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelScheduleLayout = new javax.swing.GroupLayout(panelSchedule);
        panelSchedule.setLayout(panelScheduleLayout);
        panelScheduleLayout.setHorizontalGroup(
            panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelScheduleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdateSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelScheduleLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel48)
                    .addComponent(jLabel47)
                    .addComponent(jLabel21)
                    .addComponent(jLabel51)
                    .addComponent(txtScheduleName)
                    .addComponent(comboTask, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelScheduleLayout.createSequentialGroup()
                        .addComponent(btnCreateSchedule)
                        .addGap(18, 18, 18)
                        .addComponent(btnDiscardSchedule))
                    .addComponent(comboPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50)
                    .addComponent(dateChooserSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelScheduleLayout.createSequentialGroup()
                        .addComponent(comboScheduleLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addGroup(panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelScheduleLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addComponent(jLabel49)
                        .addGap(263, 263, 263))
                    .addGroup(panelScheduleLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelScheduleLayout.setVerticalGroup(
            panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelScheduleLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelScheduleLayout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addGap(9, 9, 9)
                        .addComponent(txtScheduleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooserSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboScheduleLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(panelScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreateSchedule)
                    .addComponent(btnDiscardSchedule)
                    .addComponent(btnUpdateSchedule)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        panelCard.add(panelSchedule, "card2");

        panelProfile.setBackground(new java.awt.Color(0, 153, 204));

        jLabel16.setText("Account name :");

        jLabel39.setText("Employee ID :");

        jLabel40.setText("Username :");

        jLabel41.setText("Area of specialization :");

        jLabel42.setText("Number of clients assigned to : ");

        jLabel43.setText("Number of disposed cases :");

        jLabel44.setText("Number of pending cases :");

        jLabel45.setText("Email :");

        btnEditProfile.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnEditProfile.setText("Edit your profile");
        btnEditProfile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnEditProfile.setContentAreaFilled(false);
        btnEditProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditProfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditProfileMouseExited(evt);
            }
        });
        btnEditProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProfileActionPerformed(evt);
            }
        });

        jLabel20.setText("Date hired :");

        jLabel22.setText("Session ID :");

        jLabel23.setText("Phone :");

        jLabel38.setText("National ID :");

        jLabel64.setText("Department :");

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/User Group Man Woman.png"))); // NOI18N

        jLabel67.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel67.setText("PROFILE");

        javax.swing.GroupLayout panelProfileLayout = new javax.swing.GroupLayout(panelProfile);
        panelProfile.setLayout(panelProfileLayout);
        panelProfileLayout.setHorizontalGroup(
            panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProfileLayout.createSequentialGroup()
                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProfileLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel64)
                            .addComponent(jLabel38)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22)
                            .addComponent(jLabel20)
                            .addComponent(jLabel45)
                            .addComponent(jLabel44)
                            .addComponent(jLabel43)
                            .addComponent(jLabel42)
                            .addComponent(jLabel39)
                            .addComponent(jLabel40)
                            .addComponent(jLabel41)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSessionId, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelProfAcName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelProfEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelProfileLayout.createSequentialGroup()
                                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(panelProfileLayout.createSequentialGroup()
                                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblProfNatId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(usernameProfile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                            .addComponent(labelProfDisposed, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelProfPending, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelProfClientsAsc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelProfEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelProfSpecialization, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelProfDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblProfPhone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel65))
                                    .addComponent(lblProfDept, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEditProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel66)))))
                    .addGroup(panelProfileLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel67)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        panelProfileLayout.setVerticalGroup(
            panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProfileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67)
                .addGap(11, 11, 11)
                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelProfileLayout.createSequentialGroup()
                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(lblSessionId, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelProfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelProfileLayout.createSequentialGroup()
                                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(labelProfAcName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel39)
                                    .addComponent(labelProfEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel40)
                                    .addComponent(usernameProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelProfileLayout.createSequentialGroup()
                                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel45)
                                            .addComponent(labelProfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel65))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel41)
                                            .addComponent(labelProfSpecialization, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel42)
                                            .addComponent(labelProfClientsAsc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel43)
                                            .addComponent(labelProfDisposed, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel44)
                                            .addComponent(labelProfPending, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20))
                                    .addComponent(jLabel66))))
                        .addGap(18, 18, 18)
                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(lblProfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38)
                            .addComponent(lblProfNatId, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel64))
                    .addComponent(lblProfDept, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProfileLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEditProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        panelCard.add(panelProfile, "card2");

        btnDash.setBackground(new java.awt.Color(0, 24, 34));
        btnDash.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnDash.setForeground(java.awt.Color.cyan);
        btnDash.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Dashboard_20px.png"))); // NOI18N
        btnDash.setText("Dashboard");
        btnDash.setBorder(null);
        btnDash.setBorderPainted(false);
        btnDash.setContentAreaFilled(false);
        btnDash.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDash.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDashMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDashMouseExited(evt);
            }
        });
        btnDash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashActionPerformed(evt);
            }
        });

        btnCreate.setBackground(new java.awt.Color(0, 24, 34));
        btnCreate.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnCreate.setForeground(java.awt.Color.cyan);
        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Create New_20px.png"))); // NOI18N
        btnCreate.setText("Create");
        btnCreate.setBorder(null);
        btnCreate.setBorderPainted(false);
        btnCreate.setContentAreaFilled(false);
        btnCreate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCreate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCreateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCreateMouseExited(evt);
            }
        });
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnClients.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnClients.setForeground(java.awt.Color.cyan);
        btnClients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Client lafus_20px.png"))); // NOI18N
        btnClients.setText("Clients");
        btnClients.setContentAreaFilled(false);
        btnClients.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClients.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClientsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClientsMouseExited(evt);
            }
        });
        btnClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientsActionPerformed(evt);
            }
        });

        btnBill.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnBill.setForeground(java.awt.Color.cyan);
        btnBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Billing_20px.png"))); // NOI18N
        btnBill.setText("Billing");
        btnBill.setContentAreaFilled(false);
        btnBill.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBill.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBillMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBillMouseExited(evt);
            }
        });
        btnBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillActionPerformed(evt);
            }
        });

        btnSchedule.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnSchedule.setForeground(java.awt.Color.cyan);
        btnSchedule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/schedule_20px.png"))); // NOI18N
        btnSchedule.setText("Schedules");
        btnSchedule.setContentAreaFilled(false);
        btnSchedule.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSchedule.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSchedule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnScheduleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnScheduleMouseExited(evt);
            }
        });
        btnSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScheduleActionPerformed(evt);
            }
        });

        btnHelp2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btnHelp2.setForeground(java.awt.Color.cyan);
        btnHelp2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/Help_lafus20px.png"))); // NOI18N
        btnHelp2.setText("Help");
        btnHelp2.setContentAreaFilled(false);
        btnHelp2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHelp2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHelp2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHelp2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHelp2MouseExited(evt);
            }
        });
        btnHelp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelp2ActionPerformed(evt);
            }
        });

        timeLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(255, 255, 255));
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        timeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/clock.png"))); // NOI18N

        javax.swing.GroupLayout panelHolderLayout = new javax.swing.GroupLayout(panelHolder);
        panelHolder.setLayout(panelHolderLayout);
        panelHolderLayout.setHorizontalGroup(
            panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHolderLayout.createSequentialGroup()
                .addGroup(panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHolderLayout.createSequentialGroup()
                        .addGroup(panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelHolderLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnClients, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSchedule)))
                            .addGroup(panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelHolderLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(btnHelp2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDash, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHolderLayout.createSequentialGroup()
                                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)))
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHolderLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCard, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelHolderLayout.setVerticalGroup(
            panelHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelHolderLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnDash, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClients, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHelp2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnProfile.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/profileview.png"))); // NOI18N
        btnProfile.setText("Profile");
        btnProfile.setBorder(null);
        btnProfile.setContentAreaFilled(false);
        btnProfile.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/profilehover.png"))); // NOI18N
        btnProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProfileMouseExited(evt);
            }
        });
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/lafassoftware/lafussmall.png"))); // NOI18N
        jLabel1.setText("LAFUS");

        labelUser.setFont(new java.awt.Font("sansserif", 3, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("sansserif", 3, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Welcome");

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Work ID :");

        labelWorkid.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N

        javax.swing.GroupLayout signInMainPanelLayout = new javax.swing.GroupLayout(signInMainPanel);
        signInMainPanel.setLayout(signInMainPanelLayout);
        signInMainPanelLayout.setHorizontalGroup(
            signInMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(signInMainPanelLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelWorkid, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelUser, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSignOutUser, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(panelHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        signInMainPanelLayout.setVerticalGroup(
            signInMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signInMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(signInMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(signInMainPanelLayout.createSequentialGroup()
                        .addGroup(signInMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelWorkid, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(signInMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(signInMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnSignOutUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                .addComponent(btnProfile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(1, 1, 1)
                .addComponent(panelHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(signInMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(signInMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignOutUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignOutUserActionPerformed
        
        int response = JOptionPane.showConfirmDialog(null, "Make sure you save all your changes before you sign out." + System.lineSeparator()
                + "                            Sign out anyway?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (response) {
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CLOSED_OPTION:
                break;
            case JOptionPane.YES_OPTION:
                
               LawFirmHome.schName =null;
                LawFirmHome.schPriority = null;
                LawFirmHome.schDate=null;
                
                LawFirmHome.main(null);
                SessionEnd();
                dispose();
        }
    }//GEN-LAST:event_btnSignOutUserActionPerformed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelProfile);
        panelCard.repaint();
        panelCard.revalidate();
        String use=null;
        String use2=null;
        try{
        Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            statement = con.createStatement();
            String sgl=" select  max(signin_time) as try from session_logs";
            
            ResultSet rs=statement.executeQuery(sgl);
                    if(rs.next()){
                       use= rs.getString("try");
                    }
                String sql1="SELECT session_id from session_logs where signin_time='"+use+"'";
                 ResultSet rs1=statement.executeQuery(sql1);
                 if(rs1.next()){
                    use2=rs1.getString("session_id");
                    
                 }
                 lblSessionId.setText(use2);
        }catch (ClassNotFoundException | SQLException | HeadlessException es) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Log out of reach");
            JOptionPane.showMessageDialog(null, es.getMessage());
        }
        
// TODO add your handling code here:
    }//GEN-LAST:event_btnProfileActionPerformed

    private void btnHelp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelp2ActionPerformed
        HelpDialog help = new HelpDialog(this, true);
        help.setVisible(true);
    }//GEN-LAST:event_btnHelp2ActionPerformed

    private void btnScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScheduleActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelSchedule);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnScheduleActionPerformed

    private void btnBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelBilling);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnBillActionPerformed

    private void btnClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientsActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelClients);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnClientsActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelCreate);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnDashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelDashboard);
        panelCard.repaint();
        panelCard.revalidate();
        loadEventDashboard();
        reportDashboardInfo();
    }//GEN-LAST:event_btnDashActionPerformed

    private void txtEmpIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpIdActionPerformed

    private void btnClientSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientSaveActionPerformed
        Clientmethod();
    }//GEN-LAST:event_btnClientSaveActionPerformed

    private void btnShowAllClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllClientsActionPerformed
        while (model.getRowCount() != 0) {
            model.removeRow(0);
        }
        DisplayClient();
        searchClient.setText("");
//select=null; 
//work1=null;
    }//GEN-LAST:event_btnShowAllClientsActionPerformed

    private void btnSearchClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchClientActionPerformed
        
        String x = searchClient.getText().trim();
        if (x.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Client Id or assigned-to Id then search");
        } else {
            while (model.getRowCount() != 0) {
                model.removeRow(0);
            }
            search();
            searchUsingEmpID();
        }
    }//GEN-LAST:event_btnSearchClientActionPerformed

    private void btnProfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProfileMouseEntered
        btnProfile.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnProfileMouseEntered

    private void btnProfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProfileMouseExited
        btnProfile.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnProfileMouseExited

    private void btnSignOutUserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSignOutUserMouseEntered
        btnSignOutUser.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnSignOutUserMouseEntered

    private void btnSignOutUserMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSignOutUserMouseExited
        btnSignOutUser.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnSignOutUserMouseExited

    private void btnSearchClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchClientMouseEntered
        btnSearchClient.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnSearchClientMouseEntered

    private void btnSearchClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchClientMouseExited
        btnSearchClient.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnSearchClientMouseExited

    private void btnShowAllClientsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowAllClientsMouseEntered
        btnShowAllClients.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnShowAllClientsMouseEntered

    private void btnShowAllClientsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowAllClientsMouseExited
        btnShowAllClients.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnShowAllClientsMouseExited

    private void btnViewClientDetailsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewClientDetailsMouseEntered
        btnViewClientDetails.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnViewClientDetailsMouseEntered

    private void btnViewClientDetailsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewClientDetailsMouseExited
        btnViewClientDetails.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnViewClientDetailsMouseExited

    private void btnClientSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientSaveMouseEntered
        btnClientSave.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnClientSaveMouseEntered

    private void btnClientSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientSaveMouseExited
        btnClientSave.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnClientSaveMouseExited

    private void btnClientClearMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientClearMouseEntered
        btnClientClear.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnClientClearMouseEntered

    private void btnClientClearMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientClearMouseExited
        btnClientClear.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnClientClearMouseExited

    private void btnDashMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashMouseEntered
        btnDash.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnDashMouseEntered

    private void btnDashMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashMouseExited
        btnDash.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnDashMouseExited

    private void btnCreateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCreateMouseEntered
        btnCreate.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnCreateMouseEntered

    private void btnCreateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCreateMouseExited
        btnCreate.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnCreateMouseExited

    private void btnClientsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientsMouseEntered
        btnClients.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnClientsMouseEntered

    private void btnClientsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientsMouseExited
        btnClients.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnClientsMouseExited

    private void btnBillMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillMouseEntered
        btnBill.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnBillMouseEntered

    private void btnBillMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillMouseExited
        btnBill.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnBillMouseExited

    private void btnScheduleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnScheduleMouseEntered
        btnSchedule.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnScheduleMouseEntered

    private void btnScheduleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnScheduleMouseExited
        btnSchedule.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnScheduleMouseExited

    private void btnHelp2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHelp2MouseEntered
        btnHelp2.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnHelp2MouseEntered

    private void btnHelp2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHelp2MouseExited
        btnHelp2.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnHelp2MouseExited

    private void btnAddClientBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddClientBillActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelClients);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnAddClientBillActionPerformed

    private void btnNewInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewInvoiceActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelAddInvoice);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnNewInvoiceActionPerformed

    private void btnUnpaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnpaidActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelUnpaid);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnUnpaidActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelReports);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnReportsActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelPrevious);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnRecieptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecieptsActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelReciepts);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnRecieptsActionPerformed

    private void btnBillTrendsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillTrendsActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelTrends);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnBillTrendsActionPerformed

    private void btnUnpaidBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnpaidBackActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelBillHome);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnUnpaidBackActionPerformed

    private void btnReportsBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsBackActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelBillHome);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnReportsBackActionPerformed

    private void btnPreviousBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousBackActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelBillHome);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnPreviousBackActionPerformed

    private void btnRecieptsBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecieptsBackActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelBillHome);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnRecieptsBackActionPerformed
//back button in trends panel
    private void btnTrendsBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrendsBackActionPerformed
        panelBilling.removeAll();
        panelBilling.repaint();
        panelBilling.revalidate();
        
        panelBilling.add(panelBillHome);
        panelBilling.repaint();
        panelBilling.revalidate();
    }//GEN-LAST:event_btnTrendsBackActionPerformed
//mouse over event for trends button
    private void btnBillTrendsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillTrendsMouseEntered
        btnBillTrends.setForeground(Color.CYAN);
    }//GEN-LAST:event_btnBillTrendsMouseEntered
//mouse over event for trends button
    private void btnBillTrendsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillTrendsMouseExited
        btnBillTrends.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnBillTrendsMouseExited

    private void comboCaseStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCaseStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCaseStatusActionPerformed

    private void rdFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdFemaleActionPerformed

    private void btnClientClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientClearActionPerformed
        {
            int response = JOptionPane.showConfirmDialog(null, "Clear all the entries without saving?",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (response) {
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CLOSED_OPTION:
                    break;
                case JOptionPane.YES_OPTION:
                    // Reset Text Fields
                    clientID.setText("");
                    clientFname.setText("");
                    clientLname.setText("");
                    phoneNo.setText("");
                    clentAddresField.setText("");
                    txtClientMail.setText("");
                    txtCaseID.setText("");
                    btnGroupClientGender.clearSelection();
                    txtCaseID.setText("");
                    comboCaseStatus.setSelectedIndex(0);
                    jComboBoxCasetype.setSelectedIndex(0);
                    dateChooserFiling.setCalendar(null);
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btnClientClearActionPerformed
//button back in add invoice panel
    private void btnAddInvoiceBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddInvoiceBackActionPerformed
        
        int response = JOptionPane.showConfirmDialog(null, "Have you saved your changes?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (response) {
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CLOSED_OPTION:
                break;
            case JOptionPane.YES_OPTION:
                panelBilling.removeAll();
                panelBilling.repaint();
                panelBilling.revalidate();
                
                panelBilling.add(panelBillHome);
                panelBilling.repaint();
                panelBilling.revalidate();
        }
    }//GEN-LAST:event_btnAddInvoiceBackActionPerformed
//search button in unpaid panel
    private void txtSearchUnpaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchUnpaidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchUnpaidActionPerformed
//view button under the clients table
    private void btnViewClientDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewClientDetailsActionPerformed
        if (fnameTable == null) {
            JOptionPane.showMessageDialog(null, "Select Client whose details you wish to view/update");
        } else {
            ClientsViewDialog client = new ClientsViewDialog(this, true);
            client.setVisible(true);
        }
    }//GEN-LAST:event_btnViewClientDetailsActionPerformed
//mouse event for the table clients
    private void tableClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClientMouseClicked
        int row = tableClient.getSelectedRow();
        //public static String clientId1,fnameTable, lnameTable ,caseStatus, assignedTo, email1;
        // tb=(String) tableClient.getModel().getValueAt(row, 4);
        clientId1 = (String) tableClient.getModel().getValueAt(row, 0);
        fnameTable = (String) tableClient.getModel().getValueAt(row, 1);
        lnameTable = (String) tableClient.getModel().getValueAt(row, 2);
        caseStatus1 = (String) tableClient.getModel().getValueAt(row, 3);
        assignedTo = (String) tableClient.getModel().getValueAt(row, 4);
    }//GEN-LAST:event_tableClientMouseClicked
//create button in in reports panel
    private void btnCreateDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateDocumentActionPerformed
        
        if (reportConent.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No content on the report to save");
        } else {
            try {
                try (FileOutputStream outstream = new FileOutputStream(txtDocName.getText().trim()+".docx")) {
                    XWPFDocument doc = new XWPFDocument();
                    XWPFParagraph parag = doc.createParagraph();
                    parag.setAlignment(ParagraphAlignment.LEFT);
                    XWPFRun paragRun = parag.createRun();
                    paragRun.setFontSize(14);
                    paragRun.setBold(true);
                    paragRun.setText(titleReport.getText().toUpperCase().trim());
                    doc.createParagraph().createRun().addBreak();
                    doc.createParagraph().createRun().setText(reportConent.getText());
                    doc.write(outstream);
                    //JOptionPane.showMessageDialog(null, "Report created successfully");
                }
            } catch (IOException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);
            }
            ReportData();
            titleReport.setText("");
            reportConent.setText("");
            txtDocName.setText("");
        }
    }//GEN-LAST:event_btnCreateDocumentActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        calculatetotal();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        String invoiceNymber = txtInvoiceClientId.getText();
        if (invoiceNymber.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the client's ID first");
        } else {
            retrievClientDetails();
            showAll();
        }
    }//GEN-LAST:event_jButton23ActionPerformed
//save button in invoice panel
    private void btnSaveInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveInvoiceActionPerformed
        InvoiceData();
    }//GEN-LAST:event_btnSaveInvoiceActionPerformed
//discard button in panel reports
    private void btnDiscardReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscardReportActionPerformed
        titleReport.setText("");
        reportConent.setText("");
    }//GEN-LAST:event_btnDiscardReportActionPerformed

    private void btnDiscardInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscardInvoiceActionPerformed
        txtInvoiceID.setText("");
        jDateChooser_invoice.setCalendar(null);
        txtHoursWorked.setText("");
        txtRatingAmount.setText("");
        txtDiscount.setText("");
        txtFilingFee.setText("");
        txtCaseID.setText("");
        txtInvoiceClientId.setText("");
        txtInvoiceClientFname.setText("");
        txtInvoiceClientLname.setText("");
        txtInvoiceAssigneedTo.setText("");
        txtInvoiceCaseType.setText("");
        txtInvoiceCaseStatus.setText("");
        txtInvoiceTotal.setText("");
        comboPaymentMethod.setSelectedIndex(0);
        buttonGroupPaidUnpaid.clearSelection();
    }//GEN-LAST:event_btnDiscardInvoiceActionPerformed

    private void btnCreateEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateEventActionPerformed
        EventMethod();
    }//GEN-LAST:event_btnCreateEventActionPerformed

    private void btnDiscardEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscardEventActionPerformed
        txtEventName.setText("");
        comboEventCategory.setSelectedIndex(0);
        dateChooserEvents.setCalendar(null);
        comboEventLocation.setSelectedIndex(0);
        spinFieldHours.setValue(0);
        txtAreaDescription.setText("");
    }//GEN-LAST:event_btnDiscardEventActionPerformed

    private void searchClientKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchClientKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String x = searchClient.getText().trim();
            if (x.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter Client Id or assigned-to Id then search");
            } else {
                while (model.getRowCount() != 0) {
                    model.removeRow(0);
                }
                search();
                searchUsingEmpID();
            }
        }
    }//GEN-LAST:event_searchClientKeyPressed

    private void searchClientKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchClientKeyTyped
        char c = evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_SPACE) || (c == 9 || c >= 58 && c <= 126 || c >= 33 && c <= 42 || c >= 44 && c <= 47)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_searchClientKeyTyped

    private void btnViewEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewEventActionPerformed
        if (eventIDmouse == null) {
            JOptionPane.showMessageDialog(null, "Select an Event from the table whose details you want to view");
        } else {
            
            EventDialogView event = new EventDialogView(this, true);
            event.setVisible(true);
            
        }
    }//GEN-LAST:event_btnViewEventActionPerformed

    private void tableEventListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEventListMouseClicked
        int row = tableEventList.getSelectedRow();
        eventIDmouse = (String) tableEventList.getModel().getValueAt(row, 0);
        eventNameMouse = (String) tableEventList.getModel().getValueAt(row, 1);
        EventDateMouse = (String) tableEventList.getModel().getValueAt(row, 2);
        eventCreatorMouse = (String) tableEventList.getModel().getValueAt(row, 3);
    }//GEN-LAST:event_tableEventListMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        while (eventTableList.getRowCount() != 0) {
            eventTableList.removeRow(0);
        }
        DisplayEventList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCreateScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateScheduleActionPerformed
        ScheduleData();
    }//GEN-LAST:event_btnCreateScheduleActionPerformed

    private void btnDiscardScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiscardScheduleActionPerformed
        
        if (txtScheduleName.getText().trim().equals("") && txtTaskDesc.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nothing to discard");
        } else {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to Discard the schedule?",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (response) {
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CLOSED_OPTION:
                    break;
                case JOptionPane.YES_OPTION:
                    txtScheduleName.setText("");
                    dateChooserSchedule.setCalendar(null);
                    comboTask.setSelectedIndex(0);
                    comboPriority.setSelectedIndex(0);
                    comboScheduleLocation.setSelectedIndex(0);
                    txtTaskDesc.setText("");
            }
        }
    }//GEN-LAST:event_btnDiscardScheduleActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        while (schedulelist.getRowCount() != 0) {
            schedulelist.removeRow(0);
        }
        DisplaySchedule();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void clentAddresFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clentAddresFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clentAddresFieldActionPerformed

    private void txtInvoiceClientIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceClientIdKeyTyped
        char c = evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_SPACE) || (c == 9 || c >= 58 && c <= 126 || c >= 33 && c <= 42 || c >= 44 && c <= 47)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtInvoiceClientIdKeyTyped

    private void btnUpdateScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateScheduleActionPerformed
            
        if (schId1 == null) {
            JOptionPane.showMessageDialog(null, "Select the schedule to view or, you may not have created one");
        } else {
            
            ScheduleView sched = new ScheduleView(this, true);
            sched.setVisible(true);
            
        }
    }//GEN-LAST:event_btnUpdateScheduleActionPerformed

    private void tableScheduleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableScheduleMouseClicked
        
        int row = tableSchedule.getSelectedRow();
        //public static String schId1, schName1, schDate1, scchLocation1;
        schId1 = (String) tableSchedule.getModel().getValueAt(row, 0);
        schName1 = (String) tableSchedule.getModel().getValueAt(row, 1);
        schDate1 = (String) tableSchedule.getModel().getValueAt(row, 2);
        scchLocation1 = (String) tableSchedule.getModel().getValueAt(row, 3);
    }//GEN-LAST:event_tableScheduleMouseClicked

    private void btnEditProfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditProfileMouseEntered
        btnEditProfile.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnEditProfileMouseEntered

    private void btnEditProfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditProfileMouseExited
        btnEditProfile.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnEditProfileMouseExited

    private void btnEditProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProfileActionPerformed
        EmployeeEditDialog employer = new EmployeeEditDialog(this, true);
        employer.setVisible(true);
    }//GEN-LAST:event_btnEditProfileActionPerformed

    private void btnViewAllEventsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllEventsActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelCreate);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnViewAllEventsActionPerformed

    private void btnViewClientListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewClientListActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelClients);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnViewClientListActionPerformed

    private void btnViewAllSchedulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllSchedulesActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelSchedule);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnViewAllSchedulesActionPerformed

    private void btnManageReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageReportsActionPerformed
        panelCard.removeAll();
        panelCard.repaint();
        panelCard.revalidate();
        
        panelCard.add(panelBilling);
        panelCard.repaint();
        panelCard.revalidate();
    }//GEN-LAST:event_btnManageReportsActionPerformed

    private void btnOpenReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenReportsActionPerformed
         while (reportTable.getRowCount() != 0) {
            reportTable.removeRow(0);
        }
        DisplayReportList();
    }//GEN-LAST:event_btnOpenReportsActionPerformed

    private void jtreeDocumentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtreeDocumentsMouseClicked
        jtreevar = jtreeDocuments.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ","\\");
    }//GEN-LAST:event_jtreeDocumentsMouseClicked

    private void btnViewAllEventsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllEventsMouseEntered
        btnViewAllEvents.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnViewAllEventsMouseEntered

    private void btnViewAllEventsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllEventsMouseExited
        btnViewAllEvents.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnViewAllEventsMouseExited

    private void btnViewClientListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewClientListMouseEntered
        btnViewClientList.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnViewClientListMouseEntered

    private void btnViewClientListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewClientListMouseExited
        btnViewClientList.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnViewClientListMouseExited

    private void btnViewAllSchedulesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllSchedulesMouseEntered
        btnViewAllSchedules.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnViewAllSchedulesMouseEntered

    private void btnViewAllSchedulesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewAllSchedulesMouseExited
        btnViewAllSchedules.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnViewAllSchedulesMouseExited

    private void btnManageReportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageReportsMouseEntered
        btnManageReports.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnManageReportsMouseEntered

    private void btnManageReportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageReportsMouseExited
        btnManageReports.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnManageReportsMouseExited

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void btnRecieptsBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRecieptsBackMouseEntered
        btnRecieptsBack.setForeground(Color.BLACK);
    }//GEN-LAST:event_btnRecieptsBackMouseEntered

    private void btnRecieptsBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRecieptsBackMouseExited
        btnRecieptsBack.setForeground(Color.WHITE);
    }//GEN-LAST:event_btnRecieptsBackMouseExited

    private void openFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileActionPerformed
        try{
        File Selection = new File(jtreevar);
        if(Selection.exists()){
        if(Desktop.isDesktopSupported()){ Desktop.getDesktop().open(Selection);}
        else{JOptionPane.showMessageDialog(this, "Not supported","ERROR",JOptionPane.INFORMATION_MESSAGE);}
        }
        else{ JOptionPane.showMessageDialog(this, "File does not exist","ERROR",JOptionPane.INFORMATION_MESSAGE);}
        }
        catch(IOException | HeadlessException ex){ex.printStackTrace();}
    }//GEN-LAST:event_openFileActionPerformed

    private void txtHoursWorkedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoursWorkedKeyTyped
        char c = evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_SPACE) || (c == 9 || c >= 58 && c <= 126 || c >= 33 && c <= 42 || c >= 44 && c <= 47)) {
            getToolkit().beep();
            evt.consume();}
    }//GEN-LAST:event_txtHoursWorkedKeyTyped

    private void txtRatingAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRatingAmountKeyTyped
        char c = evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_SPACE) || (c == 9 || c >= 58 && c <= 126 || c >= 33 && c <= 42 || c >= 44 && c <= 47)) {
            getToolkit().beep();
            evt.consume();}
    }//GEN-LAST:event_txtRatingAmountKeyTyped

    private void txtDiscountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyTyped
        char c = evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_SPACE) || (c == 9 || c >= 58 && c <= 126 || c >= 33 && c <= 42 || c >= 44 && c <= 47)) {
            getToolkit().beep();
            evt.consume();}
    }//GEN-LAST:event_txtDiscountKeyTyped

    private void txtFilingFeeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilingFeeKeyTyped
        char c = evt.getKeyChar();
        if ((c == java.awt.event.KeyEvent.VK_SPACE) || (c == 9 || c >= 58 && c <= 126 || c >= 33 && c <= 42 || c >= 44 && c <= 47)) {
            getToolkit().beep();
            evt.consume();}
    }//GEN-LAST:event_txtFilingFeeKeyTyped

    private void openFileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openFileMouseEntered
        openFile.setForeground(Color.WHITE);
    }//GEN-LAST:event_openFileMouseEntered

    private void openFileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openFileMouseExited
        openFile.setForeground(Color.BLACK);
    }//GEN-LAST:event_openFileMouseExited
    
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameSignIn.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new FrameSignIn().setVisible(true);
            } catch (ParseException | IOException ex) {
                Logger.getLogger(FrameSignIn.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
//client method to register data to the client table in the dtatabase    

    public void Clientmethod() {
        
        if (RegisterData() == true) {
            
            String strNatId = clientID.getText();
            String strFname = clientFname.getText().toUpperCase();
            String strLname = clientLname.getText().toUpperCase();
            String strPhone = phoneNo.getText();
            String strAddress = clentAddresField.getText();
            String strMail = txtClientMail.getText();
            String strRadioGender = "";
            if (rdFemale.isSelected()) {
                strRadioGender = rdFemale.getText();
            }
            if (rdMale.isSelected()) {
                strRadioGender = rdMale.getText();
            }
            if (rdOther.isSelected()) {
                strRadioGender = rdOther.getText();
            }
            if (rdPreferNotSay.isSelected()) {
                strRadioGender = rdPreferNotSay.getText();
            }
            String strCaseNo = txtCaseID.getText();
            String strCaseSatusCombo = (String) comboCaseStatus.getSelectedItem();
            String strAssignedTo = txtEmpId.getText();
            String strInvoiceId = txtField_invoiceId.getText();
            String strCaseType = (String) jComboBoxCasetype.getSelectedItem();
            Date dateOfFile = dateChooserFiling.getDate();
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                
                con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
                statement = con.createStatement();

                // SQL Insert
                String sql = "INSERT INTO newclient " + "(client_natid,client_fname,client_lname,cl_phone,cl_address,client_mail,cl_gender,case_no,case_status,assigned_to,invoice_id)"
                        + "VALUES ('" + strNatId + "', '"
                        + strFname + "','"
                        + strLname + "','"
                        + strPhone + "','"
                        + strAddress + "','"
                        + strMail + "','"
                        + strRadioGender + "','"
                        + strCaseNo + "','"
                        + strCaseSatusCombo + "','"
                        + strAssignedTo + "','"
                        + strInvoiceId + "')";
                
                statement.execute(sql);
                
                String sql1 = "INSERT INTO table_case" + "(case_no,case_type,case_status,emp_id,date_of_file)"
                        + "VALUES ('" + strCaseNo + "', '"
                        + strCaseType + "','"
                        + strCaseSatusCombo + "','"
                        + strAssignedTo + "','"
                        + dateOfFile + "')";
                
                statement.execute(sql1);
                
                JOptionPane.showMessageDialog(null, "Client " + strFname + " " + strLname + " " + System.lineSeparator()
                        + "Of Client ID " + strNatId + " " + System.lineSeparator() + "added successfuly");

                // Reset Text Fields
                clientID.setText("");
                clientFname.setText("");
                clientLname.setText("");
                phoneNo.setText("");
                clentAddresField.setText("");
                txtClientMail.setText("");
                txtCaseID.setText("");
                btnGroupClientGender.clearSelection();
                txtCaseID.setText("");
                comboCaseStatus.setSelectedIndex(0);
                jComboBoxCasetype.setSelectedIndex(0);
                txtField_invoiceId.setText("");
                dateChooserFiling.setCalendar(null);

                //status  = true;
            } catch (ClassNotFoundException | SQLException | HeadlessException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Unable to add");
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
            try {
                if (statement != null) {
                    statement.close();
                    con.close();
                }
            } catch (SQLException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
        }//end of if statement that validate the form

    }
//event method to register data to the event table

    public void EventMethod() {
        
        if (RegisterEventData() == true) {
            
            String strEventName = txtEventName.getText().toUpperCase().trim();
            String strEventCatCombo = (String) comboEventCategory.getSelectedItem();
            Date dateOfEvent = dateChooserEvents.getDate();
            String strEventLocation = (String) comboEventLocation.getSelectedItem();
            int strHours = spinFieldHours.getValue();
            String strDescription = txtAreaDescription.getText().trim();
            String strEventcreator = txtEmpId.getText();
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                
                con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
                statement = con.createStatement();

                // SQL Insert
                String sql = "INSERT INTO event_table " + "(event_name,category,event_date,location,duration,description,event_creator)"
                        + "VALUES ('" + strEventName + "','"
                        + strEventCatCombo + "','"
                        + dateOfEvent + "','"
                        + strEventLocation + "','"
                        + strHours + "','"
                        + strDescription + "','"
                        + strEventcreator + "')";
                
                statement.execute(sql);
                
                JOptionPane.showMessageDialog(null, "Event of title " + strEventName + " " + System.lineSeparator()
                        + "created successfuly");

                // Reset Fields
                txtEventName.setText("");
                comboEventCategory.setSelectedIndex(0);
                dateChooserEvents.setCalendar(null);
                comboEventLocation.setSelectedIndex(0);
                spinFieldHours.setValue(0);
                txtAreaDescription.setText("");
                
            } catch (ClassNotFoundException | SQLException | HeadlessException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Unable to create this event");
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
            try {
                if (statement != null) {
                    statement.close();
                    con.close();
                }
            } catch (SQLException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
        }//end of if statement that validate the form

    }
    
    private Boolean RegisterEventData() {
        
        String strEventName = txtEventName.getText().toUpperCase().trim();
        String strEventCatCombo = (String) comboEventCategory.getSelectedItem();
        Date dateOfEvent = dateChooserEvents.getDate();
        String strEventLocation = (String) comboEventLocation.getSelectedItem();
        int strHours = spinFieldHours.getValue();
        String strDescription = txtAreaDescription.getText().trim();
        
        if (dateOfEvent == null) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please choose date of event");
            dateChooserEvents.requestFocusInWindow();
            return false;
        }
        
        if (strEventName.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please enter event name" + System.lineSeparator() + "If the event is"
                            + " less than 1 hour just select 1 hour.");
            txtEventName.requestFocusInWindow();
            return false;
        }
        if (strHours == 0) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Enter the event duration");
            txtEventName.requestFocusInWindow();
            return false;
        }
        
        if (strDescription.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please shortly describe the event");
            txtAreaDescription.requestFocusInWindow();
            return false;
        }
        if (strEventLocation.contains("select location")) // id number
        {
            JOptionPane.showMessageDialog(null,
                    "select location");
            return false;
        }
        if (strEventCatCombo.contains("select category")) // id number
        {
            JOptionPane.showMessageDialog(null,
                    "select category of event");
            return false;
        }
        return true;
    }
    
    public void InvoiceData() {
        
        if (RegisterInvoiceData() == true) {
            
            String strInvoice_id = txtInvoiceID.getText();
            Date dateOfInvoicee = jDateChooser_invoice.getDate();
            String strHrsOnMatter = txtHoursWorked.getText();
            String strRating = txtRatingAmount.getText();
            String strDiscount = txtDiscount.getText();
            String strfiling = txtFilingFee.getText();
            String strPaymentMethod = (String) comboPaymentMethod.getSelectedItem();
            String strTotal = txtInvoiceTotal.getText();
            String strPaidUnpaid = "";
            if (radioPaid.isSelected()) {
                strPaidUnpaid = radioPaid.getText();
            }
            if (radioNotYet.isSelected()) {
                strPaidUnpaid = radioNotYet.getText();
            }
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                
                con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
                statement = con.createStatement();

                // SQL Insert
                String sql = "INSERT INTO client_invoice " + "(invoice_id,invoice_date,hours_on_matter,rating,discount,filing_fee,payment_method,total,paid_notpaid)"
                        + "VALUES ('" + strInvoice_id + "', '"
                        + dateOfInvoicee + "','"
                        + strHrsOnMatter + "','"
                        + strRating + "','"
                        + strDiscount + "','"
                        + strfiling + "','"
                        + strPaymentMethod + "','"
                        + strTotal + "','"
                        + strPaidUnpaid + "')";
                
                statement.execute(sql);
                JOptionPane.showMessageDialog(null, "New invoice of ID " + strInvoice_id + " has been added successfuly");

                // Reset Text Fields
                txtInvoiceID.setText("");
                jDateChooser_invoice.setCalendar(null);
                txtHoursWorked.setText("");
                txtRatingAmount.setText("");
                txtDiscount.setText("");
                txtFilingFee.setText("");
                txtCaseID.setText("");
                txtInvoiceClientId.setText("");
                txtInvoiceClientFname.setText("");
                txtInvoiceClientLname.setText("");
                txtInvoiceAssigneedTo.setText("");
                txtInvoiceCaseType.setText("");
                txtInvoiceCaseStatus.setText("");
                txtInvoiceTotal.setText("");
                comboPaymentMethod.setSelectedIndex(0);
                buttonGroupPaidUnpaid.clearSelection();

                //status  = true;
            } catch (ClassNotFoundException | SQLException | HeadlessException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Unable to add Invoice");
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
            try {
                if (statement != null) {
                    statement.close();
                    con.close();
                }
            } catch (SQLException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
        }//end of if statement that validate the form

    }
    
    private Boolean RegisterInvoiceData() {
        Date dateOfInvoicee = jDateChooser_invoice.getDate();
        String strHrsOnMatter = txtHoursWorked.getText();
        String strRating = txtRatingAmount.getText();
        String strDiscount = txtDiscount.getText();
        String strfiling = txtFilingFee.getText();
        String strPaymentMethod = (String) comboPaymentMethod.getSelectedItem();
        
        if (dateOfInvoicee == null) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please choose date");
            jDateChooser_invoice.requestFocusInWindow();
            return false;
        }
        
        if (strHrsOnMatter.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input hours worked on matter");
            txtHoursWorked.requestFocusInWindow();
            return false;
        }
        
        if (strRating.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input Rating per hour");
            txtRatingAmount.requestFocusInWindow();
            return false;
        }
        
        if (strDiscount.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "What is the discount charge amount?");
            txtDiscount.requestFocusInWindow();
            return false;
        }
        
        if (strfiling.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please charge some filing fee for the case");
            txtFilingFee.requestFocusInWindow();
            return false;
        }
        if (strPaymentMethod.contains("select payment method")) // id number
        {
            JOptionPane.showMessageDialog(null,
                    "Please select payment method");
            return false;
        }
        
        if (buttonGroupPaidUnpaid.getSelection() == null) {
            JOptionPane.showMessageDialog(null,
                    "Please specify whether it is not yet paid or paid");
            return false;
        }
        return true;
    }
    
    private Boolean RegisterData() {

        //EmailValidator b=new EmailValidator ();
        String strFname = clientFname.getText().toUpperCase().trim();
        String strLname = clientLname.getText().toUpperCase().trim();
        String strPhone = phoneNo.getText().trim();
        String strClient = clientID.getText().trim();
        String strCaseID = txtCaseID.getText().trim();
        String strcombo = (String) comboCaseStatus.getSelectedItem();
        String strType = (String) jComboBoxCasetype.getSelectedItem();
        
        if (strFname.equals("")) //first Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input (First Name)");
            clientFname.requestFocusInWindow();
            return false;
        }
        
        if (strLname.equals("")) // second Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input (Last Name)");
            clientLname.requestFocusInWindow();
            return false;
        }
        if (strPhone.equals("")) // username Name
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input (Phone number)");
            phoneNo.requestFocusInWindow();
            return false;
        }
        
        if (strClient.equals("")) // client Id
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input (Client national ID)");
            clientID.requestFocusInWindow();
            return false;
        }
        
        if (strCaseID.equals("")) // client Id
        {
            JOptionPane.showMessageDialog(null,
                    "Please Input (Case ID)");
            txtCaseID.requestFocusInWindow();
            return false;
        }
        
        if (strcombo.contains("select case status")) // combo box 
        {
            JOptionPane.showMessageDialog(null, "Please select case status");
            return false;
        }
         if (strType.contains("select case type")) // combo box 
        {
            JOptionPane.showMessageDialog(null, "Please select case status");
            return false;
        }
        return true;
    }

//schedule method
    public void ScheduleData() {
        
        if (RegisterScheduleData() == true) {
            
            String strSchedduleName = txtScheduleName.getText().toUpperCase().trim();
            String strTask = (String) comboTask.getSelectedItem();
            String strPriority = (String) comboPriority.getSelectedItem();
            Date dateOfSchedule = dateChooserSchedule.getDate();
            String strDescribe = txtTaskDesc.getText().trim();
            String strScheduleLocation = (String) comboScheduleLocation.getSelectedItem();
            String strSchedulecreator = txtEmpId.getText();
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                
                con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
                statement = con.createStatement();

                // SQL Insert
                String sql = "INSERT INTO table_schedule " + "(schedule_name,task_type,priority,date_to_do,sch_describe,sch_location,sch_creator)"
                        + "VALUES ('" + strSchedduleName + "', '"
                        + strTask + "','"
                        + strPriority + "','"
                        + dateOfSchedule + "','"
                        + strDescribe + "','"
                        + strScheduleLocation + "','"
                        + strSchedulecreator + "')";
                
                statement.execute(sql);
                JOptionPane.showMessageDialog(null, "Schedule of title " + strSchedduleName + " created due to be done by " + dateOfSchedule + "");

                // Reset Text Fields
                txtScheduleName.setText("");
                dateChooserSchedule.setCalendar(null);
                comboTask.setSelectedIndex(0);
                comboPriority.setSelectedIndex(0);
                comboScheduleLocation.setSelectedIndex(0);
                txtTaskDesc.setText("");
                //status  = true;

            } catch (ClassNotFoundException | SQLException | HeadlessException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Unable to create schedule");
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
            try {
                if (statement != null) {
                    statement.close();
                    con.close();
                }
            } catch (SQLException es) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
            
        }//end of if statement that validate the form

    }
    
    private boolean RegisterScheduleData() {
        
        String strSchedduleName = txtScheduleName.getText().trim();
        String strTask = (String) comboTask.getSelectedItem();
        String strPriority = (String) comboPriority.getSelectedItem();
        Date dateOfSchedule = dateChooserSchedule.getDate();
        String strDescribe = txtTaskDesc.getText().trim();
        String strScheduleLocation = (String) comboScheduleLocation.getSelectedItem();
        
        if (strSchedduleName.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter schedule name");
            txtScheduleName.requestFocusInWindow();
            return false;
        }
        
        if (strTask.contains("select task")) {
            JOptionPane.showMessageDialog(null, "select task");
            return false;
        }
        
        if (strPriority.contains("choose priority")) {
            JOptionPane.showMessageDialog(null, "please choose the priority of your task");
            return false;
        }
        
        if (dateOfSchedule == null) {
            JOptionPane.showMessageDialog(null, "please select date of the schedule");
            dateChooserSchedule.requestFocusInWindow();
            return false;
        }
        
        if (strDescribe.equals("")) {
            JOptionPane.showMessageDialog(null, "please shortly describe your schedule");
            txtTaskDesc.requestFocusInWindow();
            return false;
        }
        
        if (strScheduleLocation.contains("select location")) {
            JOptionPane.showMessageDialog(null, "please select the location for your schedule");
            return false;
        } else {
            return true;
        }
    }
    
    public void DisplaySchedule() {
        
        schedulelist.setColumnIdentifiers(schedduleColumns);
        
        tableSchedule.setModel(schedulelist);
        
        tableSchedule.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select schedule_id,schedule_name,date_to_do,sch_location from  table_schedule where sch_creator='" + LawFirmHome.userId + "'");
            ResultSet rs = st.executeQuery();
            
            int i = 0;
            while (rs.next()) {
                scheduleOne = rs.getString("schedule_id");
                scheduleName = rs.getString("schedule_name");
                scheduleDate = rs.getString("date_to_do");
                scheduleWhere = rs.getString("sch_location");
                
                schedulelist.addRow(new Object[]{scheduleOne, scheduleName, scheduleDate, scheduleWhere});
                i++;
                
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

//client table
    public void DisplayClient() {
        
        model.setColumnIdentifiers(columnNames);
        
        tableClient.setModel(model);
        
        tableClient.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select client_natid,client_fname,client_lname,assigned_to,case_status from  newclient");
            ResultSet rs = st.executeQuery();
            
            int i = 0;
            while (rs.next()) {
                clientId = rs.getString("client_natid");
                fname = rs.getString("client_fname");
                lname = rs.getString("client_lname");
                caseStatus = rs.getString("case_status");
                workerId = rs.getString("assigned_to");
                
                model.addRow(new Object[]{clientId, fname, lname, caseStatus, workerId});
                i++;
                
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

//search in client table
    public void search() {
        model.setColumnIdentifiers(columnNames);
        
        tableClient.setModel(model);
        
        tableClient.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select client_natid,client_fname,client_lname,case_status,assigned_to from newclient where client_natid=" + searchClient.getText());
            //st.setString(1, tb);
            ResultSet rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                clientId = rs.getString("client_natid");
                fname = rs.getString("client_fname");
                lname = rs.getString("client_lname");
                caseStatus = rs.getString("case_status");
                workerId = rs.getString("assigned_to");
                
                model.addRow(new Object[]{clientId, fname, lname, caseStatus, workerId});
                i++;
                
            }
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void searchUsingEmpID() {
        model.setColumnIdentifiers(columnNames);
        
        tableClient.setModel(model);
        // tableAdmin.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tableClient.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select client_natid,client_fname,client_lname,case_status,assigned_to from newclient where assigned_to=" + searchClient.getText());
            //st.setString(1, tb);
            ResultSet rs = st.executeQuery();
            int i = 0;
            while (rs.next()) {
                clientId = rs.getString("client_natid");
                fname = rs.getString("client_fname");
                lname = rs.getString("client_lname");
                caseStatus = rs.getString("case_status");
                workerId = rs.getString("assigned_to");
                
                model.addRow(new Object[]{clientId, fname, lname, caseStatus, workerId});
                i++;
                
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void calculatetotal() {
        
        String hoursWorked, ratingAmount, dicount, fileFee;
        if (checkInvoice() == true) {
            
            hoursWorked = txtHoursWorked.getText().trim();
            int hrs = Integer.parseInt(hoursWorked);
            
            ratingAmount = txtRatingAmount.getText().trim();
            int rating = Integer.parseInt(ratingAmount);
            
            dicount = txtDiscount.getText().trim();
            int disc = Integer.parseInt(dicount);
            
            fileFee = txtFilingFee.getText().trim();
            int fee = Integer.parseInt(fileFee);
            
            if(hrs>=1 && rating>=3000 && disc<1499 && fee>=2000){
            
            double invoiceTotal = (fee + (hrs * rating)) - disc;
            
            txtInvoiceTotal.setText(String.valueOf(invoiceTotal));
            txtInvoiceTotal.setSelectedTextColor(Color.red);
            }else {JOptionPane.showMessageDialog(null, "wrong data");}
        }
    }
    
    private boolean checkInvoice() {
        
        String hoursWorked = txtHoursWorked.getText().trim();
        String ratingAmount = txtRatingAmount.getText().trim();
        String dicount = txtDiscount.getText().trim();
        String fileFee = txtFilingFee.getText().trim();
        String IdOfClient = txtInvoiceClientId.getText().trim();
        String invoiceNumber = txtInvoiceID.getText();
        
        if (hoursWorked.equals("")) {
            JOptionPane.showMessageDialog(null, "Hours worked on matter missing");
            txtHoursWorked.requestFocusInWindow();
            return false;
        }
        
        if (ratingAmount.equals("")) {
            JOptionPane.showMessageDialog(null, "Rating amount missing");
            txtRatingAmount.requestFocusInWindow();
            return false;
        }
        if (dicount.equals("")) {
            JOptionPane.showMessageDialog(null, "please indicate the discount");
            txtDiscount.requestFocusInWindow();
            return false;
        }
        if (fileFee.equals("")) {
            JOptionPane.showMessageDialog(null, "please charge some filing fee");
            txtFilingFee.requestFocusInWindow();
            return false;
        }
        if (IdOfClient.equals("")) {
            JOptionPane.showMessageDialog(null, "please enter a client ID and click the search icon");
            txtInvoiceClientId.requestFocusInWindow();
            return false;
        }
        if (invoiceNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "enter the client ID to laod the incoice ID");
            txtInvoiceID.requestFocusInWindow();
            return false;
        } else {
            return true;
        }
    }
    
    public void showAll() {
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement sti = con.prepareStatement("select case_type from table_case where emp_id=" + txtInvoiceAssigneedTo.getText());
            ResultSet rsCase = sti.executeQuery();
            if (rsCase.next()) {
                String caseType = rsCase.getString("case_type");
                txtInvoiceCaseType.setText(caseType);
                
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            //JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void retrievClientDetails() {
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select client_fname,client_lname,case_status,assigned_to,invoice_id from newclient where client_natid =" + txtInvoiceClientId.getText());
            ResultSet rs = st.executeQuery();
            boolean emptyRs = true;
            if (rs.next()) {
                emptyRs = false;
                String clientName1 = rs.getString("client_fname");
                txtInvoiceClientFname.setText(clientName1);
                String clientName2 = rs.getString("client_lname");
                txtInvoiceClientLname.setText(clientName2);
                String status1 = rs.getString("case_status");
                txtInvoiceCaseStatus.setText(status1);
                String assigned_to = rs.getString("assigned_to");
                txtInvoiceAssigneedTo.setText(assigned_to);
                String invoiceId1 = rs.getString("invoice_id");
                txtInvoiceID.setText(invoiceId1);
                
            }
            if (emptyRs) {
                JOptionPane.showMessageDialog(null, "No Record Found");
                txtInvoiceClientId.setText("");
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            //JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE); 
        }
        
    }

//entering report data to the database lafus
    public void ReportData() {
        
        String strReportTitle = titleReport.getText();
        String strReportBy = txtEmpId.getText();
        String strDocName = txtDocName.getText().trim();
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            statement = con.createStatement();

            // SQL Insert
            String sql = "INSERT INTO report_table " + "(report_title,report_by,doc_name)"
                    + "VALUES ('" + strReportTitle + "', '"
                    + strReportBy + "', '"
                    + strDocName + "')";
            
            statement.execute(sql);
            JOptionPane.showMessageDialog(null, "New Report of title " + strReportTitle + " has been created successfuly");

            // Reset Text Fields
            titleReport.setText("");
            reportConent.setText("");
            txtDocName.setText("");

            //status  = true;
        } catch (ClassNotFoundException | SQLException | HeadlessException es) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Unable to add Report");
            JOptionPane.showMessageDialog(null, es.getMessage());
        }
        
        try {
            if (statement != null) {
                statement.close();
                con.close();
            }
        } catch (SQLException es) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, es.getMessage());
        }

//end of if statement that validate the form
    }

//display report table
    public void DisplayReportList() {
        
        reportTable.setColumnIdentifiers(reportColumns);
        
        tableReport.setModel(reportTable);
        
        tableReport.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select report_id,report_by,date_created from  report_table");
            ResultSet rs = st.executeQuery();
            
            int i = 0;
            while (rs.next()) {
                reportOne = rs.getString("report_id");
                reportByOne = rs.getString("report_by");
                reportDateOne = rs.getString("date_created");
                
                reportTable.addRow(new Object[]{reportOne, reportByOne, reportDateOne});
                i++;
                
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

//display event table 
    public void DisplayEventList() {
        
        eventTableList.setColumnIdentifiers(eventColumns);
        
        tableEventList.setModel(eventTableList);
        
        tableEventList.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select event_id,event_name,event_date,event_creator from  event_table");
            ResultSet rs = st.executeQuery();
            
            int i = 0;
            while (rs.next()) {
                eventIDone = rs.getString("event_id");
                eventOne = rs.getString("event_name");
                eventDate = rs.getString("event_date");
                eventBy = rs.getString("event_creator");
                
                eventTableList.addRow(new Object[]{eventIDone, eventOne, eventDate, eventBy});
                i++;
            }
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void DisplayUnpaid() {
        
        unpaidlist.setColumnIdentifiers(unpaidColumns);
        
        tableUnpaid.setModel(unpaidlist);
        
        tableUnpaid.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select invoice_id,invoice_date,payment_method,total from client_invoice where paid_notpaid = 'Not yet';");
            ResultSet rs = st.executeQuery();
            
            int i = 0;
            while (rs.next()) {
                unpaidID = rs.getString("invoice_id");
                unpaidDate = rs.getString("invoice_date");
                unpaidMethodPay = rs.getString("payment_method");
                unpaidTotal = rs.getString("total");
                
                unpaidlist.addRow(new Object[]{unpaidID, unpaidDate, unpaidMethodPay, unpaidTotal});
                i++;
            }
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void DisplayPaid() {
        
        paidlist.setColumnIdentifiers(paidColumns);
        
        tablePaidInvoices.setModel(paidlist);
        
        tablePaidInvoices.setFillsViewportHeight(true);
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select invoice_id,invoice_date,payment_method,total from client_invoice where paid_notpaid = 'Paid';");
            ResultSet rs = st.executeQuery();
            
            int i = 0;
            while (rs.next()) {
                paidID = rs.getString("invoice_id");
                paidDate = rs.getString("invoice_date");
                paidMethodPay = rs.getString("payment_method");
                paidTotal = rs.getString("total");
                
                paidlist.addRow(new Object[]{paidID, paidDate, paidMethodPay, paidTotal});
                i++;
            }
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void ProfileInfo() {
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            PreparedStatement st = con.prepareStatement("select username,fname,lname,emp_id,email,practice,date_hired,phone,national_id,dept from employee where emp_id=" + LawFirmHome.userId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
               usernameProf = rs.getString("username");
                usernameProfile.setText(usernameProf);
                
                profAc1 = rs.getString("fname");
                profAc2 = rs.getString("lname");
                labelProfAcName.setText(profAc1 + " " + profAc2);
                
                ProfEmp = rs.getString("emp_id");
                labelProfEmpId.setText(ProfEmp);
                
                profEmail = rs.getString("email");
                labelProfEmail.setText(profEmail);
                
                areaOf = rs.getString("practice");
                labelProfSpecialization.setText(areaOf);
                
                dateHired = rs.getString("date_hired");
                labelProfDate.setText(dateHired);
                
                profPhone = rs.getString("phone");
                lblProfPhone.setText(profPhone);
                
                profNatid = rs.getString("national_id");
                lblProfNatId.setText(profNatid);
                
                profDept = rs.getString("dept");
                lblProfDept.setText(profDept);
            }
            
        } catch (ClassNotFoundException | SQLException | HeadlessException rt) {
            // System.out.println(rt);
            JOptionPane.showMessageDialog(null, rt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void SessionEnd() {
        String userlbl = usernameProfile.getText();
        String use=null;
        //String use1;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
            statement = con.createStatement();
            String sgl=" select  max(signin_time) as try from session_logs";
            
            ResultSet rs=statement.executeQuery(sgl);
                    if(rs.next()){
                       use= rs.getString("try");
                    }
                String sql1="SELECT session_id from session_logs where signin_time='"+use+"'";
                 ResultSet rs1=statement.executeQuery(sql1);
                 if(rs1.next()){
                    use1=rs1.getString("session_id");
                    String sql = "UPDATE session_logs SET signout_time = NOW() where session_id='"+use1+"'";
            
            statement.execute(sql);
                 }
                 else JOptionPane.showMessageDialog(null, "unable to insert");
            // SQL Insert
            
            //status  = true;
        } catch (ClassNotFoundException | SQLException | HeadlessException es) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Log out of reach");
            JOptionPane.showMessageDialog(null, es.getMessage());
        }

//end of if statement that validate the form
    }
    
    public void loadEventDashboard(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
        PreparedStatement cos = con.prepareStatement("select event_name,location,event_date,category from event_table where event_id = (select max(event_id) from event_table)");
        ResultSet rsCos = cos.executeQuery();
        
        int i = 0;
            if (rsCos.next()) {
                String evtName = rsCos.getString("event_name");
                lblEvtName.setText(evtName);
                
                String evtLocation = rsCos.getString("location");
                lblEvtLocation.setText(evtLocation);
                
                String evtDate = rsCos.getString("event_date");
                lblEvtDate.setText(evtDate);
                
                String evtCategory = rsCos.getString("category");
                lblEvtCategory.setText(evtCategory);
            }
            else{}
                
    }catch(ClassNotFoundException | SQLException | HeadlessException es){
    JOptionPane.showMessageDialog(null, es.getMessage());}
    }
    
    public void reportDashboardInfo(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/lafus?useSSL = false", "root", "");
        PreparedStatement cos = con.prepareStatement("select report_title,doc_name from report_table where report_id = (select max(report_id) from report_table)");
        ResultSet rsCos = cos.executeQuery();
        
        PreparedStatement os = con.prepareStatement("select count(report_id) as report_no from report_table");
        ResultSet rsCs = os.executeQuery();
        
        int i = 0;
            if (rsCos.next()) {
                String repoName = rsCos.getString("report_title");
                lblDashLatestrep.setText(repoName);
                
                String docName = rsCos.getString("doc_name");
                lblDashDoc.setText(docName);}
                
            if(rsCs.next()){    
                String repoCount = rsCs.getString("report_no");
                lblDashNoReports.setText(repoCount);}
            
            else{}
                
    }catch(ClassNotFoundException | SQLException | HeadlessException es){
    JOptionPane.showMessageDialog(null, es.getMessage());}
    }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddClientBill;
    private javax.swing.JButton btnAddInvoiceBack;
    private javax.swing.JButton btnBill;
    private javax.swing.JButton btnBillTrends;
    private javax.swing.JButton btnClientClear;
    private javax.swing.JButton btnClientSave;
    private javax.swing.JButton btnClients;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnCreateDocument;
    private javax.swing.JButton btnCreateEvent;
    private javax.swing.JButton btnCreateSchedule;
    private javax.swing.JButton btnDash;
    private javax.swing.JButton btnDiscardEvent;
    private javax.swing.JButton btnDiscardInvoice;
    private javax.swing.JButton btnDiscardReport;
    private javax.swing.JButton btnDiscardSchedule;
    private javax.swing.JButton btnEditProfile;
    private javax.swing.ButtonGroup btnGroupClientGender;
    private javax.swing.JButton btnHelp2;
    private javax.swing.JButton btnManageReports;
    private javax.swing.JButton btnNewInvoice;
    private javax.swing.JButton btnOpenReports;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPreviousBack;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnReciepts;
    private javax.swing.JButton btnRecieptsBack;
    private javax.swing.JButton btnRefreshPaid;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnReportsBack;
    private javax.swing.JButton btnSaveInvoice;
    private javax.swing.JButton btnSchedule;
    private javax.swing.JButton btnSearchClient;
    private javax.swing.JButton btnSearchPreviousInvoice;
    private javax.swing.JButton btnSearchUnpaid;
    private javax.swing.JButton btnShowAllClients;
    private javax.swing.JButton btnSignOutUser;
    private javax.swing.JButton btnTrendsBack;
    private javax.swing.JButton btnUnpaid;
    private javax.swing.JButton btnUnpaidBack;
    private javax.swing.JButton btnUnpaidRefresh;
    private javax.swing.JButton btnUpdateSchedule;
    private javax.swing.JButton btnViewAllEvents;
    private javax.swing.JButton btnViewAllSchedules;
    private javax.swing.JButton btnViewClientDetails;
    private javax.swing.JButton btnViewClientList;
    private javax.swing.JButton btnViewEvent;
    private javax.swing.JButton btnViewReceipt;
    private javax.swing.ButtonGroup buttonGroupPaidUnpaid;
    private javax.swing.JTextField clentAddresField;
    private javax.swing.JTextField clientFname;
    private javax.swing.JTextField clientID;
    private javax.swing.JTextField clientLname;
    private javax.swing.JComboBox<String> comboCaseStatus;
    private javax.swing.JComboBox<String> comboEventCategory;
    private javax.swing.JComboBox<String> comboEventLocation;
    private javax.swing.JComboBox<String> comboPaymentMethod;
    private javax.swing.JComboBox<String> comboPriority;
    private javax.swing.JComboBox<String> comboScheduleLocation;
    private javax.swing.JComboBox<String> comboTask;
    private com.toedter.calendar.JDateChooser dateChooserEvents;
    private com.toedter.calendar.JDateChooser dateChooserFiling;
    private com.toedter.calendar.JDateChooser dateChooserSchedule;
    private com.toedter.calendar.JDayChooser dateChooserUser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBoxCasetype;
    private com.toedter.calendar.JDateChooser jDateChooser_invoice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTree jtreeDocuments;
    private javax.swing.JLabel labelProfAcName;
    private javax.swing.JLabel labelProfClientsAsc;
    private javax.swing.JLabel labelProfDate;
    private javax.swing.JLabel labelProfDisposed;
    private javax.swing.JLabel labelProfEmail;
    private javax.swing.JLabel labelProfEmpId;
    private javax.swing.JLabel labelProfPending;
    private javax.swing.JLabel labelProfSpecialization;
    private javax.swing.JLabel labelUser;
    private javax.swing.JLabel labelWorkid;
    private javax.swing.JLabel lblAllclients;
    private javax.swing.JLabel lblAssignedclients;
    private javax.swing.JLabel lblDashDoc;
    private javax.swing.JLabel lblDashLatestrep;
    private javax.swing.JLabel lblDashNoReports;
    private javax.swing.JLabel lblDueDate;
    private javax.swing.JLabel lblEvtCategory;
    private javax.swing.JLabel lblEvtDate;
    private javax.swing.JLabel lblEvtLocation;
    private javax.swing.JLabel lblEvtName;
    private javax.swing.JLabel lblProfDept;
    private javax.swing.JLabel lblProfNatId;
    private javax.swing.JLabel lblProfPhone;
    private javax.swing.JLabel lblSchPro;
    private javax.swing.JLabel lblSchTitle;
    private javax.swing.JLabel lblSessionId;
    private javax.swing.JButton openFile;
    private javax.swing.JPanel panelAddClients;
    private javax.swing.JPanel panelAddClientsOtherDetails;
    private javax.swing.JPanel panelAddInvoice;
    private javax.swing.JPanel panelBillHome;
    private javax.swing.JPanel panelBilling;
    private javax.swing.JPanel panelCard;
    private javax.swing.JPanel panelClientPsn;
    private javax.swing.JPanel panelClients;
    private javax.swing.JPanel panelCreate;
    private javax.swing.JPanel panelCurrentClients;
    private javax.swing.JPanel panelDashboard;
    private javax.swing.JPanel panelHolder;
    private javax.swing.JPanel panelInvoiceConfirmClient;
    private javax.swing.JPanel panelInvoiceDetails;
    private javax.swing.JPanel panelPrevious;
    private javax.swing.JPanel panelProfile;
    private javax.swing.JPanel panelReciepts;
    private javax.swing.JPanel panelReports;
    private javax.swing.JPanel panelSchedule;
    private javax.swing.JPanel panelTotalInvoice;
    private javax.swing.JPanel panelTrends;
    private javax.swing.JPanel panelUnpaid;
    private javax.swing.JTextField phoneNo;
    private javax.swing.JRadioButton radioNotYet;
    private javax.swing.JRadioButton radioPaid;
    private javax.swing.JRadioButton rdFemale;
    private javax.swing.JRadioButton rdMale;
    private javax.swing.JRadioButton rdOther;
    private javax.swing.JRadioButton rdPreferNotSay;
    private javax.swing.JTextArea reportConent;
    private javax.swing.JTextField searchClient;
    private javax.swing.JPanel signInMainPanel;
    private com.toedter.components.JSpinField spinFieldHours;
    private javax.swing.JTabbedPane tabbedPaneClients;
    private javax.swing.JTable tableClient;
    private javax.swing.JTable tableEventList;
    private javax.swing.JTable tablePaidInvoices;
    private javax.swing.JTable tableReport;
    private javax.swing.JTable tableSchedule;
    private javax.swing.JTable tableUnpaid;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JTextField titleReport;
    private javax.swing.JTextArea txtAreaDescription;
    private javax.swing.JTextField txtCaseID;
    private javax.swing.JTextField txtClientMail;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtDocName;
    private javax.swing.JTextField txtEmpId;
    private javax.swing.JTextField txtEventName;
    private javax.swing.JTextField txtField_invoiceId;
    private javax.swing.JTextField txtFilingFee;
    private javax.swing.JTextField txtHoursWorked;
    private javax.swing.JTextField txtInvoiceAssigneedTo;
    private javax.swing.JTextField txtInvoiceCaseStatus;
    private javax.swing.JTextField txtInvoiceCaseType;
    private javax.swing.JTextField txtInvoiceClientFname;
    private javax.swing.JTextField txtInvoiceClientId;
    private javax.swing.JTextField txtInvoiceClientLname;
    private javax.swing.JTextField txtInvoiceID;
    private javax.swing.JTextField txtInvoiceTotal;
    private javax.swing.JTextField txtRatingAmount;
    private javax.swing.JTextField txtRecieiptSearch;
    private javax.swing.JTextField txtScheduleName;
    private javax.swing.JTextField txtSearchUnpaid;
    private javax.swing.JTextArea txtTaskDesc;
    private javax.swing.JLabel usernameProfile;
    // End of variables declaration//GEN-END:variables
}
