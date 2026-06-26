import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { LogIn, ShieldCheck, Loader2 } from 'lucide-react'
import api from '../services/api'
import useAuthStore from '../store/useAuthStore'

const Login = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const setAuth = useAuthStore((state) => state.setAuth)

  const handleLogin = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    setError('')
    
    try {
      const response = await api.post('/auth/login', { email, password })
      setAuth(
        {
          name: response.data.name,
          email: response.data.email,
          role: response.data.role,
          companyId: response.data.companyId,
          viewAccess: response.data.viewAccess,
        },
        response.data.token,
      )
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Erro ao realizar login. Verifique suas credenciais.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-brand-black flex items-center justify-center p-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-xl overflow-hidden">
        <div className="bg-brand-blue p-8 text-white text-center">
          <div className="w-16 h-16 bg-brand-yellow rounded-2xl flex items-center justify-center text-brand-black font-bold text-2xl mx-auto mb-4 shadow-lg">
            RC
          </div>
          <h1 className="text-2xl font-bold">Bem-vindo ao RotaCerta</h1>
          <p className="text-blue-100 mt-2">SaaS de Otimização Logística</p>
        </div>
        
        <form onSubmit={handleLogin} className="p-8 space-y-6">
          {error && (
            <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm font-medium border border-red-100">
              {error}
            </div>
          )}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Email</label>
            <input 
              type="email" 
              className="input-field" 
              placeholder="seu@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required 
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Senha</label>
            <input 
              type="password" 
              className="input-field" 
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required 
            />
          </div>

          <button 
            type="submit" 
            disabled={isLoading}
            className="btn-primary w-full flex items-center justify-center gap-2 py-3 disabled:bg-blue-300"
          >
            {isLoading ? <Loader2 className="animate-spin" size={20} /> : <LogIn size={20} />}
            {isLoading ? 'Autenticando...' : 'Entrar no Sistema'}
          </button>

          <div className="text-center">
            <p className="text-sm text-gray-600">
              Não tem uma conta?{' '}
              <Link to="/register" className="text-brand-blue font-bold hover:underline">
                Cadastre sua empresa
              </Link>
            </p>
          </div>
        </form>
        
        <div className="bg-gray-50 p-4 text-center border-t border-gray-100">
          <div className="flex items-center justify-center gap-2 text-xs text-gray-400 uppercase tracking-widest">
            <ShieldCheck size={14} />
            Acesso Seguro via SSL/JWT
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login
