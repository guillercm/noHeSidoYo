<?php
    $codigo = $_POST["codigoSala"];
    $preguntaResps = $_POST["preguntaResps"];
    if (isset($codigo) && isset($preguntaResps)) {
        include "libreria.php";
        if (enviarComandoSala($codigo, "PREGUNTA", $preguntaResps)) {
            echo "1";
        } else {
            echo "0";
        }
    } else {
        echo "0";
    }
?>