<?php 
	$usuario = $_POST["usuario"];
	$contra = $_POST["contra"];
	$contra2 = $_POST["contra2"];
	$error = "";
	if (isset($usuario) && isset($contra) && isset($contra2)) {
		if ($contra == $contra2) {
			if (preg_match("/^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ]+$/", $usuario)) {//validas si son letras en minuscula, mayuscla, con tildes o numeros
				$longiUsu = strlen($usuario);
				$longiContra = strlen($contra);
				if ($longiUsu < 3 || $longiUsu > 15) {
					$error = "Tu nombre de usuario tiene que tener al menos 3 carácteres y máximo 15 carácteres";
				} else if ($longiContra < 6 || $longiContra > 40) {
					$error = "Tu contraseña tiene que tener al menos 6 carácteres y máximo 40 carácteres";
				} else {
					include 'call.php';
					$call = new CALL();
					$error ="Ha habido un problema al conectarse con la base de datos";
					if ($call->conexion()) {
						$variable = $call->register($usuario, $contra);
						if ($variable != 404) {
							if ($variable == -2) {
								$error = "Ya existe un usuario con ese nombre";
							} else {
								$error = "no error"; 
				      			//Guardamos una varibale sesión cen la variable usuario
							}
						}
						$call->closeConexion();
					}			
				}
			} else {
				$error = "El nombre sólo puede contener letras o números";
			}
		} else {
			$error = "Las contraseñas no son iguales";
		}
		echo $error;
	} else {
		echo "rellena todos los campos";
	}

?>

