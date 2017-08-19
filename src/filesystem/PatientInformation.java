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

    public static final int COLUMN_ID       = 0;
    public static final int COLUMN_NAME     = 1;
    public static final int COLUMN_SEX      = 2;
    public static final int COLUMN_BIRTHDAY = 3;
    public static final int COLUMN_AGE      = 4;
    public static final int COLUMN_DATE     = 5;
    public static final int COLUMN_IMAGE    = 6;

    private String id;
    private String name;
    private String sex;
    private String birthday;
    private String age;
    private String date;
    private String image;

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

    public String getDate(){
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }

    // CSVフォーマットで返す
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
        sb.append(",");
        sb.append(getDate());
        sb.append(",");
        sb.append(getImage());
        return sb.toString();
        
    }

    // 文字列として返す
    public String patientToString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("患者ID：");
        sb.append(getId()).append(",");
        sb.append("患者氏名：");
        sb.append(getName()).append(",");
        sb.append("患者性別：");
        sb.append(getSex()).append(",");
        sb.append("生年月日：");
        sb.append(getBirthday()).append(",");
        sb.append("年齢：");
        sb.append(getAge()).append(",");
        sb.append("追加日：");
        sb.append(getDate()).append(",");
        sb.append("写真：");
        sb.append(getImage()).append(",");
        return sb.toString();
        
    }


}
