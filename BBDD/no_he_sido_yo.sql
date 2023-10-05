CREATE DATABASE IF NOT EXISTS nohesidoyo COLLATE utf8mb4_spanish2_ci;
USE nohesidoyo;

DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS preguntas;
DROP TABLE IF EXISTS tematicas;

CREATE TABLE usuarios(
  id_usuario int PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(40) NOT NULL,
  contra varchar(40) NOT NULL,
  img_perfil varchar(50) NOT NULL
);

CREATE TABLE tematicas (
  id_tematica int PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(15) NOT NULL,
  ruta_imagen varchar(15) NOT NULL
);

CREATE TABLE preguntas (
  id_pregunta int PRIMARY KEY AUTO_INCREMENT,
  pregunta varchar(250) NOT NULL,
  resp1 varchar(250) NOT NULL,
  resp2 varchar(250) NOT NULL,
  resp3 varchar(250) NOT NULL,
  resp4 varchar(250) NOT NULL,
  fk_tematica int NOT NULL,
  tipo varchar(25) NOT NULL,
  FOREIGN KEY (fk_tematica) REFERENCES tematicas(id_tematica)
);



INSERT INTO tematicas(nombre, ruta_imagen) VALUES
('Acampada', 'acampada.jpg'),
('Colegio', 'colegio.jpg'),
('Playa', 'playa.jpg'),
('Policia', 'policia.jpg'),
('Deportes', 'deportes.jpg'),
('Viaje', 'viaje.png');

-- https://youtu.be/gVI7g4y2xB0?t=730

-- Procedimiento almacenado al que le introducimos el nombre y la contraseñ para que nos devuelva -1 si el usuario y la contraseña no coiciden y en caso contrario nos devolvería el id del usuario
DROP PROCEDURE IF EXISTS Login;
DELIMITER $$
CREATE PROCEDURE Login(
IN _nombre VARCHAR(50),
IN _contra VARCHAR(50),
OUT resultado INT)
BEGIN
  DECLARE id INT DEFAULT NULL;
  SELECT id_usuario FROM usuarios WHERE nombre LIKE _nombre AND contra LIKE sha1(_contra) INTO id;
  IF id is NULL
      THEN
        	SET resultado = -1;
    	ELSE
        	SET resultado = id;
	END IF;
END;$$
DELIMITER ;

-- Procedimiento almacenado que al que le introducimos el nombre y contraseñas para que nos devuelva -2 en caso contrario lo registra con l acontraseña encriptada
DROP PROCEDURE IF EXISTS Register;
DELIMITER $$
CREATE PROCEDURE Register(
IN _nombre VARCHAR(50),
IN _contra VARCHAR(50),
OUT resultado INT)
BEGIN
	DECLARE id INT DEFAULT -1;
	SELECT id_usuario FROM usuarios WHERE nombre LIKE _nombre INTO id;
	IF id = -1
    	THEN
        	INSERT INTO usuarios (nombre, contra,img_perfil) VALUES
        	(_nombre,sha1(_contra),'defecto.jpg');
            SELECT id_usuario FROM usuarios WHERE nombre LIKE _nombre AND contra LIKE sha1(_contra) INTO resultado;
    	ELSE
        	SET resultado = -2;
	END IF;
END;$$
DELIMITER ;

/*
	INSERT INTO usuarios(nombre, contra, img_perfil) 
	VALUES ("Admin", "95f05a94461e53bc8ac690103eda7dc444cd11f6", "defecto.jpg");
*/
-- SET @salida = 55;
CALL Register("Admin", "123456", @salida);
/*
	CALL Register("Guille", "123456", @salida);
	CALL Register("Jaime", "123456", @salida);
*/

-- Procedimiento almacenado al que le introducimos el id del usuario y nos muestra tanto su nombre como su imagen de perfil
DROP PROCEDURE IF EXISTS InfoUsuario;
DELIMITER $$
CREATE PROCEDURE InfoUsuario(
IN _id INT)
BEGIN
	SELECT nombre, img_perfil FROM usuarios WHERE id_usuario = _id;
END;$$
DELIMITER ;

-- Procedimiento almacenado que nos devuelve toda la informacion de las temáticas
DROP PROCEDURE IF EXISTS CargarTematicas;
DELIMITER $$
CREATE PROCEDURE CargarTematicas()
BEGIN
	SELECT * FROM tematicas;
END;$$
DELIMITER ;

-- Procedimiento almacenado al que le introducimos el id de la temática y 
-- el numero total de preguntas que queremos(Siempre 12 porque son las rondas que se juegan) 
-- y nos devuelve toda la informacion de las preguntas
DROP PROCEDURE IF EXISTS CargarPreguntas;
DELIMITER $$
CREATE PROCEDURE CargarPreguntas(
IN _id_tematica INT,
IN _numPreguntas INT)
BEGIN
  SET _numPreguntas = _numPreguntas - 1;
  -- SET _numPreguntas = 0;
  SELECT * FROM (
      SELECT pregunta, resp1, resp2, resp3, resp4, tipo, ruta_imagen
   		FROM preguntas
  		INNER JOIN tematicas ON tematicas.id_tematica = preguntas.fk_tematica
  		WHERE fk_tematica = _id_tematica AND tipo NOT LIKE 'respFoto' ORDER BY RAND() LIMIT _numPreguntas ) as preguntas1
  UNION ALL 
  SELECT * FROM (
    	SELECT pregunta, resp1, resp2, resp3, resp4, tipo, ruta_imagen
   			FROM preguntas
  			INNER JOIN tematicas ON tematicas.id_tematica = preguntas.fk_tematica
  			WHERE fk_tematica = _id_tematica AND tipo LIKE 'respFoto' ORDER BY RAND() LIMIT 1) as preguntas2;
