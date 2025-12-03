import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { expenseService, categories } from '../services/api'
import './ExpenseList.css'

function ExpenseList() {
  const navigate = useNavigate()
  const [expenses, setExpenses] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [filters, setFilters] = useState({
    category: '',
    start: '',
    end: '',
    sortBy: 'date',
    direction: 'DESC',
  })

  const fetchExpenses = useCallback(async () => {
    try {
      setLoading(true)
      setError(null)
      const params = {
        page,
        size: 20,
        ...filters,
      }
      // Remove empty filters
      Object.keys(params).forEach((key) => {
        if (params[key] === '') delete params[key]
      })
      const data = await expenseService.getExpenses(params)
      setExpenses(data.content || [])
      setTotalPages(data.totalPages || 0)
      setTotalElements(data.totalElements || 0)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch expenses')
    } finally {
      setLoading(false)
    }
  }, [page, filters])

  useEffect(() => {
    fetchExpenses()
  }, [fetchExpenses])

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this expense?')) {
      return
    }
    try {
      await expenseService.deleteExpense(id)
      fetchExpenses()
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to delete expense')
    }
  }

  const handleFilterChange = (key, value) => {
    setFilters((prev) => ({ ...prev, [key]: value }))
    setPage(0) // Reset to first page when filter changes
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    })
  }

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount)
  }

  return (
    <div className="expense-list">
      <div className="expense-list-header">
        <h2>Expenses</h2>
      </div>

      <div className="filters">
        <div className="filter-group">
          <label htmlFor="category-filter">Category:</label>
          <select
            id="category-filter"
            value={filters.category}
            onChange={(e) => handleFilterChange('category', e.target.value)}
          >
            <option value="">All Categories</option>
            {categories.map((cat) => (
              <option key={cat.value} value={cat.value}>
                {cat.label}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="start-date">Start Date:</label>
          <input
            id="start-date"
            type="date"
            value={filters.start}
            onChange={(e) => handleFilterChange('start', e.target.value)}
          />
        </div>

        <div className="filter-group">
          <label htmlFor="end-date">End Date:</label>
          <input
            id="end-date"
            type="date"
            value={filters.end}
            onChange={(e) => handleFilterChange('end', e.target.value)}
          />
        </div>

        <div className="filter-group">
          <label htmlFor="sort-by">Sort By:</label>
          <select
            id="sort-by"
            value={filters.sortBy}
            onChange={(e) => handleFilterChange('sortBy', e.target.value)}
          >
            <option value="date">Date</option>
            <option value="title">Title</option>
            <option value="amount">Amount</option>
            <option value="id">ID</option>
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="direction">Direction:</label>
          <select
            id="direction"
            value={filters.direction}
            onChange={(e) => handleFilterChange('direction', e.target.value)}
          >
            <option value="DESC">Descending</option>
            <option value="ASC">Ascending</option>
          </select>
        </div>

        <button
          className="btn-clear-filters"
          onClick={() => {
            setFilters({
              category: '',
              start: '',
              end: '',
              sortBy: 'date',
              direction: 'DESC',
            })
            setPage(0)
          }}
        >
          Clear Filters
        </button>
      </div>

      {loading && <div className="loading">Loading expenses...</div>}
      {error && <div className="error">Error: {error}</div>}

      {!loading && !error && (
        <>
          <div className="expense-table-container">
            <table className="expense-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Category</th>
                  <th>Amount</th>
                  <th>Date</th>
                  <th>Description</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {expenses.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="no-data">
                      No expenses found
                    </td>
                  </tr>
                ) : (
                  expenses.map((expense) => (
                    <tr key={expense.id}>
                      <td>{expense.title}</td>
                      <td>
                        <span className="category-badge">
                          {categories.find((c) => c.value === expense.category)
                            ?.label || expense.category}
                        </span>
                      </td>
                      <td className="amount">{formatCurrency(expense.amount)}</td>
                      <td>{formatDate(expense.date)}</td>
                      <td className="description">
                        {expense.description || '-'}
                      </td>
                      <td>
                        <div className="action-buttons">
                          <button
                            className="btn-edit"
                            onClick={() => navigate(`/edit/${expense.id}`)}
                          >
                            Edit
                          </button>
                          <button
                            className="btn-delete"
                            onClick={() => handleDelete(expense.id)}
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          <div className="pagination">
            <button
              className="btn-pagination"
              disabled={page === 0}
              onClick={() => setPage(page - 1)}
            >
              Previous
            </button>
            <span className="page-info">
              Page {page + 1} of {totalPages || 1} (Total: {totalElements})
            </span>
            <button
              className="btn-pagination"
              disabled={page >= totalPages - 1}
              onClick={() => setPage(page + 1)}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  )
}

export default ExpenseList

