import { useState } from 'react'
import { expenseService } from '../services/api'
import './Summary.css'

function Summary() {
  const [month, setMonth] = useState(() => {
    const now = new Date()
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  })
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const fetchSummary = async () => {
    if (!month) return

    try {
      setLoading(true)
      setError(null)
      const data = await expenseService.getSummary(month)
      setSummary(data)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch summary')
      setSummary(null)
    } finally {
      setLoading(false)
    }
  }

  const handleMonthChange = (e) => {
    setMonth(e.target.value)
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    fetchSummary()
  }

  const formatCurrency = (amount) => {
    if (!amount) return '$0.00'
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount)
  }

  const formatMonth = (monthString) => {
    if (!monthString) return ''
    const [year, month] = monthString.split('-')
    const date = new Date(year, parseInt(month) - 1)
    return date.toLocaleDateString('en-US', { year: 'numeric', month: 'long' })
  }

  return (
    <div className="summary-container">
      <div className="summary">
        <h2>Monthly Summary</h2>

        <form className="summary-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="month-select">Select Month:</label>
            <input
              type="month"
              id="month-select"
              value={month}
              onChange={handleMonthChange}
              required
            />
          </div>
          <button type="submit" className="btn-fetch" disabled={loading}>
            {loading ? 'Loading...' : 'Get Summary'}
          </button>
        </form>

        {error && <div className="error-message">{error}</div>}

        {loading && <div className="loading">Loading summary...</div>}

        {summary && !loading && (
          <div className="summary-results">
            <div className="summary-header">
              <h3>{formatMonth(summary.month)}</h3>
            </div>
            <div className="summary-stats">
              <div className="stat-card">
                <div className="stat-label">Total Spent</div>
                <div className="stat-value total">
                  {formatCurrency(summary.totalSpent)}
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-label">Number of Expenses</div>
                <div className="stat-value count">{summary.count || 0}</div>
              </div>
            </div>
            {summary.count === 0 && (
              <div className="no-expenses">
                No expenses found for this month.
              </div>
            )}
          </div>
        )}

        {!summary && !loading && !error && (
          <div className="no-data">
            Select a month and click "Get Summary" to view statistics.
          </div>
        )}
      </div>
    </div>
  )
}

export default Summary

