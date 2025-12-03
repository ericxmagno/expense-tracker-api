import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import ExpenseList from './components/ExpenseList'
import ExpenseForm from './components/ExpenseForm'
import Summary from './components/Summary'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app">
        <nav className="navbar">
          <div className="nav-container">
            <h1 className="nav-title">Expense Tracker</h1>
            <div className="nav-links">
              <Link to="/" className="nav-link">Expenses</Link>
              <Link to="/add" className="nav-link">Add Expense</Link>
              <Link to="/summary" className="nav-link">Summary</Link>
            </div>
          </div>
        </nav>
        <main className="main-content">
          <Routes>
            <Route path="/" element={<ExpenseList />} />
            <Route path="/add" element={<ExpenseForm />} />
            <Route path="/edit/:id" element={<ExpenseForm />} />
            <Route path="/summary" element={<Summary />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App

