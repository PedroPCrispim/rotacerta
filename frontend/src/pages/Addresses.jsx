import { useState, useEffect } from 'react'
import { Plus, Search, MapPin, ExternalLink, FileSpreadsheet, Loader2 } from 'lucide-react'
import api from '../services/api'

const Addresses = () => {
  const [addresses, setAddresses] = useState([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const fetchAddresses = async () => {
      try {
        const response = await api.get('/addresses')
        setAddresses(response.data)
      } catch (err) {
        console.error('Erro ao buscar endereços', err)
      } finally {
        setIsLoading(false)
      }
    }
    fetchAddresses()
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
          <h1 className="text-2xl font-bold text-gray-800">Endereços e Clientes</h1>
          <p className="text-gray-500">Gerencie os pontos de entrega e suas bases operacionais.</p>
        </div>
        <div className="flex gap-2">
          <button className="bg-white text-gray-700 px-4 py-2 rounded-lg border border-gray-200 font-medium hover:bg-gray-50 transition-colors flex items-center gap-2">
            <FileSpreadsheet size={18} />
            Importar CSV
          </button>
          <button className="btn-primary flex items-center gap-2">
            <Plus size={18} />
            Novo Endereço
          </button>
        </div>
      </div>

      <div className="bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">
        <div className="p-4 border-b border-gray-100">
          <div className="relative">
            <Search className="absolute left-3 top-2.5 text-gray-400" size={18} />
            <input 
              type="text" 
              placeholder="Buscar endereço ou cliente..." 
              className="w-full pl-10 pr-4 py-2 bg-gray-50 border-none rounded-lg focus:ring-2 focus:ring-brand-blue outline-none text-sm"
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 p-6">
          {addresses.map((addr) => (
            <div key={addr.id} className="p-4 border border-gray-100 rounded-xl hover:border-brand-blue/30 hover:shadow-sm transition-all group">
              <div className="flex items-start justify-between mb-3">
                <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${
                  addr.type === 'Base' ? 'bg-brand-yellow/20 text-brand-black' : 'bg-blue-50 text-brand-blue'
                }`}>
                  <MapPin size={20} />
                </div>
                <span className={`text-[10px] font-bold uppercase tracking-wider px-2 py-1 rounded ${
                  addr.type === 'Base' ? 'bg-brand-black text-brand-yellow' : 'bg-gray-100 text-gray-500'
                }`}>
                  {addr.type}
                </span>
              </div>
              <h3 className="font-bold text-gray-800">{addr.name}</h3>
              <p className="text-sm text-gray-500 mt-1">{addr.address}</p>
              <p className="text-xs text-gray-400 mt-0.5">{addr.city}</p>
              
              <div className="mt-4 pt-4 border-t border-gray-50 flex justify-between items-center">
                <button className="text-xs font-bold text-brand-blue hover:underline flex items-center gap-1">
                  Ver no Mapa
                  <ExternalLink size={12} />
                </button>
                <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button className="text-xs text-gray-400 hover:text-brand-blue">Editar</button>
                  <button className="text-xs text-gray-400 hover:text-red-500">Excluir</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default Addresses
