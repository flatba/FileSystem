/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author y_hiraba
 */

public class FileAccess {

    public ArrayList<PatientInformation> readFile(String Path, int combo) {
        ArrayList<PatientInformation> columnsArr;
        columnsArr = new ArrayList();

        // Read CSV Format File-------------------------------------------------
        try {
            File file = new File("C:\\Users\\y_hiraba\\Documents");
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
                columnsArr.add(info);
            }

            input.close();
            stream.close();
            buffer.close();

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return columnsArr;

    }

    public void writeFile(String filePath, ArrayList<String> dataList) {
        File outputFile = new File(filePath);
        System.out.println(filePath);
        try {
            // 出力ストリームの生成
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            PrintWriter pw = new PrintWriter(osw);
            for(String data : dataList) {
                pw.println(data);
            }
//            pw.println("ファイル入出力が完了しました。");
            System.out.println("ファイルの出力が完了しました。");
            pw.close();
        } catch(Exception e) {
            e.printStackTrace(); // エラーがあった場合は、スタックトレースを出力
        }
    }
    // Read CSV Format File-----------------------------------------------------------

    // Convert CSV to XML Format File-------------------------------------------------
    public StringBuilder convertXmlFormat(String Path, int combo) {
        // TODO code application logic here
        StringBuilder sb = new StringBuilder();

        try {
            File file = new File("C:\\Users\\y_hiraba\\Documents");
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


            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<root>\n<persons>\n");
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
                sb.append("    <person>\n        <id>");
                sb.append(columns[0]); // id
                sb.append("</id>\n        <name>");
                sb.append(columns[1]); //name
                sb.append("</name>\n        <sex>");
                sb.append(columns[2]); // sex
                sb.append("</sex>\n        <birthday>");
                sb.append(columns[3]); //birthday
                sb.append("</birthday>\n        <age>");
                sb.append(columns[4]); // age
                sb.append("</age>\n        <date>");
                sb.append(columns[5]); // date
                sb.append("</date>\n    </person>\n");
            }
            sb.append("</persons>\n</root>");

            // ファイルの書き出し
            File outputFile = new File("C:\\Users\\y_hiraba\\Documents\\tmp.xml");
            try {
                // 出力ストリームの生成
                FileOutputStream fos = new FileOutputStream(outputFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                PrintWriter pw = new PrintWriter(osw);
                    pw.println(sb);
                System.out.println("ファイルの出力が完了しました。");
                pw.close();
            } catch(Exception e) {
                e.printStackTrace();
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
    // Convert CSV to XML Format File-------------------------------------------------

    // Read XML Format File-------------------------------------------------

    // Read XML Format File-------------------------------------------------
}