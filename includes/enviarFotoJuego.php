<?php
    $codigo = $_POST["codigoSala"];
    $base64Foto = $_POST["base64Foto"];
    include "libreria.php";
    $ruta = base64_to_image($base64Foto, $codigo);
    if (isset($codigo) && isset($base64Foto)) {
        
        if (enviarComandoSala($codigo, "ENVIAR_FOTO_JUEGO", $ruta)) {
            echo "1";
        } else {
            echo "0";
        }
    } else {
        echo "0";
    }
?>