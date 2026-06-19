import { useState, useEffect } from 'react'
import { Plus, Search, MoreVertical, Truck, Loader2 } from 'lucide-react'
import api from '../services/api'

const Vehicles = () => {
  const [vehicles, setVehicles] = useState([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        const response = await api.get('/vehicles')
        setVehicles(response.data)
      } catch (err) {
        console.error('Erro ao buscar veículos', err)
      } finally {
        setIsLoading(false)
      }
    }
    fetchVehicles()
  }, [])

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <Loader2 className="animate-spin text-brand-blue" size={48} />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Gestão de Frota</h1>
          <p className="text-gray-500">Cadastre e monitore os veículos da sua empresa.</p>
        </div>
        <button className="btn-primary flex items-center gap-2">
          <Plus size={18} />
          Novo Veículo
        </button>
      </div>

      <div className="bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">
        <div className="p-4 border-b border-gray-100 flex items-center gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-2.5 text-gray-400" size={18} />
            <input 
              type="text" 
              placeholder="Buscar por placa ou modelo..." 
              className="w-full pl-10 pr-4 py-2 bg-gray-50 border-none rounded-lg focus:ring-2 focus:ring-brand-blue outline-none text-sm"
            />
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="bg-gray-50 text-xs font-bold text-gray-500 uppercase tracking-wider">
                <th className="px-6 py-4">Veículo</th>
                <th className="px-6 py-4">Placa</th>
                <th className="px-6 py-4">Capacidade</th>
                <th className="px-6 py-4">Consumo</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {vehicles.map((v) => (
                <tr key={v.id} className="hover:bg-gray-50/50 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 bg-blue-50 rounded-lg flex items-center justify-center text-brand-blue">
                        <Truck size={20} />
                      </div>
                      <span className="font-medium text-gray-700">{v.model}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 font-mono text-sm">{v.plate}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{v.capacity}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{v.consumption}</td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      v.status === 'Em Rota' ? 'bg-green-100 text-green-700' :
                      v.status === 'Disponível' ? 'bg-blue-100 text-brand-blue' :
                      'bg-orange-100 text-orange-700'
                    }`}>
                      {v.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button className="text-gray-400 hover:text-gray-600">
                      <MoreVertical size={18} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default Vehicles
