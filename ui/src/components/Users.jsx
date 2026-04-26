import { useState } from 'react'
import { apiFetch } from './api.js'
import { Panel, Field, Input, Select, Btn, Result, Row, Col } from './Panel.jsx'

export default function Users({ getToken }) {
  // GET /users/{userId}
  const [getUserId, setGetUserId] = useState('')
  const [getResult, setGetResult] = useState({})

  // GET /users/tellers
  const [tellersResult, setTellersResult] = useState({})

  // GET /users/customers
  const [customersResult, setCustomersResult] = useState({})

  // POST /users
  const [createForm, setCreateForm] = useState({ username: '', role: 'CUSTOMER', firstName: '', lastName: '', email: '' })
  const [createResult, setCreateResult] = useState({})

  // PUT /users/{userId}/role
  const [roleUserId, setRoleUserId] = useState('')
  const [newRole, setNewRole] = useState('TELLER')
  const [roleResult, setRoleResult] = useState({})

  // PUT /users/{userId}/status
  const [statusUserId, setStatusUserId] = useState('')
  const [newStatus, setNewStatus] = useState('INACTIVE')
  const [statusResult, setStatusResult] = useState({})

  const call = (setter) => async (fn) => {
    try { setter({ data: await fn() }) }
    catch (e) { setter({ error: e.message }) }
  }

  return (
    <>
      {/* GET /users/tellers and GET /users/customers */}
      <Row>
        <div style={{ flex: 1 }}>
          <Panel title="GET /users/tellers">
            <Btn onClick={call(setTellersResult)(() => apiFetch(getToken, '/users/tellers'))}>
              List Tellers
            </Btn>
            <Result {...tellersResult} />
          </Panel>
        </div>
        <div style={{ flex: 1 }}>
          <Panel title="GET /users/customers">
            <Btn onClick={call(setCustomersResult)(() => apiFetch(getToken, '/users/customers'))}>
              List Customers
            </Btn>
            <Result {...customersResult} />
          </Panel>
        </div>
      </Row>

      {/* GET /users/{userId} */}
      <Panel title="GET /users/{userId}">
        <Row>
          <Col flex={3}><Field label="User ID"><Input value={getUserId} onChange={e => setGetUserId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setGetResult)(() => apiFetch(getToken, `/users/${getUserId}`))}>Get User</Btn></Col>
        </Row>
        <Result {...getResult} />
      </Panel>

      {/* POST /users */}
      <Panel title="POST /users">
        <Row>
          <Col><Field label="Username"><Input value={createForm.username} onChange={e => setCreateForm(f => ({ ...f, username: e.target.value }))} /></Field></Col>
          <Col><Field label="Role">
            <Select value={createForm.role} onChange={e => setCreateForm(f => ({ ...f, role: e.target.value }))}>
              <option>CUSTOMER</option><option>TELLER</option><option>MANAGER</option>
            </Select>
          </Field></Col>
          <Col><Field label="First Name"><Input value={createForm.firstName} onChange={e => setCreateForm(f => ({ ...f, firstName: e.target.value }))} /></Field></Col>
          <Col><Field label="Last Name"><Input value={createForm.lastName} onChange={e => setCreateForm(f => ({ ...f, lastName: e.target.value }))} /></Field></Col>
          <Col><Field label="Email"><Input value={createForm.email} onChange={e => setCreateForm(f => ({ ...f, email: e.target.value }))} /></Field></Col>
        </Row>
        <Btn style={{ marginTop: 8 }} onClick={call(setCreateResult)(() => apiFetch(getToken, '/users', { method: 'POST', body: JSON.stringify(createForm) }))}>
          Create User
        </Btn>
        <Result {...createResult} />
      </Panel>

      {/* PUT /users/{userId}/role */}
      <Panel title="PUT /users/{userId}/role">
        <Row>
          <Col flex={3}><Field label="User ID"><Input value={roleUserId} onChange={e => setRoleUserId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={2}><Field label="New Role">
            <Select value={newRole} onChange={e => setNewRole(e.target.value)}>
              <option>CUSTOMER</option><option>TELLER</option><option>MANAGER</option>
            </Select>
          </Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setRoleResult)(() => apiFetch(getToken, `/users/${roleUserId}/role`, { method: 'PUT', body: JSON.stringify({ role: newRole }) }))}>Update Role</Btn></Col>
        </Row>
        <Result {...roleResult} />
      </Panel>

      {/* PUT /users/{userId}/status */}
      <Panel title="PUT /users/{userId}/status">
        <Row>
          <Col flex={3}><Field label="User ID"><Input value={statusUserId} onChange={e => setStatusUserId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={2}><Field label="New Status">
            <Select value={newStatus} onChange={e => setNewStatus(e.target.value)}>
              <option>ACTIVE</option><option>INACTIVE</option>
            </Select>
          </Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setStatusResult)(() => apiFetch(getToken, `/users/${statusUserId}/status`, { method: 'PUT', body: JSON.stringify({ status: newStatus }) }))}>Update Status</Btn></Col>
        </Row>
        <Result {...statusResult} />
      </Panel>
    </>
  )
}
