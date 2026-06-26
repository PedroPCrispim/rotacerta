import { useEffect, useMemo, useState } from 'react'
import { CheckCircle2, ExternalLink, Loader2, MapPin, Smartphone, Truck } from 'lucide-react'
import api from '../services/api'
import { buildGoogleMapsLink, buildWazeDeepLink, hasExternalNavigationLink } from '../utils/navigation'

const DriverPortal = () => {
  const [drivers, setDrivers] = useState([])
  const [selectedDriverId, setSelectedDriverId] = useState('')
  const [portal, setPortal] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [isPortalLoading, setIsPortalLoading] = useState(false)
  const [error, setError] = useState('')

  const navigationQuery = useMemo(() => {
    const segments = [portal?.nextStopFullAddress, portal?.currentRouteLabel, portal?.assignedVehicleLabel].filter(Boolean)
    return segments.join(' - ')
  }, [portal])

  const wazeLink = useMemo(
    () =>
      buildWazeDeepLink({
        latitude: portal?.nextStopLatitude,
        longitude: portal?.nextStopLongitude,
        query: navigationQuery,
      }),
    [navigationQuery, portal?.nextStopLatitude, portal?.nextStopLongitude],
  )
  const googleMapsLink = useMemo(
    () =>
      buildGoogleMapsLink({
        latitude: portal?.nextStopLatitude,
        longitude: portal?.nextStopLongitude,
        query: navigationQuery,
      }),
    [navigationQuery, portal?.nextStopLatitude, portal?.nextStopLongitude],
  )
  const canNavigate = hasExternalNavigationLink(wazeLink, googleMapsLink)

  const fetchPortal = async (driverId) => {
    if (!driverId) {
      setPortal(null)
      return
    }

    setIsPortalLoading(true)
    setError('')

    try {
      const response = await api.get(`/drivers/${driverId}/portal`)
      setPortal(response.data)
    } catch (err) {
      console.error('Erro ao carregar portal do motorista', err)
      setError('Nao foi possivel carregar o portal do motorista agora.')
    } finally {
      setIsPortalLoading(false)
    }
  }

  useEffect(() => {
    const fetchDrivers = async () => {
      setIsLoading(true)
      setError('')

      try {
        const response = await api.get('/drivers')
        setDrivers(response.data)
        if (response.data.length > 0) {
          setSelectedDriverId(response.data[0].id)
          fetchPortal(response.data[0].id)
        }
      } catch (err) {
        console.error('Erro ao buscar motoristas', err)
        setError('Nao foi possivel carregar os motoristas agora.')
      } finally {
        setIsLoading(false)
      }
    }

    fetchDrivers()
  }, [])

  const handleDriverChange = async (driverId) => {
    setSelectedDriverId(driverId)
    await fetchPortal(driverId)
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <Loader2 className="animate-spin text-brand-blue" size={48} />
      </div>
    )
  }

  return (
    <div className="max-w-md mx-auto space-y-6">
      <section className="bg-brand-black text-white rounded-[32px] shadow-2xl p-6">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 rounded-2xl bg-brand-yellow/20 text-brand-yellow flex items-center justify-center">
            <Smartphone size={22} />
          </div>
          <div>
            <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Portal do motorista</p>
            <h1 className="text-2xl font-bold mt-1">Execucao mobile</h1>
          </div>
        </div>
        <p className="text-sm text-gray-400 mt-4">
          Base mobile-first para check-in, check-out, rota atribuida e leitura operacional no celular.
        </p>
      </section>

      {error && (
        <div className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      <section className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-5">
        <label className="block text-sm">
          <span className="block text-gray-600 mb-2">Selecionar motorista</span>
          <select
            value={selectedDriverId}
            onChange={(e) => handleDriverChange(e.target.value)}
            className="input-field"
          >
            {drivers.map((driver) => (
              <option key={driver.id} value={driver.id}>
                {driver.name}
              </option>
            ))}
          </select>
        </label>
      </section>

      {isPortalLoading ? (
        <div className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-10 flex justify-center">
          <Loader2 className="animate-spin text-brand-blue" size={36} />
        </div>
      ) : (
        <div className="space-y-4">
          <section className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-5">
            <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Motorista</p>
            <h2 className="text-2xl font-bold text-gray-900 mt-2">{portal?.driverName || 'Sem motorista'}</h2>
            <p className="text-sm text-gray-500 mt-2">{portal?.status || 'Sem status'}</p>
          </section>

          <section className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-5">
            <div className="flex items-center gap-3">
              <Truck className="text-brand-blue" size={20} />
              <div>
                <p className="text-sm font-semibold text-gray-900">Veiculo vinculado</p>
                <p className="text-sm text-gray-500 mt-1">{portal?.assignedVehicleLabel || 'Sem veiculo'}</p>
              </div>
            </div>
          </section>

          <section className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-5">
            <div className="flex items-start gap-3">
              <MapPin className="text-brand-blue mt-1" size={20} />
              <div>
                <p className="text-sm font-semibold text-gray-900">Rota do dia</p>
                <p className="text-lg font-bold text-gray-900 mt-2">{portal?.currentRouteLabel || 'Aguardando atribuicao'}</p>
                <p className="text-sm text-gray-500 mt-2">{portal?.routeExecutionStatus || 'Sem execucao iniciada'}</p>
              </div>
            </div>
          </section>

          <section className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-5">
            <div className="flex items-start gap-3">
              <MapPin className="text-green-600 mt-1" size={20} />
              <div>
                <p className="text-sm font-semibold text-gray-900">Proxima parada</p>
                <p className="text-lg font-bold text-gray-900 mt-2">{portal?.nextStopLabel || 'Sem parada definida'}</p>
                <p className="text-sm text-gray-500 mt-2">{portal?.nextStopFullAddress || 'Defina uma parada no cadastro do motorista para liberar navegacao exata.'}</p>
              </div>
            </div>
          </section>

          <section className="bg-white rounded-[28px] border border-gray-100 shadow-sm p-5 space-y-4">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-sm font-semibold text-gray-900">Navegacao externa</p>
                <p className="text-sm text-gray-500 mt-1">
                  Abra a rota atribuida diretamente no aplicativo de navegacao do motorista.
                </p>
              </div>
              <ExternalLink className="text-brand-blue" size={18} />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <a
                href={wazeLink || '#'}
                target="_blank"
                rel="noreferrer"
                aria-disabled={!wazeLink}
                className={`rounded-[20px] px-4 py-4 text-sm font-semibold text-center transition-colors ${
                  wazeLink
                    ? 'bg-brand-blue text-white hover:bg-blue-700'
                    : 'bg-gray-100 text-gray-400 pointer-events-none'
                }`}
              >
                Abrir no Waze
              </a>
              <a
                href={googleMapsLink || '#'}
                target="_blank"
                rel="noreferrer"
                aria-disabled={!googleMapsLink}
                className={`rounded-[20px] px-4 py-4 text-sm font-semibold text-center transition-colors ${
                  googleMapsLink
                    ? 'bg-green-50 text-green-700 hover:bg-green-100'
                    : 'bg-gray-100 text-gray-400 pointer-events-none'
                }`}
              >
                Abrir no Google Maps
              </a>
            </div>

            <p className="text-xs text-gray-400">
              {canNavigate
                ? portal?.nextStopLatitude !== null && portal?.nextStopLongitude !== null
                  ? 'O aplicativo sera aberto com a proxima parada real do motorista.'
                  : 'A navegacao usa a melhor referencia disponivel enquanto a parada ainda nao possui coordenadas.'
                : 'Nenhuma rota atribuida disponivel para navegacao neste momento.'}
            </p>
          </section>

          <section className="grid grid-cols-2 gap-4">
            <button type="button" className="rounded-[24px] bg-green-50 text-green-700 font-semibold py-4">
              Check-in
            </button>
            <button type="button" className="rounded-[24px] bg-blue-50 text-brand-blue font-semibold py-4">
              Check-out
            </button>
          </section>

          <section className="bg-brand-black text-white rounded-[28px] shadow-xl p-5">
            <div className="flex items-center gap-3">
              <CheckCircle2 className="text-brand-yellow" size={20} />
              <div>
                <p className="font-semibold">Base pronta para prova de entrega</p>
                <p className="text-sm text-gray-400 mt-1">
                  Check-in exigido: {portal?.checkInRequired ? 'Sim' : 'Nao'}
                </p>
              </div>
            </div>
          </section>
        </div>
      )}
    </div>
  )
}

export default DriverPortal
