package sample.gui.data;

import java.util.LinkedList;
import java.util.List;

public class LoginData{
    public static class LoginDataItem{
        private final int id;
        private final String login;
        private final String hash;
        private final int salt;

        public LoginDataItem(int id, String login, String hash, int salt){
            this.id = id;
            this.login = login;
            this.hash = hash;
            this.salt = salt;
        }

        public int getId(){
            return id;
        }

        public String getLogin(){
            return login;
        }

        public String getHash(){
            return hash;
        }

        public int getSalt(){
            return salt;
        }
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
