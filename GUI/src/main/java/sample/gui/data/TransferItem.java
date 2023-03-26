package sample.gui.data;

public record TransferItem(boolean inOrOut, int transferID, String date, String title, String contact,
                           double amountSent, String currencySent, double amountExchanged, double exchangeRate,
                           String currency){
}