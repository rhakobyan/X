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

CREATE TABLE Permission (
  permissionID INT AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (permissionID)
);

CREATE TABLE RolePermission (
  roleID INT,
  permissionID INT,
  PRIMARY KEY (roleID, permissionID),
  FOREIGN KEY (permissionID) REFERENCES Permission(permissionID)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (roleID) REFERENCES Role(roleID)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

INSERT INTO Permission(name, description) VALUES
("login", "Can Log In"),
("create_project", "Can create a project"),
("downvote", "Can downvote anything"),
("write_guides", "Can write guides"),
("delete_project", "Can delete projects"),
("delete_review", "Can delete project reviews"),
("ban", "Can ban users with higher priority"),
("unban", "Can unban users with higher priority"),
("delete_message", "Can delete messages on users' message boards"),
("control_panel", "Can access the Control Panel"),
("warn", "Can send warn messages to users when they are violating the rules"),
("delete_reputation", "Can delete reputation points"),
("role_assignment", "Can assign roles to users"),
("ip_ban", "Can ban IP addresses"),
("change_username", "Can change the username of a user"),
("rename_project", "Can rename a project"),
("change_profile_picture", "Can change users' profile pictures"),
("null_reputation", "Can delete the user's whole reputation"),
("role_mangement", "Can create/delete/change roles"),
("delete_user", "Can delete users");

SELECT * FROM (SELECT name, priority, permissionID  FROM RolePermission INNER JOIN Role on Role.roleID = RolePermission.roleID) AS tbl INNER JOIN Permission on tbl.permissionID = Permission.permissionID;


CREATE TABLE Tag(
tagID INT AUTO_INCREMENT,
name VARCHAR(20) NOT NULL,
description VARCHAR(1000) NOT NULL,
`usage` INT DEFAULT 0,
dateAdded DATE NOT NULL,
PRIMARY KEY(tagID)
);

CREATE TABLE UploadTag (
  uploadID INT,
  tagID INT,
  PRIMARY KEY (uploadID, tagID),
  FOREIGN KEY (uploadID) REFERENCES Upload(uploadID)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (tagID) REFERENCES Tag(tagID)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

CREATE TABLE VotesCast (
  uploadID INT,
  userID INT,
  positive BOOLEAN NOT NULL,
  PRIMARY KEY (uploadID, userID),
  FOREIGN KEY (uploadID) REFERENCES Upload(uploadID)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (userID) REFERENCES User(userID)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);