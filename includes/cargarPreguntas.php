<?php 

	$idTematica = 1;
	$error = "0Error, hubo algún error inesperado al cargar las preguntas, inténtelo de nuevo";
	if (isset($_POST['idTematica'])) {
		$idTematica = $_POST['idTematica'];
	}
  	include 'call.php';
  	$call = new CALL();
  	if ($call->conexion()) {
    	$preguntas = $call->CargarPreguntas($idTematica);
     	if ($preguntas != null) {
     		$pregs = "";
        	while ($row = $preguntas->fetch_assoc()) {
           		$pregunta = $row["pregunta"];
           		$resp1 = $row["resp1"];
           		$resp2 = $row["resp2"];
           		$resp3 = $row["resp3"];
           		$resp4 = $row["resp4"];
           		$tipo = $row["tipo"];
           		$ruta_imagen = $row["ruta_imagen"];
           		$pregs = $pregs . "|||" . $pregunta . "|" . $ruta_imagen . "|" . $tipo . "|" . $resp1 . "|" . $resp2 . "|" . $resp3 . "|" . $resp4; 
        	}
        	$pregs = trim($pregs, "|||");
        	echo $pregs;
     	} else {
     		echo $error;
     	}
     	$call->closeConexion();
  	} else {
     	echo $error;
    }
?>