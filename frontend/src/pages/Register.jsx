import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Building2, User, Mail, Lock, ArrowRight, Loader2 } from 'lucide-react'
import api from '../services/api'

const Register = () => {
  const [formData, setFormData] = useState({
    companyName: '',
    cnpj: '',
    adminName: '',
    adminEmail: '',
    adminPassword: ''
  })
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    setError('')

    try {
      await api.post('/auth/register', formData)
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message || 'Erro ao cadastrar empresa. Verifique os dados.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  return (
    <div className="min-h-screen bg-brand-black flex items-center justify-center p-4">
      <div className="max-w-2xl w-full bg-white rounded-2xl shadow-xl overflow-hidden flex flex-col md:flex-row">
        {/* ... (sidebar content) ... */}
        <div className="md:w-1/3 bg-brand-blue p-8 text-white flex flex-col justify-between">
          <div>
            <div className="w-12 h-12 bg-brand-yellow rounded-xl flex items-center justify-center text-brand-black font-bold text-xl mb-6">
              RC
            </div>
            <h2 className="text-2xl font-bold mb-4">Escalabilidade para sua Logística</h2>
            <p className="text-blue-100 text-sm">Junte-se a milhares de empresas que já otimizam suas rotas diariamente.</p>
          </div>
        </div>

        <div className="md:w-2/3 p-8">
          <h1 className="text-2xl font-bold text-gray-800 mb-6">Cadastrar Empresa</h1>
          
          {error && (
            <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm mb-4 border border-red-100">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">Nome da Empresa</label>
                <div className="relative">
                  <Building2 className="absolute left-3 top-3 text-gray-400" size={18} />
                  <input 
                    name="companyName"
                    type="text" 
                    className="input-field pl-10" 
                    placeholder="Minha Logística LTDA" 
                    value={formData.companyName}
                    onChange={handleChange}
                    required 
                  />
                </div>
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">CNPJ</label>
                <input 
                  name="cnpj"
                  type="text" 
                  className="input-field" 
                  placeholder="00.000.000/0001-00" 
                  value={formData.cnpj}
                  onChange={handleChange}
                  required 
                />
              </div>
            </div>

            <hr className="my-6 border-gray-100" />

            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">Nome do Administrador</label>
              <div className="relative">
                <User className="absolute left-3 top-3 text-gray-400" size={18} />
                <input 
                  name="adminName"
                  type="text" 
                  className="input-field pl-10" 
                  placeholder="Seu nome completo" 
                  value={formData.adminName}
                  onChange={handleChange}
                  required 
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">Email Corporativo</label>
              <div className="relative">
                <Mail className="absolute left-3 top-3 text-gray-400" size={18} />
                <input 
                  name="adminEmail"
                  type="email" 
                  className="input-field pl-10" 
                  placeholder="admin@empresa.com" 
                  value={formData.adminEmail}
                  onChange={handleChange}
                  required 
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-700">Senha de Acesso</label>
              <div className="relative">
                <Lock className="absolute left-3 top-3 text-gray-400" size={18} />
                <input 
                  name="adminPassword"
                  type="password" 
                  className="input-field pl-10" 
                  placeholder="••••••••" 
                  value={formData.adminPassword}
                  onChange={handleChange}
                  required 
                />
              </div>
            </div>

            <button 
              type="submit" 
              disabled={isLoading}
              className="btn-primary w-full flex items-center justify-center gap-2 py-3 mt-6 disabled:bg-blue-300"
            >
              {isLoading ? <Loader2 className="animate-spin" size={18} /> : <ArrowRight size={18} />}
              {isLoading ? 'Cadastrando...' : 'Criar Conta e Acessar'}
            </button>

            <p className="text-center text-sm text-gray-500 mt-4">
              Já possui conta?{' '}
              <Link to="/login" className="text-brand-blue font-bold hover:underline">Faça login</Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  )
}

export default Register
