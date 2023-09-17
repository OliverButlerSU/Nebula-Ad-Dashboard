DROP TABLE IF EXISTS Impression;
DROP TABLE IF EXISTS Click;
DROP TABLE IF EXISTS Server;
DROP TABLE IF EXISTS User;


CREATE TABLE IF NOT EXISTS Impression(
    "Date" TIMESTAMP NOT NULL,
    "ID" VARCHAR(20) NOT NULL,
    "ImpressionCost" FLOAT NOT NULL,
    "Gender" VARCHAR(6) NOT NULL,
    "Age" VARCHAR(6) NOT NULL,
    "Income" VARCHAR(5) NOT NULL,
    "Context" VARCHAR(12) NOT NULL,
    PRIMARY KEY (Date,ID),
    FOREIGN KEY (ID) references User(ID)
);

CREATE TABLE IF NOT EXISTS Click(
    "Date" TIMESTAMP NOT NULL,
    "ID" VARCHAR(20) NOT NULL,
    "ClickCost" FLOAT NOT NULL,
    "Gender" VARCHAR(6) NOT NULL,
    "Age" VARCHAR(6) NOT NULL,
    "Income" VARCHAR(5) NOT NULL,
    "Context" VARCHAR(12) NOT NULL,
    PRIMARY KEY (Date,ID),
    FOREIGN KEY (ID) references User(ID)
);

CREATE TABLE IF NOT EXISTS Server(
    "EntryDate" TIMESTAMP NOT NULL,
    "ID" VARCHAR(20) NOT NULL,
    "ExitDate" TIMESTAMP NULL ,
    "PagesViewed" INTEGER NOT NULL,
    "Conversion" BOOL NOT NULL,
    "Gender" VARCHAR(6) NOT NULL,
    "Age" VARCHAR(6) NOT NULL,
    "Income" VARCHAR(5) NOT NULL,
    "Context" VARCHAR(12) NOT NULL,
    PRIMARY KEY ("EntryDate",ID, "ExitDate"),
    FOREIGN KEY (ID) references User(ID)
);

CREATE TABLE IF NOT EXISTS User(
    "ID" VARCHAR(20) NOT NULL ,
    "Gender" VARCHAR(6) NOT NULL,
    "Age" VARCHAR(6) NOT NULL,
    "Income" VARCHAR(5) NOT NULL,
    "Context" VARCHAR(12) NOT NULL,
    PRIMARY KEY (ID)
);

INSERT INTO User SELECT ID, Gender, Age, Income, Context FROM ImpressionLog GROUP BY ID;
INSERT INTO Impression SELECT Date, ID, ImpressionCost, Gender, Age, Income, Context FROM ImpressionLog GROUP BY Date, ID;
INSERT INTO Click SELECT Date, ClickLog.ID, ClickCost, Gender, Age, Income, Context FROM ClickLog INNER JOIN USER ON User.ID=ClickLog.ID;
INSERT INTO Server SELECT EntryDate ,ServerLog.ID ,ExitDate ,PagesViewed ,Conversion, Gender, Age, Income, Context FROM ServerLog INNER JOIN USER ON User.ID=ServerLog.ID;

DROP TABLE IF EXISTS ImpressionLog;
DROP TABLE IF EXISTS ClickLog;
DROP TABLE IF EXISTS ServerLog;

-- TODO: Add indexing