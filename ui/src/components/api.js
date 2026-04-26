const BASE = import.meta.env.VITE_API_BASE_URL || ''

export async function apiFetch(getToken, path, options = {}) {
  const token = await getToken()
  const res = await fetch(`${BASE}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
      ...options.headers,
    },
  })
  if (!res.ok) {
    const text = await res.text()
    throw new Error(`${res.status} ${text}`)
  }
  return res.status === 204 ? null : res.json()
}
