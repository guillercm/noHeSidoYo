<!DOCTYPE html>
<html>

<head>
   <title>No he sido yo!</title>
   <link rel="shortcut icon" href="multimedia/juego/noHeSidoYo.png">
   <link href="styles/styles.css" rel="stylesheet" type="text/css">
   <link href="styles/menu.css" rel="stylesheet" type="text/css">
   <link href="styles/emojis.css" rel="stylesheet" type="text/css">
   <!-- <script src="scripts/lz-string.min.js"></script> -->
   <script src="scripts/libreria.js"></script>
   <script type="text/javascript" src="scripts/script.js"></script>
   <meta charset="utf-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <!-- Reescalar en el móvil y las ultimas dos para quitar el zoom en los móviles -->
   <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">

</head>

<body>
   <div id="pantallaCarga">
      <img class="logoPantallaCarga" src="multimedia/juego/noHeSidoYo.png">
      <img class="barajaCartas" src="multimedia/juego/cartas.png">
      <progress id="progreso">Progreso</progress>
      <?php
      // Array de mensajes que aparecen en la pantalla de carga
      $mensajes = array(
         "Si el líder abandona la sala, está se eliminará.",
         "¿No os decidís que temática escoger?, si escogéis la aleatoria podrán salir preguntas de cualquier temática.",
         "Pulsa en crear una sala para crear una sala.", "¿Te aburres?, Echate un no he sido yo con tus panas."
      );
      $random = rand(0, count($mensajes) - 1);
      ?>
      <p id="info"><?php echo $mensajes[$random] ?></p>
   </div>
   <div id="divLoading" class="oculto">
      <img src="multimedia/juego/loading.gif">
   </div>
   <!--<div id="divImagenGrande">
      <img src="" id="imagenGrande">
   </div>
   -->
   <?php

   session_start(); //Empieza la sesión
   $idUsu = $_SESSION['id']; //se obtiene el id que hemos guardado al hacer login
   $nombre = $_SESSION['nombre'];
   $img_perfil = $_SESSION['img_perfil'];

 
   if (isset($idUsu) && isset($nombre) && isset($img_perfil)) {//eliminara las salas que ya habian sido creadas, 
      include 'includes/libreria.php';
      eliminarSalasAntiguas();

      $ruta = 'multimedia/juego/tematicas';
      $fotosTematicas = scandir($ruta, 1); //obtine una array con el nombre de los archivos
      do {//Esto pone las imágenes de fondo aleatorio
         $numFoto = rand(0, (count($fotosTematicas) - 2) - 1);
         $imgFondo = $fotosTematicas[$numFoto];
      } while ($imgFondo == "java"); //al haber una carpeta que se llama java dentro de la carpeta tematicas, se la salta para no poder elegirla
      $img = "multimedia/perfiles/" . $img_perfil; //en caso de logearse se queda en la pagina
   ?>
      <script type="text/javascript"> //manda el nombre y la ruta de la imagen a javascript. perdon a todos a todos los programadores profesionales
         const nombre = '<?php echo $nombre; ?>';
         const rutaImg = '<?php echo $img_perfil; ?>';
      </script>
      <div id="fondoGris" class="oculto"></div>
      <div class="navigation-bar">
         <div id="navigation-container">
            <!-- HAY DOS IMAGENES PARA PROBAR COMO SE VERÍA -->
            <img class="logo" id="abrirSubmenu" src="<?php echo $img; ?>">
            <img class="logo logoMarca" src="multimedia/juego/noHeSidoYo.png">
            <!-- LA IMAGEN ESTA ESTÁ recursos/multimedia -->
            <div class="dropdown-content oculto" id="subMenu">
               <img class="logo logoSubMenu" src="<?php echo $img; ?>">
               <ul>
                  <li><span><?php echo $nombre; ?></span></li>
               </ul>
               <div id="irModificarPerfil">Modificar Perfil</div>
               <div id="divCerrarSesion">Cerrar Sesión</div>
            </div>
         </div>
      </div>
   <?php
   } else { //se le redirige al login
      header('Location: login.html');
   }
   ?>
   <div id="divJuego" style="background-image: url('multimedia/juego/tematicas/<?php echo $imgFondo; ?>');">
      <div id="inicio">
         <div id="divImgLogo">
            <img src="multimedia/juego/noHeSidoYo.png">
         </div>
         <div id="botonesJugar">
            <center>
               <button class="botonAnimation" id="btnCrearSala">Crear sala</button>
               <button class="botonAnimation" id="btnUnirseASala">Unirse a una sala</button>
            </center>
         </div>
      </div>
      <div id="divUnirseASala" class="oculto">
         <div id="divFormBuscarSala">
            <div id="divCodigo">
               <h2>¿TIENES UN CÓDIGO?</h2>
               <br>
               <span>
                  <form method="POST" id="formBuscarSala">
                     <input class="gate" type="text" id="inputNumSala" placeholder="XXXXXX" minlength="6" maxlength="6" onkeypress="return /[a-z]/i.test(event.key)" autocomplete="off" required>
                     <label for="inputNumSala">Código</label><br><br>
                     <input type="submit" name="unirseASala" id="unirseASala" value="Unirse a esta sala">
                  </form>
               </span>
            </div>
         </div>
      </div>
      <div id="divSala" class="oculto">
         <img src="multimedia/juego/mesa3.jpg" id="fondoSala">
         <div id="puntuacion" class="oculto">0</div>
         <?php
         include 'includes/emojis.php';
         ?>
         <div id="btnSalirSala" class="btnSalirSala">
            <span class="fondoSalirSala"></span>
            <button class="learn-more">
               <span class="circle" aria-hidden="true">
                  <span class="icon arrow"></span>
               </span>
               <span class="button-text">Abandonar <br>sala</span>
            </button>
         </div>
         <div id="eligirTematicasYJugadores">
            <div class="divJugadores" id="divJugadoresSalaEspera">
               <!--
                     <div class="divJugador">
                        <div class="divImg">
                           <img src="multimedia/perfiles/defecto.jpg">
                        </div>
                        <div class="divNombreJug">
                           <p>NombreJugador</p>
                        </div>
                        <span class="liderSala">Líder</span>
                     </div>
                -->
            </div>
            <div id="divElegirTematicas">
               <?php //Cargar la tabla de las tematicas para que el líder de la sala escoja la que quieren jugar
               include 'includes/call.php';
               $call = new CALL();
               if ($call->conexion()) {
                  $tematicas = $call->infoTematicas();
                  if ($tematicas != null) {
                     while ($row = $tematicas->fetch_assoc()) {
                        $id_tematica = $row["id_tematica"];
                        $nombreTem = $row["nombre"];
                        $ruta_imagen = $row["ruta_imagen"];
               ?>
                        <div class="divTematica" idTematica="<?php echo $id_tematica; ?>" style="background-image: url('multimedia/juego/tematicas/<?php echo $ruta_imagen ?>');"><?php echo $nombreTem; ?></div>
               <?php
                     }
                  }
                  $call->closeConexion();
               }

               ?>
            </div>
         </div>
         <input type="text" id="inputCopiarCodigo">
         <div id="botones_copy_start">
            <button id="btnCopiarCodigo">
               <span class="info">Pulsa para copiar</span>
               <span id="spanCopiarCodigo">XXXXXX</span>
            </button>
            <span id="btnEmpezarPartida" class="start-btn">START</span>
         </div>
         <div id="divPregunta" class="oculto"></div>
         <div id="votaciones" class="oculto">
            <div id="cartas">
            </div>
            <button id="btnSiguientePregunta" class="oculto">
               Siguiente
            </button>
         </div>
         <div id="resultados" class="oculto">
            <!-- BOTON SALIR DE LOS RESULTADOS -->
            <div id="salirResultados" class="close-container">
               <div class="leftright"></div>
               <div class="rightleft"></div>
               <label class="close">salir</label>
            </div>
            <!-- TABLA DE CLASIFICAION DE LA PARTIDA -->
            <div class="leaderboard">
               <h1>
                  <svg class="ico-cup">
                     <use xlink:href="#cup"></use>
                  </svg>
                  Puntuaciones
               </h1>
               <ol id="listaPuntuaciones">
               </ol>
            </div>
            <svg>
               <symbol id="cup" x="0px" y="0px" width="25px" height="26px" viewBox="0 0 25 26" enable-background="new 0 0 25 26" xml:space="preserve">
                  <path fill="#F26856" d="M21.215,1.428c-0.744,0-1.438,0.213-2.024,0.579V0.865c0-0.478-0.394-0.865-0.88-0.865H6.69
                        C6.204,0,5.81,0.387,5.81,0.865v1.142C5.224,1.641,4.53,1.428,3.785,1.428C1.698,1.428,0,3.097,0,5.148
                        C0,7.2,1.698,8.869,3.785,8.869h1.453c0.315,0,0.572,0.252,0.572,0.562c0,0.311-0.257,0.563-0.572,0.563
                        c-0.486,0-0.88,0.388-0.88,0.865c0,0.478,0.395,0.865,0.88,0.865c0.421,0,0.816-0.111,1.158-0.303
                        c0.318,0.865,0.761,1.647,1.318,2.31c0.686,0.814,1.515,1.425,2.433,1.808c-0.04,0.487-0.154,1.349-0.481,2.191
                        c-0.591,1.519-1.564,2.257-2.975,2.257H5.238c-0.486,0-0.88,0.388-0.88,0.865v4.283c0,0.478,0.395,0.865,0.88,0.865h14.525
                        c0.485,0,0.88-0.388,0.88-0.865v-4.283c0-0.478-0.395-0.865-0.88-0.865h-1.452c-1.411,0-2.385-0.738-2.975-2.257
                        c-0.328-0.843-0.441-1.704-0.482-2.191c0.918-0.383,1.748-0.993,2.434-1.808c0.557-0.663,1-1.445,1.318-2.31
                        c0.342,0.192,0.736,0.303,1.157,0.303c0.486,0,0.88-0.387,0.88-0.865c0-0.478-0.394-0.865-0.88-0.865
                        c-0.315,0-0.572-0.252-0.572-0.563c0-0.31,0.257-0.562,0.572-0.562h1.452C23.303,8.869,25,7.2,25,5.148
                        C25,3.097,23.303,1.428,21.215,1.428z M5.238,7.138H3.785c-1.116,0-2.024-0.893-2.024-1.99c0-1.097,0.908-1.99,2.024-1.99
                        c1.117,0,2.025,0.893,2.025,1.99v2.06C5.627,7.163,5.435,7.138,5.238,7.138z M18.883,21.717v2.553H6.118v-2.553H18.883
                        L18.883,21.717z M13.673,18.301c0.248,0.65,0.566,1.214,0.947,1.686h-4.24c0.381-0.472,0.699-1.035,0.947-1.686
                        c0.33-0.865,0.479-1.723,0.545-2.327c0.207,0.021,0.416,0.033,0.627,0.033c0.211,0,0.42-0.013,0.627-0.033
                        C13.195,16.578,13.344,17.436,13.673,18.301z M12.5,14.276c-2.856,0-4.93-2.638-4.93-6.273V1.73h9.859v6.273
                        C17.43,11.638,15.357,14.276,12.5,14.276z M21.215,7.138h-1.452c-0.197,0-0.39,0.024-0.572,0.07v-2.06
                        c0-1.097,0.908-1.99,2.024-1.99c1.117,0,2.025,0.893,2.025,1.99C23.241,6.246,22.333,7.138,21.215,7.138z" />
               </symbol>
            </svg>
         </div>
      </div>
   </div>
   <div id="colorcilloGris" class="oculto"></div>
   <div id="alertaError" class="oculto">
      <div id="container">
         <div id="error-box">
            <div class="dot"></div>
            <div class="dot two"></div>
            <div class="face2">
               <div class="eye"></div>
               <div class="eye right"></div>
               <div class="mouth sad"></div>
            </div>
            <div class="shadow move"></div>
            <div class="message">
               <h1 class="alert">Error!</h1>
               <p id="mensjError">oh no, no has elegido una temática.</p>
            </div>
            <button class="button-box" id="cerrarAlert">
               <h1 class="red">Aceptar</h1>
            </button>
         </div>
      </div>
   </div>
   <div id="divFormModificarPerfil" class="oculto">

      <form method="POST" action="includes/modificarPerfil.php" enctype="multipart/form-data">
         <h2 id="nombreUsuario">Cambia tu foto</h2>
         <input type="file" name="fileFoto" id="fileFoto" accept="image/*" style="position: absolute;opacity: 0;" required>
         <label for="fileFoto">
            <img src="<?php echo $img; ?>" id="imgCambiar">
         </label>
         <input type="submit" name="" value="Guardar" id="botonGuardar">
      </form>
      <button id="cerrarModificarP"></button>
   </div>
</body>
</html>