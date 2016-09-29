/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

/**
 *
 * @author y_hiraba
 */
public class PatientInformation {

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_SEX = 2;
    public static final int COLUMN_BIRTHDAY = 3;
    public static final int COLUMN_AGE = 4;

    private String id;
    private String name;
    private String sex;
    private String birthday;
    private String age;

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSex(){
        return sex;
    }
    public void setSex(String sex){
        this.sex = sex;
    }

    public String getBirthday(){
        return birthday;
    }
    public void setBirthday(String birthday){
        this.birthday =  birthday;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String convertCsvFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId());
        sb.append(",");
        sb.append(getName());
        sb.append(",");
        sb.append(getSex());
        sb.append(",");
        sb.append(getBirthday());
        sb.append(",");
        sb.append(getAge());

        return sb.toString();
    }

    public String patientToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("患者ID：");
        sb.append(getId()).append("\n　");
        sb.append("患者氏名：");
        sb.append(getName()).append("\n　");
        sb.append("患者性別：");
        sb.append(getSex()).append("\n　");
        sb.append("生年月日：");
        sb.append(getBirthday()).append("\n　");
        sb.append("年齢：");
        sb.append(getAge()).append("\n　");

        return sb.toString();
    }
}