import React, { useState, useEffect, useRef } from 'react';

export default function App() {
  const [tab, setTab] = useState('inicio');
  const [xp, setXp] = useState(1240);
  const [streak, setStreak] = useState(7);
  const [memCards, setMemCards] = useState([]);
  const [flipped, setFlipped] = useState([]);
  const [matched, setMatched] = useState([]);
  const [mathQ, setMathQ] = useState({ a: 5, b: 3 });
  const [mathScore, setMathScore] = useState(0);
  const [selectedHobby, setSelectedHobby] = useState(null);
  const [hobbyDone, setHobbyDone] = useState({});
  const [goals, setGoals] = useState([{ t: 'Aprender inglés 30 min', p: 60 }]);
  const [newGoal, setNewGoal] = useState('');
  const [messages, setMessages] = useState([
    { role: 'ai', text: '¡Hey! Soy tu Entrenador MIND 🧠🔥\n\nSoy tu guía personal para memoria, metas y hobbies. Cuéntame:\n• Cómo va tu día\n• Qué quieres memorizar\n• Qué hobby quieres probar\n\nYo te armo el plan paso a paso.' }
  ]);
  const [input, setInput] = useState('');
  const chatRef = useRef(null);

  useEffect(() => { resetMem(); }, []);
  useEffect(() => { if (chatRef.current) chatRef.current.scrollTop = chatRef.current.scrollHeight; }, [messages]);

  const resetMem = () => {
    const emojis = ['🧠', '🚀', '🌈', '🔥', '💎', '🎨', '⚡', '🦄'];
    const deck = [...emojis,...emojis].sort(() => Math.random() - 0.5).map((e, i) => ({ id: i, e }));
    setMemCards(deck); setFlipped([]); setMatched([]);
  };
  const clickMem = (i) => {
    if (flipped.length === 2 || flipped.includes(i) || matched.includes(i)) return;
    const nf = [...flipped, i]; setFlipped(nf);
    if (nf.length === 2) {
      if (memCards[nf[0]].e === memCards[nf[1]].e) {
        const nm = [...matched,...nf]; setMatched(nm); setFlipped([]);
        if (nm.length === memCards.length) { setXp(x => x + 100); setTimeout(resetMem, 1000); }
      } else setTimeout(() => setFlipped([]), 700);
    }
  };
  const newMath = () => setMathQ({ a: Math.floor(Math.random() * 20), b: Math.floor(Math.random() * 20) });

  const askTrainer = () => {
    if (!input.trim()) return;
    const userText = input; setMessages(m => [...m, { role: 'user', text: userText }]); setInput('');
    setTimeout(() => {
      let reply = `¡Vamos con "${userText}"! 🚀\n\nVe a 🌈 Hobbies, abre un hobby y dale a "Marcar como hecho". Cada día te da +50 XP.\n\nLa única regla para ser constante: no falles 2 días seguidos.`;
      const t = userText.toLowerCase();
      if (t.includes('desmotiv') || t.includes('pereza') || t.includes('cansad') || t.includes('no puedo')) {
        reply = `Te entiendo, hoy no necesitas hacer 100%. Haz solo 2 MINUTOS 💙\n\n1. Pon timer 2 min\n2. Haz lo mínimo\n3. Si quieres parar, paras. El 90% sigue después.\n\n¿Lo hacemos? Marca +15% en tu meta y salvas tu racha 🔥`;
      }
      if (t.includes('tejer') || t.includes('hobbie') || t.includes('aburrid')) {
        reply = `¡Para hobbies nuevos te recomiendo! 🌈\n\nSi quieres CALMA: Tejer o Cerámica (bajan ansiedad)\nSi quieres FOCO: Origami o Dibujo\nSi quieres ENERGÍA: Ukulele o Cocina\n\nToca 🌈 Hobbies → Elige uno → Se abre su escuela con fotos y guía Día 1, Día 2, Día 3.\n\nYo te acompaño en cada paso.`;
      }
      setMessages(m => [...m, { role: 'ai', text: reply }]); setXp(x => x + 5);
    }, 600);
  };

  const hobbiesData = [
    { id: 'tejer', e: '🧶', n: 'Tejer Consciente', color: 'linear-gradient(135deg,#f093fb,#f5576c)', cover: 'https://images.unsplash.com/photo-1584992236310-6edddc08acff?w=600', desc: 'Meditar con las manos. Baja ansiedad 40%.', benefit: '+ Calma + Foco', steps: [
        { title: 'Día 1 - Punto Base', img: 'https://images.unsplash.com/photo-1619250907682-662554a506ea?w=500', what: 'Aprende a montar 20 puntos.', how: '1. Nudo corredizo\n2. Monta 20 puntos en aguja\n3. Teje 10 filas punto derecho', tip: 'Suelto al inicio es normal. Ritmo > perfección.' },
        { title: 'Día 2 - Bufanda Mini', img: 'https://images.unsplash.com/photo-1584992236310-6edddc08acff?w=500', what: 'Tu primera bufanda de 15cm.', how: '1. Monta 15 puntos\n2. Teje 30 filas\n3. Cierra puntos (ver YouTube: bind off)', tip: 'Pon música lofi. 20 min cuenta.' },
        { title: 'Día 3 - Gorro Simple', img: 'https://images.unsplash.com/photo-1576871337632-b9aef4c17ab9?w=500', what: 'Tu primer proyecto usable.', how: '1. Monta 60 puntos circular\n2. Teje 20 cm\n3. Cierra arriba', tip: 'Foto = +100 XP' },
    ]},
    { id: 'ceramica', e: '🏺', n: 'Cerámica Fría', color: 'linear-gradient(135deg,#ffecd2,#fcb69f)', cover: 'https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=600', desc: 'Moldea sin horno. Terapia pura.', benefit: '+ Anti-estrés', steps: [
        { title: 'Día 1 - Cuenco', img: 'https://images.unsplash.com/photo-1610701596007-11502861dcfa?w=500', what: 'Técnica pellizco.', how: '1. Bola puño\n2. Pulgar al centro\n3. Pellizca paredes 1cm grosor', tip: 'Si se agrieta, humedece dedos.' },
        { title: 'Día 2 - Taza', img: 'https://images.unsplash.com/photo-1610701596061-2ecf227e85b2?w=500', what: 'Cuenco + asa.', how: '1. Haz cuenco\n2. Churrito para asa\n3. Pega con barbotina', tip: 'Pinta con acrílico al secar.' },
    ]},
    { id: 'origami', e: '📄', n: 'Origami Focus', color: 'linear-gradient(135deg,#a1c4fd,#c2e9fb)', cover: 'https://images.unsplash.com/photo-1586953208448-b95a79798f07?w=500', desc: 'Mejora foco 30%.', benefit: '+ Paciencia', steps: [
        { title: 'Día 1 - Grulla', img: 'https://images.unsplash.com/photo-1586953208448-b95a79798f07?w=500', what: 'La clásica de la suerte.', how: 'Papel cuadrado, dobla diagonal x2, busca "origami grulla" 5 min YouTube', tip: 'Haz 3, la tercera sale perfecta.' },
    ]},
    { id: 'cocina', e: '🍳', n: 'Cocina Memoria', color: 'linear-gradient(135deg,#f6d365,#fda085)', cover: 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600', desc: 'Cocinar sin receta entrena memoria.', benefit: '+ Vida real', steps: [
        { title: 'Día 1 - Arroz perfecto', img: 'https://images.unsplash.com/photo-1536304929831-ee1ca9d44906?w=500', what: 'Hazlo de memoria.', how: '1 taza arroz, 2 agua, sal. Sofríe 1 min, agua, tapa 15 min fuego bajo', tip: 'Mañana hazlo sin mirar receta.' },
    ]},
  ];

  const S = {
    bg: { background: '#080812', minHeight: '100vh', color: 'white', fontFamily: 'Outfit, sans-serif' },
    header: { position: 'sticky', top: 0, zIndex: 20, display: 'flex', justifyContent: 'space-between', padding: '14px 20px', background: 'rgba(15,15,25,0.95)', borderBottom: '1px solid rgba(255,255,255,0.1)' },
    card: { background: 'rgba(255,255,255,0.07)', border: '1px solid rgba(255,255,255,0.12)', borderRadius: 24, padding: 20 },
    btn: { background: 'white', color: 'black', border: 'none', padding: '12px 22px', borderRadius: 999, fontWeight: 900, cursor: 'pointer' },
    btnGhost: { background: 'rgba(255,255,255,0.15)', color: 'white', border: 'none', padding: '10px 18px', borderRadius: 999, cursor: 'pointer' },
  };

  if (selectedHobby) {
    const h = hobbiesData.find(x => x.id === selectedHobby);
    const done = hobbyDone[h.id] || 0;
    const step = h.steps[Math.min(done, h.steps.length - 1)];
    return (
      <div style={S.bg}>
        <div style={S.header}>
          <button onClick={() => setSelectedHobby(null)} style={S.btnGhost}>← Volver</button>
          <div style={{ display: 'flex', gap: 8 }}><div style={{ background: 'linear-gradient(135deg,#ff9a00,#ff6a00)', padding: '6px 12px', borderRadius: 999, fontSize: 12 }}>🔥 {done} días</div><div style={{ background: 'white', color: 'black', padding: '6px 12px', borderRadius: 999, fontSize: 12, fontWeight: 800 }}>{xp} XP</div></div>
        </div>
        <div style={{ maxWidth: 800, margin: '0 auto', padding: 16 }}>
          <div style={{ borderRadius: 24, overflow: 'hidden', height: 200, background: `url(${h.cover}) center/cover`, position: 'relative' }}>
            <div style={{ position: 'absolute', inset: 0, background: 'linear-gradient(to top, rgba(0,0,0,0.8), transparent)' }}></div>
            <div style={{ position: 'absolute', bottom: 16, left: 16 }}><div style={{ fontSize: 40 }}>{h.e}</div><h1 style={{ margin: 0, fontSize: 28, fontWeight: 900 }}>{h.n}</h1><p style={{ margin: 0, opacity: 0.8, fontSize: 13 }}>{h.benefit}</p></div>
          </div>
          <div style={{ display: 'flex', gap: 8, marginTop: 16, overflowX: 'auto' }}>
            {h.steps.map((s, i) => <div key={i} style={{ minWidth: 100, padding: 10, borderRadius: 16, background: i < done? '#10b981' : i === done? 'white' : 'rgba(255,255,255,0.1)', color: i < done? 'white' : i === done? 'black' : 'white', textAlign: 'center', fontWeight: 700 }}>{i < done? '✓' : `Día ${i + 1}`}</div>)}
          </div>
          <div style={{...S.card, marginTop: 16 }}>
            <h2 style={{ margin: 0 }}>{step.title}</h2>
            <img src={step.img} alt="paso" style={{ width: '100%', height: 220, objectFit: 'cover', borderRadius: 16, marginTop: 12 }} />
            <h3>🎯 Qué harás:</h3><p style={{ opacity: 0.8, fontSize: 14 }}>{step.what}</p>
            <h3>📝 Paso a paso:</h3><p style={{ opacity: 0.8, fontSize: 14, whiteSpace: 'pre-wrap', background: 'rgba(0,0,0,0.3)', padding: 12, borderRadius: 12 }}>{step.how}</p>
            <div style={{ background: 'linear-gradient(135deg,#667eea,#764ba2)', padding: 12, borderRadius: 12, marginTop: 12 }}><b>💡 Tip Entrenador:</b><p style={{ margin: '4px 0 0', fontSize: 13 }}>{step.tip}</p></div>
            <button onClick={() => { const nd = Math.min(done + 1, h.steps.length); setHobbyDone({...hobbyDone, [h.id]: nd }); setXp(x => x + 50); setStreak(s => s + 1); }} style={{...S.btn, width: '100%', marginTop: 16 }}>{done >= h.steps.length? '¡Completado! 🎉' : `Marcar Día ${done + 1} como hecho +50 XP`}</button>
            <div style={{ marginTop: 12, height: 8, background: 'rgba(255,255,255,0.1)', borderRadius: 999 }}><div style={{ width: `${(done / h.steps.length) * 100}%`, height: '100%', background: 'linear-gradient(90deg,#ff6a00,#ee0979)', borderRadius: 999 }}></div></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div style={S.bg}>
      <style>{`@import url('https://fonts.googleapis.com/css2?family=Outfit:wght@700;900&display=swap');`}</style>
      <div style={S.header}>
        <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}><div style={{ width: 40, height: 40, borderRadius: 14, background: 'linear-gradient(135deg,#ff6a00,#ee0979)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 900 }}>M</div><b>MINDLAB WORLD</b></div>
        <div style={{ display: 'flex', gap: 8 }}><div style={{ background: 'linear-gradient(135deg,#ff9a00,#ff6a00)', padding: '8px 14px', borderRadius: 999, fontSize: 12, fontWeight: 800 }}>🔥 {streak} DÍAS</div><div style={{ background: 'white', color: 'black', padding: '8px 14px', borderRadius: 999, fontSize: 12, fontWeight: 900 }}>{xp} XP</div></div>
      </div>

      <div style={{ maxWidth: 1200, margin: '0 auto', padding: 16 }}>
        <div style={{ display: 'flex', gap: 8, overflowX: 'auto', paddingBottom: 10 }}>
          {[{ id: 'inicio', l: '🏠 Inicio' }, { id: 'juegos', l: '🎮 Juegos' }, { id: 'coach', l: '🤖 Entrenador' }, { id: 'hobbies', l: '🌈 Hobbies' }, { id: 'metas', l: '🎯 Metas' }].map(m => (
            <button key={m.id} onClick={() => setTab(m.id)} style={{...S.btnGhost, background: tab === m.id? 'white' : 'rgba(255,255,255,0.1)', color: tab === m.id? 'black' : 'white', fontWeight: 800, whiteSpace: 'nowrap' }}>{m.l}</button>
          ))}
        </div>

        {tab === 'inicio' && <>
          <div style={{ background: 'linear-gradient(135deg,#ff6a00 0%,#ee0979 25%,#7b2ff7 50%,#00d2ff 100%)', borderRadius: 32, padding: 32 }}>
            <h1 style={{ fontSize: 44, fontWeight: 900, lineHeight: 0.9, margin: 0 }}>LA APP MÁS<br />CHÉVERE PARA<br />TU CEREBRO</h1>
            <p style={{ maxWidth: 500, marginTop: 12 }}>Juegos, entrenador IA que te guía día a día y hobbies con escuela por dentro.</p>
            <div style={{ display: 'flex', gap: 10, marginTop: 18 }}><button onClick={() => setTab('coach')} style={S.btn}>Hablar con Entrenador →</button><button onClick={() => setTab('hobbies')} style={S.btnGhost}>Ver Hobbies</button></div>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(240px,1fr))', gap: 12, marginTop: 14 }}>
            <div onClick={() => setTab('coach')} style={{...S.card, background: 'linear-gradient(135deg,#667eea,#764ba2)', cursor: 'pointer' }}><div style={{ fontSize: 32 }}>🤖</div><b>Entrenador IA</b><p style={{ fontSize: 13, opacity: 0.8 }}>Te habla, te guía, no te deja abandonar.</p></div>
            <div onClick={() => setTab('hobbies')} style={{...S.card, background: 'linear-gradient(135deg,#f093fb,#f5576c)', cursor: 'pointer' }}><div style={{ fontSize: 32 }}>🌈</div><b>Hobbies con Escuela</b><p style={{ fontSize: 13, opacity: 0.8 }}>Tejer, cerámica, origami, cocina. Cada uno con fotos paso a paso.</p></div>
            <div onClick={() => setTab('juegos')} style={{...S.card, background: 'linear-gradient(135deg,#4facfe,#00f2fe)', cursor: 'pointer' }}><div style={{ fontSize: 32 }}>🎮</div><b>Gimnasio</b><p style={{ fontSize: 13, opacity: 0.8 }}>Memoria neón y cálculo veloz.</p></div>
          </div>
        </>}

        {tab === 'hobbies' && <div>
          <h2 style={{ fontSize: 32, fontWeight: 900 }}>🌈 Elige tu Hobby</h2>
          <p style={{ opacity: 0.7, fontSize: 14 }}>Toca uno → se abre su escuela interna</p>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(280px,1fr))', gap: 14, marginTop: 14 }}>
            {hobbiesData.map(h => {
              const done = hobbyDone[h.id] || 0;
              return <div key={h.id} onClick={() => setSelectedHobby(h.id)} style={{...S.card, padding: 0, overflow: 'hidden', cursor: 'pointer', background: h.color }}>
                <img src={h.cover} style={{ width: '100%', height: 140, objectFit: 'cover' }} alt="" />
                <div style={{ padding: 16 }}><div style={{ display: 'flex', justifyContent: 'space-between' }}><div style={{ fontSize: 28 }}>{h.e}</div><span style={{ background: 'rgba(0,0,0,0.2)', padding: '4px 10px', borderRadius: 999, fontSize: 11 }}>{done}/{h.steps.length} días</span></div>
                  <h3 style={{ margin: '6px 0' }}>{h.n}</h3><p style={{ fontSize: 12, margin: 0, opacity: 0.9 }}>{h.desc}</p>
                  <div style={{ height: 6, background: 'rgba(0,0,0,0.2)', borderRadius: 999, marginTop: 10 }}><div style={{ width: `${(done / h.steps.length) * 100}%`, height: '100%', background: 'white', borderRadius: 999 }}></div></div>
                  <div style={{ marginTop: 10, fontWeight: 800, fontSize: 12 }}>→ Abrir escuela</div>
                </div>
              </div>
            })}
          </div>
        </div>}

        {tab === 'coach' && <div style={{ maxWidth: 600, margin: '0 auto' }}>
          <div style={{...S.card, background: 'linear-gradient(135deg,#667eea,#764ba2)' }}><b>🤖 Tu Entrenador Personal</b><p style={{ fontSize: 12, opacity: 0.8, margin: 0 }}>Cuéntame tu día, te guío.</p></div>
          <div ref={chatRef} style={{...S.card, height: 360, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: 8, marginTop: 12, background: 'rgba(0,0,0,0.3)' }}>
            {messages.map((m, i) => <div key={i} style={{ alignSelf: m.role === 'user'? 'flex-end' : 'flex-start', maxWidth: '80%', padding: '10px 14px', borderRadius: 16, background: m.role === 'user'? 'white' : 'rgba(139,92,246,0.25)', color: m.role === 'user'? 'black' : 'white', fontSize: 13, whiteSpace: 'pre-wrap' }}>{m.text}</div>)}
          </div>
          <div style={{ display: 'flex', gap: 8, marginTop: 10 }}>
            <input value={input} onChange={e => setInput(e.target.value)} onKeyDown={e => e.key === 'Enter' && askTrainer()} placeholder="Ej: quiero aprender a tejer..." style={{ flex: 1, padding: 12, borderRadius: 999, border: 'none', background: 'rgba(255,255,255,0.1)', color: 'white' }} />
            <button onClick={askTrainer} style={S.btn}>→</button>
          </div>
        </div>}

        {tab === 'juegos' && <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(300px,1fr))', gap: 14 }}>
          <div style={S.card}><b>🌈 Memoria Neón</b><div style={{ display: 'grid', gridTemplateColumns: 'repeat(4,1fr)', gap: 8, marginTop: 12 }}>{memCards.map((c, i) => <div key={i} onClick={() => clickMem(i)} style={{ height: 60, borderRadius: 12, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 20, cursor: 'pointer', background: flipped.includes(i) || matched.includes(i)? 'white' : 'linear-gradient(135deg,#7b2ff7,#f107a3)' }}>{flipped.includes(i) || matched.includes(i)? c.e : '?'}</div>)}</div></div>
          <div style={S.card}><b>⚡ Cálculo Veloz</b><h1 style={{ textAlign: 'center' }}>{mathQ.a} + {mathQ.b} =?</h1><div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)', gap: 8 }}>{[mathQ.a + mathQ.b, mathQ.a + mathQ.b + 2, mathQ.a + mathQ.b + 5, mathQ.a + mathQ.b - 1, mathQ.a + mathQ.b + 10, mathQ.a + mathQ.b - 3].sort(() => Math.random() - 0.5).map((v, k) => <button key={k} onClick={() => { if (v === mathQ.a + mathQ.b) { setMathScore(s => s + 1); setXp(x => x + 10); newMath(); } }} style={{ padding: 14, borderRadius: 12, border: 'none', background: 'linear-gradient(135deg,#ff6a00,#ee0979)', color: 'white', fontWeight: 900, cursor: 'pointer' }}>{v}</button>)}</div><div style={{ textAlign: 'center', marginTop: 8 }}>Score: {mathScore}</div></div>
        </div>}

        {tab === 'metas' && <div style={{ maxWidth: 600 }}><h2 style={{ fontSize: 30, fontWeight: 900 }}>🎯 Mis Metas</h2><div style={{...S.card, display: 'flex', gap: 8, marginTop: 12 }}><input value={newGoal} onChange={e => setNewGoal(e.target.value)} placeholder="Ej: Tejer 10 min diarios" style={{ flex: 1, padding: 12, borderRadius: 12, border: 'none', background: 'rgba(0,0,0,0.3)', color: 'white' }} /><button onClick={() => { if (newGoal) { setGoals([...goals, { t: newGoal, p: 0 }]); setNewGoal(''); setXp(x => x + 20); } }} style={S.btn}>Crear</button></div>{goals.map((g, i) => <div key={i} style={{...S.card, marginTop: 10 }}><div style={{ display: 'flex', justifyContent: 'space-between' }}><b>{g.t}</b><span>{g.p}%</span></div><div style={{ height: 8, background: 'rgba(255,255,255,0.1)', borderRadius: 999, marginTop: 8 }}><div style={{ width: `${g.p}%`, height: '100%', background: 'linear-gradient(90deg,#ff6a00,#ee0979)', borderRadius: 999 }}></div></div><button onClick={() => { let ng = [...goals]; ng[i].p = Math.min(100, ng[i].p + 15); setGoals(ng); setXp(x => x + 15); }} style={{...S.btnGhost, width: '100%', marginTop: 10 }}>+15% Hoy 🚀</button></div>)}</div>}
      </div>
    </div>
  );
}