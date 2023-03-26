CREATE PROC deleteBalance
@id INT
AS

BEGIN TRY
    DELETE FROM Balances
    WHERE BalanceID = @id
END TRY
BEGIN CATCH
    RAISERROR('Error occured', 16, 1)
END CATCH