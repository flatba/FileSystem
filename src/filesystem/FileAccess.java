/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author y_hiraba
 */

public class FileAccess {

    public StringBuilder xmlSb;
//    public String[] columns = null;
    public String[] columns;

    // Read CSV Format File
    public ArrayList<PatientInformation> readCsvFormat(String Path, int combo) {

        ArrayList<PatientInformation> columnsCsvArr = new ArrayList();

        try {
            File file = new File("");

            if(Path != "") {
                    file = new File(Path);
            }

            FileInputStream input = new FileInputStream(file);
            InputStreamReader stream = new InputStreamReader(input,"SJIS");

            if(combo == 0) {
                stream = new InputStreamReader(input,"SJIS");
            }else if(combo == 1) {
                stream = new InputStreamReader(input,"UTF-8");
            }

            BufferedReader buffer = new BufferedReader(stream);
            String ReadLine;

            while ((ReadLine = buffer.readLine()) != null) {
                byte[] b = ReadLine.getBytes();
                ReadLine = new String(b, "UTF-8");
                String[] columns = ReadLine.split(",",-1);
                PatientInformation info = new PatientInformation();
                info.setId(columns[PatientInformation.COLUMN_ID]);
                info.setName(columns[PatientInformation.COLUMN_NAME]);
                info.setSex(columns[PatientInformation.COLUMN_SEX]);
                info.setBirthday(columns[PatientInformation.COLUMN_BIRTHDAY]);
                info.setAge(columns[PatientInformation.COLUMN_AGE]);
                info.setDate(columns[PatientInformation.COLUMN_DATE]);
                columnsCsvArr.add(info);
            }

            input.close();
            stream.close();
            buffer.close();

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return columnsCsvArr;

    }


    // Write CSV Format File
    public void writeCsv(String filePath, ArrayList<String> dataList) {

        File outputFile = new File(filePath);

        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            PrintWriter pw = new PrintWriter(osw);

            for(String data : dataList) {
                pw.println(data);
            }

            System.out.println("ファイルの出力が完了しました。");
            pw.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    // Convert CSV to XML Format File：コンバートと読み込み時にも使用
    public StringBuilder convertXmlFormat(String filePath, int combo) {

        StringBuilder sb = new StringBuilder();

        try {
            File file = new File("");

            if(!"".equals(filePath)) {
                file = new File(filePath);
            }


            FileInputStream input = new FileInputStream(file);
            InputStreamReader stream = new InputStreamReader(input,"SJIS");
            if(combo == 0) {
                stream = new InputStreamReader(input,"SJIS");
            }else if(combo == 1) {
                stream = new InputStreamReader(input,"UTF-8");
            }
            BufferedReader buffer = new BufferedReader(stream);
            String ReadLine;

            if(filePath.lastIndexOf(".csv") > 0) {
                // make xml format

                sb.append("<?xml version=\"1.0\"?>\n<persons>\n");
                while ((ReadLine = buffer.readLine()) != null) {
                    // 本来は変換が必要だが、java内部でUTF-8の変換をやってくれている。
                    // 何も指定しないと基本UTF-8と考える（デフォルト）。
//                    byte[] b = ReadLine.getBytes();
//                    ReadLine = new String(b, "UTF-8");
                    // ReadLine = "患者ID,氏名,性別,生年月日,年齢,追加日"
                    String[] columns = ReadLine.split(",",-1);
                    // readLine() : １行ずつ読み込みを行っている
                    // line 1 : columns[] = { "患者ID", "氏名", "性別", "生年月日", "年齢", "追加日" }
                    // line 2 : columns[] = { "患者ID", "氏名", "性別", "生年月日", "年齢", "追加日" }
                    // line n : ...
                    // convert XML Format

                    //personの切り出しができたら、入れていくようにする。

                    sb.append("<person>\n<id>");
                    sb.append(columns[0]); // id
                    sb.append("</id>\n<name>");
                    sb.append(columns[1]); //name
                    sb.append("</name>\n<sex>");
                    sb.append(columns[2]); // sex
                    sb.append("</sex>\n<birthday>");
                    sb.append(columns[3]); //birthday
                    sb.append("</birthday>\n<age>");
                    sb.append(columns[4]); // age
                    sb.append("</age>\n<date>");
                    sb.append(columns[5]); // date
                    sb.append("</date>\n</person>\n");
                }

                sb.append("</persons>\n");
                System.out.println("指定されたファイルはcsv形式のためxml形式に変換しました。");

            }else if(filePath.lastIndexOf(".xml") > 0) {
                // Read xml Format return StringBuilder
                // xmlが読み込まれた場合は、タグ情報を付与する必要が無いのでこちらの何もしない処理をする。
            }

            input.close();
            stream.close();
            buffer.close();

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb;

    }


    // Read XML Format File
    public ArrayList readXmlFormat(StringBuilder convertXmlData, String filePath) throws ParserConfigurationException, SAXException, XPathExpressionException {
        ArrayList<PatientInformation> xmlElement = new ArrayList();
        StringBuilder sb = new StringBuilder();
        Document document = null;

        try{
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            if(convertXmlData != null) { // 読み込まれたファイルがcsvの場合xml形式に変換した文字列を渡してきている
                sb = convertXmlData;
                document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(sb.toString().getBytes("UTF-8"))));
            }else if(filePath != null) { // 読み込まれたファイルがxmlの場合filePathを渡してきている
                document = documentBuilder.parse(filePath); // ファイルディレクトリパスから直接ファイルを読み込む
            }

            NodeList list = document.getElementsByTagName("person");
            XPathFactory xpfactory = XPathFactory.newInstance();
            XPath xpath = xpfactory.newXPath();

            for (int i = 0; i < list.getLength(); i++) {
                PatientInformation info = new PatientInformation();

                Node pNode = list.item(i);
                Node node = (Node)xpath.evaluate("id", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setId(node.getFirstChild().getNodeValue());
                }
                node = (Node)xpath.evaluate("name", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setName(node.getFirstChild().getNodeValue());
                }
                node = (Node)xpath.evaluate("sex", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setSex(node.getFirstChild().getNodeValue());
                }
                node = (Node)xpath.evaluate("birthday", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setBirthday(node.getFirstChild().getNodeValue());
                }
                node = (Node)xpath.evaluate("age", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setAge(node.getFirstChild().getNodeValue());
                }
                node = (Node)xpath.evaluate("date", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setDate(node.getFirstChild().getNodeValue());
                }

                xmlElement.add(info);

            }

        } catch (FileNotFoundException ex) {
//            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        return xmlElement;

    }

    // create xml Format
    // １：createPersonsの切り出し
    public StringBuilder createPersons(ArrayList dataList) {
        xmlSb = new StringBuilder();
        xmlSb.append("<?xml version=\"1.0\"?>\n");
        xmlSb.append("<persons>\n");
        xmlSb = createPerson(dataList);
        xmlSb.append("</persons>\n");
        return xmlSb;
    }

    // ２：createPersonの切り出し
    public StringBuilder createPerson(ArrayList dataList) {
        for (int i = 0; i < dataList.size(); i++){
                String str = dataList.get(i).toString();
                String[] columns = str.split(",",-1);
                xmlSb.append("        <person>\n");
                xmlSb.append(createPatientInformationTags(columns));
                xmlSb.append("        </person>\n");
            }

        return xmlSb;
    }

    // ３：createTagsの切り出し
    public StringBuilder createPatientInformationTags(String[] columns) {
        StringBuilder xmlSbTmp = new StringBuilder();

        xmlSbTmp.append("            <id>" + columns[0] + "</id>\n");
        xmlSbTmp.append("            <name>" + columns[1] + "</name>\n");
        xmlSbTmp.append("            <sex>" + columns[2] + "</sex>\n");
        xmlSbTmp.append("            <birthday>" + columns[3] + "</birthday>\n");
        xmlSbTmp.append("            <age>" + columns[4] + "</age>\n");
        xmlSbTmp.append("            <date>" + columns[5] + "</date>\n");

        return xmlSbTmp;

    }

    // ４：クラスに切り分け

    // Write XML Format File
    public void writeXml(ArrayList dataList, String filePath){
            // 保存先のファイルパス
            File outputFile = new File("");

            if(!"".equals(filePath)) {
                outputFile = new File(filePath);
            }

            StringBuilder xmlSb = new StringBuilder();
            xmlSb = createPersons(dataList);

            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                PrintWriter pw = new PrintWriter(osw);
                pw.println(xmlSb);
                pw.close();
                System.out.println("ファイルの出力が完了しました。");

            } catch(Exception e) {
                e.printStackTrace();
            }
    }


}