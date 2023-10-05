//(function(){
let salaActual = null; //Código de la sala de juego
const jugadoresSalaActual = [];
const puntuacionesJugadores = []; 
const jugadoresSalaActualTerminaronDibujar = [];
const dibujos = [];
let contadorLineasSala = 0;
let primerRecargaSala = true;
let soyLiderSala = false;
let preguntasJuego = [];
let numPreguntaActual = 0;
let idRespuestaCarta = "1";
let puedoPintar = true;

let fondoTematica = "";



document.addEventListener("DOMContentLoaded", function() {
	const publicidad = document.getElementsByTagName("a")[0];
	if (publicidad !== undefined) {
		ocultar(publicidad);
	}
	progreso.className = 'llenandose1';
	clickSubmenu();
	clickCerrarSesion();
	clicks();

	//ocultarEmojis();
	getCodigoURL(); //LO ULTIMO
});

//Abrir el menu del perfil
function clickSubmenu() {
	const subMenu = document.getElementById('subMenu');
	window.onclick = function(event) {
		let elementoClickado = event.target;
		//console.log(elementoClickado);
	  	if (elementoClickado === abrirSubmenu) {
	  		if (salaActual === null) {
	  			subMenu.classList.toggle("oculto");
	  		}
	  	} else if (!esHijoDe(elementoClickado, subMenu)) {
	  		ocultar(subMenu);
	  	}
	}
}
 
function clickCerrarSesion() {
	const divCerrarSesion = document.getElementById('divCerrarSesion');
	divCerrarSesion.onclick = cerrarSesion;
}


function random(a,b) {
    return Math.round(Math.random()*(b-a)+parseInt(a));
}

/*
Esta función se encarga de meter a un jugador en la sala que metamos en el parámetro, en caso de que la sala tenga el 
formato correcto,(6 letras mayúsculas), llamaremos a un php para comprobar si esa sala existe, y en caso de existir,
nos mete en ella, y si no nos da error porque no existe la sala
*/
function entrarASala(codigo) {
	codigo = codigo.toString().toUpperCase().trim();
	if (/([A-Z]{6})/.test(codigo)) {
		getPHP("includes/buscarSala.php", buscarSala,"codigoABuscar",codigo, "nombre", nombre, "rutaImg", rutaImg);
	} else {
		alertError("El código debe de tener 6 letras");
	}
}

function EventChnageFileImage(inputFile, imageId) {
	if (inputFile == null) return;
	inputFile.addEventListener("change",function(event) {
		const image = document.getElementById(imageId);
		if (image == null) return;
		image.src = URL.createObjectURL(event.target.files[0]);	
	});
}
function clicks() {
	// MOSTRAR Y OCULTAR "MODIFICAR PERFIL"
	irModificarPerfil.onclick = function(){
		mostrar(divFormModificarPerfil);
		ocultar(subMenu);
	}

	// CERRAR "MODIFICAR PERFIL"
	cerrarModificarP.onclick = function(){
		ocultar(divFormModificarPerfil);
	}


	// MUESTRA LA IMAGEN SELECIONADA DESDE EL ORDENADOR PARA CAMBIAR LA DE PERFIL
	EventChnageFileImage(fileFoto, 'imgCambiar');

	// MOSTRAR PANTALLA PARA INTRODUCIR CODIGO DE LA SALA
	const divUnirseASala = document.getElementById('divUnirseASala');
	btnUnirseASala.onclick = function() {
		mostrar(divUnirseASala);
		mostrar(fondoGris);
	}

	// CREA UNA SALA NUEVA
	btnCrearSala.onclick = function() {
		mostrarLoading();
		getPHP("includes/crearSala.php", crearSala,"nombre", nombre, "rutaImg", rutaImg);
	}

	
	fondoGris.onclick = function() {
		ocultarBuscadorSala();
	}

	formBuscarSala.addEventListener("submit", function(e) {
		e.preventDefault();
		entrarASala(inputNumSala.value);
		inputNumSala.value = "";
	});

	btnEmpezarPartida.onclick = function() {
		if (soyLiderSala) {
			if(jugadoresSalaActual.length > 1){
				const divTematicaActiva = divElegirTematicas.getElementsByClassName("tematicaActiva");
				if (divTematicaActiva.length === 1) {
					let numTematica = divTematicaActiva[0].getAttribute("idtematica").replace(".","");
					if (!isNaN(numTematica)) {
						mostrarLoading();
						numTematica *= 1;
						cerrarResultados();
						getPHP("includes/cargarPreguntas.php", cargarPreguntas,"idTematica",numTematica);
					}
				} else {
					alertError("No hay ninguna temática selecionada");
				}
			}else{
				alertError("Se necesitan al menos dos jugadores para comenzar una partida.");
			}		
		} else {
			alertError("El líder de la sala está eligiendo temática...");
		}
		
	}

	//
	abrirEmojis.onclick = function() {
		if (abrirEmojis.classList.contains('noEnviarEmoji')) {
			ocultar(contReaciones);
		} else {
			contReaciones.classList.toggle("oculto");
		}
	}
	const emojis = document.getElementById('botonReacionar').getElementsByClassName('divEmoji');
	const numEmojis = emojis.length;
	let emoji = null;
	for (let i = 0; i < numEmojis; i++) {
		emoji = emojis[i];
		emoji.onclick = function() {
			enviarEmoji(this.getAttribute("ruta"), rutaImg);
			ocultar(contReaciones);
			agregarClase(abrirEmojis, "noEnviarEmoji");
		}
	}
	const divTematicas = divElegirTematicas.getElementsByClassName("divTematica");
	const numTematicas = divTematicas.length;
	let divTematica = null;
	const nombreClaseTematica = "tematicaActiva";
	for (let i = 0; i < numTematicas; i++) {
		divTematica = divTematicas[i];
		divTematica.onclick = function() {
			if (soyLiderSala) {
				for (let j = 0; j < numTematicas; j++) {
					divTematicas[j].classList.remove(nombreClaseTematica);
				}
				this.classList.add(nombreClaseTematica);		
			}
		}
	}
	salirResultados.onclick = function() {
		cerrarResultados();
	}
	//console.log(numEmojis);
	cerrarAlert.onclick = function() {
		ocultar(alertaError);
		ocultar(colorcilloGris);
	}
}

