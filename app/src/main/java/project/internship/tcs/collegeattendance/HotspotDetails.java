package project.internship.tcs.collegeattendance;

public class HotspotDetails {
    private String ssid;
    private String password;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return ssid + ":" + password;
    }
}
