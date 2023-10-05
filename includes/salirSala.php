<?php
    $codigo = $_POST["codigo"];
    $nombre = $_POST["nombre"];
    $soyLiderDeSala = $_POST["soyLiderDeSala"];

    if (isset($codigo) && isset($nombre) && isset($soyLiderDeSala)) {
        include "libreria.php";
        if ($soyLiderDeSala == "1") {
            eliminarSala($codigo);
            echo "1";
        } else {
            if (enviarComandoSala($codigo, "SALIR_SALA", $nombre)){
                echo "1";
            } else {
                echo "0";
            }            
        }
    } else {
        echo "0";
    }
?>