function cerrarResultados() {
	ocultar(divPregunta);
	ocultar(resultados);
	botonReacionar.style.marginTop = "var(--altoBarra)";
}

function cargarPreguntas(preguntas) {
	if (preguntas[0] === "0") {
		alertError(preguntas.replace("0",""));
	} else {
		preguntasJuego = preguntas.split("|||");
		iniciarPreguntas();		
	}
}

function iniciarPreguntas() {
	if (soyLiderSala && salaActual !== null && preguntasJuego.length !== 0) {
		if (preguntasJuego[numPreguntaActual] === undefined) {
			preguntasJuego[numPreguntaActual] = "NO_MAS_PREGUNTAS";
		} else {
			if (!(preguntasJuego[numPreguntaActual].includes("|respFoto|"))) {
				preguntasJuego[numPreguntaActual] = preguntasJuego[numPreguntaActual].replace("nombreJug", jugadoresSalaActual[random(0,jugadoresSalaActual.length-1)]);
			} else {
				const arrayJugs = randomElements(jugadoresSalaActual, 2);
				let jugadorASacarLaFoto = arrayJugs[0].replace("\n","").replace("\r","").trim();
				let jugadorQueLeHacenFoto = arrayJugs[1].replace("\n","").replace("\r","").trim();
				preguntasJuego[numPreguntaActual] += "|" + jugadorASacarLaFoto + "|" + jugadorQueLeHacenFoto;
			} 
			//alert(preguntasJuego[numPreguntaActual]);
		}
		mostrarLoading();
		getPHP("includes/enviarPregunta.php", preguntaEnviada,"codigoSala",salaActual,"preguntaResps",preguntasJuego[numPreguntaActual].replace("\n","").replace("\r",""));
	}  
}

function preguntaEnviada(respuesta) {
	if (respuesta === "1") {
		numPreguntaActual++;
		ocultar(btnSiguientePregunta);
	} else {
		alertError("No se pudo cargar la siguiente pregunta, intentalo de nuevo porfavor");
	}
}

function enviarEmoji(rutaEmoji, rutaImgJug) {
	if (!abrirEmojis.classList.contains('noEnviarEmoji')) {
		getPHP("includes/enviarEmoji.php", emojiEnviado,"codigoSala",
		salaActual,"rutaEmoji",rutaEmoji, "rutaImgJug", rutaImgJug);
	}
}

