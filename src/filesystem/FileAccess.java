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

    public ArrayList<PatientInformation> columnsArr;

    public ArrayList<PatientInformation> readFile(String Path, int combo) {
        columnsArr = new ArrayList();

        // Read File
        try {
            File file = new File("C:\\Users\\y_hiraba\\Documents");
            if(Path != "") {
                    file = new File(Path);
            }
            int comboNum = combo;
            FileInputStream input = new FileInputStream(file);
            InputStreamReader stream = new InputStreamReader(input,"SJIS");

            if(comboNum == 0) {
                stream = new InputStreamReader(input,"SJIS");
            }else if(comboNum == 1) {
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
}