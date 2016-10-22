package util;

/**
 * Created by Gleb_Yants on 04.10.2016.
 */
public class GetLessValue {
    public static String getLessValue(int first, int second){
        String result;
        if(first<second){
            result = first+","+second;
            return result;
        }else{
            result = second+","+first;
            return result;
        }
    }
}