function emojiEnviado(respuesta) {
	if (respuesta === "0") {
		//console.log("No se pudo enviar el emoji");
	} else {
		setTimeout(function(){ 
			quitarClase(abrirEmojis, "noEnviarEmoji");
		}, 2000);
	}
}

function crearSala(sala){
	if(sala[0] === "0"){
		sala = sala.replace("0", "");
		alertError(sala);
	} else {
		addParameterURL("codigo",sala);
		mostrar(divSala);
		quitarTematicaActiva();
		ocultar(inicio);
		salaActual = sala;
		contadorLineasSala = 0;
		primerRecargaSala = true;
		soyLiderSala = true;
		ponerBotonCopiar(sala);
	}
	ocultarLoading();
}

function quitarTematicaActiva() {
	const divTematicas = divElegirTematicas.getElementsByClassName("divTematica");
	const numTematicas = divTematicas.length;
	const nombreClaseTematica = "tematicaActiva";
	for (let i = 0; i < numTematicas; i++) {
		for (let j = 0; j < numTematicas; j++) {
			divTematicas[j].classList.remove(nombreClaseTematica);
		}
	}
}

function ponerBotonCopiar(sala){
	spanCopiarCodigo.innerHTML = sala;
	inputCopiarCodigo.value = sala;
	btnCopiarCodigo.onclick = function() {
		copiarAlPortapapeles(inputCopiarCodigo);
	}
	btnSalirSala.onclick = function(){
		let aux = "0";
		if (soyLiderSala) {
			aux = "1";
		}
		getPHP("includes/salirSala.php", salirSala,"codigo",sala, "nombre", nombre, "soyLiderDeSala",aux);
	}
}

// MOSTRAR RESULTADOS DE LA PARTIDA
/* <ol id="listaPuntuaciones">
                     <!-- <li>
                        <mark>Jerry Wood</mark>
                        <small>315</small>
                     </li> -->
                  </ol> */
function quitarTematicaActiva() {
	const tActiva = document.getElementsByClassName("tematicaActiva")[0];
	if (tActiva !== undefined) {
		tActiva.classList.remove("tematicaActiva");
	}
}
function mostrarResultados() {
	quitarTematicaActiva();
	ocultar(puntuacion);
	ocultar(votaciones);
	mostrar(resultados);
	puntuacion.innerHTML = "0";
	let longi = jugadoresSalaActual.length;
	let aux;
	for(let i=0;i<longi;i++) {
		for(let j=0;j <longi-i;j++) {
			if (puntuacionesJugadores[j] < puntuacionesJugadores[j+1]) {
				aux=puntuacionesJugadores[j];
				puntuacionesJugadores[j]=puntuacionesJugadores[j+1];
				puntuacionesJugadores[j+1]=aux;

				aux=jugadoresSalaActual[j];
				jugadoresSalaActual[j]=jugadoresSalaActual[j+1];
				jugadoresSalaActual[j+1]=aux;
			}
		}
	}

	listaPuntuaciones.innerHTML="";

	

	let newLi = null;
	let newMark = null;
	let newSmall = null;
	for (jug in jugadoresSalaActual) {
		newLi = document.createElement("li");
		newMark = document.createElement("mark");
		newSmall = document.createElement("small");
		newMark.innerHTML = jugadoresSalaActual[jug];
		newSmall.innerHTML = puntuacionesJugadores[jug];
		newLi.appendChild(newMark);
		newLi.appendChild(newSmall);
		listaPuntuaciones.appendChild(newLi);
	}
	divPregunta.innerHTML = "";
	puntuacionesJugadores.length = 0;
	preguntasJuego.length = 0;
	numPreguntaActual = 0;
	ocultarLoading();
}

function salirSala(respuesta){
	irseDeLaSala();/*
	const numJugadores = divJugadoresSalaEspera.children.length;
	for (let i = 0; i < numJugadores; i++) {
		divJugadoresSalaEspera.children[i].remove();
	}*/
}

function buscarSala(sala) {
	const encontroLaSala = sala[0];
	if (encontroLaSala === "1") {
		sala = sala.replace("1","");
		salaActual = sala;
		mostrar(divSala);
		quitarTematicaActiva();
		ocultarBuscadorSala();
		ocultar(inicio);
		addParameterURL("codigo",sala);
		ponerBotonCopiar(sala);
	} else {
		alertError(sala);
		removeParametersURL();
	}
}

