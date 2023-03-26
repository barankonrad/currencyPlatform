CREATE PROC addNewContact
@user INT,
@friend INT
AS

BEGIN TRY
    INSERT INTO Contacts VALUES
    (@user, @friend)
END TRY
BEGIN CATCH
    RAISERROR('Error occured', 16, 1)
END CATCH