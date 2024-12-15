CREATE TABLE credenciais (

    id BIGINT not null AUTO_INCREMENT ,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,

    primary key(id)
);
