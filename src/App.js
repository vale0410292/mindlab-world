import React, { useState } from 'react';

const App = () => {
  const [seccion, setSeccion] = useState('inicio');
  const [nombre, setNombre] = useState('');
  const [registrado, setRegistrado] = useState(false);
  const [estadoAnimo, setEstadoAnimo] = useState(3);
  const [tiempoEnfoque, setTiempoEnfoque] = useState(0);
  const [puntuacion, setPuntuacion] = useState(0);
  const [nivel, setNivel] = useState(1);
  const [juegoMemoria, setJuegoMemoria] = useState([]);
  const [secuenciaUsuario, setSecuenciaUsuario] = useState([]);
  const [palabras, setPalabras] = useState([]);
  const [palabraOculta, setPalabraOculta] = useState('');
  const [respuesta, setRespuesta] = useState('');
  const [mensaje, setMensaje] = useState('');

  const iniciarJuegoMemoria = () => {
    const secuencia = Array.from({length: 4 + nivel}, () => Math.floor(Math.random() * 4));
    setJuegoMemoria(secuencia);
    setSecuenciaUsuario([]);
    setMensaje('Observa la secuencia...');
    setTimeout(() => setMensaje('Ahora repítela!'), 2000);
  };

  const iniciarJuegoPalabras = () => {
    const lista = ['CONCENTRACION', 'MEMORIA', 'ATENCION', 'ENFOQUE', 'AVANCE', 'LUZ', 'FUERZA', 'CRECIMIENTO'];
    const elegida = lista[Math.floor(Math.random() * lista.length)];
    setPalabras(elegida.split(''));
    setPalabraOculta(elegida);
    setRespuesta('');
  };

  const registrarUsuario = (e) => {
    e.preventDefault();
    if(nombre.trim()) setRegistrado(true);
  };

  const guardarProgreso = () => {
    setTiempoEnfoque(prev => prev + 1);
    if(puntuacion >= nivel * 50) setNivel(prev => prev + 1);
    setMensaje('Progreso guardado! Sigue asi!');
    setTimeout(() => setMensaje(''), 3000);
  };

  return (
    <div style={{fontFamily: 'Arial, sans-serif', minHeight: '100vh', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white', padding: '20px'}}>
      
      <nav style={{display: 'flex', gap: '10px', flexWrap: 'wrap', justifyContent: 'center', marginBottom: '30px'}}>
        {['inicio', 'registro', 'juegos', 'seguimiento', 'avances', 'consejos'].map(s => (
          <button key={s} onClick={() => setSeccion(s)} style={{padding: '10px 15px', borderRadius: '20px', border: 'none', background: seccion === s ? '#ffd700' : 'rgba(255,255,255,0.2)', color: 'white', fontWeight: 'bold', cursor: 'pointer'}}>
            {s.charAt(0).toUpperCase() + s.slice(1)}
          </button>
        ))}
      </nav>

      {seccion === 'inicio' && (
        <div style={{textAlign: 'center', maxWidth: '800px', margin: '0 auto'}}>
          <h1 style={{fontSize: '3em', marginBottom: '10px'}}>LUMINAR: Entrenador de Concentracion y Memoria</h1>
          <p style={{fontSize: '1.2em', marginBottom: '30px'}}>La herramienta completa para fortalecer tu mente, mejorar tu atencion y guardar todo lo importante con facilidad.</p>
          <div style={{background: 'rgba(255,255,255,0.1)', padding: '25px', borderRadius: '15px', marginBottom: '20px'}}>
            <h3>Que encontraras aqui?</h3>
            <ul style={{textAlign: 'left', fontSize: '1.1em'}}>
              <li>Juegos para entrenar la memoria y la atencion</li>
              <li>Registro de como te sientes cada dia</li>
              <li>Seguimiento de tus niveles y puntos</li>
              <li>Consejos practicos para concentrarte mejor</li>
            </ul>
          </div>
          <button onClick={() => setSeccion('registro')} style={{padding: '15px 30px', fontSize: '1.2em', borderRadius: '30px', border: 'none', background: '#ffd700', color: '#667eea', fontWeight: 'bold', cursor: 'pointer'}}>
            Empieza ahora!
          </button>
        </div>
      )}

      {seccion === 'registro' && !registrado && (
        <div style={{maxWidth: '500px', margin: '0 auto', textAlign: 'center'}}>
          <h2>Bienvenido! Cuentanos quien eres</h2>
          <form onSubmit={registrarUsuario} style={{display: 'flex', flexDirection: 'column', gap: '15px', marginTop: '20px'}}>
            <input type="text" placeholder="Tu nombre completo" value={nombre} onChange={(e) => setNombre(e.target.value)} style={{padding: '15px', borderRadius: '10px', border: 'none', fontSize: '1em'}} required />
            <button type="submit" style={{padding: '15px', borderRadius: '10px', border: 'none', background: '#ffd700', color: '#667eea', fontWeight: 'bold', fontSize: '1.1em', cursor: 'pointer'}}>
              Guardar y continuar
            </button>
          </form>
        </div>
      )}

      {registrado && seccion === 'registro' && (
        <div style={{textAlign: 'center'}}>
          <h2>Hola {nombre}! Ya estas registrado</h2>
          <p>Ahora puedes usar todas las funciones y ver como mejoras cada dia.</p>
        </div>
      )}

      {seccion === 'juegos' && registrado && (
        <div style={{maxWidth: '900px', margin: '0 auto'}}>
          <h2 style={{textAlign: 'center'}}>Juegos para tu Memoria y Concentracion</h2>
          
          <div style={{background: 'rgba(255,255,255,0.1)', padding: '20px', borderRadius: '15px', marginBottom: '20px'}}>
            <h3>Juego de Secuencias</h3>
            <p>Recuerda y repite el orden para entrenar tu memoria.</p>
            <button onClick={iniciarJuegoMemoria} style={{padding: '10px 20px', borderRadius: '10px', border: 'none', background: '#ffd700', color: '#667eea', fontWeight: 'bold', margin: '10px 0', cursor: 'pointer'}}>
              Iniciar juego (Nivel {nivel})
            </button>
            {mensaje && <p style={{fontWeight: 'bold'}}>{mensaje}</p>}
            {juegoMemoria.length > 0 && (
              <div style={{display: 'flex', gap: '10px', justifyContent: 'center', margin: '15px 0'}}>
                {['Rojo', 'Azul', 'Verde', 'Amarillo'].map((color, i) => (
                  <button key={i} onClick={() => {
                    const nuevaSecuencia = [...secuenciaUsuario, i];
                    setSecuenciaUsuario(nuevaSecuencia);
                    if(nuevaSecuencia.length === juegoMemoria.length) {
                      if(JSON.stringify(nuevaSecuencia) === JSON.stringify(juegoMemoria)) {
                        setPuntuacion(prev => prev + 25);
                        setMensaje('Correcto! +25 puntos');
                      } else {
                        setMensaje('Incorrecto. Intentalo de nuevo');
                      }
                      setTimeout(() => setMensaje(''), 3000);
                    }
                  }} style={{fontSize: '1em', border: 'none', borderRadius: '10px', padding: '15px', cursor: 'pointer', background: ['#ef4444','#3b82f6','#22c55e','#eab308'][i], color: 'white'}}>{color}</button>
                ))}
              </div>
            )}
          </div>

          <div style={{background: 'rgba(255,255,255,0.1)', padding: '20px', borderRadius: '15px'}}>
            <h3>Recuerda la palabra</h3>
            <p>Escribe la palabra oculta para entrenar tu memoria.</p>
            <button onClick={iniciarJuegoPalabras} style={{padding: '10px 20px', borderRadius: '10px', border: 'none', background: '#ffd700', color: '#667eea', fontWeight: 'bold', margin: '10px 0', cursor: 'pointer'}}>
              Nueva palabra
            </button>
            {palabras.length > 0 && (
              <div style={{marginTop: '15px'}}>
                <p>Letras desordenadas: {palabras.join(' - ')}</p>
                <input type="text" placeholder="Escribe la palabra aqui" value={respuesta} onChange={(e) => setRespuesta(e.target.value.toUpperCase())} style={{padding: '10px', borderRadius: '8px', border: 'none', width: '80%', fontSize: '1em'}} />
                <button onClick={() => {
                  if(respuesta === palabraOculta) {
                    setPuntuacion(prev => prev + 30);
                    setMensaje('Exacto! +30 puntos');
                  } else {
                    setMensaje('Incorrecto. Era: ' + palabraOculta);
                  }
                  setTimeout(() => setMensaje(''), 4000);
                }} style={{padding: '10px 15px', borderRadius: '8px', border: 'none', background: '#4ade80', color: 'white', fontWeight: 'bold', marginLeft: '10px', cursor: 'pointer'}}>
                  Comprobar
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {seccion === 'seguimiento' && registrado && (
        <div style={{maxWidth: '700px', margin: '0 auto'}}>
          <h2 style={{textAlign: 'center'}}>Como te sientes hoy?</h2>
          <div style={{textAlign: 'center', margin: '20px 0'}}>
            <p>Del 1 al 5, cuanta concentracion tienes?</p>
            <div style={{display: 'flex', justifyContent: 'center', gap: '15px'}}>
              {[1,2,3,4,5].map(n => (
                <button key={n} onClick={() => setEstadoAnimo(n)} style={{background: estadoAnimo === n ? '#ffd700' : 'rgba(255,255,255,0.2)', border: 'none', borderRadius: '50%', width: '50px', height: '50px', cursor: 'pointer', color: 'white', fontWeight: 'bold'}}>{n}</button>
              ))}
            </div>
            <p style={{marginTop: '15px'}}>
              {estadoAnimo === 1 && 'Un poco cansado/a'}
              {estadoAnimo === 2 && 'Normal, podemos mejorar'}
              {estadoAnimo === 3 && 'Bien, con ganas de entrenar'}
              {estadoAnimo === 4 && 'Muy bien, mucha atencion'}
              {estadoAnimo === 5 && 'Excelente! Al maximo'}
            </p>
            <button onClick={guardarProgreso} style={{padding: '12px 25px', borderRadius: '10px', border: 'none', background: '#ffd700', color: '#667eea', fontWeight: 'bold', fontSize: '1em', cursor: 'pointer'}}>
              Guardar mi estado
            </button>
            {mensaje && <p style={{marginTop: '10px', fontWeight: 'bold'}}>{mensaje}</p>}
          </div>
        </div>
      )}

      {seccion === 'avances' && registrado && (
        <div style={{maxWidth: '700px', margin: '0 auto', textAlign: 'center'}}>
          <h2>Tus Avances</h2>
          <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '15px', margin: '20px 0'}}>
            <div style={{background: 'rgba(255,255,255,0.1)', padding: '20px', borderRadius: '15px'}}>
              <h3>Nivel Alcanzado</h3>
              <p style={{fontSize: '2.5em', fontWeight: 'bold', color: '#ffd700'}}>{nivel}</p>
            </div>
            <div style={{background: 'rgba(255,255,255,0.1)', padding: '20px', borderRadius: '15px'}}>
              <h3>Puntos Totales</h3>
              <p style={{fontSize: '2.5em', fontWeight: 'bold', color: '#4ade80'}}>{puntuacion}</p>
            </div>
            <div style={{background: 'rgba(255,255,255,0.1)', padding: '20px', borderRadius: '15px'}}>
              <h3>Dias de Entrenamiento</h3>
              <p style={{fontSize: '2.5em', fontWeight: 'bold', color: '#60a5fa'}}>{tiempoEnfoque}</p>
            </div>
            <div style={{background: 'rgba(255,255,255,0.1)', padding: '20px', borderRadius: '15px'}}>
              <h3>Estado Promedio</h3>
              <p style={{fontSize: '2.5em', fontWeight: 'bold', color: '#f472b6'}}>{estadoAnimo}/5</p>
            </div>
          </div>
          <p style={{fontSize: '1.2em', marginTop: '20px'}}>Recuerda: la constancia es la clave para mejorar.</p>
        </div>
      )}

      {seccion === 'consejos' && (
        <div style={{maxWidth: '800px', margin: '0 auto'}}>
          <h2 style={{textAlign: 'center'}}>Consejos para mejorar tu concentracion</h2>
          <div style={{background: 'rgba(255,255,255,0.1)', padding: '25px', borderRadius: '15px'}}>
            <ul style={{fontSize: '1.1em', lineHeight: '1.8'}}>
              <li>Descansa 7-8 horas cada noche</li>
              <li>Bebe suficiente agua</li>
              <li>Usa la tecnica Pomodoro: 25 min de enfoque, 5 de descanso</li>
              <li>Elimina distracciones cuando entrenes</li>
              <li>Come alimentos ricos en omega 3</li>
            </ul>
          </div>
        </div>
      )}

    </div>
  );
};

export default App;
