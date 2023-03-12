package sample.gui.data;

import java.util.LinkedList;
import java.util.List;

public class LoginDataList{

    private static final List<LoginDataItem> instance = new LinkedList<>();

    private LoginDataList(){}

    public static List<LoginDataItem> getInstance(){
        return instance;
    }

    public static void add(LoginDataItem data){
        instance.add(data);
    }

}
