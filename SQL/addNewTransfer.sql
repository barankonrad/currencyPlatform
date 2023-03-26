CREATE PROC addNewTransfer
@senderBalance INT,
@receiverBalance INT,
@amount MONEY,
@rate FLOAT,
@title NVARCHAR(50)
AS
SET XACT_ABORT OFF
SET IMPLICIT_TRANSACTIONS OFF


BEGIN TRAN
DECLARE @exchangedAmount MONEY = @amount * @rate

INSERT INTO Transfers VALUES
(@senderBalance, @receiverBalance, @amount, @exchangedAmount, @rate, @title, GETDATE())

UPDATE Balances
SET Balance = Balance - @amount
WHERE BalanceID = @senderBalance

UPDATE Balances
SET Balance = Balance + @exchangedAmount
WHERE BalanceID = @receiverBalance

IF(SELECT Balance FROM Balances WHERE BalanceID = @senderBalance) < 0
    ROLLBACK TRAN
COMMIT TRAN