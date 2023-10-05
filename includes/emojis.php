<?php
    $ruta = 'multimedia/juego/emojis';
    $fotosEmojis = scandir($ruta, 1);
    array_pop($fotosEmojis);//sirve para quietar los dos puntos que dan el scandir
    array_pop($fotosEmojis);
?>
<div id="botonReacionar">
	<img src="<?php echo $ruta; ?>/pulgar.gif" id="abrirEmojis">
	<div id="contReaciones" class="oculto">
<?php
    foreach ($fotosEmojis as $emoji) { // emoji = fotosemoji[i]
?>
  	<div class="divEmoji emojiReaccionar" ruta="<?php echo "$emoji"; ?>">
  		<img src="<?php echo "$ruta/$emoji"; ?>">
  	</div>
<?php
	}
?>
    </div>
</div>

<!-- Donde se agregan todas las reacciones -->
<div id="divContReaciones">
	<!--
		<span class="nod-message-wrapper">
			<div class="nod-message">
				<div class="nod-emoji-wrapper">
					<img src="<?php echo $ruta; ?>/risa.gif" class="nod-emoji">
				</div>
				<img src="<?php echo $ruta; ?>/angel.gif" class="nod-avatar"> 
			</div>
		</span>
	-->
</div>
