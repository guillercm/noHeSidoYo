<?php

	class CALL {

        private $servername = "localhost";
        private $username = "root"; 
        private $password = "1234";


        private $dbname = "nohesidoyo";
    	private $conn = null;
    	private $nomVarOut = "@p_out";



		public function conexion() {
			//PARA QUE FUNCIONE EL TRY CATCH
			mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
            mysqli_report(MYSQLI_REPORT_ALL & ~MYSQLI_REPORT_INDEX);
			try {
				$this->conn = new mysqli($this->servername, $this->username, $this->password, $this->dbname);
    			$this->conn->set_charset("utf8");
    			return true;
    		} catch (Exception $e) {
    			$this->conn = null;
   				return false;
			}
		}

		private function getOutParameterStoredProcedure($nombreProc) { //PROCEDIMIENTOS ALMACENADOS CON OUTS
			try {
	    		$nomVarOut = $this->nomVarOut;

	   			// $sql = "SET $nomVarOut = -66;";
                // echo $sql;
				// if (!($this->conn->query($sql))) {
				//     return 404;
				// }
				$sql = "CALL " . $nombreProc . ",$nomVarOut);";
                //echo $sql;
				if (!($this->conn->query($sql))) {
				    return 404;
				}
	   			$sql = "SELECT $nomVarOut;";
				if (!($resultado = $this->conn->query($sql))) {
				    return 404; 
				}
				$fila = $resultado->fetch_assoc();
				return $fila[''.$nomVarOut.''];
			} catch (Exception $e) {
				return 404;
			}
		}

    	public function login($user, $pass) { //Ejecuta el procedimiento almacenado del login
    		if ($this->conn != null) {
    			$user = $this->avoidSQLInyection($user);
    			$pass = $this->avoidSQLInyection($pass);
    			$pass = $this->encryptPassword($pass);
    			$sql = "Login('" . $user . "','" . $pass . "'";
    			return $this->getOutParameterStoredProcedure($sql);
    		}
    		return 404;
    	}

    	public function register($user, $pass) { //Ejecuta el procedimiento almacenado del registro
    		if ($this->conn != null) {
    			//$user = $this->avoidSQLInyection($user);
    			//$pass = $this->avoidSQLInyection($pass);
    			//$pass = $this->encryptPassword($pass);
	    		$sql = "Register('" . $user . "','" . $pass . "'";
	    		return $this->getOutParameterStoredProcedure($sql);    			
    		}
    		return 404;
    	}

		public function infoTematicas() { //Ejecuta el procedimiento almacenado de las tematicas
			$sql = "CALL CargarTematicas();";
			if (!($result = $this->conn->query($sql))) {
			    return null;
			}
			if ($result->num_rows > 0) {
				return $result;
			}
			return null;
		}

        public function CargarPreguntas($idTematica) { //Ejecuta el procedimiento almacenado de las preguntas
            $sql = "CALL CargarPreguntas($idTematica,12);";
            if (!($result = $this->conn->query($sql))) {
                return null;
            }
            if ($result->num_rows > 0) {
                return $result;
            }
            return null;
        }

        public function getSelectData($sql) {
           if (!($result = $this->conn->query($sql))) {
               return null;
           }
           if ($result->num_rows > 0) {
               return $result;
           }
           return null;
        }

        public function cambiarPerfil($idUsuario,$rutaImg) { //Ejecuta el procedimiento almacenado de cambiar el usuario
            $idUsuario = $this->parseInt($idUsuario);
            $sql = "CALL CambiarPerfil($idUsuario,'$rutaImg');";
            if ($this->conn->query($sql)) {
                return true;
            }
            return false;
        }

    	private function avoidSQLInyection($texto) {//Cuando detecta que hay carateres especiales para barras para que lo tome como literal y asi evitar las inyeccione sql
    		$longi = strlen($texto);
    		$texto2 = "";
    		for ($i = 0; $i < $longi; ++$i) {
    			if ($texto[$i-1] != "\\") {
    				$letra = $texto[$i];
    				if ($letra == "'" || $letra == "\"" || $letra == ";" || $letra == "`" || $letra == "-") {
    					$texto2 = $texto2 . "\\" . $letra;
    				} else {
    					$texto2 = $texto2 . $letra;
    				}
    			}
    		}
    		return $texto2;
    	}

		public function InfoUsuario($id) {
			$sql = "CALL InfoUsuario(".$id.");";
			if (!($result = $this->conn->query($sql))) {
			    return null;
			}
			if ($result->num_rows > 0) {
				return $result->fetch_assoc();
			}
			return null;
		}

        private function parseInt($numero) { //Te covierte una string en un int
            if (gettype($numero) == "string") {
                $numero = (int) preg_replace('/[^0-9]/', '', $numero);
            }
            return $numero;
        }

    	private function encryptPassword($texto) {
			return $texto;
    		// return sha1($texto);
    	}

    	public function closeConexion() {//Cierra la conexion
    		if ($this->conn != null) {
    			$this->conn->close();
    		}
    	}
	}
?>
