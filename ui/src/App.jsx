import { useState } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import Users from './components/Users.jsx'
import Accounts from './components/Accounts.jsx'
import Transactions from './components/Transactions.jsx'

const C = {
  bg:      '#07101f',
  surface: '#0d1928',
  card:    '#111f35',
  border:  '#1b3252',
  accent:  '#2563eb',
  text:    '#e2e8f0',
  muted:   '#64748b',
  green:   '#10b981',
  red:     '#ef4444',
}

const TABS = ['Users', 'Accounts', 'Transactions']

export default function App() {
  const { isLoading, isAuthenticated, loginWithRedirect, logout, user, getAccessTokenSilently } = useAuth0()
  const [tab, setTab] = useState('Users')

  if (isLoading) return (
    <div style={{ background: C.bg, minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', color: C.muted, fontFamily: 'monospace' }}>
      loading…
    </div>
  )

  if (!isAuthenticated) return (
    <div style={{ background: C.bg, minHeight: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', fontFamily: 'system-ui', gap: 16 }}>
      <div style={{ color: C.text, fontSize: 24, fontWeight: 700, letterSpacing: 1 }}>Fintech Platform</div>
      <div style={{ color: C.muted, fontSize: 14 }}>Reference Implementation</div>
      <button
        onClick={() => loginWithRedirect()}
        style={{ marginTop: 16, background: C.accent, color: '#fff', border: 'none', borderRadius: 6, padding: '10px 28px', fontSize: 15, cursor: 'pointer', fontWeight: 600 }}
      >
        Log In
      </button>
    </div>
  )

  return (
    <div style={{ background: C.bg, minHeight: '100vh', fontFamily: 'system-ui', color: C.text }}>
      {/* Header */}
      <div style={{ background: C.surface, borderBottom: `1px solid ${C.border}`, padding: '0 24px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', height: 52 }}>
        <span style={{ fontWeight: 700, fontSize: 16, letterSpacing: 0.5 }}>Fintech Platform</span>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <span style={{ color: C.muted, fontSize: 13 }}>{user.email}</span>
          <button
            onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
            style={{ background: 'transparent', border: `1px solid ${C.border}`, color: C.muted, borderRadius: 4, padding: '4px 12px', fontSize: 13, cursor: 'pointer' }}
          >
            Log Out
          </button>
        </div>
      </div>

      {/* Tabs */}
      <div style={{ background: C.surface, borderBottom: `1px solid ${C.border}`, padding: '0 24px', display: 'flex', gap: 4 }}>
        {TABS.map(t => (
          <button
            key={t}
            onClick={() => setTab(t)}
            style={{
              background: 'transparent',
              border: 'none',
              borderBottom: tab === t ? `2px solid ${C.accent}` : '2px solid transparent',
              color: tab === t ? C.text : C.muted,
              padding: '12px 16px',
              fontSize: 14,
              cursor: 'pointer',
              fontWeight: tab === t ? 600 : 400,
            }}
          >
            {t}
          </button>
        ))}
      </div>

      {/* Content */}
      <div style={{ padding: 24, maxWidth: 1100, margin: '0 auto' }}>
        {tab === 'Users'        && <Users        getToken={getAccessTokenSilently} />}
        {tab === 'Accounts'     && <Accounts     getToken={getAccessTokenSilently} />}
        {tab === 'Transactions' && <Transactions getToken={getAccessTokenSilently} />}
      </div>
    </div>
  )
}
