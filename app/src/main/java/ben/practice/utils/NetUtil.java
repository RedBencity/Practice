package ben.practice.utils;

/**
 * Created by dell on 2015-10-1.
 */
public class NetUtil {
    //模拟器访问
//    final  static public String URL = "http://192.168.1.107:8080/PracticeServer";
    final  static public String URL = "http://192.168.56.1:8080/PracticeServer";
    //手机访问
    final  static public String URL_PHONE = "http://192.168.155.1:8080/PracticeServer";


    public static String getPhotoUrl(String username){
        String url = URL + "/photo/"+username+".png";
        return url;

    }

    public static String getImageUrl(String username,String time){
        String url = URL + "/image/"+time+username+".jpg";
        return url;
    }

}
