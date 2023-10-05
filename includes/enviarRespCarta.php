<?php
    $codigo = $_POST["codigoSala"];
    $contenido = $_POST["contenido"];
    if (isset($codigo) && isset($contenido)) {
        include "libreria.php";
        if (enviarComandoSala($codigo, "CARTA", $contenido)) {
            echo "1";
        } else {
            echo "0";
        }
    } else {
        echo "0";
    }
?>