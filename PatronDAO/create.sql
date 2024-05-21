DROP TABLE IF EXISTS MEDICAMENTOS;

CREATE TABLE MEDICAMENTOS(
    ID INT AUTO_INCREMENT PRIMARY KEY,
    CODIGO INT NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    LABORATORIO VARCHAR(50) NOT NULL,
    PRECIO NUMERIC(7,2) NOT NULL,
    CANTIDAD INT NOT NULL
);

INSERT INTO MEDICAMENTOS
VALUES (DEFAULT, 567, 'COVID-19', 'MODERNA', 12, 2500),
(DEFAULT, 400, 'COVID-21', 'SPUNIK', 12, 3000);

