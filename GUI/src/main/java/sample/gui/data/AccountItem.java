package sample.gui.data;

public class AccountItem{
    private final int id;
    private final String currency;
    private final String name;
    private double balance;

    public AccountItem(int id, String currency){
        this.id = id;
        this.currency = currency;
        this.name = "";
        this.balance = 0.0;
    }
    public AccountItem(int id, String currency, String name, double balance){
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString(){
        return String.format("%s: %.2f %s", name, balance, currency);
    }

    public int getId(){
        return id;
    }

    public String getCurrency(){
        return currency;
    }

    public double getBalance(){
        return balance;
    }

    public String getName(){
        return name;
    }
}
