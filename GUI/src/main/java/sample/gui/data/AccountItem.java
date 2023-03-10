package sample.gui.data;

public class AccountItem{
    private final int id;
    private final String currency;
    private double balance;

    public AccountItem(int id, String currency, double balance){
        this.id = id;
        this.currency = currency;
        this.balance = balance;
    }

    @Override
    public String toString(){
        return currency.toUpperCase() + ": " + String.format("%.2f", balance);
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

    public void moneyIn(double value){
        this.balance += value;
    }

    public void moneyOut(double value){
        this.balance -= value;
    }
}
