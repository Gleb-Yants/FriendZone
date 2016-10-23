package util;

/**
Util method for ordering storing history of users' conversation in db. This method chooses less id value for
  first value and than save in db, as conversation between 2 users with these ids
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
