package facesign.adplayer.fanhong.fs_sys.models;

/**
 * Created by Administrator on 2017/10/16.
 */

public class CameraInfo {
    private String IP="";
    private String Port="";
    private String User="";
    private String Pwd="";
    private int Login=-1;
    private int Alarm=-1;

    public CameraInfo() {
    }

    public CameraInfo(String IP, String port, String user, String pwd) {
        this.IP = IP;
        Port = port;
        User = user;
        Pwd = pwd;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public int getLogin() {
        return Login;
    }

    public void setLogin(int login) {
        Login = login;
    }

    public int getAlarm() {
        return Alarm;
    }

    public void setAlarm(int alarm) {
        Alarm = alarm;
    }

    @Override
    public String toString() {
        return "CameraInfo{" +
                "IP='" + IP + '\'' +
                ", Port='" + Port + '\'' +
                ", User='" + User + '\'' +
                ", Pwd='" + Pwd + '\'' +
                ", Login=" + Login +
                ", Alarm=" + Alarm +
                '}';
    }
}
