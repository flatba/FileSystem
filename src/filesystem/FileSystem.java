/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package filesystem;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
*　GUI上でCSVファイルの読み書きを行うプログラム
* @author y_hiraba
*/
/**
* @param args the command line arguments
*/

// 透かし文字の処理　普通に入力するときにも文字が透けてしまうのでいったんコメントアウト
//    class JTextFt extends JTextField implements FocusListener {
//      private static final long serialVersionUID = 1L;
//      String helpmsg;
//      String bakstr="";
//      JTextFt(String msg){
//          helpmsg=msg;
//          addFocusListener(this);
//          drawmsg();
//        }
//        void drawmsg() {
//            setForeground(Color.LIGHT_GRAY);
//            setText(helpmsg);
//          }
//        @Override
//        public void focusGained(FocusEvent arg0) {
//            setForeground(Color.BLACK);
//            setText(bakstr);
//        }
//        @Override
//        public void focusLost(FocusEvent arg0) {
//            bakstr=getText();
//            if (bakstr.equals("")) {
//                drawmsg();
//            }
//        }
//    }


class FileSystem extends JFrame {
    // Button
    private JButton selectButton;
    private JButton load;
    private JButton save;
    private JButton imageButton;

    // Item
    private JTextField id;
    private JTextField name;
    private String sex = "";
    private JTextField birthday;
    private JTextField age;
    public String filePath = "";
    public int row;
    public int comboData;
    private JTextField date;
//    private JTextField image;
    private String imagePath;

    // Label
    public JLabel patientInformation;
    public JLabel label;
    public JLabel imageLabel;


    // For dispaly
    public JTextField selected;
    public JTextArea loadField;
    public JTextField selectField;
    public JRadioButton men;
    public JRadioButton woman;

    // Panel
    public JPanel TopPanenl;
    public JPanel CenterPanel;
    public JPanel patientPanel;
    public JPanel patientPanelBase;
    public JPanel patientInformationPanelField;

    // Array
    public ArrayList<PatientInformation> patientInformationArrTmp;
    public ArrayList<String> patientInformationArr;

    // Table
    public JTable loadFieldTable;

    // Model
    public DefaultTableModel patientInformationModel;

    PatientInformation pasteInfo;



    public static void main(String[] args) {
        FileSystem f = new FileSystem();


    }

    // 新規患者入力時、未入力がある場合にエラーダイアログを表示する
//    public void error() {
//        // 条件が良くないので適切な状態で判定ができていない。
//        if(patientInformationArrTmp.indexOf("") >= 0){
//            JOptionPane.showMessageDialog(this, "記入漏れがあります。");
//            patientInformationArrTmp.clear();
//        }
//    }

    public void add(){
        JPanel DialogPanelBase = createDialog();
        int r = JOptionPane.showConfirmDialog(
            FileSystem.this, // オーナーウィンドウ
            DialogPanelBase, // メッセージ
            "追加", // タイトル
            JOptionPane.OK_CANCEL_OPTION,	// オプション（ボタンの種類）
            JOptionPane.QUESTION_MESSAGE	// メッセージタイプ（アイコンの種類）
        );
        dataSet(true);
    }

    public String imagePathCut(String imagePath) {
        int imagePathNum = imagePath.lastIndexOf("\\");
        System.out.println(imagePath.substring(imagePathNum+1, imagePath.length()));
        imagePath = imagePath.substring(imagePathNum+1, imagePath.length());
        return imagePath;
    }

    public void update(){
        JPanel DialogPanelBase = createDialog();

        int selectedRow = getSelectedRow();
        PatientInformation info = this.getTabledRowInfo(selectedRow);

        if(info == null) {
            JOptionPane.showMessageDialog(FileSystem.this, "選択されていません。");
            return;
        }else{
            id.setText(info.getId());
            name.setText(info.getName());
            if(info.getSex().equals("男")){
                men.setSelected(true);
                woman.setSelected(false);
            }else if(info.getSex().equals("女")){
                men.setSelected(false);
                woman.setSelected(true);
            }
            birthday.setText(info.getBirthday());
            age.setText(info.getAge());
            date.setText(info.getDate());
            imagePath = info.getImage();
            imagePath = imagePathCut(imagePath);

            imageLabel.setText(imagePath);
            patientInformationPanelField.add(imageLabel);
        }
        // JFrame, JDialog
        int r = JOptionPane.showConfirmDialog(
            FileSystem.this, // オーナウィンドウ
            DialogPanelBase, // メッセージ（DialogPanelBaseにしておかないと意図した表示にならない）メッセージの代わりにパネルを入れ込んでいる。
            "編集", // タイトル
            JOptionPane.OK_CANCEL_OPTION,	// オプション（ボタンの種類）
            JOptionPane.QUESTION_MESSAGE	// メッセージタイプ（アイコンの種類）
        );
        dataSet(false);
    }

