package sample.gui.data;

import java.util.LinkedList;
import java.util.List;

// TODO: 27.02.2023 CHANGE TO SINGLETON LIST OF USERS
public class LoginDataList{

    private static final List<LoginData> instance = new LinkedList<>();

    public static List<LoginData> getInstance(){
        return instance;
    }

    public static void add(LoginData data){
        instance.add(data);
    }
}
