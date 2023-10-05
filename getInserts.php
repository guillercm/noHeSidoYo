<!DOCTYPE html>
<html>
<head>
   <title></title>
</head>
<body>
<?php
include 'includes/call.php';
$call = new CALL();
if ($call->conexion()) {
   $tematicas = $call->infoTematicas();
   if ($tematicas != null) {
      $espacios = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
      $numPregsTotales = 0;
      $infoPregTem = "";
      echo "DELETE FROM preguntas;<br>ALTER TABLE preguntas AUTO_INCREMENT = 1;<br><br>";
      while ($row = $tematicas->fetch_assoc()) {
         $id_tematica = $row["id_tematica"];
         $nombreTem = $row["nombre"];
         $ruta_imagen = $row["ruta_imagen"];
         $nombreTemMayus = strtoupper($nombreTem);
         echo "<br>-- PREGUNTAS DE $nombreTemMayus<br><br>";
         echo "INSERT INTO preguntas (pregunta, resp1, resp2, resp3, resp4, fk_tematica, tipo) VALUES " . "<br>";
         $call2 = new CALL();
         if ($call2->conexion()) {
            $consulta = "SELECT * FROM preguntas WHERE fk_tematica = $id_tematica;";
            $preguntas = $call2->getSelectData($consulta);
            if ($preguntas != null) {
               $pregs = "";
               $numPregT = 0;
               while ($row2 = $preguntas->fetch_assoc()) {
                     $pregunta = $row2["pregunta"];
                     $resp1 = $row2["resp1"];
                     $resp2 = $row2["resp2"];
                     $resp3 = $row2["resp3"];
                     $resp4 = $row2["resp4"];
                     $tipo = $row2["tipo"];
                     $pregunta = str_replace("'","\\'",$pregunta);
                     $resp1 = str_replace("'","\\'",$resp1);
                     $resp2 = str_replace("'","\\'",$resp2);
                     $resp3 = str_replace("'","\\'",$resp3);
                     $resp4 = str_replace("'","\\'",$resp4);
                     $numPregsTotales++;
                     $numPregT++;
                     if ($resp1 == "" || $resp1 == null) {
                        $resp1 = " ";
                     }
                     if ($resp2 == "" || $resp2 == null) {
                        $resp2 = " ";
                     }
                     if ($resp3 == "" || $resp3 == null) {
                        $resp3 = " ";
                     }
                     if ($resp4 == "" || $resp4 == null) {
                        $resp4 = " ";
                     }
                     $pregs .= "('$pregunta', '$resp1', '$resp2', '$resp3', '$resp4', $id_tematica, '$tipo'),<br>";   
               }
               $infoPregTem .= "<br>$espacios TOTAL PREGUNTAS DE $nombreTemMayus: $numPregT ";
               $pregs = trim($pregs, ",<br>");
               echo $pregs . ";" . "<br><br><br>";
            }
         }
      }
      echo "/*";
      echo $infoPregTem . "<br>";
      echo "$espacios TOTAL DE PREGUNTAS: $numPregsTotales";
      echo "<br>*/";
   }
   $call->closeConexion();
} else {
   echo "null";
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