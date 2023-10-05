<?php 

    session_start();
    $idUsu = $_SESSION['id']; //se obtiene el id que hemos guardado al hacer login
    $nombreAnt = $_SESSION['nombre'];
    $img_perfilAnt = $_SESSION['img_perfil'];

    //$nuevoNombre = $_POST['nuevoNombre'];
    //$fileFoto = $_FILES['fileFoto'];
    $nuevoPerfil = $_FILES["fileFoto"];
    $error = "Hubo algún poblema al subir la imagen, porfavor, intentelo más tarde";
    if (isset($nuevoPerfil)) {
        $target_dir = "../multimedia/perfiles/";
        $check = getimagesize($nuevoPerfil["tmp_name"]);
        // PARA COMPROBAR QUE SEA UNA IMAGEN
        if ($check !== false) {
            $target_file = $target_dir . basename($nuevoPerfil["name"]);
            $tipoImg = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
            $formatos = array("jpg", "png", "jpeg", "svg");
            $found = array_search($tipoImg, $formatos);
            $nombreNuevaFoto = uniqid() . rand(0,999) ."." . $tipoImg;
            if ($found !== false) { //si es formato permitido
                if (move_uploaded_file($_FILES["fileFoto"]["tmp_name"], $target_dir . $nombreNuevaFoto)) {
                    require 'call.php';
                    $call = new CALL();
                    if ($call->conexion()) {
                        if ($call->cambiarPerfil($idUsu, $nombreNuevaFoto)) {
                            if ($img_perfilAnt != "defecto.jpg") {
                                unlink($target_dir . $img_perfilAnt);
                            }
                            $_SESSION['img_perfil'] = $nombreNuevaFoto;
                            $error = "no error";
                        } else {
                            unlink($target_dir . $nombreNuevaFoto);
                        }
                    } else {
                        unlink($target_dir . $nombreNuevaFoto);
                    }
                }
            } else {
                $error = 'Usa un formato de imágen de tipo "jpg", "png", "jpeg", "svg"';
            }
        } else {
            $error = "Selecione una imagen porfavor.";
        }
        if ($error != "no error") {
            echo "<script>alert('$error');</script>";
        }        
    }
    echo "<script>window.location.href = '../index.php';</script>";
    //header("Location: ../index.php");

?>