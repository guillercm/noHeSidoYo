<?php

    if (isset($_POST["codigo"]) && isset($_POST["nombre"]) && isset($_POST["base64"])) {
        $codigo = $_POST["codigo"];
        $nombre = $_POST["nombre"];
        $base64Foto = $_POST["base64"];
        include "libreria.php";
        $ruta = base64_to_image($base64Foto, $codigo);
        if (enviarComandoSala($codigo, "ENVIAR_DIBUJO", $nombre . "|" . $ruta)) {
            echo "1";
        } else {
            echo "0";
        }
    } else {
        echo "0";
    }
?>