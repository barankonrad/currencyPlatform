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

    CONSTRAINT RelationUnique UNIQUE (UserID, FriendID),
    CONSTRAINT NotSamePersonCheck CHECK (UserID != FriendID)
)

CREATE TABLE Balances(
    BalanceID INT CONSTRAINT BalancePK PRIMARY KEY IDENTITY(1,1),
    ClientID INT CONSTRAINT BalanceClientFK FOREIGN KEY REFERENCES Clients(ClientID),
    Currency NVARCHAR(3),
    Name NVARCHAR(30),
    Balance MONEY

    CONSTRAINT ClientCurrencyUnique UNIQUE (ClientID, Currency)
)

CREATE TABLE Transfers(
    TransferID INT CONSTRAINT TransfersPK PRIMARY KEY IDENTITY(1,1),
    SenderAccountID INT CONSTRAINT SenderAccountFK FOREIGN KEY REFERENCES Balances(BalanceID),
    ReceiverAccountID INT CONSTRAINT ReceiverAccountFK FOREIGN KEY REFERENCES Balances(BalanceID),
    Amount MONEY,
    AmountExchanged MONEY,
    ExchangeRate FLOAT,
    Title NVARCHAR(100),
    Date DATE
)