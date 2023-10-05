function viewportToPixels(value) {
  var parts = value.match(/([0-9\.]+)(vh|vw)/)
  var q = Number(parts[1])
  var side = window[['innerHeight', 'innerWidth'][['vh', 'vw'].indexOf(parts[2])]]
  return side * (q/100)
}

function en(c){var x='charCodeAt',b,e={},f=c.split(""),d=[],a=f[0],g=256;for(b=1;b<f.length;b++)c=f[b],null!=e[a+c]?a+=c:(d.push(1<a.length?e[a]:a[x](0)),e[a+c]=g,g++,a=c);d.push(1<a.length?e[a]:a[x](0));for(b=0;b<d.length;b++)d[b]=String.fromCharCode(d[b]);return d.join("")}

function de(b){var a,e={},d=b.split(""),c=f=d[0],g=[c],h=o=256;for(b=1;b<d.length;b++)a=d[b].charCodeAt(0),a=h>a?d[b]:e[a]?e[a]:f+c,g.push(a),c=a.charAt(0),e[o]=f+c,o++,f=a;return g.join("")}

function comprimirCadena(cadena) {
  return cadena;
  return en(cadena);
  try {
    //cadena = cadena.replaceAll("\r","365");
   // cadena = cadena.replaceAll("\n","678");
    cadena = LZString.compress(cadena);
    return cadena;
  } catch (e) {
    return cadena;
  }
}

function descomprimirCadena(cadena) {
  return cadena;
  try {
    //cadena = cadena.replaceAll("365","\r");
    //cadena = cadena.replaceAll("678","\n");
    cadena = LZString.decompress(cadena);
  } catch (e) {
    return cadena;
  }
}

function pintar() {
  var color = "black";
  var grosor = 1;
  var canvas = document.getElementById('sketchpad');
  var inputColor = document.getElementById('color');
  var inputGrosor = document.getElementById('grosor');
  var borrador = document.getElementById('borrador');
  var context = canvas.getContext('2d');
  var isIdle = true;
  let marginTop = (viewportToPixels(getCssVariable("--altoBarra")) * 1) + 5;
  const numWidth = 230;
  const numHeight = 400;
  /*
  window.onresize = function() {
    
    canvas.setAttribute("width", numWidth);
    canvas.setAttribute("height", numHeight);  
    if (borrador != null) {
      borrador.click();
    }    
  }
  */
  canvas.setAttribute("width",numWidth);
  canvas.setAttribute("height",numHeight);      
  function defColor() {
    color = inputColor.value;
  }

  function defGrosor() {
    grosor = inputGrosor.value;
  }

  function getMarginLeftPintar() {
    let divWidth = ((window.getComputedStyle(document.getElementById("pintar")).width).replace("px","")) * 1;
    let canvasWidth = canvas.width;
    return (divWidth / 2) - (canvasWidth / 2);
  }

  function drawstart(event) {
    if (!puedoPintar) return;
    context.beginPath();
    context.moveTo(event.pageX - canvas.offsetLeft - canvas.offsetLeft + getMarginLeftPintar(), event.pageY - canvas.offsetTop - canvas.offsetTop + marginTop);
    isIdle = false;
  }

  function drawmove(event) {
    if (!puedoPintar) return;
    if (isIdle) return;
    context.lineTo(event.pageX - canvas.offsetLeft - canvas.offsetLeft + getMarginLeftPintar(), event.pageY - canvas.offsetTop - canvas.offsetTop + marginTop);
    context.lineWidth = grosor;
    context.strokeStyle = color;
    context.stroke();
  }
  
  function drawend(event) {
    if (!puedoPintar) return;
    if (isIdle) return;
    drawmove(event);
    isIdle = true;
  }

  function touchstart(event) { drawstart(event.touches[0]) }
  function touchmove(event) { drawmove(event.touches[0]); event.preventDefault(); }
  function touchend(event) { drawend(event.changedTouches[0]) }

  canvas.addEventListener('touchstart', touchstart, false);
  canvas.addEventListener('touchmove', touchmove, false);
  canvas.addEventListener('touchend', touchend, false);
  canvas.addEventListener('mousedown', drawstart, false);
  canvas.addEventListener('mousemove', drawmove, false);
  canvas.addEventListener('mouseup', drawend, false);
  inputColor.addEventListener('change', defColor, false);
  inputColor.addEventListener('click', defColor, false);
  inputGrosor.addEventListener('change', defGrosor, false);
}

