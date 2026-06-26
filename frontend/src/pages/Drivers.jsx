import { useEffect, useMemo, useState } from 'react'
import { Loader2, Plus, Smartphone, UserRound } from 'lucide-react'
import { Link } from 'react-router-dom'
import api from '../services/api'

const initialForm = {
  name: '',
  email: '',
  phone: '',
  licenseNumber: '',
  status: 'Ativo no dia',
  assignedVehicleId: '',
  nextStopAddressId: '',
  currentRouteLabel: '',
  routeExecutionStatus: 'Aguardando saida',
  checkInRequired: false,
}

const Drivers = () => {
  const [drivers, setDrivers] = useState([])
  const [vehicles, setVehicles] = useState([])
  const [addresses, setAddresses] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState('')
  const [form, setForm] = useState(initialForm)

  const fetchData = async () => {
    setIsLoading(true)
    setError('')

    try {
      const [driversResponse, vehiclesResponse, addressesResponse] = await Promise.all([
        api.get('/drivers'),
        api.get('/vehicles'),
        api.get('/addresses'),
      ])
      setDrivers(driversResponse.data)
      setVehicles(vehiclesResponse.data)
      setAddresses(addressesResponse.data)
    } catch (err) {
      console.error('Erro ao carregar motoristas', err)
      setError('Nao foi possivel carregar os motoristas agora.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const linkedDrivers = useMemo(
    () => drivers.filter((driver) => driver.assignedVehicleId),
    [drivers],
  )

  const handleChange = (field, value) => {
    setForm((current) => ({
      ...current,
      [field]: value,
    }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setIsSubmitting(true)
    setError('')

    try {
      await api.post('/drivers', {
        ...form,
        assignedVehicleId: form.assignedVehicleId || null,
        nextStopAddressId: form.nextStopAddressId || null,
      })
      setForm(initialForm)
      await fetchData()
    } catch (err) {
      console.error('Erro ao salvar motorista', err)
      setError(err.response?.data?.message || 'Nao foi possivel salvar o motorista.')
    } finally {
      setIsSubmitting(false)
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <Loader2 className="animate-spin text-brand-blue" size={48} />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Motoristas</h1>
            <p className="text-sm text-gray-500 mt-1">
              Cadastre motoristas, vincule veículos e prepare a base do portal mobile.
            </p>
          </div>
          <Link
            to="/driver-portal"
            className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-800 inline-flex items-center gap-2"
          >
            <Smartphone size={18} />
            Abrir portal
          </Link>
        </div>
      </section>

      {error && (
        <div className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      <section className="grid grid-cols-1 xl:grid-cols-[0.95fr_1.05fr] gap-6">
        <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
          <div className="flex items-center gap-3 mb-5">
            <Plus className="text-brand-blue" size={20} />
            <div>
              <h3 className="text-lg font-bold text-gray-900">Novo motorista</h3>
              <p className="text-sm text-gray-500">Crie o cadastro operacional e defina a base do portal.</p>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Nome</span>
                <input value={form.name} onChange={(e) => handleChange('name', e.target.value)} className="input-field" />
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Email</span>
                <input value={form.email} onChange={(e) => handleChange('email', e.target.value)} className="input-field" />
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Telefone</span>
                <input value={form.phone} onChange={(e) => handleChange('phone', e.target.value)} className="input-field" />
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">CNH</span>
                <input value={form.licenseNumber} onChange={(e) => handleChange('licenseNumber', e.target.value)} className="input-field" />
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Status</span>
                <select value={form.status} onChange={(e) => handleChange('status', e.target.value)} className="input-field">
                  <option value="Ativo no dia">Ativo no dia</option>
                  <option value="Em rota">Em rota</option>
                  <option value="Em descanso">Em descanso</option>
                  <option value="Indisponivel">Indisponivel</option>
                </select>
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Veiculo vinculado</span>
                <select value={form.assignedVehicleId} onChange={(e) => handleChange('assignedVehicleId', e.target.value)} className="input-field">
                  <option value="">Sem veiculo</option>
                  {vehicles.map((vehicle) => (
                    <option key={vehicle.id} value={vehicle.id}>
                      {vehicle.model} • {vehicle.plate}
                    </option>
                  ))}
                </select>
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Proxima parada</span>
                <select value={form.nextStopAddressId} onChange={(e) => handleChange('nextStopAddressId', e.target.value)} className="input-field">
                  <option value="">Sem parada definida</option>
                  {addresses.map((address) => (
                    <option key={address.id} value={address.id}>
                      {address.street}, {address.number} - {address.city}
                    </option>
                  ))}
                </select>
              </label>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Rota atual</span>
                <input value={form.currentRouteLabel} onChange={(e) => handleChange('currentRouteLabel', e.target.value)} className="input-field" />
              </label>
              <label className="block text-sm">
                <span className="block text-gray-600 mb-2">Status da execucao</span>
                <select value={form.routeExecutionStatus} onChange={(e) => handleChange('routeExecutionStatus', e.target.value)} className="input-field">
                  <option value="Aguardando saida">Aguardando saida</option>
                  <option value="Em rota">Em rota</option>
                  <option value="Concluida">Concluida</option>
                  <option value="Parado">Parado</option>
                </select>
              </label>
            </div>

            <label className="flex items-center gap-3 text-sm text-gray-700">
              <input
                type="checkbox"
                checked={form.checkInRequired}
                onChange={(e) => handleChange('checkInRequired', e.target.checked)}
                className="w-4 h-4 rounded"
              />
              Exigir check-in/check-out no portal do motorista
            </label>

            <button type="submit" disabled={isSubmitting} className="btn-primary rounded-2xl px-5">
              {isSubmitting ? 'Salvando...' : 'Salvar motorista'}
            </button>
          </form>
        </div>

        <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
          <div className="flex items-center gap-3 mb-5">
            <UserRound className="text-brand-blue" size={20} />
            <div>
              <h3 className="text-lg font-bold text-gray-900">Equipe cadastrada</h3>
              <p className="text-sm text-gray-500">Visao rapida da alocacao entre motorista, veiculo e rota.</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mb-5">
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Motoristas</p>
              <p className="text-2xl font-bold text-gray-900 mt-2">{drivers.length}</p>
            </div>
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Com veiculo</p>
              <p className="text-2xl font-bold text-green-600 mt-2">{linkedDrivers.length}</p>
            </div>
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Sem veiculo</p>
              <p className="text-2xl font-bold text-brand-blue mt-2">{drivers.length - linkedDrivers.length}</p>
            </div>
          </div>

          <div className="space-y-3">
            {drivers.length === 0 ? (
              <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                Nenhum motorista cadastrado ainda.
              </div>
            ) : (
              drivers.map((driver) => (
                <div key={driver.id} className="border border-gray-100 rounded-2xl px-4 py-4">
                  <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                    <div>
                      <p className="font-semibold text-gray-900">{driver.name}</p>
                      <p className="text-sm text-gray-500 mt-1">
                        {driver.assignedVehicleLabel || 'Sem veiculo'} • {driver.currentRouteLabel || 'Sem rota atribuida'}
                      </p>
                      <p className="text-xs text-gray-400 mt-2">
                        Proxima parada: {driver.nextStopLabel || 'Sem parada definida'}
                      </p>
                    </div>
                    <span className="inline-flex px-3 py-1 rounded-full text-xs font-semibold bg-gray-100 text-gray-700">
                      {driver.routeExecutionStatus || driver.status}
                    </span>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </section>
    </div>
  )
}

export default Drivers
