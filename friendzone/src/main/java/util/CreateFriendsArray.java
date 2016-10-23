package util;

import java.util.ArrayList;

/**
Util method to converse string from db to arraylist
 */
public class CreateFriendsArray {
    public static ArrayList<Integer> varcharToArrayList(String varchar){
        ArrayList<Integer> result = new ArrayList<>();
        if(varchar!=null&&!(varchar.equals(""))){
        String [] temp = varchar.split(",");
        for(String tmp : temp){
            result.add(Integer.parseInt(tmp.trim()));
        }}
        return result;
    }
}