    private void dataSet(boolean check) {
        if(men.isSelected() == true){
            sex = "男";
        }else if(woman.isSelected() == true){
            sex = "女";
        }else if(men.isSelected() == false || woman.isSelected() == false){
            JOptionPane.showMessageDialog(this, "性別を選択してください。");
            return;
        }
        if(check == false){
            int selectedRow = loadFieldTable.getSelectedRow();
            // データの反映 ： update時の処理
            loadFieldTable.setValueAt(id.getText(), selectedRow, 0);
            loadFieldTable.setValueAt(name.getText(), selectedRow, 1);
            loadFieldTable.setValueAt(sex, selectedRow, 2);
            loadFieldTable.setValueAt(birthday.getText().replaceAll("-", "/"), selectedRow, 3);
            loadFieldTable.setValueAt(age.getText(), selectedRow, 4);
            loadFieldTable.setValueAt(date.getText(), selectedRow, 5);
            imagePath = imagePathCut(imagePath);
            loadFieldTable.setValueAt(imagePath, selectedRow, 6);

        }else{
            // データの反映 : add時の処理
            ArrayList<String> ret = new ArrayList<>();
            ret.add(id.getText());
            ret.add(name.getText());
            ret.add(sex);
            ret.add(birthday.getText().replaceAll("-", "/"));
            ret.add(age.getText());
            ret.add(date.getText());
            ret.add(imagePath);
            DefaultTableModel model = (DefaultTableModel)loadFieldTable.getModel();
            model.addRow(ret.toArray());
        }
    }

