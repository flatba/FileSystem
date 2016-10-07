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

    FileCreate fc = new FileCreate();

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



    // Write XML Format File
    public void writeXml(ArrayList dataList, String filePath){
            // 保存先のファイルパス
            File outputFile = new File("");

            if(!"".equals(filePath)) {
                outputFile = new File(filePath);
            }

            StringBuilder xmlSb = new StringBuilder();
            xmlSb = fc.createPersons(dataList);

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