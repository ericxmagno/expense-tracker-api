import axios from 'axios'

// In production (served from backend), use relative path
// In development, Vite proxy will handle /api requests
const API_BASE_URL = '/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const expenseService = {
  // Get all expenses with filters
  getExpenses: async (params = {}) => {
    const {
      page = 0,
      size = 20,
      category,
      start,
      end,
      sortBy = 'date',
      direction = 'DESC',
    } = params

    const queryParams = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      sortBy,
      direction,
    })

    if (category) queryParams.append('category', category)
    if (start) queryParams.append('start', start)
    if (end) queryParams.append('end', end)

    const response = await api.get(`/expenses?${queryParams.toString()}`)
    return response.data
  },

  // Get expense by ID
  getExpenseById: async (id) => {
    const response = await api.get(`/expenses/${id}`)
    return response.data
  },

  // Create expense
  createExpense: async (expenseData) => {
    const response = await api.post('/expenses', expenseData)
    return response.data
  },

  // Update expense
  updateExpense: async (id, expenseData) => {
    const response = await api.put(`/expenses/${id}`, expenseData)
    return response.data
  },

  // Delete expense
  deleteExpense: async (id) => {
    await api.delete(`/expenses/${id}`)
  },

  // Get monthly summary
  getSummary: async (month) => {
    const response = await api.get(`/expenses/summary?month=${month}`)
    return response.data
  },
}

export const categories = [
  { value: 'FOOD', label: 'Food' },
  { value: 'ENT', label: 'Entertainment' },
  { value: 'TRAVEL', label: 'Travel' },
  { value: 'RENT', label: 'Rent' },
  { value: 'OTHER', label: 'Other' },
]

