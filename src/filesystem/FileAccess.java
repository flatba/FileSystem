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


    // Convert CSV to XML Format File
    public StringBuilder convertXmlFormat(String FilePath, int combo) {

        StringBuilder sb = new StringBuilder();

        try {

            File file = new File("");

            if(!"".equals(FilePath)) {
                file = new File(FilePath);
            }

            // 上のほうでcsvかxmlの条件分岐は行ってしまうので、ｘｍｌを読み込む処理に書き換える。2016/10/05_csvの読み込みのみ対応
            // read csv
            FileInputStream input = new FileInputStream(file);
            InputStreamReader stream = new InputStreamReader(input,"SJIS");
            if(combo == 0) {
                stream = new InputStreamReader(input,"SJIS");
            }else if(combo == 1) {
                stream = new InputStreamReader(input,"UTF-8");
            }
            BufferedReader buffer = new BufferedReader(stream);
            String ReadLine;

            // make xml format
            sb.append("<?xml version=\"1.0\"?>\n<persons>\n");
            while ((ReadLine = buffer.readLine()) != null) {
                byte[] b = ReadLine.getBytes();
                ReadLine = new String(b, "UTF-8");
                // ReadLine = "患者ID,氏名,性別,生年月日,年齢,追加日"
                String[] columns = ReadLine.split(",",-1);
                // readLine() : １行ずつ読み込みを行っている
                // line 1 : columns[] = { "患者ID", "氏名", "性別", "生年月日", "年齢", "追加日" }
                // line 2 : columns[] = { "患者ID", "氏名", "性別", "生年月日", "年齢", "追加日" }
                // line n : ...
                // convert XML Format
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




    // xmlファイルの書き出し
    public void writeXml(ArrayList dataList){
            // 保存先のファイルパス
            File outputFile = new File("C:\\Users\\y_hiraba\\Documents\\tmp\\tmp.xml");

            String str = dataList.get(0).toString();
            String[] columns = str.split(",",-1);

            StringBuilder xmlsb = new StringBuilder();

            // make xml format
            xmlsb.append("<?xml version=\"1.0\"?>\n<persons>\n");
            for (int i = 0; i < dataList.size(); i++){
                xmlsb.append("<person>\n<id>");
                xmlsb.append(columns[0]); // id
                xmlsb.append("</id>\n<name>");
                xmlsb.append(columns[1]); //name
                xmlsb.append("</name>\n<sex>");
                xmlsb.append(columns[2]); // sex
                xmlsb.append("</sex>\n<birthday>");
                xmlsb.append(columns[3]); //birthday
                xmlsb.append("</birthday>\n<age>");
                xmlsb.append(columns[4]); // age
                xmlsb.append("</age>\n<date>");
                xmlsb.append(columns[5]); // date
                xmlsb.append("</date>\n</person>\n");
            }
            xmlsb.append("</persons>\n");

            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                PrintWriter pw = new PrintWriter(osw);
                pw.println(xmlsb);
                pw.close();
                System.out.println("ファイルの出力が完了しました。");

            } catch(Exception e) {

                e.printStackTrace();

            }
    }


    // Read XML Format File
    public ArrayList readXmlFormat(StringBuilder convertXmlData) throws ParserConfigurationException, SAXException, XPathExpressionException {
//        受け取っているデータは、
//        convertXmlData：StringBuilder型のxml形式に変換された患者情報の文字列

        ArrayList<PatientInformation> xmlElement = new ArrayList();

        try{

            StringBuilder sb = new StringBuilder();
            sb = convertXmlData;

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(sb.toString().getBytes("UTF-8"))));
            NodeList list = document.getElementsByTagName("person");

            XPathFactory xpfactory = XPathFactory.newInstance();
            XPath xpath = xpfactory.newXPath();

            // いま読み込んでいるCSVファイルに0番目がないから1始まりにしている（ﾌｧｲﾙを直したら下記も修正する）
            for (int i = 0; i < list.getLength(); i++) {
                PatientInformation info = new PatientInformation();

                Node pNode = list.item(i);

                Node node = (Node)xpath.evaluate("id", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setId(node.getFirstChild().getNodeValue());
//                    System.out.println(info);
                }
                node = (Node)xpath.evaluate("name", pNode, XPathConstants.NODE);
                if(node != null) {
                    info.setName(node.getFirstChild().getNodeValue());
//                    System.out.println(info);
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


}