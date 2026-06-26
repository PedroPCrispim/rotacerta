import { Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import MainLayout from './layouts/MainLayout'
import Vehicles from './pages/Vehicles'
import Addresses from './pages/Addresses'
import Routing from './pages/Routing'
import Drivers from './pages/Drivers'
import DriverPortal from './pages/DriverPortal'

function App() {
  const isAuthenticated = !!localStorage.getItem('token')

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      
      <Route element={isAuthenticated ? <MainLayout /> : <Navigate to="/login" />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/vehicles" element={<Vehicles />} />
        <Route path="/addresses" element={<Addresses />} />
        <Route path="/routing" element={<Routing />} />
        <Route path="/drivers" element={<Drivers />} />
        <Route path="/driver-portal" element={<DriverPortal />} />
      </Route>

      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  )
}

export default App
