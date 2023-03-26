CREATE FUNCTION GetOutgoingTransfers(@clientID INT)
RETURNS TABLE
AS

RETURN
    SELECT T.TransferID,
        T.Date,
        T.Title,
        (C.FirstName + ' ' + C.LastName) AS Contact,
        T.Amount 'Amount sent',
        BS.Currency 'CurrencySent',
        T.AmountExchanged,
        T.ExchangeRate,
        BR.Currency
    FROM Transfers T
    JOIN Balances BS ON T.SenderAccountID = BS.BalanceID
    JOIN Balances BR ON T.ReceiverAccountID = BR.BalanceID
    JOIN Clients C ON BR.ClientID = C.ClientID
    WHERE T.SenderAccountID IN (SELECT BalanceID FROM Balances WHERE ClientID = @clientID)
GO