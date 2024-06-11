CREATE TABLE users (
    id_users NUMBER GENERATED BY DEFAULT AS IDENTITY,
    username VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    passwordsita VARCHAR2(100) NOT NULL,
    PRIMARY KEY (id_users)
);

CREATE TABLE tickets (
    id_tickets NUMBER GENERATED BY DEFAULT AS IDENTITY,
    ticket_number NUMBER NOT NULL,
    title VARCHAR2(100) NOT NULL,
    descripcion VARCHAR2(500) NOT NULL,
    author VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    creation_date DATE DEFAULT SYSDATE,
    status VARCHAR2(20) DEFAULT 'Activo',
    completion_date DATE,
    PRIMARY KEY (id_tickets)
);

drop TABLE users
INSERT INTO users (username, email, passwordsita) values ('theloko', 'roandhq@gmail.com', 'urtado1243')

drop table users 
drop table tickets