END;$$
DELIMITER ;
-- call CargarPreguntas(1,12);

-- Procedimiento almacenado al que le introducimos el id del usuario y la ruta de la  imagen para que modifique el perfil 
DROP PROCEDURE IF EXISTS CambiarPerfil;
DELIMITER $$
CREATE PROCEDURE CambiarPerfil(
IN _id_usuario INT,
IN _ruta_imagen VARCHAR(50))
BEGIN
  UPDATE `usuarios` SET `img_perfil` = _ruta_imagen WHERE `usuarios`.`id_usuario` = _id_usuario;
END;$$
DELIMITER ;

-- Lo mismo que el Login para JAVA para la conexion del Admin
DROP PROCEDURE IF EXISTS LoginAdmin;
DELIMITER $$
CREATE PROCEDURE LoginAdmin(
IN _contra VARCHAR(50),
OUT resultado INT)
BEGIN
	DECLARE id INT DEFAULT NULL;
	SELECT id_usuario FROM usuarios WHERE nombre LIKE "Admin" AND contra LIKE sha1(_contra) INTO id;
	IF id is NULL
    	THEN
        	SET resultado = -1;
    	ELSE
        	SET resultado = id;
	END IF;
END;$$
DELIMITER ;

-- SET @salida = 55;
-- CALL LoginAdmin("Joyfe2020",@salida);
-- SELECT @salida;

-- (JAVA) Procedimiento almacenado al que le introducimos el nombre de la temática para que nos muestre todas las preguntas de esa temática
DROP PROCEDURE IF EXISTS VerPreguntas;
DELIMITER $$
CREATE PROCEDURE VerPreguntas(
IN _nombre VARCHAR(15))
BEGIN
  SELECT id_pregunta, pregunta, resp1, resp2, resp3, resp4, tipo FROM preguntas INNER JOIN tematicas ON tematicas.id_tematica = preguntas.fk_tematica WHERE nombre = _nombre;
END;$$
DELIMITER ;
-- CALL VerPreguntas("Acampada");

-- (JAVA) Procedimiento almacenado que nos muestra todos los usuarios
DROP PROCEDURE IF EXISTS CargarUsuarios;
DELIMITER $$
CREATE PROCEDURE CargarUsuarios()
BEGIN
	SELECT id_usuario, nombre, img_perfil FROM usuarios;
END;$$
DELIMITER ;

-- CALL CargarUsuarios();

-- Procedimiento almacenado al que le introducimos una pregunta con sus correspondientes respuestas, su tematica y el tipo de pregunta para introducirla en la BBDD
DROP PROCEDURE IF EXISTS InsertarPregunta;
DELIMITER $$
CREATE PROCEDURE InsertarPregunta(
	in _pregunta VARCHAR(250), _resp1 varchar(250), _resp2 varchar(250), _resp3 varchar(250), _resp4 varchar(250), _fk_tematica int, _tipo varchar(10))
BEGIN
	INSERT INTO preguntas (pregunta, resp1, resp2, resp3, resp4, fk_tematica, tipo) VALUES (_pregunta, _resp1, _resp2, _resp3, _resp4, _fk_tematica, _tipo);
END;$$
DELIMITER ;

-- CALL InsertarPregunta ("pre","HOLA","resp2","HOLA","HOLA",2,"respFrases");

-- (JAVA) Procedimiento almacenado al que le introducimos el id de la pregunta para eliminarla
DROP PROCEDURE IF EXISTS EliminarPregunta;
DELIMITER $$
CREATE PROCEDURE EliminarPregunta(
	in _idPregunta int)
BEGIN
	DELETE FROM preguntas WHERE id_pregunta = _idPregunta;
END;$$
DELIMITER ;

-- Procedimiento almacenado para eliminar al usuario, le introducimos el id del usuario
DROP PROCEDURE IF EXISTS EliminarUsuario;
DELIMITER $$
CREATE PROCEDURE EliminarUsuario(
	in _idUsuario int)
BEGIN
	IF _idUsuario != 1
    	THEN
        	DELETE FROM usuarios WHERE id_usuario = _idUsuario;
	END IF;
END;$$
DELIMITER ;
-- CALL EliminarPregunta("HOLA");

-- INSERT INTO `preguntas`(`pregunta`, `resp1`, `resp2`, `resp3`, `resp4`, `fk_tematica`, `tipo`) VALUES ("HOLA","HOLA","HOLA","HOLA","HOLA",2,"HOLA");

-- Procediemiento almacenado para el cambio de contraseña al que le introducimos el id y la contraseña del usuario que queremos cambiar
DROP PROCEDURE IF EXISTS CambiarContra;
DELIMITER $$
CREATE PROCEDURE CambiarContra(
	in _idUsuario int, in _contra VARCHAR(40))
BEGIN
	UPDATE usuarios SET contra = sha1(_contra) WHERE id_usuario =_idUsuario;
END;$$
DELIMITER ;