    private JPanel createDialog() {
        JPanel DialogPanelBase = new JPanel();
        JPanel DialogPanel = new JPanel();
        JPanel patientInformationPanelItem = new JPanel();
        patientInformationPanelField = new JPanel();

        DialogPanel.setLayout(new GridLayout(1, 2));
        patientInformationPanelItem.setLayout(new GridLayout(7,1));
        patientInformationPanelField.setLayout(new GridLayout(7,1));

        JLabel idLabel = new JLabel("患者ID");
        JLabel nameLabel = new JLabel("氏名");
        JLabel sexLabel = new JLabel("性別");
        JLabel birthdayLabel = new JLabel("生年月日");
        JLabel ageLabel = new JLabel("年齢");
        JLabel dateLabel = new JLabel("追加日");
        imageLabel = new JLabel();

        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sexLabel.setHorizontalAlignment(SwingConstants.CENTER);
        birthdayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        patientInformationPanelItem.add(idLabel);
        patientInformationPanelItem.add(nameLabel);
        patientInformationPanelItem.add(sexLabel);
        patientInformationPanelItem.add(birthdayLabel);
        patientInformationPanelItem.add(ageLabel);
        patientInformationPanelItem.add(dateLabel);
        imageButton = new JButton("写真");
        patientInformationPanelItem.add(imageButton);
        // imageボタンにdialogの表示

        id = new JTextField("", 20);
        patientInformationPanelField.add(id);
        name = new JTextField("", 20);
        patientInformationPanelField.add(name);
        JPanel SexPanel = new JPanel();
        SexPanel.setLayout(new GridLayout(1,2));
        ButtonGroup sexGroup = new ButtonGroup();
        men = new JRadioButton("男性");
        woman = new JRadioButton("女性");
        sexGroup.add(men);
        sexGroup.add(woman);
        SexPanel.add(men);
        SexPanel.add(woman);
        patientInformationPanelField.add(SexPanel);
        birthday = new JTextField("", 20);
        patientInformationPanelField.add(birthday);
        age = new JTextField("", 20);
        patientInformationPanelField.add(age);
        date = new JTextField("", 20);
        patientInformationPanelField.add(date);

        DialogPanel.add(patientInformationPanelItem);
        DialogPanel.add(patientInformationPanelField);
        DialogPanelBase.add(DialogPanel);

        // Dialog用のOverride
        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filechooser = new JFileChooser("C:\\Users\\y_hiraba\\Documents\\tmp\\images");
                filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("jpgファイル(*.jpg)", "jpg", "jpeg");
                filechooser.addChoosableFileFilter(filter);

                int selected = filechooser.showOpenDialog(FileSystem.this);

                if (selected == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    JLabel selectedLabel = new JLabel();
                    selectedLabel.setText(file.getAbsolutePath());
                    imagePath = file.getAbsolutePath();
                    String imagePathCut = imagePathCut(imagePath);
                    System.out.println(imagePath);
                    imageLabel.setText(imagePathCut);
                    patientInformationPanelField.add(imageLabel);
                    patientInformationPanelField.updateUI();

                }else if (selected == JFileChooser.CANCEL_OPTION){
                    System.out.println("キャンセルされました");
                }else if (selected == JFileChooser.ERROR_OPTION){
                    System.out.println("エラー又は取消しがありました");
                }
            }
        });

        return DialogPanelBase;
    }

    public PatientInformation copy() {
       return getSelectedTableRow();
    }

    public void paste(PatientInformation pasteInfo) {
        int selectedRow = getSelectedRow();
        ArrayList<String> ret = new ArrayList<>();
        ret.add(pasteInfo.getId());
        ret.add(pasteInfo.getName());
        ret.add(pasteInfo.getSex());
        ret.add(pasteInfo.getBirthday());
        ret.add(pasteInfo.getAge());
        ret.add(pasteInfo.getDate());
        ret.add(pasteInfo.getImage());
        DefaultTableModel model = (DefaultTableModel)loadFieldTable.getModel();
        model.insertRow(selectedRow + 1 , ret.toArray());
    }

    public void copyPaste(boolean check){
        if(check == true){
            pasteInfo = new PatientInformation();
            pasteInfo = copy();
        }else{
            paste(pasteInfo);
        }
    }

    // セル内のパスを取得して画像を別ウィンドウで表示する
    public void imageOpen(String imagePath) {
        JFrame frame = new JFrame("画像表示");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        imagePath = "C:\\Users\\y_hiraba\\Documents\\tmp\\images\\" + imagePath;
        ImageIcon icon = new ImageIcon(imagePath);
        JLabel label = new JLabel(icon);
        frame.getContentPane().add(label);
        frame.setVisible(true);
    }

    public void imageSelect(){
        JPanel DialogPanelBase = createDialog();
        int r = JOptionPane.showConfirmDialog(
            FileSystem.this, // オーナーウィンドウ
            DialogPanelBase, // メッセージ
            "画像の選択", // タイトル
            JOptionPane.OK_CANCEL_OPTION,	// オプション（ボタンの種類）
            JOptionPane.QUESTION_MESSAGE	// メッセージタイプ（アイコンの種類）
        );
    }

    public FileSystem(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ファイルの読み書き");
        setBounds(200, 100, 680, 700); // 出力場所 (x, y, width, heigft);

        patientInformationArr = new ArrayList();

        // Top Panelの設定-------------------------------------------------------
        JPanel TopPanel = new JPanel();
        patientPanelBase = new JPanel();
        patientPanel = new JPanel();
        JPanel ButtonPanel1 = new JPanel();
        TopPanel.setPreferredSize(new Dimension(600, 150));

        patientPanelBase.setPreferredSize(new Dimension(505, 85));
        patientPanelBase.setBackground(new Color(232,226,232)); // set Color
        patientPanelBase.setLayout(new FlowLayout(FlowLayout.CENTER));
        patientPanel.setPreferredSize(new Dimension(400, 80));
        patientPanel.setBackground(new Color(232,226,232)); // set Color
        patientPanel.setLayout(new GridLayout(2,3));

//        ButtonPanel1.setBackground(new Color(140,140,140)); // set Color
//        ButtonPanel2.setBackground(new Color(140,140,140)); // set Color
//        TopPanel.setBackground(new Color(30,232,203)); // set Color

        selectField = new JTextField(filePath, 30);
        TopPanel.add(selectField);
        selectButton = new JButton("選択...");
        TopPanel.add(selectButton);

        String[] charComboData = {"SJIS", "UTF-8"};
        JComboBox charCode = new JComboBox(charComboData);
        TopPanel.add(charCode);

        TopPanel.add(patientPanelBase);

        load = new JButton("読み込み");
        save = new JButton("保存");
        ButtonPanel1.add(load);
        ButtonPanel1.add(save);

        TopPanel.add(ButtonPanel1);
        // Top Panelの設定-------------------------------------------------------

        // Center Panelの設定----------------------------------------------------
        CenterPanel = new JPanel();
        loadFieldTable = new JTable();
        loadFieldTable.setDefaultEditor(Object.class, null); // セルの編集不可
//        loadFieldTable.setAutoCreateRowSorter(true); // ソート機能 int型に対応していないのでまだ使えない。
        loadFieldTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane loadFieldTableScollpane = new JScrollPane(loadFieldTable);
        loadFieldTableScollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        CenterPanel.add(loadFieldTableScollpane);
//        CenterPanel.setBackground(new Color(30,232,203)); // set Color
        // Center Panelの設定----------------------------------------------------

        // ポップアップ（右クリックメニュー）の設定----------------------------------
        JPopupMenu submenuPopup;
        submenuPopup = new JPopupMenu();
        JMenuItem addMenuItem = new JMenuItem("追加");
        JMenuItem updateMenuItem = new JMenuItem("編集");
        JMenuItem copyMenuItem = new JMenuItem("コピー");
        JMenuItem pasteMenuItem = new JMenuItem("貼り付け");
        JMenuItem deleteMenuItem = new JMenuItem("削除");

        submenuPopup.add(addMenuItem);
        submenuPopup.add(updateMenuItem);
        submenuPopup.addSeparator();
        submenuPopup.add(copyMenuItem);
        submenuPopup.add(pasteMenuItem);
        submenuPopup.addSeparator();
        submenuPopup.add(deleteMenuItem);
        // ポップアップ（右クリックメニュー）の設定----------------------------------

        // BottomPanelの設定-----------------------------------------------------
        JPanel BottomPanel = new JPanel();
//        BottomPanel.setBackground(new Color(30,232,203)); // set Color
        // BottomPanelの設定-----------------------------------------------------

        // 各Panelの表示---------------------------------------------------------
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(TopPanel);
        contentPane.add(CenterPanel);
        contentPane.add(submenuPopup);
        contentPane.add(BottomPanel);
        // 各Panelの表示---------------------------------------------------------

        // カラム設定------------------------------------------------------------
        final String[] columnNames = {"ID", "氏名", "性別", "生年月日", "年齢", "追加日", "写真"}; // カラムを増やしたければここに追加するだけ
        patientInformationModel = new DefaultTableModel(columnNames, 0);
        loadFieldTable.setModel(patientInformationModel);
        // カラムの幅設定
        loadFieldTable.getColumn("ID").setPreferredWidth(30);
        loadFieldTable.getColumn("氏名").setPreferredWidth(150);
        loadFieldTable.getColumn("性別").setPreferredWidth(50);
        loadFieldTable.getColumn("生年月日").setPreferredWidth(150);
        loadFieldTable.getColumn("年齢").setPreferredWidth(50);
        loadFieldTable.getColumn("追加日").setPreferredWidth(150);
        loadFieldTable.getColumn("写真").setPreferredWidth(150);
        // カラム設定------------------------------------------------------------

        // ボタン設定------------------------------------------------------------
        FileAccess fa = new FileAccess();
        FileCreate fc = new FileCreate();

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filechooser = new JFileChooser("C:\\Users\\y_hiraba\\Documents\\tmp");
                filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("xmlファイル(*.xml)", "xml");
                filechooser.addChoosableFileFilter(filter);

                int selected = filechooser.showOpenDialog(FileSystem.this);

                if (selected == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    JLabel selectedLabel = new JLabel();
                    selectedLabel.setText(file.getAbsolutePath());
                    filePath = file.getAbsolutePath();
                    selectField.setText(filePath);
                }else if (selected == JFileChooser.CANCEL_OPTION){
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("キャンセルされました"));
                    BottomPanel.updateUI();
                }else if (selected == JFileChooser.ERROR_OPTION){
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("エラー又は取消しがありました"));
                    BottomPanel.updateUI();
                }
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 文字コード判別用
                comboData = charCode.getSelectedIndex();

                ArrayList<PatientInformation> convertXmlPatientInformationData = new ArrayList();
                StringBuilder convertXmlData = new StringBuilder();

                // 選択されたファイル形式の識別
                if(filePath.equals("")) {
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("ファイルが指定されていません")); // error message : not selected file
                    BottomPanel.updateUI();
                    return;
                }else if(filePath.lastIndexOf(".csv") > 0 || filePath.lastIndexOf(".xml") > 0){
                    if(filePath.lastIndexOf(".csv") > 0) { // output csv Format：csvフォーマットの展開
                    // read CSV Format：fileのディレクトリパスと文字コード情報を入れるとArrayList型の患者情報が返ってくる
                    // ArrayList<PatientInformation> data = new ArrayList();
                    // data = fa.readCsvFormat(filePath, comboData);

                    // Convert CSV to XML Format
                    convertXmlData = fc.convertXmlFormat(filePath, comboData);

                }else if(filePath.lastIndexOf(".xml") > 0) {
                    convertXmlData = fc.convertXmlFormat(filePath, comboData);
                }

                    // output xml Format：xmlフォーマットの展開
                    try {
                        if(filePath.lastIndexOf(".csv") > 0) {
                            convertXmlPatientInformationData = fa.readXmlFormat(convertXmlData, null);
                        }else if(filePath.lastIndexOf(".xml") > 0) {
                            convertXmlPatientInformationData = fa.readXmlFormat(null, filePath);
                        }

                        for (int row = 0; row < convertXmlPatientInformationData.size(); row++) {
                            PatientInformation info = convertXmlPatientInformationData.get(row);
                            int rc = patientInformationModel.getRowCount();
                            patientInformationModel.addRow(new Object[] {rc});
                            patientInformationModel.setValueAt(info.getId(), row, PatientInformation.COLUMN_ID);
                            patientInformationModel.setValueAt(info.getName(), row, PatientInformation.COLUMN_NAME);
                            patientInformationModel.setValueAt(info.getSex(), row, PatientInformation.COLUMN_SEX);
                            patientInformationModel.setValueAt(info.getBirthday(), row, PatientInformation.COLUMN_BIRTHDAY);
                            patientInformationModel.setValueAt(info.getAge(), row, PatientInformation.COLUMN_AGE);
                            patientInformationModel.setValueAt(info.getDate(), row, PatientInformation.COLUMN_DATE);
                            patientInformationModel.setValueAt(info.getImage(), row, PatientInformation.COLUMN_IMAGE);
                        }
                        loadFieldTable.setModel(patientInformationModel);

                        BottomPanel.removeAll();
                        BottomPanel.add(new JLabel("ファイルの読み込みが完了しました。"));
                        BottomPanel.updateUI();

                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (XPathExpressionException ex) {
                        Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }else{
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("ファイル形式が不明です。")); // error message : not selected file
                    BottomPanel.updateUI();
                    return;
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filechooser = new JFileChooser("C:\\Users\\y_hiraba\\Documents\\tmp");
                filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int selected = filechooser.showSaveDialog(FileSystem.this);

                if (selected == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    filePath = file.getAbsolutePath();

                    ArrayList dataList = new ArrayList<>();
                    ArrayList<PatientInformation> infoList = getTableItems();
                    for(int i = 0; i < infoList.size(); i++) {
                        PatientInformation info = infoList.get(i);
                        dataList.add(info.convertCsvFormat());
                    }

                    // fa.writeCsv(filePath, dataList); // csvの書き出しにはこの関数を使う
                    fa.writeXml(dataList, filePath);
//                    System.out.println("出力しました。");

                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("ファイルの出力が完了しました。"));
                    BottomPanel.updateUI();


                }else if (selected == JFileChooser.CANCEL_OPTION){
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("キャンセルされました"));
                    BottomPanel.updateUI();
                }else if (selected == JFileChooser.ERROR_OPTION){
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("エラー又は取消しがありました"));
                    BottomPanel.updateUI();
                }
            }
        });

        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyPaste(true);
                BottomPanel.removeAll();
                BottomPanel.add(new JLabel("選択された行をコピーしました。"));
                BottomPanel.updateUI();
            }
        });

        pasteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyPaste(false);
                BottomPanel.removeAll();
                BottomPanel.add(new JLabel("行の最後尾に追加しました。"));
                BottomPanel.updateUI();
            }
        });

        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectvalues[] = {"選択した行を削除","すべてを削除","取消し"};
                int option = JOptionPane.showOptionDialog(FileSystem.this,
                    "削除しますか？",
                    "警告",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    selectvalues,
                    selectvalues[0]
                );

                if (option == JOptionPane.YES_OPTION) {
                    // 選択している行のみ削除
                    int[] selection = loadFieldTable.getSelectedRows();
                    int selectionRow = loadFieldTable.getSelectedColumn();
                    System.out.print(selectionRow);
                    if( selectionRow < 0 ) {
                        BottomPanel.removeAll();
                        BottomPanel.add(new JLabel("選択されていません。"));
                        BottomPanel.updateUI();
                        return;
                    }
                    for (int i = selection.length - 1; i >= 0; i--) {
                        patientInformationModel.removeRow(loadFieldTable.convertRowIndexToModel(selection[i]));
                    }
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("選択した行を削除しました。"));
                    BottomPanel.updateUI();
                }else if(option == JOptionPane.NO_OPTION) {
                    // すべての列を削除
                    int loadFieldTableNUM = loadFieldTable.getRowCount();
                    for (int i = loadFieldTableNUM - 1; i >= 0; i--) {
                        patientInformationModel.removeRow(i);
                    }
                    patientPanel.removeAll();
                    patientPanel.updateUI();
                    BottomPanel.removeAll();
                    BottomPanel.add(new JLabel("リストをすべて削除しました。"));
                    BottomPanel.updateUI();
                }else if (option == JOptionPane.CLOSED_OPTION){
                    // 選択なしで×を押してダイアログ終了の処理
                }
            }
        });

        updateMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });



        // MouseEvent ----------------------------------------------------------
        loadFieldTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int btn = e.getButton();
                if (btn == MouseEvent.BUTTON1){
//                    System.out.println("左ボタンクリック");
                    if (e.getClickCount() == 1) {
                        patientPanel.removeAll();
                        PatientInformation info = getSelectedTableRow();

                        JLabel idLabel = new JLabel();
                        idLabel.setText("患者ID：" + info.getId());
                        patientPanel.add(idLabel);
                        JLabel nameLabel = new JLabel();
                        nameLabel.setText("氏名：" + info.getName());
                        patientPanel.add(nameLabel);
                        JLabel sexLabel = new JLabel();
                        sexLabel.setText("性別：" + info.getSex());
                        patientPanel.add(sexLabel);
                        JLabel birthdayLabel = new JLabel();
                        birthdayLabel.setText("生年月日：" + info.getBirthday());
                        patientPanel.add(birthdayLabel);
                        JLabel ageLabel = new JLabel();
                        ageLabel.setText("年齢：" + info.getAge());
                        patientPanel.add(ageLabel);
                        JLabel dateLabel = new JLabel();
                        dateLabel.setText("追加日：" + info.getDate());
                        patientPanel.add(dateLabel);
                        // 写真のパスは特に表示する必要はない

                        patientPanelBase.add(patientPanel);
                        patientPanel.updateUI();

                    }else if(e.getClickCount() == 2){

                        // セル位置を取得
                        int cellRow = loadFieldTable.getSelectedRow();
                        int cellCol = loadFieldTable.getSelectedColumn();

                        if(cellCol <= 5) {
                            update();
                        }else if(cellCol == 6){
//                            System.out.println("行" + cellRow + "::" + "列" + cellCol);
//                            System.out.println(loadFieldTable.getValueAt(cellRow, cellCol));
                            String cellVal = loadFieldTable.getValueAt(cellRow, cellCol).toString(); // クリックされたセル内の値をString型で取得
                            imageOpen(cellVal);
                        }
                    }
                }
            }
            public void mouseReleased(MouseEvent e){
              showPopup(e);
            }

            public void mousePressed(MouseEvent e){
              showPopup(e);
            }

            private void showPopup(MouseEvent e){
              if (e.isPopupTrigger()) {
                /* ポップアップメニューを表示させる */
                submenuPopup.show(e.getComponent(), e.getX(), e.getY());

              }
            }
        });

        setVisible(true);

    }
    // 行番号の取得
    private int getSelectedRow() {
        return loadFieldTable.getSelectedRow();
    }

    // 指定した行の情報を取得する
    private PatientInformation getTabledRowInfo(int row) {
        PatientInformation ret = null;
        if(row >= 0) {
            ret = new PatientInformation();
            ret.setId((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_ID));
            ret.setName((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_NAME));
            ret.setSex((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_SEX));
            ret.setBirthday((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_BIRTHDAY));
            ret.setAge((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_AGE));
            ret.setDate((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_DATE));
            ret.setImage((String)loadFieldTable.getValueAt(row, PatientInformation.COLUMN_IMAGE));
        }
//        System.out.println(ret);
        return ret;
    }

    // 選択している行を含んで行の情報を取得する
    private PatientInformation getSelectedTableRow() {
        return getTabledRowInfo(getSelectedRow());
    }

    // テーブルにあるすべての情報を行ごとに取得して返す
    private ArrayList<PatientInformation> getTableItems() {
        ArrayList<PatientInformation> ret = new ArrayList<>();
        for(int row = 0; row < loadFieldTable.getRowCount(); row++) {
            PatientInformation info = getTabledRowInfo(row);
            ret.add(info);
        }
        return ret;
    }
}