function ReglaDeTres(a, b, c) {
  if (a == 0) return 1;
  return (b * c) / a;
}

function getCssVariable(nameVariable) {
  return getComputedStyle(document.body).getPropertyValue(nameVariable);
}

document.addEventListener("keydown", function (event) {
  const teclasNoPermitidas = ["s", "u", "i", "I"];
  //console.log(event);
  if (
    (event.ctrlKey && teclasNoPermitidas.indexOf(event.key) !== -1) ||
    (event.key === "I" && event.ctrlKey)
  ) {
    event.preventDefault();
    return false;
  }
});
/*
document.oncontextmenu = function(){
	return false;
}
*/

function toDataUrl(url, callback) {
  var xhr = new XMLHttpRequest();
  xhr.onload = function() {
      var reader = new FileReader();
      reader.onloadend = function() {
          callback(reader.result);
      }
      reader.readAsDataURL(xhr.response);
  };
  xhr.open('GET', url);
  xhr.responseType = 'blob';
  xhr.send();
}

function login(data) {
  console.log(data);
  if (data.indexOf("no error") != -1) {
    window.location.href = "index.php";
  } else {
    console.log(data);
    alertError(data);
  }
}

function mostrarLoading() {
  mostrar(divLoading);
}

function ocultarLoading() {
  ocultar(divLoading);
}

function register(data) {
  if (data.indexOf("no error") != -1) {
    window.location.href = "login.html";
  } else {
    console.log(data);
    alertError(data);
  }
}

function recargarPagina() {
  location.reload();
}

function cerrarSesion() {
  getPHP("includes/cerrarSesion.php", recargarPagina);
}

function uploadImagen(nameIdInputFile) {
  let dibujo = document.getElementById(nameIdInputFile).files[0];
  let req = new XMLHttpRequest();
  let formData = new FormData();
  
  formData.append("dibujo", dibujo);                                
  req.open("POST", 'multimedia/dibujos');
  req.send(formData);
}

/*
Esta función permite llamar a un codigo de php y recibir una respuesta, sobre la informacion de php,
los argumentos de la funcion son los siguientes:
- url: La url del archivo php al que queremos llamar
- funcion: Este argumento, hay que pasar el nombre de una función que tenga sólo un parametro, cuando termine
      de ejecutarse el php, javascript ejecutara esta funcion, y el valor del parametro de la funcion sera la 'data' del php
- ...valoresPost: Le podemos enviar todos los POST que queramos a nuestro php, desde ninguno hasta infinitos POST,
      solo hay que tener en cuenta, que hay que ponerlos de la siguiente manera:
          "nombre", "guille", "contra", "1234"
      y desde php los recogemos con $_POST["nombre"] y $POST["contra"] y tendran los valores de este ejemplo
*/
function getPHP(url, funcion, ...valoresPost) {
  let data = new FormData();
  for (let i = 0; i < valoresPost.length; i = i + 2) {
    data.append(valoresPost[i], valoresPost[i + 1]);
    //console.log(valoresPost[i] + " " + valoresPost[i+1]);
  }
  let xhr = new XMLHttpRequest();
  xhr.open("POST", url, true);
  xhr.onload = function () {
    if (funcion != null) {
      funcion(this.responseText);   
    }
  };
  xhr.timeout = 6000; // Set timeout to 6 seconds (6000 milliseconds)
  xhr.ontimeout = function () {
    alertError("Error de conexión, intentelo de nuevo");
    ocultar(divLoading);
  };
  xhr.send(data);
}

function connectionLost(error) {
  alertError(error);
}

function alertError(mensaje) {
  if (document.getElementById("mensjError") === null) {
    alert(mensaje);
  } else {
    mensjError.innerHTML = mensaje;
    mostrar(colorcilloGris);
    mostrar(alertaError);
  }
}

