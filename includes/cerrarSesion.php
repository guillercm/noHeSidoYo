<?php
	/* PARA SALIR Y ELIMINAR LA SESIÓN */
	session_start();
	session_destroy();
	//header("Location: $direcion/login.php");
	exit();
?>