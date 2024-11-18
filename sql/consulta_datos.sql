select preguntas.identificador, REPLACE(CONVERT(concat(preguntas.enunciado,'\r\n',GROUP_CONCAT(respuestas.respuesta SEPARATOR '\r\n')) USING utf8), '"', "'") enunciado, 
       preguntas.respuesta, preguntas.norma
from transportes.preguntas
join transportes.respuestas on respuestas.pregunta = preguntas.id
group by preguntas.id;