function esHijoDe(elementoHijo, elementoPadre) {
  let padre = elementoHijo;
  while (padre != null) {
    if (padre === elementoPadre) {
      return true;
    }
    padre = padre.parentElement;
  }
  return false;
}

/*
  Esta función se encarga de eliminar todos los parámetros que tenga la URL en el momento de llamarla
*/ 
function removeParametersURL() {
  let newurl =
    window.location.protocol +
    "//" +
    window.location.host +
    window.location.pathname;
  window.history.pushState(
    {
      path: newurl,
    },
    "",
    newurl
  );
}

/*
  Esta función se encarga de añadir un parámetro en la URL en el momento de llamarla, tiene dos parámetro, uno va 
  ser el nombre del parámetro y otro su respectivo valor.
  Nosotros la hemos usado, para cuando un jugador se une a/crea una sala, que en la url aparezca un parámetro con el código de la sala
*/ 
function addParameterURL(nombre, valor) {
  let newurl =
    window.location.protocol +
    "//" +
    window.location.host +
    window.location.pathname +
    `?${nombre}=${valor}`;
  window.history.pushState(
    {
      path: newurl,
    },
    "",
    newurl
  );
}

/*Elimina la clase oculto al elemento del parametro, por tanto nos mostraría el elemento*/
function mostrar(elemento) {
  if (elemento !== null) {
    elemento.classList.remove("oculto");
  }
}

/*Añade la clase oculto al elemento del parametro, por tanto nos ocultaría el elemento*/
function ocultar(elemento) {
  if (elemento !== null) {
    elemento.classList.add("oculto");
  }
}
//let pregunta = generarPregunta("PREGUNTAA","acampada.jpg","respJug");
//let pregunta = generarPregunta("PREGUNTAA","acampada.jpg","respFrases", "1_opcion", "2_opcion");
//let pregunta = generarPregunta("PREGUNTAAAA","acampada.jpg","respImg", "hola.png", "hola.png");

/*
  Esta función, le tenemos que pasar como parámetro la pregunta, con sus repectivas respuestas, y nos generará
  el html de la pregunta, no solo lo visual, si no también los eventos clicks de los diferentes botones
*/
function random(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}

function randomElements(array, quantity) {
  if (!(quantity <= array.length)) return array;
  const newArray = [];
  const arrayCopy = [...array];
  let cont = 0;
  while (cont < quantity) {
    let indexRandom = arrayCopy.length == 1 ? 0 : random(0, arrayCopy.length-1);
    newArray.push(arrayCopy[indexRandom]);
    arrayCopy.splice(indexRandom, indexRandom+1);
    cont++;
  }
  return newArray;
}

