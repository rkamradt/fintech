import { useState } from 'react'
import { apiFetch } from './api.js'
import { Panel, Field, Input, Btn, Result, Row, Col } from './Panel.jsx'

export default function Transactions({ getToken }) {
  // POST /transactions/deposit
  const [depositForm, setDepositForm] = useState({ accountId: '', amount: '', customerId: '' })
  const [depositResult, setDepositResult] = useState({})

  // POST /transactions/withdraw
  const [withdrawForm, setWithdrawForm] = useState({ accountId: '', amount: '', customerId: '' })
  const [withdrawResult, setWithdrawResult] = useState({})

  // POST /transactions/transfer
  const [transferForm, setTransferForm] = useState({ fromAccountId: '', toAccountId: '', amount: '' })
  const [transferResult, setTransferResult] = useState({})

  // GET /transactions/account/{accountId}
  const [historyAccountId, setHistoryAccountId] = useState('')
  const [historyResult, setHistoryResult] = useState({})

  // GET /transactions/{transactionId}
  const [transactionId, setTransactionId] = useState('')
  const [txResult, setTxResult] = useState({})

  // GET /transactions/user/{userId}
  const [txUserId, setTxUserId] = useState('')
  const [userTxResult, setUserTxResult] = useState({})

  const call = (setter) => async (fn) => {
    try { setter({ data: await fn() }) }
    catch (e) { setter({ error: e.message }) }
  }

  return (
    <>
      <Row>
        {/* POST /transactions/deposit */}
        <div style={{ flex: 1 }}>
          <Panel title="POST /transactions/deposit">
            <Field label="Account ID"><Input value={depositForm.accountId} onChange={e => setDepositForm(f => ({ ...f, accountId: e.target.value }))} placeholder="uuid" /></Field>
            <Field label="Amount"><Input type="number" value={depositForm.amount} onChange={e => setDepositForm(f => ({ ...f, amount: e.target.value }))} placeholder="0.00" /></Field>
            <Field label="Customer ID (optional)"><Input value={depositForm.customerId} onChange={e => setDepositForm(f => ({ ...f, customerId: e.target.value }))} placeholder="uuid" /></Field>
            <Btn onClick={call(setDepositResult)(() => apiFetch(getToken, '/transactions/deposit', {
              method: 'POST',
              body: JSON.stringify({ accountId: depositForm.accountId, amount: parseFloat(depositForm.amount) || 0, customerId: depositForm.customerId || undefined }),
            }))}>Deposit</Btn>
            <Result {...depositResult} />
          </Panel>
        </div>

        {/* POST /transactions/withdraw */}
        <div style={{ flex: 1 }}>
          <Panel title="POST /transactions/withdraw">
            <Field label="Account ID"><Input value={withdrawForm.accountId} onChange={e => setWithdrawForm(f => ({ ...f, accountId: e.target.value }))} placeholder="uuid" /></Field>
            <Field label="Amount"><Input type="number" value={withdrawForm.amount} onChange={e => setWithdrawForm(f => ({ ...f, amount: e.target.value }))} placeholder="0.00" /></Field>
            <Field label="Customer ID (optional)"><Input value={withdrawForm.customerId} onChange={e => setWithdrawForm(f => ({ ...f, customerId: e.target.value }))} placeholder="uuid" /></Field>
            <Btn onClick={call(setWithdrawResult)(() => apiFetch(getToken, '/transactions/withdraw', {
              method: 'POST',
              body: JSON.stringify({ accountId: withdrawForm.accountId, amount: parseFloat(withdrawForm.amount) || 0, customerId: withdrawForm.customerId || undefined }),
            }))}>Withdraw</Btn>
            <Result {...withdrawResult} />
          </Panel>
        </div>
      </Row>

      {/* POST /transactions/transfer */}
      <Panel title="POST /transactions/transfer">
        <Row>
          <Col><Field label="From Account ID"><Input value={transferForm.fromAccountId} onChange={e => setTransferForm(f => ({ ...f, fromAccountId: e.target.value }))} placeholder="uuid" /></Field></Col>
          <Col><Field label="To Account ID"><Input value={transferForm.toAccountId} onChange={e => setTransferForm(f => ({ ...f, toAccountId: e.target.value }))} placeholder="uuid" /></Field></Col>
          <Col><Field label="Amount"><Input type="number" value={transferForm.amount} onChange={e => setTransferForm(f => ({ ...f, amount: e.target.value }))} placeholder="0.00" /></Field></Col>
        </Row>
        <Btn style={{ marginTop: 8 }} onClick={call(setTransferResult)(() => apiFetch(getToken, '/transactions/transfer', {
          method: 'POST',
          body: JSON.stringify({ fromAccountId: transferForm.fromAccountId, toAccountId: transferForm.toAccountId, amount: parseFloat(transferForm.amount) || 0 }),
        }))}>Transfer</Btn>
        <Result {...transferResult} />
      </Panel>

      {/* GET /transactions/account/{accountId} */}
      <Panel title="GET /transactions/account/{accountId}">
        <Row>
          <Col flex={3}><Field label="Account ID"><Input value={historyAccountId} onChange={e => setHistoryAccountId(e.target.value)} placeholder="uuid" /></Field></Col>
          <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setHistoryResult)(() => apiFetch(getToken, `/transactions/account/${historyAccountId}`))}>Get History</Btn></Col>
        </Row>
        <Result {...historyResult} />
      </Panel>

      <Row>
        {/* GET /transactions/{transactionId} */}
        <div style={{ flex: 1 }}>
          <Panel title="GET /transactions/{transactionId}">
            <Row>
              <Col flex={3}><Field label="Transaction ID"><Input value={transactionId} onChange={e => setTransactionId(e.target.value)} placeholder="uuid" /></Field></Col>
              <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setTxResult)(() => apiFetch(getToken, `/transactions/${transactionId}`))}>Get Tx</Btn></Col>
            </Row>
            <Result {...txResult} />
          </Panel>
        </div>

        {/* GET /transactions/user/{userId} */}
        <div style={{ flex: 1 }}>
          <Panel title="GET /transactions/user/{userId}">
            <Row>
              <Col flex={3}><Field label="User ID"><Input value={txUserId} onChange={e => setTxUserId(e.target.value)} placeholder="uuid" /></Field></Col>
              <Col flex={1}><Btn style={{ marginTop: 20 }} onClick={call(setUserTxResult)(() => apiFetch(getToken, `/transactions/user/${txUserId}`))}>Get by User</Btn></Col>
            </Row>
            <Result {...userTxResult} />
          </Panel>
        </div>
      </Row>
    </>
  )
}
