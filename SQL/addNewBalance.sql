CREATE PROC addNewBalance
@id INT,
@currency NVARCHAR(3),
@name NVARCHAR(30)
AS

BEGIN TRY
    INSERT INTO Balances VALUES
    (@id, @currency, @name, 0)
END TRY
BEGIN CATCH
    RAISERROR('Error occured', 16, 1)
END CATCH