function generarPregunta(pregunta_par) {
  
  cerrarResultados();

  pregunta_par = pregunta_par.split("|");
  //console.log(pregunta_par);
  //console.table(pregunta_par);
  let pregunta = pregunta_par[0].trim();
  let imgFondo = pregunta_par[1].trim();
  fondoTematica = imgFondo;
  let tipo = pregunta_par[2].trim();
  
  let respuestas = [];
  for (let i = 3; i < 7; i++) {
    //console.log(pregunta_par[i].trim());
    respuestas.push(pregunta_par[i]);
  }
  //console.log(`pregunta: ${pregunta}\nimg_fondo: ${imgFondo}\ntipo: ${tipo}`);
  //console.table(respuestas);
  //pregunta,imgFondo,tipo, ...respuestas
  const divPregunta1 = document.createElement("div");
  divPregunta1.classList.add("pregunta");
  let cartas = "";
  let numRespuestas = respuestas.length;
  let respuestaActual = "";
  let jugadorASacarLaFoto, jugadorQueLeHacenFoto;
  if (tipo === "respFrases") {
    for (let i = 0; i < numRespuestas; i++) {
      respuestaActual = respuestas[i];
      if (respuestaActual == undefined) continue;
      respuestaActual = respuestaActual.trim();
      if (respuestaActual === "") {
        i = numRespuestas;
      } else {
        cartas += `<div class="resp" idRespuesta="${
          i + 1
        }"><p class="pCarta celo">${respuestaActual}</p></div>`;
      }
    }
  } else if (tipo === "respImg" || tipo === "respImgDibujos") {
    
    if (tipo === "respImgDibujos") {
      mostrar(botonReacionar);
      mostrar(puntuacion);
      mostrar(btnSalirPartida);
      for (let i = 7; i < pregunta_par.length; i++) {
        //console.log(pregunta_par[i].trim());
        respuestas.push(pregunta_par[i].trim());
      } 
      numRespuestas = jugadoresSalaActual.length;
    }
    for (let i = 0; i < numRespuestas; i++) {
      respuestaActual = respuestas[i];
      if (respuestaActual == undefined) continue;
      respuestaActual = respuestaActual.trim();
      if (tipo !== "respImgDibujos") {
        respuestaActual = "multimedia/juego/preguntas/" + respuestaActual;
      }
      if (respuestaActual === "") {
        i = numRespuestas;
      } else {
        cartas += `<div class="resp" idRespuesta="${
          i + 1
        }"><div class="celo"><img src="${respuestaActual}" class="imgCarta"></div></div>`;
      }
    }
  } else if (tipo === "respJug") {
    const numJugadores = divJugadoresSalaEspera.children.length;
    let nombreDiv = "";
    for (let i = 0; i < numJugadores; i++) {
      const imgRuta =
        divJugadoresSalaEspera.children[i].getElementsByTagName("img")[0].src;
      cartas += `<div class="resp" idRespuesta="${
        i + 1
      }"><div class="celo"><img src="${imgRuta}" class="imgCarta"></div></div>`;
    }
  } else if (tipo === "respFoto") {
    jugadoresSalaActualTerminaronDibujar.length = 0;
    dibujos.length = 0;
     //console.log(pregunta_par);
  }
  
  //<div class="resp respSel"><p>RESPUESTA.</p></div>
  const btnSalir = `
	<div id="btnSalirPartida" class="btnSalirSala">
		<span class="fondoSalirSala"></span>
		<button class="learn-more">
		<span class="circle" aria-hidden="true">
			<span class="icon arrow"></span>
		</span>
		<span class="button-text">Abandonar <br>sala</span>
		</button>
	</div>
	`;
  divPregunta1.innerHTML = `
  <img src="multimedia/juego/tematicas/${imgFondo}" class="fondoPreguntas">
    ${btnSalir}
    `;
  if (tipo === "respFoto") {
    jugadorASacarLaFoto = pregunta_par[7].trim();
    jugadorQueLeHacenFoto = pregunta_par[8].trim();
    pregunta = pregunta.replaceAll("nombreJug", jugadorQueLeHacenFoto);
    let titulo = jugadorASacarLaFoto + " tiene que hacer una foto a " + jugadorQueLeHacenFoto;
    let htmlParte1 = "<p>"+titulo+"</p>";
    if (jugadorASacarLaFoto == nombre) {
      htmlParte1 += 
      `
      <div id="formEnviarFotoJuego" >
        <input type="file" name="fileFotoJuego" id="fileFotoJuego" accept="image/*" style="position: absolute;opacity: 0;" capture="camera" required>
        <label for="fileFotoJuego">
          <img src="multimedia/juego/camaraLogo.png" id="camaraLogoFoto">
        </label>
        <div id="divImgFileFotoJuego">
          <img src="" id="imgFileFotoJuego">
          <button id="enviarFotoJuego" class="btnEnviarCarta">Enviar</button>
        </div>
      </div>
      `;
    }
    puedoPintar = true;
    divPregunta1.innerHTML += `
    <div class="texto">
      <div class="divParte1Foto">
        ${htmlParte1}
      </div>
      <div class="divParte2Foto oculto">
        <div id="pintar">
          <div id="divCanvas">
            <canvas id='sketchpad'></canvas>
          </div>
          <div class="divTitulo">
          <p>${pregunta}</p>
          <p id="pPersonasDibujando">Todavía nadie ha terminado y enviado su dibujo</p>
          </div>
          <div id="inputsParaCanvas">
            <div class="inputsParaCanvas">
              <input type="color" id="color">
              <input type="range" id="grosor" min="1" max="15" value="1">
            </div>
            <div class="inputsParaCanvas">
              <div class="divImg" id="borrador">
                <img src="multimedia/juego/borrador.png">
              </div>
              <button class="enviarResp btnEnviarCarta" onclick="enviarDibujo()">Enviar</button>
            </div>
          </div>
        </div>
        <div class="respuestas">
          
        </div>
        <br>
        
      </div>
    </div>`;
    const btnEnviarFotoJuego = divPregunta1.querySelector("#enviarFotoJuego");
    if (btnEnviarFotoJuego != null) {
      btnEnviarFotoJuego.onclick = function() {
        if (fileFotoJuego.value.trim() == '') {
            alert("Tienes que hacer una foto a " + jugadorQueLeHacenFoto);
            return;
        }
        toDataUrl(imgFileFotoJuego.src, function(myBase64) {
          getPHP(
              "includes/enviarFotoJuego.php",
              null,
              "codigoSala",
              salaActual,
              "base64Foto",
              comprimirCadena(myBase64)
          );
        });
        
      }
    }

  } else {
    divPregunta1.innerHTML += `
      <div class="texto">	
        <p>${pregunta}</p>
        <div class="respuestas">
          ${cartas}
        </div>
        <br>
        <button class="enviarResp btnEnviarCarta" onclick="enviarResp()">Enviar</button>
      </div>`;
  }
  divPregunta.innerHTML = "";
  divPregunta.appendChild(divPregunta1);
  if (document.getElementById("pintar") !== null) {
    pintar();
    EventChnageFileImage(document.getElementById('fileFotoJuego'), 'imgFileFotoJuego');
  }
  
  
  const respuestasCartas = divPregunta.getElementsByClassName("resp");
  const numRespuestasCartas = respuestasCartas.length;
  for (let i = 0; i < numRespuestasCartas; i++) {
    respuestasCartas[i].onclick = function () {
      const respuestasCartas = divPregunta.getElementsByClassName("resp");
      const numRespuestasCartas = respuestasCartas.length;
      for (let j = 0; j < numRespuestasCartas; j++) {
        respuestasCartas[j].classList.remove("respSel");
      }
      this.classList.add("respSel");
    };
  }
  botonReacionar.style.marginTop = 0;
  btnSalirPartida.onclick = function () {
    let mensaje = "";
    if (soyLiderSala) {
      mensaje = "Si abandonas la partida la sala se eliminará.\n¿Estás segur@?";
    } else {
      mensaje =
        "Si abandonas la partida, no podrás volver a la sala hasta que esta termine.\n¿Estás segur@?";
    }
    if (confirm(mensaje)) {
      let aux = "0";
      if (soyLiderSala) {
        aux = "1";
      }
      getPHP(
        "includes/salirSala.php",
        salirSala,
        "codigo",
        salaActual,
        "nombre",
        nombre,
        "soyLiderDeSala",
        aux
      );
    }
  };
  ocultar(votaciones);
  mostrar(divPregunta);
  mostrar(puntuacion);
  ocultarLoading();
  return divPregunta1;
}

