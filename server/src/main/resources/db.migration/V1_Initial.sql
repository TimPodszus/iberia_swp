CREATE TABLE User (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Role (
    roleID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE City (
    cityID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    foundationDate YEAR,
    isHarbourCity BOOLEAN,
    hasHospital BOOLEAN
);

CREATE TABLE Board (
    boardID INT PRIMARY KEY AUTO_INCREMENT,
    infectionCounter INT,
    escalationStage INT
);

CREATE TABLE Plague (
    plagueID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    researched BOOLEAN,
    plagueCubesRemaining INT
);

CREATE TABLE Lobby (
    lobbyID INT PRIMARY KEY AUTO_INCREMENT,
    difficulty INT NOT NULL,
    boardID INT,
    FOREIGN KEY (boardID) REFERENCES Board(boardID)
);

CREATE TABLE Player (
    playerID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    roleID INT,
    currentPositionID INT,
    userID INT,
    lobbyID INT,
    FOREIGN KEY (roleID) REFERENCES Role(roleID),
    FOREIGN KEY (currentPositionID) REFERENCES City(cityID),
    FOREIGN KEY (userID) REFERENCES User(userID),
    FOREIGN KEY (lobbyID) REFERENCES Lobby(lobbyID)
);

CREATE TABLE Infection (
    infectionID INT PRIMARY KEY AUTO_INCREMENT,
    severity INT NOT NULL,
    plagueID INT,
    cityID INT,
    FOREIGN KEY (plagueID) REFERENCES Plague(plagueID),
    FOREIGN KEY (cityID) REFERENCES City(cityID)
);

CREATE TABLE Connection (
    connectionID INT PRIMARY KEY AUTO_INCREMENT,
    startCityID INT,
    endCityID INT,
    hasTrainTrack BOOLEAN,
    isWaterWay BOOLEAN,
    canBuildTrainTracks BOOLEAN,
    isShipRoute BOOLEAN,
    FOREIGN KEY (startCityID) REFERENCES City(cityID),
    FOREIGN KEY (endCityID) REFERENCES City(cityID)
);

CREATE TABLE Region (
    regionID INT PRIMARY KEY AUTO_INCREMENT,
    waterTreatments INT,
    preventionMarker BOOLEAN
);

CREATE TABLE RegionCity (
    regionID INT,
    cityID INT,
    PRIMARY KEY (regionID, cityID),
    FOREIGN KEY (regionID) REFERENCES Region(regionID),
    FOREIGN KEY (cityID) REFERENCES City(cityID)
);

CREATE TABLE GameTurn (
    gameTurnID INT PRIMARY KEY AUTO_INCREMENT,
    round INT NOT NULL,
    currentPlayerID INT,
    boardID INT,
    FOREIGN KEY (currentPlayerID) REFERENCES Player(playerID),
    FOREIGN KEY (boardID) REFERENCES Board(boardID)
);

CREATE TABLE Card (
    cardID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL
);

CREATE TABLE CityCard (
    cityCardID INT PRIMARY KEY,
    cityID INT,
    FOREIGN KEY (cityCardID) REFERENCES Card(cardID),
    FOREIGN KEY (cityID) REFERENCES City(cityID)
);

CREATE TABLE InfectionCard (
    infectionCardID INT PRIMARY KEY,
    cityID INT,
    FOREIGN KEY (infectionCardID) REFERENCES Card(cardID),
    FOREIGN KEY (cityID) REFERENCES City(cityID)
);

CREATE TABLE EventCard (
    eventCardID INT PRIMARY KEY,
    action TEXT,
    FOREIGN KEY (eventCardID) REFERENCES Card(cardID)
);

CREATE TABLE EpidemicCard (
    epidemicCardID INT PRIMARY KEY,
    description TEXT,
    FOREIGN KEY (epidemicCardID) REFERENCES Card(cardID)
);

CREATE TABLE PlayerCard (
    playerID INT,
    cardID INT,
    PRIMARY KEY (playerID, cardID),
    FOREIGN KEY (playerID) REFERENCES Player(playerID),
    FOREIGN KEY (cardID) REFERENCES Card(cardID)
);

CREATE TABLE BoardPlague (
    boardID INT,
    plagueID INT,
    PRIMARY KEY (boardID, plagueID),
    FOREIGN KEY (boardID) REFERENCES Board(boardID),
    FOREIGN KEY (plagueID) REFERENCES Plague(plagueID)
);
