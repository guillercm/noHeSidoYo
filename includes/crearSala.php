<?php 
	$nombre = $_POST['nombre'];
	$rutaImg = $_POST['rutaImg'];
    $error = "0Error, no se pudo crear la sala";
	if (isset($nombre) && isset($rutaImg)) {
		include 'libreria.php';
        $codigo = crearSala();
        if (enviarComandoSala($codigo, "JUGADOR_LIDER", "$nombre|$rutaImg") ){
            echo $codigo;
        }else{
            echo $error;
        }
    }else{
        echo $error;
    }
?>