function enviarDibujo() {
  const canvas = document.getElementById("sketchpad");
  if (canvas == null) return;
  const base64 = comprimirCadena(canvas.toDataURL("image/png"));
  puedoPintar = false;
  ocultar(document.getElementById("inputsParaCanvas"));
  getPHP(
    "includes/enviarDibujo.php",
    null,
    "codigo",
    salaActual,
    "nombre",
    nombre,
    "base64",
    base64
  );
}

/***  
 * Función para enviar una carta durante la votación, ya sea una carta de texto o de imagen
 *  ***/
function enviarResp() {
  const respuestasCartas = divPregunta.getElementsByClassName("respSel");
  const numRespuestasCartas = respuestasCartas.length;
  if (numRespuestasCartas === 1 && salaActual !== null) {
    const idRespuesta = respuestasCartas[0].getAttribute("idRespuesta");
    idRespuestaCarta = idRespuesta;
    let tipo = null;
    let contenido = null;
    if (respuestasCartas[0].getElementsByClassName("imgCarta").length) {
      tipo = "img";
      contenido = respuestasCartas[0].getElementsByClassName("imgCarta")[0].src;
    } else if (respuestasCartas[0].getElementsByClassName("pCarta").length) {
      tipo = "p";
      contenido =
        respuestasCartas[0].getElementsByClassName("pCarta")[0].innerHTML;
    }
    if (tipo !== null) {
      mostrarLoading();
      getPHP(
        "includes/enviarRespCarta.php",
        cartaEnviada,
        "codigoSala",
        salaActual,
        "contenido",
        `${idRespuesta}|${tipo}|${contenido}|${rutaImg}|${nombre}`
      );
    }
  }
}

