import { useState } from 'react'
import { apiFetch } from './api.js'
import { Panel, Field, Input, Select, Btn, Result, Row, Col } from './Panel.jsx'

export default function Accounts({ getToken }) {
  // GET /accounts/{accountId}
  const [getAccountId, setGetAccountId] = useState('')
  const [getResult, setGetResult] = useState({})

  // GET /accounts/customer/{customerId}
  const [customerId, setCustomerId] = useState('')
  const [byCustomerResult, setByCustomerResult] = useState({})

  // POST /accounts
  const [createForm, setCreateForm] = useState({ customerId: '', accountType: 'CHECKING', initialBalance: '' })
  const [createResult, setCreateResult] = useState({})

  // PUT /accounts/{accountId}/status
  const [statusAccountId, setStatusAccountId] = useState('')
  const [newStatus, setNewStatus] = useState('SUSPENDED')
  const [statusResult, setStatusResult] = useState({})

  // PUT /accounts/{accountId}/balance
  const [balanceAccountId, setBalanceAccountId] = useState('')
  const [newBalance, setNewBalance] = useState('')
  const [balanceResult, setBalanceResult] = useState({})

  const call = (setter) => async (fn) => {
    try { setter({ data: await fn() }) }
    catch (e) { setter({ error: e.message }) }
  }

  return (
    <>
      {/* GET /accounts/{accountId} */}
      <Panel title="GET /accounts/{accountId}">
        <Row>
          <Col flex={3}><Field label="Account ID"><Input value={getAccountId} onChange={e => setGetAccountId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setGetResult)(() => apiFetch(getToken, `/accounts/${getAccountId}`))}>Get Account</Btn></Col>
        </Row>
        <Result {...getResult} />
      </Panel>

      {/* GET /accounts/customer/{customerId} */}
      <Panel title="GET /accounts/customer/{customerId}">
        <Row>
          <Col flex={3}><Field label="Customer ID"><Input value={customerId} onChange={e => setCustomerId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setByCustomerResult)(() => apiFetch(getToken, `/accounts/customer/${customerId}`))}>List Accounts</Btn></Col>
        </Row>
        <Result {...byCustomerResult} />
      </Panel>

      {/* POST /accounts */}
      <Panel title="POST /accounts">
        <Row>
          <Col flex={3}><Field label="Customer ID"><Input value={createForm.customerId} onChange={e => setCreateForm(f => ({ ...f, customerId: e.target.value }))} placeholder="uuid" /></Field></Col>
          <Col flex={2}><Field label="Account Type">
            <Select value={createForm.accountType} onChange={e => setCreateForm(f => ({ ...f, accountType: e.target.value }))}>
              <option>CHECKING</option><option>SAVINGS</option>
            </Select>
          </Field></Col>
          <Col flex={2}><Field label="Initial Balance"><Input type="number" value={createForm.initialBalance} onChange={e => setCreateForm(f => ({ ...f, initialBalance: e.target.value }))} placeholder="0.00" /></Field></Col>
        </Row>
        <Btn style={{ marginTop: 8 }} onClick={call(setCreateResult)(() => apiFetch(getToken, '/accounts', {
          method: 'POST',
          body: JSON.stringify({ ...createForm, initialBalance: parseFloat(createForm.initialBalance) || 0 }),
        }))}>
          Create Account
        </Btn>
        <Result {...createResult} />
      </Panel>

      {/* PUT /accounts/{accountId}/status */}
      <Panel title="PUT /accounts/{accountId}/status">
        <Row>
          <Col flex={3}><Field label="Account ID"><Input value={statusAccountId} onChange={e => setStatusAccountId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={2}><Field label="New Status">
            <Select value={newStatus} onChange={e => setNewStatus(e.target.value)}>
              <option>ACTIVE</option><option>SUSPENDED</option><option>CLOSED</option>
            </Select>
          </Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setStatusResult)(() => apiFetch(getToken, `/accounts/${statusAccountId}/status`, { method: 'PUT', body: JSON.stringify({ status: newStatus }) }))}>Update Status</Btn></Col>
        </Row>
        <Result {...statusResult} />
      </Panel>

      {/* PUT /accounts/{accountId}/balance */}
      <Panel title="PUT /accounts/{accountId}/balance">
        <Row>
          <Col flex={3}><Field label="Account ID"><Input value={balanceAccountId} onChange={e => setBalanceAccountId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={2}><Field label="New Balance"><Input type="number" value={newBalance} onChange={e => setNewBalance(e.target.value)} placeholder="0.00" /></Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setBalanceResult)(() => apiFetch(getToken, `/accounts/${balanceAccountId}/balance`, { method: 'PUT', body: JSON.stringify({ balance: parseFloat(newBalance) || 0 }) }))}>Update Balance</Btn></Col>
        </Row>
        <Result {...balanceResult} />
      </Panel>
    </>
  )
}
