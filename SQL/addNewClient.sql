CREATE PROC addNewClient
@login NVARCHAR(30),
@hash NVARCHAR(64),
@salt BINARY(2),
@firstName NVARCHAR(30),
@lastName NVARCHAR(30)
AS
SET XACT_ABORT ON
SET IMPLICIT_TRANSACTIONS OFF

BEGIN TRAN

BEGIN TRY
    INSERT INTO Clients VALUES
    (@login, @firstName, @lastName)
    DECLARE @id INT = @@IDENTITY
    INSERT INTO Passwords VALUES
    (@id, @hash, @salt)

    COMMIT
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK
    RAISERROR('Error occured', 16, 1)
END CATCH