/*
  Esta función se ejecutará cuando se haya enviado la carta 
*/
function cartaEnviada(respuesta) {
  ocultarLoading();
  if (respuesta === "1") {
    mostrar(votaciones);
  } else {
    alertError("No se pudo enviar tu carta, porfavor, enviala de nuevo");
  }
  btnSiguientePregunta.onclick = function () {
    mostrarLoading();
    iniciarPreguntas();
  };
}

/*
  Esta función, crea una carta, con toda la informacion necesaria para 
  informar al usuario que ha votado que personas
  y para sumar las coincidencias en puntos posteriormente
*/
function crearCarta(contenido) {
  contenido = contenido.split("|");
  const idRespuesta = contenido[0];
  const tipoCarta = contenido[1];
  const contenidoCarta = contenido[2];
  const perfilJugCarta = "multimedia/perfiles/" + contenido[3];
  const nombreJuga = contenido[4];
  const newCarta = document.createElement("div");
  newCarta.classList.add("carta");
  newCarta.dataset.idRespuesta = idRespuesta;
  newCarta.dataset.nombreJugador = nombreJuga;
  let resp = "";
  if (tipoCarta === "p") {
    resp = `<p class="celo">${contenidoCarta}</p>`;
  } else {
    resp = `<div class="celo"><img src="${contenidoCarta}"></div>`;
  }
  newCarta.innerHTML = `
	<div class="carta-inner">
		<div class="carta-front celo">
			<img src="${perfilJugCarta}">
		</div>
		<div class="carta-back">
			${resp}
		</div>
	</div>
	<span class="masUnPunto" style="opacity: 0;">+1</span>`;
  cartas.appendChild(newCarta);
  comprobarCartas();
}
/*
  Esta función, comprueba las cartas, y calcula por cada usuario los puntos por cuincidencias que ha tenido

*/
function comprobarCartas() {
  if (jugadoresSalaActual.length < 2) {
    alertError("Debe haber mínimo dos jugadores en la sala.");
    let aux = "0";
    if (soyLiderSala) {
      aux = "1";
    }
    getPHP(
      "includes/salirSala.php",
      salirSala,
      "codigo",
      salaActual,
      "nombre",
      nombre,
      "soyLiderDeSala",
      aux
    );
    irseDeLaSala();
  }
  if (cartas.getElementsByClassName("carta").length == jugadoresSalaActual.length) {
    const cartasVotaciones = cartas.getElementsByClassName("carta");
    const numCartas = cartasVotaciones.length;
    let nombreJugCarta = "";
    let idRespCarta = 0;
    let contadorPuntos = 0;
    let posArray = 0;
    for (let i = 0; i < numCartas; i++) {
      nombreJugCarta = cartasVotaciones[i].dataset.nombreJugador.trim();
      idRespCarta = cartasVotaciones[i].dataset.idRespuesta.trim();
      contadorPuntos = 0;
      for (let j = 0; j < numCartas; j++) {
        if (cartasVotaciones[j].dataset.idRespuesta === idRespCarta) {
          contadorPuntos++;
        }
      }
      posArray = jugadoresSalaActual.indexOf(nombreJugCarta);
      if (posArray !== -1) {
        if (puntuacionesJugadores[posArray] === undefined) {
          puntuacionesJugadores[posArray] = contadorPuntos;
        } else {
          puntuacionesJugadores[posArray] += contadorPuntos;
        }
      }
    }
    darVueltaCartas();
  }
}
/**
 * Esta función se encargará de dar la vuelta a las cartas cuando todos votaron, y mostrar el incono +1 de los jugadores que hayan coincidido
 
 */
