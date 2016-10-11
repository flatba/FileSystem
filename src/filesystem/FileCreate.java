/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author y_hiraba
 */
public class FileCreate {
    public StringBuilder xmlSb;
    public String[] columns;

    // create xml Format
    // １：createPersons
    public StringBuilder createPersons(ArrayList dataList) {
        xmlSb = new StringBuilder();
        xmlSb.append("<?xml version=\"1.0\"?>\n");
        xmlSb.append("<persons>\n");
        xmlSb = createPerson(dataList);
        xmlSb.append("</persons>\n");
        return xmlSb;
    }

    public String[] splitData(String str) {
        String[] columns = str.split(",",-1);
        return columns;
    }

    // ２：createPerson
    public StringBuilder createPerson(ArrayList dataList) {
        for (int i = 0; i < dataList.size(); i++){
                String str = dataList.get(i).toString();
                String[] columns = splitData(str);
                xmlSb.append("        <person>\n");
                xmlSb.append(createPatientInformationTags(columns));
                xmlSb.append("        </person>\n");
            }

        return xmlSb;
    }

    // ３：createTags
    public StringBuilder createPatientInformationTags(String[] columns) {
        StringBuilder xmlSbTmp = new StringBuilder();

        xmlSbTmp.append("            <id>" + columns[0] + "</id>\n");
        xmlSbTmp.append("            <name>" + columns[1] + "</name>\n");
        xmlSbTmp.append("            <sex>" + columns[2] + "</sex>\n");
        xmlSbTmp.append("            <birthday>" + columns[3] + "</birthday>\n");
        xmlSbTmp.append("            <age>" + columns[4] + "</age>\n");
        xmlSbTmp.append("            <date>" + columns[5] + "</date>\n");
        xmlSbTmp.append("            <image>" + columns[6] + "</image>\n");

        return xmlSbTmp;

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
                    String[] columns = splitData(ReadLine);
                    StringBuilder sbTmp = new StringBuilder();
                    sbTmp.append("        <person>\n");
                    sbTmp.append(createPatientInformationTags(columns));
                    sbTmp.append("        </person>\n");
                    sb.append(sbTmp);
                }
                sb.append("</persons>\n");
                System.out.println("指定されたファイルはcsv形式のためxml形式に変換しました。");

            }else if(filePath.lastIndexOf(".xml") > 0) {
                // Read xml Format return StringBuilder
                // xmlが読み込まれた場合は、タグ情報を付与する必要が無いので何もしない。
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
}
