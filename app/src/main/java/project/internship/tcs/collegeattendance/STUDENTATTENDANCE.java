package project.internship.tcs.collegeattendance;

public class STUDENTATTENDANCE {
    String rollno,name,attendance,pic,mobileno;
    boolean present;

    public STUDENTATTENDANCE() {
    }

    public STUDENTATTENDANCE(String rollno, String name, String attendance, String pic, String mobileno, boolean present) {
        this.rollno = rollno;
        this.name = name;
        this.attendance = attendance;
        this.pic = pic;
        this.mobileno = mobileno;
        this.present = present;

    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