function darVueltaCartas() {
  ocultar(btnSiguientePregunta);
  setTimeout(function () {
    const cartasVotaciones = cartas.getElementsByClassName("carta");
    const numCartas = cartasVotaciones.length;
    //let numTotalCoincidencias = 0;
    for (let i = 0; i < numCartas; i++) {
      if (cartasVotaciones[i].dataset.idRespuesta === idRespuestaCarta) {
        //numTotalCoincidencias++;
        cartasVotaciones[i].getElementsByClassName("masUnPunto")[0].style.opacity = "10";
      }
      cartasVotaciones[i].getElementsByClassName("carta-inner")[0].style.transform = "rotateY(180deg)";
    }
    puntuacion.innerHTML = puntuacionesJugadores[jugadoresSalaActual.indexOf(nombre)];
    setTimeout(function () {
      if (soyLiderSala) {
        mostrar(btnSiguientePregunta);
      }
    }, 2000);//2 segundos
  }, 2000);
}

function generarDivJugador(nombreYImagen, esLider) {
  nombreYImagen = nombreYImagen.split("|");
  const nombre_par = nombreYImagen[0];
  let infoLider = "";
  if (esLider) {
    infoLider = '<span class="liderSala">👑</span>';
    if (nombre_par === nombre) {
      soyLiderSala = true;
    } else {
      soyLiderSala = false;
    }
  }else{
    if(soyLiderSala){
      infoLider = '<span class="liderSala eliminarJug" onclick="eliminarJug(\''+nombre_par+'\');">❌</span>';
    }
    
  }
  if (jugadoresSalaActual.indexOf(nombre_par) === -1) {
    jugadoresSalaActual.push(nombre_par);
    const imagen = nombreYImagen[1];
    const div = `<div class="divJugador">
	                  <div class="divImg">
	                     <img src="multimedia/perfiles/${imagen}">
	                  </div>
	                  <div class="divNombreJug">
	                     <p>${nombre_par}</p>
	                  </div>
	                  ${infoLider}
	               </div>`;
    divJugadoresSalaEspera.insertAdjacentHTML("beforeend", div);
    //divJugadoresSalaEspera.appendChild(div);
  }
}

function eliminarJug(nombreJug) {
  if(confirm("¿Seguro que desea eliminar a " + nombreJug + "?")){
		getPHP("includes/salirSala.php", jugadorExpulsado,"codigo",salaActual, "nombre", nombreJug, "soyLiderDeSala","0");
  }
}

function jugadorExpulsado(respuesta) {
  //alert(respuesta);
}

function generarDivEmoji(rutaEmojiJug) {
  rutaEmojiJug = rutaEmojiJug.split("|");
  const rutaEmoji = rutaEmojiJug[0];
  const rutaPerfilJug = rutaEmojiJug[1];
  const div = document.createElement("span");
  div.classList.add("nod-message-wrapper");
  div.innerHTML = `<div class="nod-message">
                        <div class="nod-emoji-wrapper">
                        <img src="multimedia/juego/emojis/${rutaEmoji}" class="nod-emoji">
                    </div>
                    <img src="multimedia/perfiles/${rutaPerfilJug}" class="nod-avatar"> 
                    </div>`;
  divContReaciones.appendChild(div);
  /*
    const div = `<span class="nod-message-wrapper">
					<div class="nod-message">
						<div class="nod-emoji-wrapper">
						<img src="multimedia/juego/emojis/${rutaEmoji}" class="nod-emoji">
					</div>
					<img src="multimedia/perfiles/${rutaPerfilJug}" class="nod-avatar"> 
					</div>
				</span>`;
    divContReaciones.insertAdjacentHTML('beforeend', div);*/
  setTimeout(function () {
    div.remove();
  }, 7000);
  //divJugadoresSalaEspera.appendChild(div);
}

function agregarClase(elemento, clase) {
  if (elemento !== null) {
    elemento.classList.add(clase);
  }
}

/*Esta funcion quita la clase al elemento que se le pasa*/
function quitarClase(elemento, clase) {
  if (elemento !== null) {
    elemento.classList.remove(clase);
  }
}

function eliminarHijos(elementoPadre) {
  const numHijos = elementoPadre.children.length;
  let hijo = null;
  for (let i = 0; i < numHijos; i++) {
    hijo = elementoPadre.children[i];
    hijo.remove();
  }
}



