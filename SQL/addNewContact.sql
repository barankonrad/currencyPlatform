CREATE PROC addNewContact
@user INT,
@friend INT
AS
SET XACT_ABORT OFF
SET IMPLICIT_TRANSACTIONS OFF

BEGIN TRAN

BEGIN TRY
    INSERT INTO Contacts VALUES
    (@user, @friend)
    COMMIT
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK
    RAISERROR('Error occured', 16, 1)
END CATCH