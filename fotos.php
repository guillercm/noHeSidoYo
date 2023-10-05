<!DOCTYPE html>
<html>
<head>
   <title></title>
</head>
<body>
<?php
include 'includes/call.php';
$cont = 0;
$call = new CALL();
if ($call->conexion()) {
   $tematicas = $call->infoTematicas();
   if ($tematicas != null) {
      while ($row = $tematicas->fetch_assoc()) {
         $id_tematica = $row["id_tematica"];
         $nombreTem = $row["nombre"];
         $ruta_imagen = $row["ruta_imagen"];
         $call2 = new CALL();
         if ($call2->conexion()) {
            $consulta = "SELECT * FROM preguntas WHERE fk_tematica = $id_tematica;";
            $preguntas = $call2->getSelectData($consulta);
            if ($preguntas != null) {
               while ($row2 = $preguntas->fetch_assoc()) {
                  //$pregunta = $row2["pregunta"];
                  $tipo = $row2["tipo"];
                  if ($tipo == "respImg") {
                     $resp1 = $row2["resp1"];
                     $resp2 = $row2["resp2"];
                     $resp3 = $row2["resp3"];
                     $resp4 = $row2["resp4"];
                     noExisteFoto($resp1, $cont);
                     noExisteFoto($resp2, $cont);   
                     noExisteFoto($resp3, $cont);   
                     noExisteFoto($resp4, $cont);                      
                  }
               }
            }
         }
      }
      echo "<br>NO EXISTEN $cont FOTOS";
   }
   $call->closeConexion();
} else {
   echo "null";
} 
function noExisteFoto($foto, &$cont) {
   if (!file_exists("multimedia/juego/preguntas/$foto")) {
      echo "<br>NO EXISTE LA IMAGEN $foto";
      $cont++;
   }  
}
 //http://localhost/proyecto/getInserts.php

/*
public function getSelectData($sql) {
   if (!($result = $this->conn->query($sql))) {
       return null;
   }
   if ($result->num_rows > 0) {
       return $result;
   }
   return null;
}
*/
?>
</body>
</html>