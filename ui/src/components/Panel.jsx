const C = {
  card:   '#111f35',
  border: '#1b3252',
  text:   '#e2e8f0',
  muted:  '#64748b',
  accent: '#2563eb',
  green:  '#10b981',
  red:    '#ef4444',
}

export function Panel({ title, children }) {
  return (
    <div style={{ background: C.card, border: `1px solid ${C.border}`, borderRadius: 8, padding: 20, marginBottom: 20 }}>
      {title && <div style={{ fontWeight: 600, fontSize: 15, marginBottom: 16, color: C.text }}>{title}</div>}
      {children}
    </div>
  )
}

export function Field({ label, children }) {
  return (
    <div style={{ marginBottom: 12 }}>
      <div style={{ fontSize: 12, color: C.muted, marginBottom: 4 }}>{label}</div>
      {children}
    </div>
  )
}

export function Input({ ...props }) {
  return (
    <input
      {...props}
      style={{
        background: '#0d1928',
        border: `1px solid ${C.border}`,
        borderRadius: 4,
        color: C.text,
        padding: '6px 10px',
        fontSize: 13,
        width: '100%',
        boxSizing: 'border-box',
        ...props.style,
      }}
    />
  )
}

export function Select({ children, ...props }) {
  return (
    <select
      {...props}
      style={{
        background: '#0d1928',
        border: `1px solid ${C.border}`,
        borderRadius: 4,
        color: C.text,
        padding: '6px 10px',
        fontSize: 13,
        width: '100%',
        boxSizing: 'border-box',
        ...props.style,
      }}
    >
      {children}
    </select>
  )
}

export function Btn({ variant = 'primary', children, ...props }) {
  const bg = variant === 'primary' ? C.accent : variant === 'danger' ? C.red : '#1b3252'
  return (
    <button
      {...props}
      style={{
        background: bg,
        color: '#fff',
        border: 'none',
        borderRadius: 4,
        padding: '7px 16px',
        fontSize: 13,
        cursor: 'pointer',
        fontWeight: 600,
        ...props.style,
      }}
    >
      {children}
    </button>
  )
}

export function Result({ data, error }) {
  if (error) return <pre style={{ color: C.red, fontSize: 12, margin: '12px 0 0', whiteSpace: 'pre-wrap' }}>{error}</pre>
  if (data === undefined) return null
  return <pre style={{ color: C.green, fontSize: 12, margin: '12px 0 0', whiteSpace: 'pre-wrap', overflowX: 'auto' }}>{JSON.stringify(data, null, 2)}</pre>
}

export function Row({ children, gap = 12 }) {
  return <div style={{ display: 'flex', gap, flexWrap: 'wrap', alignItems: 'flex-end' }}>{children}</div>
}

export function Col({ children, flex = 1 }) {
  return <div style={{ flex, minWidth: 140 }}>{children}</div>
}