function copiarAlPortapapeles(elemento) {
  //const caja = document.querySelector("#informacion");
  elemento.select();
  document.execCommand('copy');
  //Remover seleccion
  //window.getSelection().removeAllRanges(); 
  //En un input si quitamos el focus por
  //consecuente se quita la seleccion asi que
  //se puede omitir el removeallranges
  elemento.blur();
}

function ocultarBuscadorSala() {
	ocultar(divUnirseASala);
	ocultar(fondoGris);
}

setInterval(function(){
	if (salaActual !== null) {
		//console.clear();					SADASD
		leerTxt(`salas/${salaActual}.txt`);	
	}
}, 1000);

/*
Esta función, interpreta los comandos enviados a la sala, y actuá en consecuencia, llamando a funciones determinadas,
enviandoles como parametros el argumento del comando
*/
function leerTxt(ruta) {
	fetch(ruta, {cache: "no-store"}).then(async (data) => {
	    if (data.ok) {
	        data = await data.text();
	        const lines = data.split('NUEVO_COMANDO: ');
			console.log(lines);
		      lines.pop();
		      const longi = lines.length;
		      while (contadorLineasSala < longi) {
		      	let linea = lines[contadorLineasSala];
		      	if (linea.startsWith('EMOJI: ') && primerRecargaSala === false) {
		      		linea = linea.replace('EMOJI: ',"");
		      		generarDivEmoji(linea);
		      	} else if (linea.startsWith('JUGADOR: ')) {
		      		linea = linea.replace('JUGADOR: ',"");
		      		generarDivJugador(linea, false);
		      	} else if (linea.startsWith('PREGUNTA: ') && primerRecargaSala === false) {
		      		linea = linea.replace('PREGUNTA: ',"");
		      		cartas.innerHTML = "";
		      		if (linea.indexOf("NO_MAS_PREGUNTAS") !== -1) {
		      			//console.log("JUEGO TERMINADO");
		      			mostrarResultados();
		      		} else {
						try {
							generarPregunta(linea);	
						} catch (error) {
							console.log(error);
						}
		      			
		      		}
		      	} else if (linea.startsWith('JUGADOR_LIDER: ')){
					linea = linea.replace('JUGADOR_LIDER: ',"");
		      		generarDivJugador(linea, true);
				} else if (linea.startsWith("SALIR_SALA")){
					linea = linea.replace('SALIR_SALA: ',"");
					salirJugador(linea);
				} else if (linea.startsWith("CARTA") && primerRecargaSala === false) {
					linea = linea.replace('CARTA: ',"");
					crearCarta(linea);
				} else if (linea.startsWith("ENVIAR_FOTO_JUEGO") && primerRecargaSala === false) {
					linea = linea.replace('ENVIAR_FOTO_JUEGO: ',"");
					enviarFotoJuego(linea);
				} else if (linea.startsWith("ENVIAR_DIBUJO") && primerRecargaSala === false) {
					linea = linea.replace('ENVIAR_DIBUJO: ',"");
					dibujoEnviado(linea);
				}
		      	contadorLineasSala++;
		      }
		      primerRecargaSala = false;
	    } else {
	    	irseDeLaSala();
	    }
	}).catch(e => irseDeLaSala())
}

function dibujoEnviado(respuesta) {
	respuesta = respuesta.split("|");
	let nombreUs = respuesta[0];
	let base64 = descomprimirCadena(respuesta[1]);
	jugadoresSalaActualTerminaronDibujar.push(nombreUs);
	dibujos[jugadoresSalaActual.indexOf(nombreUs)] = base64;
	const pPersonasDibujando = document.getElementById("pPersonasDibujando");
	if (pPersonasDibujando == null) return;
	let nombresUsu = "";
	let conta = 0;
	for (let i = 0; i < jugadoresSalaActual.length; i++) {

		if (!(jugadoresSalaActualTerminaronDibujar.includes(jugadoresSalaActual[i]))) {
			nombresUsu += jugadoresSalaActual[i] + ", ";
			conta++;
		}
	}

	nombresUsu = nombresUsu.trim();
	nombresUsu = nombresUsu.length > 1 ? nombresUsu.substring(0, nombresUsu.length -1 ) : nombresUsu;
	nombresUsu = nombresUsu.replace(/,(?=[^,]*$)/, ' y');
	let mensaje = "";
	if (conta == 0) {
		mensaje = "";
		
		let dibujosStr = dibujos.join('|');
		for (let i = 0; i < 4; i++) {
			dibujosStr += " |";
		}
		let newPregunta = "¿CUÁL ES EL MEJOR DIBUJO?|" + fondoTematica + "|respImgDibujos|" + dibujosStr;
		preguntasJuego.push(newPregunta);
		mostrarLoading();
		iniciarPreguntas();
	} else if (conta == 1) {
		if (nombresUsu == nombre) {
			mensaje = "Sólo faltas tú por acabar y enviar tu dibujo";
		} else {
			mensaje = "Sólo falta " + nombresUsu + " por acabar y enviar su dibujo";
		}
	} else {
		mensaje = "Faltan por acabar y enviar su dibujo a " + nombresUsu;
	}
	pPersonasDibujando.innerHTML = mensaje;
}

