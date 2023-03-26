CREATE FUNCTION GetIncomingTransfers(@clientID INT)
RETURNS TABLE
AS

RETURN
    SELECT T.TransferID,
        T.Date,
        T.Title,
        (C.FirstName + ' ' + C.LastName) AS Contact,
        T.AmountExchanged,
        BS.Currency, 
        T.Amount 'Amount sent',
        T.ExchangeRate,
        BR.Currency 'CurrencySent'
    FROM Transfers T
    JOIN Balances BS ON T.SenderAccountID = BS.BalanceID
    JOIN Balances BR ON T.ReceiverAccountID = BR.BalanceID
    JOIN Clients C ON BS.ClientID = C.ClientID
    WHERE T.ReceiverAccountID IN (SELECT BalanceID FROM Balances WHERE ClientID = @clientID)
GO