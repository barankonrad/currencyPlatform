package sample.gui.data;

import java.util.LinkedList;
import java.util.List;

public class LoginData{
    public record LoginDataItem(int id, String login, String hash, int salt){
    }

    private static final List<LoginDataItem> instance = new LinkedList<>();

    private LoginData(){}

    public static List<LoginDataItem> getListInstance(){
        return instance;
    }

    public static void add(LoginDataItem data){
        instance.add(data);
    }

}