function enviarFotoJuego(base64Foto) {
	
	let divs = document.getElementsByClassName("divParte1Foto");
	if (divs.length != 1) return;
	ocultar(divs[0]);
	divs = document.getElementsByClassName("divParte2Foto");
	if (divs.length != 1) return;
	mostrar(divs[0]);
	let imgPintar = document.getElementById("imgPintar");
	ocultar(botonReacionar);
	ocultar(puntuacion);
	ocultar(btnSalirPartida);
	var canvas = document.getElementById("sketchpad");
	var ctx = canvas.getContext("2d");
	
	var image = new Image();
	base64Foto = descomprimirCadena(base64Foto);
	image.src = base64Foto;
	const borrador = document.getElementById("borrador");
	function funcionBorrar() {
		ctx.fillStyle = "#FFFFFF";
		ctx.clearRect(0, 0, canvas.width, canvas.height);
		ctx.rect(0, 0, canvas.width, canvas.height);
		ctx.fill();
		if (image.height > canvas.height) {
			image.width = ReglaDeTres(image.height, image.width, canvas.height);
			image.height = canvas.height;
		}
		ctx.drawImage(image, (canvas.width / 2) - (image.width / 2), (canvas.height / 2) - (image.height / 2), image.width, image.height);
	}
	image.onload = function() {
		//ctx.drawImage(image, 0, 0);
		funcionBorrar();
	};
	borrador.onclick = function() {
		funcionBorrar();
	}
	if (imgPintar == null) return;
	imgPintar.src = base64Foto;
}

function salirJugador(jugador){
	if (jugador === "YO") {
		jugador = nombre;
	}
	jugador = jugador.trim();
	let posicionJugador = jugadoresSalaActual.indexOf(jugador);
	if (posicionJugador !== -1){
		if (jugador === nombre){
			if (!primerRecargaSala) {
				salaActual = "no";
			}
		} else {
			jugadoresSalaActual.splice(posicionJugador, 1);
			const numJugadores = divJugadoresSalaEspera.children.length;
			let nombreDiv = '';
			for (let i = 0; i < numJugadores; i++) {
				nombreDiv = divJugadoresSalaEspera.children[i].getElementsByTagName('p')[0].innerHTML;
				if (nombreDiv === jugador) {
					divJugadoresSalaEspera.children[i].remove();
				}
			}
		}
	}
	if (!votaciones.classList.contains('oculto')) {
		comprobarCartas();
	}
}


function irseDeLaSala() {
	//console.log(divJugadoresSalaEspera);
	ocultar(divLoading);
	quitarTematicaActiva();
	jugadoresSalaActual.length=0;
	puntuacionesJugadores.length = 0;
	salaActual = null; 
	contadorLineasSala = 0;
	primerRecargaSala = true;
	soyLiderSala = false;
	ocultar(divPregunta);
	ocultar(divSala);
	mostrar(inicio);
	ocultar(votaciones);
	ocultar(resultados);
	removeParametersURL();
	divJugadoresSalaEspera.innerHTML = "";
	numPreguntaActual = 0;
	preguntasJuego.length = 0;
	ocultar(puntuacion);
	puntuacion.innerHTML = "0";
}

function getCodigoURL() {
	window.addEventListener("load", function() {
		const valores = window.location.search;
		const urlParams = new URLSearchParams(valores);
		let codigoUrl = urlParams.get('codigo');
		if (codigoUrl !== null) {
			codigoUrl = codigoUrl.toString();
			if (codigoUrl.length === 6) {
				entrarASala(codigoUrl);
			}
		}
		progreso.className = 'llenandose2';
		pantallaCarga.remove();
	}, false);
}
//http://localhost/proyecto/index.php?codigo=CYKSUC


//})();