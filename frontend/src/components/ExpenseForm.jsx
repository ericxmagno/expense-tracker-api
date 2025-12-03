import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { expenseService, categories } from '../services/api'
import './ExpenseForm.css'

function ExpenseForm() {
  const navigate = useNavigate()
  const { id } = useParams()
  const isEdit = !!id

  const [formData, setFormData] = useState({
    title: '',
    amount: '',
    category: '',
    date: new Date().toISOString().split('T')[0],
    description: '',
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [loadingExpense, setLoadingExpense] = useState(isEdit)

  useEffect(() => {
    if (isEdit) {
      fetchExpense()
    }
  }, [id])

  const fetchExpense = async () => {
    try {
      setLoadingExpense(true)
      const expense = await expenseService.getExpenseById(id)
      setFormData({
        title: expense.title || '',
        amount: expense.amount?.toString() || '',
        category: expense.category || '',
        date: expense.date || new Date().toISOString().split('T')[0],
        description: expense.description || '',
      })
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch expense')
    } finally {
      setLoadingExpense(false)
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      const expenseData = {
        title: formData.title,
        amount: parseFloat(formData.amount),
        category: formData.category,
        date: formData.date,
        description: formData.description || null,
      }

      if (isEdit) {
        await expenseService.updateExpense(id, expenseData)
      } else {
        await expenseService.createExpense(expenseData)
      }

      navigate('/')
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data?.errors?.map((e) => e.defaultMessage).join(', ') ||
          'Failed to save expense'
      )
    } finally {
      setLoading(false)
    }
  }

  if (loadingExpense) {
    return <div className="loading">Loading expense...</div>
  }

  return (
    <div className="expense-form-container">
      <div className="expense-form">
        <h2>{isEdit ? 'Edit Expense' : 'Add New Expense'}</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="title">
              Title <span className="required">*</span>
            </label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="Enter expense title"
            />
          </div>

          <div className="form-group">
            <label htmlFor="amount">
              Amount <span className="required">*</span>
            </label>
            <input
              type="number"
              id="amount"
              name="amount"
              value={formData.amount}
              onChange={handleChange}
              required
              min="0"
              step="0.01"
              placeholder="0.00"
            />
          </div>

          <div className="form-group">
            <label htmlFor="category">
              Category <span className="required">*</span>
            </label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleChange}
              required
            >
              <option value="">Select a category</option>
              {categories.map((cat) => (
                <option key={cat.value} value={cat.value}>
                  {cat.label}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="date">
              Date <span className="required">*</span>
            </label>
            <input
              type="date"
              id="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              rows="4"
              placeholder="Optional description"
            />
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn-cancel"
              onClick={() => navigate('/')}
              disabled={loading}
            >
              Cancel
            </button>
            <button type="submit" className="btn-submit" disabled={loading}>
              {loading ? 'Saving...' : isEdit ? 'Update Expense' : 'Add Expense'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default ExpenseForm

