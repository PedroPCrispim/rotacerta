import { useState, useEffect } from 'react'
import { 
  Play, 
  Map as MapIcon, 
  CheckCircle2, 
  Clock, 
  Navigation,
  ExternalLink,
  Smartphone,
  Loader2
} from 'lucide-react'
import api from '../services/api'

const Routing = () => {
  const [step, setStep] = useState(1) // 1: Setup, 2: Result
  const [isCalculating, setIsCalculating] = useState(false)
  const [bases, setBases] = useState([])
  const [selectedBaseId, setSelectedBaseId] = useState('')
  const [routingResult, setRoutingResult] = useState(null)

  useEffect(() => {
    const fetchBases = async () => {
      try {
        const response = await api.get('/addresses')
        setBases(response.data)
        if (response.data.length > 0) setSelectedBaseId(response.data[0].id)
      } catch (err) {
        console.error('Erro ao buscar endereços', err)
      }
    }
    fetchBases()
  }, [])

  const handleCalculate = async () => {
    if (!selectedBaseId) return
    setIsCalculating(true)
    try {
      const response = await api.post('/routing/calculate', { depotAddressId: selectedBaseId })
      setRoutingResult(response.data)
      setStep(2)
    } catch (err) {
      alert(err.response?.data?.message || 'Erro ao calcular rota')
    } finally {
      setIsCalculating(false)
    }
  }

  return (
    <div className="space-y-6 max-w-6xl mx-auto">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">Otimização de Rotas</h1>
          <p className="text-gray-500">Gere a melhor sequência de entregas para sua frota.</p>
        </div>
        {step === 2 && (
          <button 
            onClick={() => setStep(1)}
            className="text-sm font-bold text-brand-blue hover:underline"
          >
            ← Novo Planejamento
          </button>
        )}
      </div>

      {step === 1 ? (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* Passo 1: Base */}
          <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-sm">
            <div className="w-10 h-10 bg-brand-yellow/20 text-brand-black rounded-lg flex items-center justify-center mb-4">
              <MapIcon size={20} />
            </div>
            <h3 className="font-bold text-gray-800 mb-2">1. Ponto de Partida</h3>
            <p className="text-sm text-gray-500 mb-4">Selecione o depósito ou base de onde os veículos sairão.</p>
            <select 
              className="input-field text-sm"
              value={selectedBaseId}
              onChange={(e) => setSelectedBaseId(e.target.value)}
            >
              {bases.map((b) => (
                <option key={b.id} value={b.id}>{b.street}, {b.number} - {b.city}</option>
              ))}
            </select>
          </div>

          {/* Passo 2: Veículos */}
          <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-sm">
            <div className="w-10 h-10 bg-blue-50 text-brand-blue rounded-lg flex items-center justify-center mb-4">
              <Navigation size={20} />
            </div>
            <h3 className="font-bold text-gray-800 mb-2">2. Frota Disponível</h3>
            <p className="text-sm text-gray-500 mb-4">Selecione os veículos que participarão desta rota.</p>
            <div className="space-y-2 max-h-32 overflow-y-auto">
              {['Caminhão ABC-1234', 'Van DEF-5678', 'Fiorino GHI-9012'].map((v) => (
                <label key={v} className="flex items-center gap-3 p-2 hover:bg-gray-50 rounded-lg cursor-pointer">
                  <input type="checkbox" defaultChecked className="w-4 h-4 text-brand-blue rounded" />
                  <span className="text-sm text-gray-700">{v}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Passo 3: Executar */}
          <div className="bg-brand-black p-6 rounded-2xl shadow-xl text-white flex flex-col justify-between">
            <div>
              <h3 className="font-bold mb-2">3. Otimizar Agora</h3>
              <p className="text-sm text-gray-400">Temos 42 pontos pendentes para entrega. O sistema calculará a melhor distribuição.</p>
            </div>
            <button 
              onClick={handleCalculate}
              disabled={isCalculating}
              className="btn-secondary w-full flex items-center justify-center gap-2 mt-6 py-4 disabled:bg-gray-600 disabled:text-gray-400"
            >
              {isCalculating ? (
                <>Calculando...</>
              ) : (
                <>
                  <Play size={20} fill="currentColor" />
                  Iniciar Roteirização
                </>
              )}
            </button>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 animate-in fade-in duration-500">
          {/* Mapa Simulado */}
          <div className="lg:col-span-2 bg-gray-200 rounded-3xl min-h-[500px] relative overflow-hidden border-4 border-white shadow-2xl">
            <div className="absolute inset-0 flex items-center justify-center text-gray-400 flex-col gap-2">
              <MapIcon size={64} />
              <span className="font-medium">Mapa Interativo (Integração Google)</span>
            </div>
            {/* Overlay de Resumo no Mapa */}
            <div className="absolute bottom-6 left-6 right-6 bg-white/90 backdrop-blur p-4 rounded-2xl border border-white flex justify-around shadow-lg">
              <div className="text-center">
                <p className="text-[10px] uppercase font-bold text-gray-400">Total KM</p>
                <p className="font-bold text-gray-800">{(routingResult?.totalDistance / 1000).toFixed(1)} km</p>
              </div>
              <div className="w-px bg-gray-200" />
              <div className="text-center">
                <p className="text-[10px] uppercase font-bold text-gray-400">Tempo Est.</p>
                <p className="font-bold text-gray-800">{Math.round(routingResult?.totalEstimatedTime / 60)} min</p>
              </div>
              <div className="w-px bg-gray-200" />
              <div className="text-center">
                <p className="text-[10px] uppercase font-bold text-gray-400">Status</p>
                <p className="font-bold text-green-600">Otimizado</p>
              </div>
            </div>
          </div>

          {/* Lista de Rotas Otimizadas */}
          <div className="space-y-4">
            <h3 className="font-bold text-gray-800 flex items-center gap-2">
              <CheckCircle2 className="text-green-500" size={20} />
              Rotas Geradas
            </h3>
            
            {routingResult?.routes.map((route, i) => (
              <div key={i} className={`bg-white p-5 rounded-2xl border-l-4 ${i % 2 === 0 ? 'border-l-brand-blue' : 'border-l-brand-yellow'} shadow-sm hover:shadow-md transition-shadow`}>
                <div className="flex justify-between items-start mb-3">
                  <h4 className="font-bold text-gray-800 text-sm">Veículo: {route.vehicleId.substring(0, 8)}</h4>
                  <span className="text-[10px] font-bold bg-gray-100 px-2 py-0.5 rounded uppercase">Rota #{i + 1}</span>
                </div>
                <div className="flex gap-4 text-xs text-gray-500 mb-4">
                  <span className="flex items-center gap-1"><MapIcon size={12} /> {route.visits.length} paradas</span>
                  <span className="flex items-center gap-1"><Navigation size={12} /> {(route.distance / 1000).toFixed(1)} km</span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <button className="flex items-center justify-center gap-2 text-xs font-bold bg-gray-100 hover:bg-gray-200 py-2 rounded-lg transition-colors">
                    <Smartphone size={14} />
                    WhatsApp
                  </button>
                  <a 
                    href={route.googleMapsUrl} 
                    target="_blank" 
                    rel="noreferrer"
                    className="flex items-center justify-center gap-2 text-xs font-bold bg-brand-blue text-white hover:bg-blue-700 py-2 rounded-lg transition-colors"
                  >
                    <ExternalLink size={14} />
                    Google Maps
                  </a>
                </div>
              </div>
            ))}

            <button className="w-full py-4 bg-green-600 text-white font-bold rounded-2xl shadow-lg shadow-green-200 hover:bg-green-700 transition-all flex items-center justify-center gap-2">
              Finalizar e Salvar Planejamento
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default Routing
