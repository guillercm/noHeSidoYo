<?php 

	$user = $_POST["usuario"];
	$contra = $_POST["contra"];
	if (isset($user) && isset($contra)) {
		require 'call.php';
		$call = new CALL();
		$error = "Ha habido un problema al conectarse con la base de datos";
		if ($call->conexion()) {
			$variable = $call->login($user, $contra);
			if ($variable != 404) {
				if ($variable == -1) {
					$error = "Usuario y/o contraseña incorrecta";
				} else {
					
	      			session_start(); //Empieza la sesión
	      			$_SESSION['id'] = $variable;
	      			$nombreImg = $call->InfoUsuario($variable);
	      			if ($nombreImg != null) {
	      				if($row = $nombreImg) {
	      					$error = "no error";
	      					$_SESSION['nombre'] = $row["nombre"];
	      					$_SESSION['img_perfil'] = $row["img_perfil"];
	      				}
	      			}
	      			
	      			//Guardamos una varibale sesión cen la variable usuario
				}
			}
			$call->closeConexion();
		}
		echo $error;		
	} else {
		echo "rellena todos los campos";
	}


?>
