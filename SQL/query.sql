CREATE TABLE Clients(
    ClientID INT CONSTRAINT ClientsPK PRIMARY KEY IDENTITY(1,1),
    Login NVARCHAR(30) UNIQUE,
    FirstName NVARCHAR(30),
    LastName NVARCHAR(30)

    INDEX LoginIndex(Login)
)

CREATE TABLE Passwords(
    ClientID INT CONSTRAINT ClientFK PRIMARY KEY REFERENCES Clients(ClientID),
    Hash NVARCHAR(64),
    Salt BINARY(2)

    INDEX HashSaltIndex(Hash, Salt)
)

CREATE TABLE Contacts(
    ID INT CONSTRAINT ContactsPK PRIMARY KEY IDENTITY(1,1),
    UserID INT CONSTRAINT ContactsUsersFK FOREIGN KEY REFERENCES Clients(ClientID),
    FriendID INT CONSTRAINT ContactsFriendsFK FOREIGN KEY REFERENCES Clients(ClientID)
)

CREATE TABLE Balances(
    BalanceID INT CONSTRAINT BalancePK PRIMARY KEY IDENTITY(1,1),
    ClientID INT CONSTRAINT BalanceClientFK FOREIGN KEY REFERENCES Clients(ClientID),
    Currency NVARCHAR(3),
    Balance MONEY

    CONSTRAINT ClientCurrencyUnique UNIQUE (ClientID, Currency)
)

CREATE TABLE Transfers(
    TransferID INT CONSTRAINT TransfersPK PRIMARY KEY IDENTITY(1,1),
    SenderID INT CONSTRAINT SenderFK FOREIGN KEY REFERENCES Clients(ClientID),
    ReceiverID INT CONSTRAINT ReceiverFK FOREIGN KEY REFERENCES Clients(ClientID),
    Currency NVARCHAR(3),
    Amount MONEY,
    Title NVARCHAR(100),
    Date DATE
)

CREATE TABLE TransfersExchangeDetails(
    TransferID INT CONSTRAINT ExchangeDetails PRIMARY KEY REFERENCES Transfers(TransferID),
    ReceiverCurrency NVARCHAR(3),
    ExchangeRate FLOAT,
    AmountExchanged MONEY
)
