<?php

	function base64_to_image($base64_string, $codigo) {

		// open the output file for writing
		$output_file = 'multimedia/dibujos/' . $codigo . rand(0,9999) . uniqid() . '.png';
		$ifp = fopen( '../' . $output_file, 'wb' ); 

		// split the string on commas
		// $data[ 0 ] == "data:image/png;base64"
		// $data[ 1 ] == <actual base64 string>
		$data = explode( ',', $base64_string );

		// we could add validation here with ensuring count( $data ) > 1
		fwrite( $ifp, base64_decode( $data[ 1 ] ) );

		// clean up the file resource
		fclose( $ifp ); 

		return $output_file; 
	}

	function existeSala($codigo) {//Comprueba si existe la sala(.txt) del parametro
		if (file_exists("../salas/$codigo.txt")) {
			return true;
		}
		return false;
		//return file_exists("../salas/$codigo.txt");
	}

	function hayPartidaIniciadaAhora($codigo) {
		//comando = todo lo que pase en una sala
		$sala = file_get_contents("../salas/$codigo.txt", true);//para obtener los comandos de uan sala
		$sala = explode("\n", $sala); //Crea un array en cada comando
		$cont = sizeof($sala)-1;//numero de comandos
		while ($cont > -1) {//Empieza desde el último comando
			if (startsWith($sala[$cont], 'PREGUNTA: ')) {//Si hay partida
				if (startsWith($sala[$cont], 'PREGUNTA: NO_MAS_PREGUNTAS')) { //NO hay partida
					return false;
				} else {
					return true;
				}
			}
			$cont = $cont -1;
		}
		return false;
	}

	function eliminarSala($codigo) {
		unlink("../salas/$codigo.txt");
		$dibujos = '../multimedia/dibujos';
		$files = scandir($dibujos);
		foreach ($files as $file)
		{
			if (substr($file,0, 6) === $codigo) {
				unlink($dibujos . '/' . $file);
			}
		}
	}

	function enviarComandoSala($codigo, $comando, $texto) { //envia lo que se esta haciedno en la sala/partida al txt y lo escribe
		$intentos = 40;
		$seguir = true;
		$file = null;
		do {
			$file = fopen("../salas/$codigo.txt","a");
			if ($file == null) {
				return false;
			}
			if (fwrite($file, "$comando: $texto" . 'NUEVO_COMANDO: ')) {// PHP_EOL = \n
				$seguir = false;
			} else {
				$intentos = $intentos - 1;
				usleep(200000);
			}
		} while ($intentos > 0 && $seguir);
		fclose($file);
		return !$seguir;
	}

	function crearSala() {//crea la sala(.txt)
		$codigoNuevaSala = "";
		$seguir = true;
		do {
			for ($i = 0; $i < 6; $i++) {
				$codigoNuevaSala = $codigoNuevaSala . chr(rand(65,90));
			}
			if (!existeSala($codigoNuevaSala)) {
				$seguir = false;
			} else {
				$codigoNuevaSala = "";
			}
		} while ($seguir);
		$file = fopen("../salas/$codigoNuevaSala.txt","a");
		fclose($file);
		return $codigoNuevaSala; //retorna el código de la sala
	}

	function eliminarSalasAntiguas() { //Elimina las salas antiguas cuando han pasado 15 minutos inactivos
		$ficheros = scandir("salas", 1); //abre la carpeta sala y coge todos los ficheros
		$numFicheros = count($ficheros)-2;
		$cont = 0;
		$segundosTieneUnMinuto = 60;
		$minutosFicheroInactivo = 1;
		$segundosInactivos = $minutosFicheroInactivo * $segundosTieneUnMinuto;
		while ($cont < $numFicheros) {
			$f = $ficheros[$cont];
			$noEsReciente = (time()-filemtime('salas/'.$f) > $segundosInactivos);//Creea un boolean para saber si ha estado inactivo los 15 mins
			if ($noEsReciente) {
				$f = str_replace(".txt","",$f);
				eliminarSala($f);
			}
			$cont = $cont + 1;
		}
	}
	
	function countFilesFolder($dir) { //Cuenta los ficheros de una carpeta
        return (count(scandir($dir)) - 2);
    }

    function startsWith($string, $startString) {  //Para ver si empiezan por...
    	$len = strlen($startString); 
    	return (substr($string, 0, $len) === $startString); 
	} 

?>