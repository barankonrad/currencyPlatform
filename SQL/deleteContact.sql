CREATE PROC deleteContact
@user INT,
@friend INT
AS

BEGIN TRY
    DELETE FROM Contacts
    WHERE UserID = @user AND FriendID = @friend
END TRY
BEGIN CATCH
    RAISERROR('Error occured', 16, 1)
END CATCH