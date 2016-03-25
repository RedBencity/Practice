package ben.practice.utils;

/**
 * Created by dell on 2015-10-1.
 */
public class NetUtil {
//    final  static public String IP = "10.219.1.175";
final  static public String IP = "42.96.176.131";
    //模拟器访问
//    final  static public String URL = "http://10.219.1.175:8080/PracticeServer";
//    final  static public String URL = "http://192.168.56.1:8080/PracticeServer";
    //手机访问
    final  static public String URL = "http://"+IP+":8080/PracticeServer";


    final static public String downloadApk_URL = "http://"+IP+":8080/PracticeServer/version/app-release.apk";

    public static String getPhotoUrl(String username){
        String url = URL + "/photo/"+username+".png";
        return url;

    }

    public static String getImageUrl(String username,String time){
        String url = URL + "/image/"+time+username+".jpg";
        return url;
    }

}
