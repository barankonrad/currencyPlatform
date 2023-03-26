CREATE FUNCTION GetTransferHistory(@clientID INT)
RETURNS TABLE
AS
RETURN
    SELECT 'OUT' AS InOut, * FROM GetOutgoingTransfers(@clientID)
    UNION ALL
    SELECT 'IN', * FROM GetIncomingTransfers(@clientID)
GO