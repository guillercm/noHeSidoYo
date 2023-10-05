<?php 
	
	$codigoSala = $_POST['codigoSala'];
	$rutaEmoji = $_POST['rutaEmoji'];
	$rutaImgJug = $_POST['rutaImgJug'];
	if (isset($codigoSala) && isset($rutaEmoji) && isset($rutaImgJug)) {
		include 'libreria.php';
		if (enviarComandoSala($codigoSala, "EMOJI", $rutaEmoji ."|". $rutaImgJug)) {
			echo "1";
		} else {
			echo "0";
		}
	} else {
		echo "0";
	}
	
?>