<?php 

	$codigoABuscar = $_POST['codigoABuscar'];
	$nombre = $_POST['nombre'];
	$rutaImg = $_POST['rutaImg'];
	if (isset($codigoABuscar) && isset($nombre) && isset($rutaImg)) {
		include 'libreria.php';
		if (existeSala($codigoABuscar)) {
			if (hayPartidaIniciadaAhora($codigoABuscar)) {
				echo "Ya hay una partida iniciada, cuando se acabe, te podrás unir a la sala";
			} else {
				if (enviarComandoSala($codigoABuscar, "JUGADOR", "$nombre|$rutaImg")) {
					echo "1" . $codigoABuscar;
				} else {
					echo "Ha habido algún error, inténtelo de nuevo";
				}				
			}
		} else {
			echo "Esa sala no existe";
		}
	} else {
		echo "Introduce el codigo que quieres buscar";
	}

?>