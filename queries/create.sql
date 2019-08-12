CREATE TABLE Upload (
  uploadID INT AUTO_INCREMENT,
  projectName VARCHAR(25) UNIQUE NOT NULL,
  projectDescription VARCHAR(1000) NOT NULL,
  location VARCHAR(50) NOT NULL,
  fileName VARCHAR(50) NOT NULL,
  dateAdded DATE NOT NULL,
  rating INT NOT NULL DEFAULT 1,
  uploaderID INT NOT NULL,
  PRIMARY KEY (uploadID),
  FOREIGN KEY (uploaderID) REFERENCES User(userID)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

CREATE TABLE Tag (
  tagID INT AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL UNIQUE,
  description VARCHAR(1000) NOT NULL,
  `usage` INT NOT NULL,
  dateAdded DATE NOT NULL,
  PRIMARY KEY (tagID)
);

CREATE TABLE Role (
  roleID INT AUTO_INCREMENT,
  name VARCHAR(25) NOT NULL UNIQUE,
  colour VARCHAR(7) NOT NULL,
  PRIMARY KEY (roleID)
);

CREATE TABLE Role (
  roleID INT AUTO_INCREMENT,
  name VARCHAR(25) NOT NULL UNIQUE,
  colour VARCHAR(7) NOT NULL,
  priority INT NOT NULL,
  PRIMARY KEY (roleID)
);

INSERT INTO Role(name, colour, priority) VALUES('User','#192b3b', 1),
('Administrator', '#b43636', 5),
('Banned', '#929292', 4),
('Super Moderator', '#ffa100', 3),
('Moderator', '#7e8c65', 2);


CREATE TABLE UserRole (
  roleID INT,
  userID INT,
  PRIMARY KEY (roleID, userID),
  FOREIGN KEY (userID) REFERENCES User(userID)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (roleID) REFERENCES Role(roleID)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);
