import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import {
  AlertTriangle,
  ArrowRight,
  BarChart3,
  CalendarRange,
  Clock,
  Fuel,
  Loader2,
  MapPin,
  PackageCheck,
  RefreshCw,
  Route,
  Settings2,
  ShieldCheck,
  ShieldAlert,
  Smartphone,
  Target,
  TrendingUp,
  Truck,
  UserRound,
  Users,
  Wallet,
  Wrench,
} from 'lucide-react'
import api from '../services/api'
import useAuthStore from '../store/useAuthStore'

const METRIC_KEYS = ['costSaved', 'distanceSaved', 'timeSaved', 'deliveries']
const OPERATIONAL_METRICS = ['distanceSaved', 'timeSaved', 'deliveries']
const FINANCIAL_METRICS = ['costSaved', 'distanceSaved', 'timeSaved', 'deliveries']
const ACCESS_STORAGE_KEY = 'rotacerta_dashboard_view_access'
const FINANCIAL_CONFIG_STORAGE_KEY = 'rotacerta_financial_config'

const DASHBOARD_VIEWS = [
  {
    key: 'operational',
    label: 'Visao Operacional',
    shortLabel: 'Operacional',
    description: 'Execucao, entregas, tempo e gargalos da operacao.',
    icon: BarChart3,
  },
  {
    key: 'financial',
    label: 'Visao Financeira',
    shortLabel: 'Financeira',
    description: 'Custos, economia, combustivel e leitura de ROI.',
    icon: Wallet,
  },
  {
    key: 'fleet',
    label: 'Visao Frota',
    shortLabel: 'Frota',
    description: 'Veiculos, capacidade e base do portal do motorista.',
    icon: Truck,
  },
]

const formatCurrency = (value) =>
  new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(Number(value || 0))

const formatNumber = (value) => new Intl.NumberFormat('pt-BR').format(Number(value || 0))

const toInputDate = (date) => date.toISOString().split('T')[0]

const granularityOptions = [
  { value: 'day', label: 'Dia' },
  { value: 'month', label: 'Mes' },
  { value: 'year', label: 'Ano' },
]

const metricDefinitions = {
  costSaved: {
    label: 'Combustivel',
    subtitle: 'Economia financeira',
    currentKey: 'currentCostSaved',
    previousKey: 'previousCostSaved',
    color: '#16a34a',
    softColor: '#dcfce7',
    icon: Fuel,
  },
  distanceSaved: {
    label: 'KM',
    subtitle: 'Quilometragem reduzida',
    currentKey: 'currentDistanceSaved',
    previousKey: 'previousDistanceSaved',
    color: '#2563eb',
    softColor: '#dbeafe',
    icon: TrendingUp,
  },
  timeSaved: {
    label: 'Tempo',
    subtitle: 'Horas economizadas',
    currentKey: 'currentTimeSaved',
    previousKey: 'previousTimeSaved',
    color: '#d97706',
    softColor: '#fef3c7',
    icon: Clock,
  },
  deliveries: {
    label: 'Entregas',
    subtitle: 'Volume do periodo',
    currentKey: 'currentDeliveries',
    previousKey: 'previousDeliveries',
    color: '#7c3aed',
    softColor: '#ede9fe',
    icon: PackageCheck,
  },
}

const createAllAccess = () => ({
  operational: true,
  financial: true,
  fleet: true,
})

const createDefaultUserAccess = () => ({
  operational: true,
  financial: false,
  fleet: true,
})

const createDefaultFinancialConfig = () => ({
  region: 'Sao Paulo',
  gasolinePrice: 6.19,
  ethanolPrice: 4.09,
  dieselPrice: 6.02,
  fixedCostPerVehicle: 1800,
  maintenanceReserve: 650,
  tollCost: 280,
  driverDailyCost: 180,
  targetSavings: 4500,
  maxCostPerDelivery: 22,
})

const formatMetricValue = (metric, value) => {
  const numericValue = Number(value || 0)

  switch (metric) {
    case 'distanceSaved':
      return `${formatNumber(numericValue.toFixed(1))} km`
    case 'timeSaved':
      return `${formatNumber(numericValue.toFixed(1))} h`
    case 'deliveries':
      return formatNumber(Math.round(numericValue))
    case 'costSaved':
    default:
      return formatCurrency(numericValue)
  }
}

const normalizeAccess = (access, fallback) => ({
  operational: access?.operational ?? fallback.operational,
  financial: access?.financial ?? fallback.financial,
  fleet: access?.fleet ?? fallback.fleet,
})

const loadAccessMatrix = () => {
  if (typeof window === 'undefined') {
    return {}
  }

  try {
    const stored = window.localStorage.getItem(ACCESS_STORAGE_KEY)
    return stored ? JSON.parse(stored) : {}
  } catch (error) {
    return {}
  }
}

const saveAccessMatrix = (matrix) => {
  if (typeof window !== 'undefined') {
    window.localStorage.setItem(ACCESS_STORAGE_KEY, JSON.stringify(matrix))
  }
}

const loadFinancialConfig = () => {
  if (typeof window === 'undefined') {
    return createDefaultFinancialConfig()
  }

  try {
    const stored = window.localStorage.getItem(FINANCIAL_CONFIG_STORAGE_KEY)
    return stored ? { ...createDefaultFinancialConfig(), ...JSON.parse(stored) } : createDefaultFinancialConfig()
  } catch (error) {
    return createDefaultFinancialConfig()
  }
}

const saveFinancialConfig = (config) => {
  if (typeof window !== 'undefined') {
    window.localStorage.setItem(FINANCIAL_CONFIG_STORAGE_KEY, JSON.stringify(config))
  }
}

const calculateVariation = (currentValue, previousValue) => {
  if (previousValue === 0) {
    return currentValue > 0 ? 100 : 0
  }

  return ((currentValue - previousValue) / previousValue) * 100
}

const buildTimelineQuery = (period, granularity, vehicleId, metric) => {
  const params = new URLSearchParams({
    start: period.start,
    end: period.end,
    granularity,
    metric,
  })

  if (vehicleId) {
    params.set('vehicleId', vehicleId)
  }

  return params.toString()
}

const mergeTimelineSeries = (timelines) => {
  const firstTimeline = METRIC_KEYS
    .map((metricKey) => timelines[metricKey])
    .find((timeline) => timeline?.points?.length)

  if (!firstTimeline?.points?.length) {
    return []
  }

  return firstTimeline.points.map((point, index) => ({
    label: point.label,
    comparisonLabel: point.comparisonLabel,
    currentCostSaved: Number(timelines.costSaved?.points?.[index]?.currentValue || 0),
    previousCostSaved: Number(timelines.costSaved?.points?.[index]?.previousValue || 0),
    currentDistanceSaved: Number(timelines.distanceSaved?.points?.[index]?.currentValue || 0),
    previousDistanceSaved: Number(timelines.distanceSaved?.points?.[index]?.previousValue || 0),
    currentTimeSaved: Number(timelines.timeSaved?.points?.[index]?.currentValue || 0),
    previousTimeSaved: Number(timelines.timeSaved?.points?.[index]?.previousValue || 0),
    currentDeliveries: Number(timelines.deliveries?.points?.[index]?.currentValue || 0),
    previousDeliveries: Number(timelines.deliveries?.points?.[index]?.previousValue || 0),
  }))
}

const buildLabelBuckets = (period, granularity) => {
  const startDate = new Date(`${period.start}T00:00:00`)
  const endDate = new Date(`${period.end}T00:00:00`)
  const locale = 'pt-BR'
  const buckets = []

  if (Number.isNaN(startDate.getTime()) || Number.isNaN(endDate.getTime())) {
    return []
  }

  if (granularity === 'year') {
    for (let year = startDate.getFullYear(); year <= endDate.getFullYear(); year += 1) {
      buckets.push({ label: String(year), comparisonLabel: String(year - 1) })
    }
    return buckets
  }

  if (granularity === 'month') {
    const cursor = new Date(startDate.getFullYear(), startDate.getMonth(), 1)
    const limit = new Date(endDate.getFullYear(), endDate.getMonth(), 1)

    while (cursor <= limit) {
      const currentMonth = new Date(cursor)
      const previousMonth = new Date(cursor)
      previousMonth.setMonth(previousMonth.getMonth() - 1)

      buckets.push({
        label: currentMonth.toLocaleDateString(locale, { month: 'short', year: '2-digit' }),
        comparisonLabel: previousMonth.toLocaleDateString(locale, { month: 'short', year: '2-digit' }),
      })

      cursor.setMonth(cursor.getMonth() + 1)
    }

    return buckets
  }

  const cursor = new Date(startDate)
  while (cursor <= endDate) {
    const currentDay = new Date(cursor)
    const previousDay = new Date(cursor)
    previousDay.setDate(previousDay.getDate() - 1)

    buckets.push({
      label: currentDay.toLocaleDateString(locale, { day: '2-digit', month: '2-digit' }),
      comparisonLabel: previousDay.toLocaleDateString(locale, { day: '2-digit', month: '2-digit' }),
    })

    cursor.setDate(cursor.getDate() + 1)
  }

  return buckets
}

const buildDemoChartData = (basePoints, period, granularity) => {
  const sourcePoints = basePoints.length > 0 ? basePoints : buildLabelBuckets(period, granularity)

  return sourcePoints.map((point, index) => {
    const wave = Math.sin(index / 1.4)
    const trend = index + 1

    return {
      label: point.label,
      comparisonLabel: point.comparisonLabel,
      currentCostSaved: 3800 + trend * 620 + wave * 320,
      previousCostSaved: 3200 + trend * 480 + wave * 180,
      currentDistanceSaved: 110 + trend * 18 + wave * 10,
      previousDistanceSaved: 95 + trend * 15 + wave * 8,
      currentTimeSaved: 26 + trend * 4 + wave * 2.4,
      previousTimeSaved: 22 + trend * 3.2 + wave * 1.5,
      currentDeliveries: 145 + trend * 16 + wave * 6,
      previousDeliveries: 130 + trend * 14 + wave * 4,
    }
  })
}

const buildLinePath = (points) =>
  points.reduce((path, point, index) => `${path}${index === 0 ? 'M' : ' L'} ${point.x} ${point.y}`, '')

const mockDriverNames = [
  'Carlos Silva',
  'Marina Souza',
  'Paulo Lima',
  'Ana Beatriz',
  'Rafael Costa',
  'Juliana Alves',
]

const Dashboard = () => {
  const user = useAuthStore((state) => state.user)
  const [dashboard, setDashboard] = useState(null)
  const [timelines, setTimelines] = useState({})
  const [vehicles, setVehicles] = useState([])
  const [addresses, setAddresses] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [selectedGranularity, setSelectedGranularity] = useState('month')
  const [selectedVehicleId, setSelectedVehicleId] = useState('')
  const [visibleMetrics, setVisibleMetrics] = useState({
    costSaved: true,
    distanceSaved: true,
    timeSaved: true,
    deliveries: true,
  })
  const [hoveredIndex, setHoveredIndex] = useState(null)
  const [currentView, setCurrentView] = useState('operational')
  const [isSummaryOpen, setIsSummaryOpen] = useState(false)
  const [isAccessModalOpen, setIsAccessModalOpen] = useState(false)
  const [isFinancialConfigOpen, setIsFinancialConfigOpen] = useState(false)
  const [accessMatrix, setAccessMatrix] = useState(() => loadAccessMatrix())
  const [financialConfig, setFinancialConfig] = useState(() => loadFinancialConfig())
  const [managedEmail, setManagedEmail] = useState('')
  const [managedAccess, setManagedAccess] = useState(createDefaultUserAccess())
  const [period, setPeriod] = useState(() => {
    const end = new Date()
    const start = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)

    return {
      start: toInputDate(start),
      end: toInputDate(end),
    }
  })

  const isAdmin = user?.role === 'ADMIN'
  const currentUserEmail = user?.email?.toLowerCase?.() || ''
  const currentUserAccess = isAdmin
    ? createAllAccess()
    : normalizeAccess(accessMatrix[currentUserEmail], createDefaultUserAccess())
  const availableViews = DASHBOARD_VIEWS.filter((view) => currentUserAccess[view.key])

  useEffect(() => {
    if (availableViews.length > 0 && !availableViews.some((view) => view.key === currentView)) {
      setCurrentView(availableViews[0].key)
    }
  }, [availableViews, currentView])

  useEffect(() => {
    setHoveredIndex(null)
  }, [currentView])

  const fetchDashboard = async () => {
    setIsLoading(true)
    setError('')

    try {
      const timelineRequests = METRIC_KEYS.map((metric) =>
        api.get(`/analytics/timeline?${buildTimelineQuery(period, selectedGranularity, selectedVehicleId, metric)}`),
      )

      const [dashboardResponse, vehiclesResponse, addressesResponse, ...timelineResponses] = await Promise.all([
        api.get(`/analytics/dashboard?start=${period.start}&end=${period.end}`),
        api.get('/vehicles'),
        api.get('/addresses'),
        ...timelineRequests,
      ])

      setDashboard(dashboardResponse.data)
      setVehicles(vehiclesResponse.data)
      setAddresses(addressesResponse.data)
      setTimelines(
        METRIC_KEYS.reduce((accumulator, metric, index) => {
          accumulator[metric] = timelineResponses[index]?.data
          return accumulator
        }, {}),
      )
    } catch (err) {
      console.error('Erro ao carregar dashboard', err)
      setError('Nao foi possivel carregar os dados da dashboard agora.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchDashboard()
  }, [])

  const toggleVisibleMetric = (metric) => {
    const enabledCount = Object.values(visibleMetrics).filter(Boolean).length

    if (visibleMetrics[metric] && enabledCount === 1) {
      return
    }

    setVisibleMetrics((current) => ({
      ...current,
      [metric]: !current[metric],
    }))
  }

  const handleManagedAccessChange = (viewKey) => {
    setManagedAccess((current) => ({
      ...current,
      [viewKey]: !current[viewKey],
    }))
  }

  const saveManagedPermissions = () => {
    const normalizedEmail = managedEmail.trim().toLowerCase()
    if (!normalizedEmail) {
      return
    }

    const nextMatrix = {
      ...accessMatrix,
      [normalizedEmail]: normalizeAccess(managedAccess, createDefaultUserAccess()),
    }

    setAccessMatrix(nextMatrix)
    saveAccessMatrix(nextMatrix)
    setIsAccessModalOpen(false)
  }

  const editPermissionEntry = (email) => {
    setManagedEmail(email)
    setManagedAccess(normalizeAccess(accessMatrix[email], createDefaultUserAccess()))
    setIsAccessModalOpen(true)
  }

  const updateFinancialConfigField = (field, value) => {
    setFinancialConfig((current) => ({
      ...current,
      [field]: value,
    }))
  }

  const persistFinancialConfig = () => {
    saveFinancialConfig(financialConfig)
    setIsFinancialConfigOpen(false)
  }

  const resetFinancialConfig = () => {
    const defaults = createDefaultFinancialConfig()
    setFinancialConfig(defaults)
    saveFinancialConfig(defaults)
  }

  const totalKmSaved = Number(dashboard?.totalKmSaved || 0)
  const totalTimeSaved = Number(dashboard?.totalTimeSaved || 0)
  const totalCostSaved = Number(dashboard?.totalCostSaved || 0)
  const totalDeliveries = Number(dashboard?.totalDeliveries || 0)
  const avgCapacityUtilization = Number(dashboard?.avgCapacityUtilization || 0)
  const totalVehicles = vehicles.length
  const totalAddresses = addresses.length
  const averageCostPerDelivery = totalDeliveries > 0 ? totalCostSaved / totalDeliveries : 0
  const averageTimePerDelivery = totalDeliveries > 0 ? totalTimeSaved / 60 / totalDeliveries : 0
  const averageCapacityPerVehicle =
    totalVehicles > 0
      ? vehicles.reduce((acc, vehicle) => acc + Number(vehicle.capacity || 0), 0) / totalVehicles
      : 0

  const baseCount = addresses.filter((address) => {
    const street = `${address.street || ''} ${address.number || ''}`.toLowerCase()
    return street.includes('base') || street.includes('deposito') || street.includes('depósito')
  }).length
  const averageVehicleConsumption =
    totalVehicles > 0
      ? vehicles.reduce((acc, vehicle) => acc + Number(vehicle.avgConsumption || 0), 0) / totalVehicles
      : 0
  const fuelMix = vehicles.reduce(
    (accumulator, vehicle) => {
      const fuelType = String(vehicle.fuelType || '').toLowerCase()

      if (fuelType.includes('etan') || fuelType.includes('alcool')) {
        accumulator.ethanol += 1
      } else if (fuelType.includes('diesel')) {
        accumulator.diesel += 1
      } else {
        accumulator.gasoline += 1
      }

      return accumulator
    },
    { gasoline: 0, ethanol: 0, diesel: 0 },
  )
  const averageFuelPrice =
    totalVehicles > 0
      ? (fuelMix.gasoline * Number(financialConfig.gasolinePrice || 0) +
          fuelMix.ethanol * Number(financialConfig.ethanolPrice || 0) +
          fuelMix.diesel * Number(financialConfig.dieselPrice || 0)) /
        totalVehicles
      : Number(financialConfig.gasolinePrice || 0)
  const estimatedFuelSpend =
    averageVehicleConsumption > 0 ? (Math.max(totalKmSaved, 1) / averageVehicleConsumption) * averageFuelPrice : 0
  const estimatedFixedFleetCost =
    totalVehicles * Number(financialConfig.fixedCostPerVehicle || 0) +
    totalVehicles * Number(financialConfig.maintenanceReserve || 0)
  const estimatedDriverCost = totalVehicles * Number(financialConfig.driverDailyCost || 0)
  const estimatedOperationalCost =
    estimatedFixedFleetCost + estimatedDriverCost + Number(financialConfig.tollCost || 0) + estimatedFuelSpend
  const configuredCostPerDelivery = totalDeliveries > 0 ? estimatedOperationalCost / totalDeliveries : 0
  const savingsAgainstConfiguredCost = totalCostSaved - estimatedOperationalCost
  const estimatedRoi =
    estimatedOperationalCost > 0 ? ((totalCostSaved - estimatedOperationalCost) / estimatedOperationalCost) * 100 : 0
  const savingsTargetGap = totalCostSaved - Number(financialConfig.targetSavings || 0)

  const mergedChartData = useMemo(() => mergeTimelineSeries(timelines), [timelines])

  const hasHistoricalData = useMemo(
    () =>
      mergedChartData.some((point) =>
        METRIC_KEYS.some((metric) => {
          const definition = metricDefinitions[metric]
          return Number(point[definition.currentKey] || 0) > 0 || Number(point[definition.previousKey] || 0) > 0
        }),
      ),
    [mergedChartData],
  )

  const chartData = useMemo(
    () => (hasHistoricalData ? mergedChartData : buildDemoChartData(mergedChartData, period, selectedGranularity)),
    [hasHistoricalData, mergedChartData, period, selectedGranularity],
  )

  const metricSummaries = useMemo(
    () =>
      METRIC_KEYS.map((metric) => {
        const definition = metricDefinitions[metric]
        const currentTotal = chartData.reduce((acc, point) => acc + Number(point[definition.currentKey] || 0), 0)
        const previousTotal = chartData.reduce((acc, point) => acc + Number(point[definition.previousKey] || 0), 0)

        return {
          metric,
          label: definition.label,
          subtitle: definition.subtitle,
          icon: definition.icon,
          color: definition.color,
          softColor: definition.softColor,
          currentTotal,
          previousTotal,
          variation: calculateVariation(currentTotal, previousTotal),
        }
      }),
    [chartData],
  )

  const metricSummaryByKey = useMemo(
    () =>
      metricSummaries.reduce((accumulator, summary) => {
        accumulator[summary.metric] = summary
        return accumulator
      }, {}),
    [metricSummaries],
  )

  const currentViewDefinition =
    DASHBOARD_VIEWS.find((view) => view.key === currentView) || DASHBOARD_VIEWS[0]
  const chartMetricKeys = currentView === 'financial' ? FINANCIAL_METRICS : OPERATIONAL_METRICS
  const currentMetricSummaries = metricSummaries.filter((summary) => chartMetricKeys.includes(summary.metric))
  const hoveredPoint = hoveredIndex === null ? null : chartData[hoveredIndex]
  const activeMetrics = chartMetricKeys.filter((metric) => visibleMetrics[metric])

  const chartDimensions = {
    width: 1120,
    height: 360,
    paddingTop: 22,
    paddingRight: 24,
    paddingBottom: 54,
    paddingLeft: 24,
  }
  const chartInnerHeight = chartDimensions.height - chartDimensions.paddingTop - chartDimensions.paddingBottom
  const chartInnerWidth = chartDimensions.width - chartDimensions.paddingLeft - chartDimensions.paddingRight
  const bucketWidth = chartData.length > 0 ? chartInnerWidth / chartData.length : chartInnerWidth

  const metricPeaks = useMemo(
    () =>
      METRIC_KEYS.reduce((accumulator, metric) => {
        const definition = metricDefinitions[metric]
        accumulator[metric] = Math.max(
          1,
          ...chartData.flatMap((point) => [
            Number(point[definition.currentKey] || 0),
            Number(point[definition.previousKey] || 0),
          ]),
        )
        return accumulator
      }, {}),
    [chartData],
  )

  const gridLines = [0, 0.25, 0.5, 0.75, 1]

  const linePointsByMetric = useMemo(
    () =>
      METRIC_KEYS.reduce((accumulator, metric) => {
        const definition = metricDefinitions[metric]
        accumulator[metric] = chartData.map((point, index) => {
          const x = chartDimensions.paddingLeft + bucketWidth * index + bucketWidth / 2
          const currentValue = Number(point[definition.currentKey] || 0)
          const previousValue = Number(point[definition.previousKey] || 0)

          return {
            x,
            currentY:
              chartDimensions.paddingTop + chartInnerHeight - (currentValue / metricPeaks[metric]) * chartInnerHeight,
            previousY:
              chartDimensions.paddingTop + chartInnerHeight - (previousValue / metricPeaks[metric]) * chartInnerHeight,
          }
        })
        return accumulator
      }, {}),
    [bucketWidth, chartData, chartInnerHeight, metricPeaks],
  )

  const tooltipLeftPercent =
    hoveredIndex === null || chartData.length === 0
      ? 50
      : Math.min(82, Math.max(18, ((hoveredIndex + 0.5) / chartData.length) * 100))

  const priorities = [
    {
      title: totalVehicles === 0 ? 'Cadastrar os primeiros veiculos' : 'Revisar disponibilidade da frota',
      description:
        totalVehicles === 0
          ? 'Sem veiculos cadastrados, a roteirizacao nao consegue distribuir entregas.'
          : `${formatNumber(totalVehicles)} veiculos cadastrados para a operacao atual.`,
      ctaLabel: 'Ir para veiculos',
      ctaLink: '/vehicles',
    },
    {
      title: totalAddresses === 0 ? 'Cadastrar enderecos e clientes' : 'Expandir cobertura de atendimento',
      description:
        totalAddresses === 0
          ? 'Sem enderecos, a empresa ainda nao tem destinos para gerar rotas.'
          : `${formatNumber(totalAddresses)} enderecos disponiveis para planejamento logistico.`,
      ctaLabel: 'Ir para enderecos',
      ctaLink: '/addresses',
    },
    {
      title: totalDeliveries === 0 ? 'Gerar as primeiras rotas' : 'Acompanhar eficiencia da operacao',
      description:
        totalDeliveries === 0
          ? 'Ainda nao existe historico operacional suficiente para comparacao real.'
          : `${formatNumber(totalDeliveries)} entregas analisadas no periodo da dashboard.`,
      ctaLabel: 'Abrir roteirizacao',
      ctaLink: '/routing',
    },
  ]

  const summaryEntries = {
    operational: [
      `Entregas analisadas no periodo: ${formatNumber(totalDeliveries)}`,
      `Tempo poupado acumulado: ${formatNumber((totalTimeSaved / 3600).toFixed(1))} h`,
      `Quilometragem reduzida: ${formatNumber(totalKmSaved.toFixed(1))} km`,
      totalDeliveries === 0
        ? 'A operacao ainda depende de mais rotas executadas para gerar leitura real.'
        : 'A operacao ja apresenta leitura de produtividade e comparativo temporal.',
    ],
    financial: [
      `Economia acumulada: ${formatCurrency(totalCostSaved)}`,
      `Custo operacional configurado: ${formatCurrency(estimatedOperationalCost)}`,
      `Combustivel medio configurado em ${financialConfig.region}: ${formatCurrency(averageFuelPrice)}/L`,
      `ROI estimado com os parametros atuais: ${formatNumber(estimatedRoi.toFixed(1))}%`,
    ],
    fleet: [
      `Veiculos cadastrados: ${formatNumber(totalVehicles)}`,
      `Capacidade media por veiculo: ${formatNumber(averageCapacityPerVehicle.toFixed(1))}`,
      `Bases operacionais identificadas: ${formatNumber(baseCount)}`,
      'Esta visao prepara a entrada do portal do motorista para celular com check-in e check-out.',
    ],
  }

  const operationalRows = [
    { label: 'Entregas do periodo', value: formatNumber(totalDeliveries), helper: 'Volume consolidado da operacao' },
    { label: 'Tempo medio poupado', value: `${formatNumber(averageTimePerDelivery.toFixed(1))} min`, helper: 'Media por entrega no recorte' },
    { label: 'Cobertura operacional', value: formatNumber(totalAddresses), helper: 'Pontos disponiveis para atendimento' },
  ]

  const financialRows = [
    { label: 'Economia estimada', value: formatCurrency(totalCostSaved), helper: 'Leitura acumulada do periodo' },
    {
      label: 'Custo operacional configurado',
      value: formatCurrency(estimatedOperationalCost),
      helper: 'Custos fixos, motorista, pedagio e combustivel estimado',
    },
    {
      label: 'Custo configurado por entrega',
      value: formatCurrency(configuredCostPerDelivery),
      helper: 'Base calculada a partir dos parametros preenchidos',
    },
    {
      label: 'ROI estimado',
      value: `${formatNumber(estimatedRoi.toFixed(1))}%`,
      helper: 'Retorno estimado com a configuracao financeira atual',
    },
  ]

  const fleetRows = [
    { label: 'Veiculos ativos', value: formatNumber(totalVehicles), helper: 'Frota cadastrada no sistema' },
    { label: 'Capacidade media', value: formatNumber(averageCapacityPerVehicle.toFixed(1)), helper: 'Carga media por veiculo' },
    { label: 'Utilizacao media', value: `${formatNumber(avgCapacityUtilization.toFixed(1))}%`, helper: 'Aproveitamento informado no analytics' },
    { label: 'Bases detectadas', value: formatNumber(baseCount), helper: 'Pontos de partida para operacao' },
  ]

  const topVehicles = vehicles.slice(0, 5)
  const configuredUsers = Object.keys(accessMatrix).sort()
  const fleetOperationalRows = vehicles.map((vehicle, index) => {
    const statusCycle = ['Ativo no dia', 'Em manutencao', 'Indisponivel', 'Disponivel']
    const executionCycle = ['Em rota', 'Aguardando saida', 'Concluida', 'Parado']
    const routeLabel = `Rota ${String(index + 1).padStart(2, '0')}`
    const status = statusCycle[index % statusCycle.length]
    const driverAssigned = index % 4 === 3 ? null : mockDriverNames[index % mockDriverNames.length]
    const kmRodados = Number((48 + index * 19 + Number(vehicle.avgConsumption || 0) * 3).toFixed(1))
    const routesExecuted = Math.max(1, (index % 4) + 1)
    const totalTimeHours = Number((2.4 + index * 0.8).toFixed(1))
    const estimatedFuelCost = Number((kmRodados * Number(vehicle.costPerKm || 0)).toFixed(2))
    const estimatedSavings = Number((estimatedFuelCost * 0.18 + index * 24).toFixed(2))
    const maintenancePending = index % 3 === 1
    const docsExpiring = index % 4 === 2
    const revisionForecast = index % 2 === 0 ? 'Revisao em 15 dias' : 'Troca de oleo em 30 dias'
    const alert =
      status === 'Indisponivel'
        ? 'Veiculo fora de operacao'
        : maintenancePending
          ? 'Manutencao preventiva pendente'
          : docsExpiring
            ? 'Documento vence este mes'
            : 'Operacao dentro do esperado'

    return {
      ...vehicle,
      status,
      driverAssigned,
      lastRoute: `${routeLabel} • ${status === 'Concluida' ? 'concluida hoje' : 'ultima execucao recente'}`,
      executionStatus: executionCycle[index % executionCycle.length],
      currentRoute: `${routeLabel} / Base Centro`,
      kmRodados,
      routesExecuted,
      totalTimeHours,
      estimatedFuelCost,
      estimatedSavings,
      maintenancePending,
      docsExpiring,
      revisionForecast,
      alert,
      swapCount: index % 3,
    }
  })

  const fleetStatusSummary = {
    totalVehicles,
    activeToday: fleetOperationalRows.filter((item) => item.status === 'Ativo no dia').length,
    inMaintenance: fleetOperationalRows.filter((item) => item.status === 'Em manutencao').length,
    unavailable: fleetOperationalRows.filter((item) => item.status === 'Indisponivel').length,
    averageOccupancy: Number((avgCapacityUtilization || 68).toFixed(1)),
  }

  const assignedVehicles = fleetOperationalRows.filter((item) => item.driverAssigned)
  const unassignedVehicles = fleetOperationalRows.filter((item) => !item.driverAssigned)
  const driversWithoutVehicle = mockDriverNames.filter(
    (driver) => !fleetOperationalRows.some((item) => item.driverAssigned === driver),
  )
  const totalSwapCount = fleetOperationalRows.reduce((acc, item) => acc + item.swapCount, 0)

  const chartPanel = (
    <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6 space-y-6">
      <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-gray-900">
            {currentView === 'financial' ? 'Comparativo financeiro' : 'Comparativo operacional'}
          </h2>
          <p className="text-sm text-gray-500 mt-1">
            {currentView === 'financial'
              ? 'Compara combustivel, economia, tempo, km e entregas contra o periodo anterior.'
              : 'Compara entregas, tempo e quilometragem da operacao contra o periodo anterior.'}
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-3 text-xs font-medium">
          <span className="inline-flex items-center gap-2 text-gray-500">
            <span className="w-3 h-3 rounded-full bg-gray-300" />
            Periodo anterior
          </span>
          <span className="inline-flex items-center gap-2 text-gray-500">
            <span className="w-3 h-3 rounded-full bg-brand-blue" />
            Periodo atual
          </span>
          {!hasHistoricalData && (
            <span className="inline-flex items-center gap-2 bg-brand-yellow/20 text-brand-black px-3 py-1 rounded-full">
              Demonstracao ativa
            </span>
          )}
        </div>
      </div>

      <div className={`grid grid-cols-1 md:grid-cols-2 ${currentView === 'financial' ? 'xl:grid-cols-4' : 'xl:grid-cols-3'} gap-4`}>
        {currentMetricSummaries.map((summary) => {
          const Icon = summary.icon

          return (
            <div key={summary.metric} className="rounded-2xl border border-gray-100 bg-gray-50 px-4 py-4">
              <div className="flex items-center justify-between">
                <div className="w-10 h-10 rounded-2xl flex items-center justify-center" style={{ backgroundColor: summary.softColor }}>
                  <Icon size={20} style={{ color: summary.color }} />
                </div>
                <span
                  className={`text-xs font-bold px-2 py-1 rounded-full ${
                    summary.variation >= 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                  }`}
                >
                  {summary.variation >= 0 ? '+' : ''}
                  {formatNumber(summary.variation.toFixed(1))}%
                </span>
              </div>
              <p className="text-xs uppercase tracking-wider text-gray-400 font-bold mt-4">{summary.label}</p>
              <p className="text-2xl font-bold text-gray-900 mt-2">{formatMetricValue(summary.metric, summary.currentTotal)}</p>
              <p className="text-xs text-gray-500 mt-2">{summary.subtitle}</p>
            </div>
          )
        })}
      </div>

      <div className="flex flex-wrap gap-3">
        {chartMetricKeys.map((metric) => {
          const definition = metricDefinitions[metric]
          const isActive = visibleMetrics[metric]

          return (
            <button
              key={metric}
              type="button"
              onClick={() => toggleVisibleMetric(metric)}
              className={`px-4 py-2 rounded-2xl border text-sm font-medium transition-colors ${
                isActive ? 'text-gray-900 border-gray-200 bg-white shadow-sm' : 'text-gray-400 border-gray-100 bg-gray-50'
              }`}
            >
              <span className="inline-flex items-center gap-2">
                <span className="w-3 h-3 rounded-full" style={{ backgroundColor: definition.color }} />
                {definition.label}
              </span>
            </button>
          )
        })}
      </div>

      <div className="bg-gray-50 rounded-3xl p-4 lg:p-6 border border-gray-100">
        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-4">
          <div>
            <p className="text-sm font-semibold text-gray-900">Leitura executiva</p>
            <p className="text-xs text-gray-500 mt-1">
              {currentView === 'financial'
                ? 'Passe o mouse para abrir o resumo financeiro detalhado do bucket comparado.'
                : 'Passe o mouse para abrir o resumo operacional detalhado do bucket comparado.'}
            </p>
          </div>
          <button
            type="button"
            onClick={() => setIsSummaryOpen(true)}
            className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-800"
          >
            Resumo
          </button>
        </div>

        {chartData.length === 0 ? (
          <div className="h-80 flex flex-col items-center justify-center text-center text-gray-500">
            <TrendingUp size={36} className="mb-3 text-gray-300" />
            <p className="font-medium">Sem dados suficientes para desenhar o grafico.</p>
          </div>
        ) : (
          <div className="relative">
            <div className="overflow-x-auto">
              <div className="min-w-[960px]">
                <svg viewBox={`0 0 ${chartDimensions.width} ${chartDimensions.height}`} className="w-full h-[360px]">
                  {gridLines.map((line) => {
                    const y = chartDimensions.paddingTop + chartInnerHeight * line
                    return (
                      <line
                        key={line}
                        x1={chartDimensions.paddingLeft}
                        y1={y}
                        x2={chartDimensions.width - chartDimensions.paddingRight}
                        y2={y}
                        stroke="#e5e7eb"
                        strokeDasharray="4 6"
                      />
                    )
                  })}

                  {hoveredIndex !== null && chartData[hoveredIndex] && (
                    <rect
                      x={chartDimensions.paddingLeft + bucketWidth * hoveredIndex}
                      y={chartDimensions.paddingTop}
                      width={bucketWidth}
                      height={chartInnerHeight}
                      fill="#dbeafe"
                      opacity="0.35"
                      rx="16"
                    />
                  )}

                  {currentView === 'financial' && visibleMetrics.costSaved &&
                    chartData.map((point, index) => {
                      const centerX = chartDimensions.paddingLeft + bucketWidth * index + bucketWidth / 2
                      const currentHeight =
                        (Number(point.currentCostSaved || 0) / metricPeaks.costSaved) * chartInnerHeight
                      const previousHeight =
                        (Number(point.previousCostSaved || 0) / metricPeaks.costSaved) * chartInnerHeight

                      return (
                        <g key={`fuel-bar-${point.label}`}>
                          <rect
                            x={centerX - 16}
                            y={chartDimensions.paddingTop + chartInnerHeight - previousHeight}
                            width="12"
                            height={previousHeight}
                            rx="8"
                            fill="#d1d5db"
                          />
                          <rect
                            x={centerX + 4}
                            y={chartDimensions.paddingTop + chartInnerHeight - currentHeight}
                            width="12"
                            height={currentHeight}
                            rx="8"
                            fill={metricDefinitions.costSaved.color}
                          />
                        </g>
                      )
                    })}

                  {activeMetrics
                    .filter((metric) => metric !== 'costSaved')
                    .map((metric) => {
                      const definition = metricDefinitions[metric]
                      const points = linePointsByMetric[metric] || []
                      const currentPath = buildLinePath(points.map((point) => ({ x: point.x, y: point.currentY })))
                      const previousPath = buildLinePath(points.map((point) => ({ x: point.x, y: point.previousY })))

                      return (
                        <g key={`line-${metric}`}>
                          <path
                            d={previousPath}
                            fill="none"
                            stroke={definition.color}
                            strokeWidth="2.5"
                            strokeDasharray="6 6"
                            opacity="0.35"
                          />
                          <path d={currentPath} fill="none" stroke={definition.color} strokeWidth="3" />

                          {points.map((point, index) => (
                            <g key={`${metric}-${chartData[index]?.label}`}>
                              <circle cx={point.x} cy={point.previousY} r="4" fill="#ffffff" stroke={definition.color} strokeWidth="2" opacity="0.35" />
                              <circle cx={point.x} cy={point.currentY} r="4.5" fill={definition.color} />
                            </g>
                          ))}
                        </g>
                      )
                    })}

                  {chartData.map((point, index) => {
                    const x = chartDimensions.paddingLeft + bucketWidth * index + bucketWidth / 2

                    return (
                      <g key={`axis-${point.label}`}>
                        <text
                          x={x}
                          y={chartDimensions.height - 22}
                          textAnchor="middle"
                          fill="#374151"
                          fontSize="12"
                          fontWeight="700"
                        >
                          {point.label}
                        </text>
                        <text x={x} y={chartDimensions.height - 8} textAnchor="middle" fill="#9ca3af" fontSize="10">
                          vs {point.comparisonLabel || '-'}
                        </text>
                      </g>
                    )
                  })}
                </svg>
              </div>
            </div>

            <div className="absolute inset-0 flex pointer-events-none">
              {chartData.map((point, index) => (
                <button
                  key={`hover-${point.label}`}
                  type="button"
                  onMouseEnter={() => setHoveredIndex(index)}
                  onMouseLeave={() => setHoveredIndex(null)}
                  onFocus={() => setHoveredIndex(index)}
                  onBlur={() => setHoveredIndex(null)}
                  className="flex-1 pointer-events-auto bg-transparent"
                  aria-label={`Detalhes do periodo ${point.label}`}
                />
              ))}
            </div>

            {hoveredPoint && (
              <div
                className="absolute top-3 z-10 w-[320px] max-w-[calc(100%-1rem)] bg-white border border-gray-200 shadow-xl rounded-2xl p-4"
                style={{
                  left: `${tooltipLeftPercent}%`,
                  transform: 'translateX(-50%)',
                }}
              >
                <div className="flex items-start justify-between gap-4">
                  <div>
                    <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">{hoveredPoint.label}</p>
                    <p className="text-sm text-gray-500 mt-1">Comparado com {hoveredPoint.comparisonLabel || 'periodo anterior'}</p>
                  </div>
                  {!hasHistoricalData && (
                    <span className="text-[10px] font-bold bg-brand-yellow/20 text-brand-black px-2 py-1 rounded-full">
                      DEMO
                    </span>
                  )}
                </div>

                <div className="grid grid-cols-2 gap-3 mt-4">
                  {chartMetricKeys.map((metric) => {
                    const definition = metricDefinitions[metric]

                    return (
                      <div key={`tooltip-${metric}`} className="bg-gray-50 rounded-xl p-3">
                        <div className="flex items-center gap-2">
                          <span className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: definition.color }} />
                          <p className="text-xs font-bold uppercase tracking-wider text-gray-500">{definition.label}</p>
                        </div>
                        <p className="text-sm font-semibold text-gray-900 mt-2">
                          {formatMetricValue(metric, hoveredPoint[definition.currentKey])}
                        </p>
                        <p className="text-xs text-gray-400 mt-1">
                          Antes: {formatMetricValue(metric, hoveredPoint[definition.previousKey])}
                        </p>
                      </div>
                    )
                  })}
                </div>
              </div>
            )}
          </div>
        )}

        {!hasHistoricalData && (
          <div className="mt-4 bg-brand-yellow/10 border border-yellow-200 rounded-2xl px-4 py-3 text-sm text-gray-700">
            O painel esta mostrando dados de demonstracao para voce visualizar a experiencia antes da operacao gerar historico real.
          </div>
        )}
      </div>
    </section>
  )

  const actionStrip = (
    <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
      <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
        <div>
          <h3 className="text-lg font-bold text-gray-900">Acoes rapidas</h3>
          <p className="text-sm text-gray-500 mt-1">
            Continue a operacao sem sair da visao atual.
          </p>
        </div>
        <div className="flex flex-wrap gap-3">
          <Link to="/routing" className="px-4 py-2 rounded-2xl bg-brand-blue text-white text-sm font-medium inline-flex items-center gap-2">
            <Route size={18} />
            Roteirizacao
          </Link>
          <Link to="/vehicles" className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium inline-flex items-center gap-2">
            <Truck size={18} />
            Veiculos
          </Link>
          <Link to="/addresses" className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium inline-flex items-center gap-2">
            <MapPin size={18} />
            Enderecos
          </Link>
        </div>
      </div>
    </section>
  )

  const viewContent = {
    operational: (
      <div className="space-y-6">
        {chartPanel}
        <section className="grid grid-cols-1 xl:grid-cols-[1.3fr_0.7fr] gap-6">
          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h3 className="text-lg font-bold text-gray-900">Fila operacional</h3>
                <p className="text-sm text-gray-500 mt-1">O que precisa de atencao imediata na empresa.</p>
              </div>
              <AlertTriangle className="text-brand-yellow" size={20} />
            </div>

            <div className="space-y-4">
              {priorities.map((item) => (
                <div key={item.title} className="border border-gray-100 rounded-2xl p-4">
                  <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                    <div>
                      <p className="font-semibold text-gray-900">{item.title}</p>
                      <p className="text-sm text-gray-500 mt-1">{item.description}</p>
                    </div>
                    <Link to={item.ctaLink} className="text-sm font-bold text-brand-blue inline-flex items-center gap-2">
                      {item.ctaLabel}
                      <ArrowRight size={16} />
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-brand-black text-white rounded-3xl shadow-xl p-6">
            <h3 className="text-lg font-bold">Leitura do dia</h3>
            <div className="divide-y divide-white/10 mt-5">
              {operationalRows.map((row) => (
                <div key={row.label} className="py-4">
                  <p className="text-sm text-gray-300">{row.label}</p>
                  <p className="text-2xl font-bold mt-2">{row.value}</p>
                  <p className="text-xs text-gray-400 mt-1">{row.helper}</p>
                </div>
              ))}
            </div>
          </div>
        </section>
        {actionStrip}
      </div>
    ),
    financial: (
      <div className="space-y-6">
        {chartPanel}
        <section className="grid grid-cols-1 xl:grid-cols-[1.1fr_0.9fr] gap-6">
          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h3 className="text-lg font-bold text-gray-900">Leitura financeira da operacao</h3>
                <p className="text-sm text-gray-500 mt-1">Base para custo, margem e futura analise de combustivel por regiao.</p>
              </div>
              <Wallet className="text-brand-blue" size={20} />
            </div>

            <div className="divide-y divide-gray-100">
              {financialRows.map((row) => (
                <div key={row.label} className="py-4 flex flex-col md:flex-row md:items-center md:justify-between gap-2">
                  <div>
                    <p className="font-medium text-gray-900">{row.label}</p>
                    <p className="text-sm text-gray-500 mt-1">{row.helper}</p>
                  </div>
                  <p className="text-xl font-bold text-gray-900">{row.value}</p>
                </div>
              ))}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mt-6">
              <div className="border border-gray-100 rounded-2xl px-4 py-4">
                <p className="text-sm text-gray-500">Meta de economia</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">{formatCurrency(financialConfig.targetSavings)}</p>
                <p className={`text-xs mt-2 ${savingsTargetGap >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                  {savingsTargetGap >= 0 ? 'Meta atingida no periodo' : 'Abaixo da meta configurada'}
                </p>
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-4">
                <p className="text-sm text-gray-500">Limite de custo por entrega</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">{formatCurrency(financialConfig.maxCostPerDelivery)}</p>
                <p className={`text-xs mt-2 ${
                  configuredCostPerDelivery <= Number(financialConfig.maxCostPerDelivery || 0) ? 'text-green-600' : 'text-red-600'
                }`}>
                  {configuredCostPerDelivery <= Number(financialConfig.maxCostPerDelivery || 0)
                    ? 'Dentro do teto configurado'
                    : 'Acima do teto configurado'}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-start justify-between gap-4 mb-5">
              <div className="flex items-center gap-3">
                <ShieldCheck className="text-green-600" size={20} />
                <div>
                  <h3 className="text-lg font-bold text-gray-900">Parametros financeiros</h3>
                  <p className="text-sm text-gray-500">O que a empresa informa para o sistema calcular custos reais.</p>
                </div>
              </div>
              <button
                type="button"
                onClick={() => setIsFinancialConfigOpen(true)}
                className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-800"
              >
                Configurar custos
              </button>
            </div>

            <div className="space-y-3 text-sm text-gray-600">
              <div className="border border-gray-100 rounded-2xl px-4 py-3">
                Combustivel em {financialConfig.region}: gasolina {formatCurrency(financialConfig.gasolinePrice)}/L, etanol {formatCurrency(financialConfig.ethanolPrice)}/L e diesel {formatCurrency(financialConfig.dieselPrice)}/L
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-3">
                Custos fixos por veiculo: {formatCurrency(financialConfig.fixedCostPerVehicle)} e reserva de manutencao de {formatCurrency(financialConfig.maintenanceReserve)}
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-3">
                Motorista por dia: {formatCurrency(financialConfig.driverDailyCost)} • Pedagios e extras: {formatCurrency(financialConfig.tollCost)}
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-3">
                Economia alvo: {formatCurrency(financialConfig.targetSavings)} • Teto por entrega: {formatCurrency(financialConfig.maxCostPerDelivery)}
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-3">
                Resultado configurado no periodo: {formatCurrency(savingsAgainstConfiguredCost)} frente ao custo operacional previsto.
              </div>
            </div>
          </div>
        </section>
        {actionStrip}
      </div>
    ),
    fleet: (
      <div className="space-y-6">
        <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
          <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-4 mb-6">
            <div>
              <h3 className="text-lg font-bold text-gray-900">Painel de status da frota</h3>
              <p className="text-sm text-gray-500 mt-1">
                Total de veículos, ativos no dia, manutenção, indisponibilidade e ocupação média.
              </p>
            </div>
            <button
              type="button"
              onClick={() => setIsSummaryOpen(true)}
              className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-800"
            >
              Resumo
            </button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-5 gap-4">
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Total de veículos</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">{formatNumber(fleetStatusSummary.totalVehicles)}</p>
            </div>
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Ativos no dia</p>
              <p className="text-3xl font-bold text-green-600 mt-2">{formatNumber(fleetStatusSummary.activeToday)}</p>
            </div>
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Em manutenção</p>
              <p className="text-3xl font-bold text-yellow-600 mt-2">{formatNumber(fleetStatusSummary.inMaintenance)}</p>
            </div>
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Indisponíveis</p>
              <p className="text-3xl font-bold text-red-600 mt-2">{formatNumber(fleetStatusSummary.unavailable)}</p>
            </div>
            <div className="border border-gray-100 rounded-2xl px-4 py-4">
              <p className="text-sm text-gray-500">Ocupação média</p>
              <p className="text-3xl font-bold text-brand-blue mt-2">{formatNumber(fleetStatusSummary.averageOccupancy)}%</p>
            </div>
          </div>
        </section>

        <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-lg font-bold text-gray-900">Lista operacional de veículos</h3>
              <p className="text-sm text-gray-500 mt-1">
                Placa, modelo, capacidade, custo por km, status atual, motorista vinculado e última rota.
              </p>
            </div>
            <Link to="/vehicles" className="text-sm font-bold text-brand-blue inline-flex items-center gap-2">
              Gerenciar veiculos
              <ArrowRight size={16} />
            </Link>
          </div>

          {fleetOperationalRows.length === 0 ? (
            <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
              Nenhum veículo cadastrado ainda.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full min-w-[980px] text-left">
                <thead>
                  <tr className="border-b border-gray-100 text-xs font-bold uppercase tracking-wider text-gray-400">
                    <th className="py-3 pr-4">Placa</th>
                    <th className="py-3 pr-4">Modelo</th>
                    <th className="py-3 pr-4">Capacidade</th>
                    <th className="py-3 pr-4">Custo/km</th>
                    <th className="py-3 pr-4">Status atual</th>
                    <th className="py-3 pr-4">Motorista vinculado</th>
                    <th className="py-3">Última rota executada</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {fleetOperationalRows.map((vehicle) => (
                    <tr key={vehicle.id}>
                      <td className="py-4 pr-4 font-mono text-sm text-gray-700">{vehicle.plate || 'Sem placa'}</td>
                      <td className="py-4 pr-4 font-medium text-gray-900">{vehicle.model || 'Veículo'}</td>
                      <td className="py-4 pr-4 text-sm text-gray-600">{formatNumber(vehicle.capacity || 0)}</td>
                      <td className="py-4 pr-4 text-sm text-gray-600">{formatCurrency(vehicle.costPerKm || 0)}</td>
                      <td className="py-4 pr-4">
                        <span className={`inline-flex px-3 py-1 rounded-full text-xs font-semibold ${
                          vehicle.status === 'Ativo no dia'
                            ? 'bg-green-100 text-green-700'
                            : vehicle.status === 'Em manutencao'
                              ? 'bg-yellow-100 text-yellow-700'
                              : vehicle.status === 'Indisponivel'
                                ? 'bg-red-100 text-red-700'
                                : 'bg-slate-100 text-slate-700'
                        }`}>
                          {vehicle.status}
                        </span>
                      </td>
                      <td className="py-4 pr-4 text-sm text-gray-600">{vehicle.driverAssigned || 'Nao atribuido'}</td>
                      <td className="py-4 text-sm text-gray-600">{vehicle.lastRoute}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>

        <section className="grid grid-cols-1 xl:grid-cols-[1fr_1fr] gap-6">
          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center gap-3 mb-5">
              <Users className="text-brand-blue" size={20} />
              <div>
                <h3 className="text-lg font-bold text-gray-900">Alocação veículo x motorista</h3>
                <p className="text-sm text-gray-500">Quem está com qual veículo, trocas e lacunas da operação.</p>
              </div>
            </div>

            <div className="space-y-3">
              {assignedVehicles.length === 0 ? (
                <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                  Nenhuma alocação registrada ainda.
                </div>
              ) : (
                assignedVehicles.map((vehicle) => (
                  <div key={`alloc-${vehicle.id}`} className="border border-gray-100 rounded-2xl px-4 py-4 flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                    <div>
                      <p className="font-semibold text-gray-900">{vehicle.driverAssigned}</p>
                      <p className="text-sm text-gray-500 mt-1">
                        {vehicle.model} • {vehicle.plate} • {vehicle.currentRoute}
                      </p>
                    </div>
                    <p className="text-sm text-gray-600">Trocas de veículo: {vehicle.swapCount}</p>
                  </div>
                ))
              )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mt-5">
              <div className="border border-gray-100 rounded-2xl px-4 py-4">
                <p className="text-sm text-gray-500">Trocas de veículo</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">{formatNumber(totalSwapCount)}</p>
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-4">
                <p className="text-sm text-gray-500">Veículos sem motorista</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">{formatNumber(unassignedVehicles.length)}</p>
              </div>
              <div className="border border-gray-100 rounded-2xl px-4 py-4">
                <p className="text-sm text-gray-500">Motoristas sem veículo</p>
                <p className="text-2xl font-bold text-gray-900 mt-2">{formatNumber(driversWithoutVehicle.length)}</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center gap-3 mb-5">
              <TrendingUp className="text-green-600" size={20} />
              <div>
                <h3 className="text-lg font-bold text-gray-900">Desempenho por veículo</h3>
                <p className="text-sm text-gray-500">KM rodados, rotas, tempo total, economia gerada e custo estimado.</p>
              </div>
            </div>

            {fleetOperationalRows.length === 0 ? (
              <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                Cadastre veículos para acompanhar desempenho.
              </div>
            ) : (
              <div className="divide-y divide-gray-100">
                {fleetOperationalRows.map((vehicle) => (
                  <div key={`perf-${vehicle.id}`} className="py-4 grid grid-cols-1 md:grid-cols-5 gap-3">
                    <div>
                      <p className="font-semibold text-gray-900">{vehicle.model}</p>
                      <p className="text-xs text-gray-500 mt-1">{vehicle.plate}</p>
                    </div>
                    <div>
                      <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">KM rodados</p>
                      <p className="text-sm text-gray-700 mt-1">{formatNumber(vehicle.kmRodados)} km</p>
                    </div>
                    <div>
                      <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Rotas executadas</p>
                      <p className="text-sm text-gray-700 mt-1">{formatNumber(vehicle.routesExecuted)}</p>
                    </div>
                    <div>
                      <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Tempo total</p>
                      <p className="text-sm text-gray-700 mt-1">{formatNumber(vehicle.totalTimeHours)} h</p>
                    </div>
                    <div>
                      <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Economia / custo</p>
                      <p className="text-sm text-gray-700 mt-1">
                        {formatCurrency(vehicle.estimatedSavings)} / {formatCurrency(vehicle.estimatedFuelCost)}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>

        <section className="grid grid-cols-1 xl:grid-cols-[0.95fr_1.05fr] gap-6">
          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center gap-3 mb-5">
              <Wrench className="text-yellow-600" size={20} />
              <div>
                <h3 className="text-lg font-bold text-gray-900">Saúde da frota</h3>
                <p className="text-sm text-gray-500">Manutenção pendente, documentação vencendo, revisão e alertas.</p>
              </div>
            </div>

            <div className="space-y-3">
              {fleetOperationalRows.length === 0 ? (
                <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                  Sem veículos para monitorar alertas.
                </div>
              ) : (
                fleetOperationalRows.map((vehicle) => (
                  <div key={`health-${vehicle.id}`} className="border border-gray-100 rounded-2xl px-4 py-4">
                    <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-3">
                      <div>
                        <p className="font-semibold text-gray-900">{vehicle.model} • {vehicle.plate}</p>
                        <p className="text-sm text-gray-500 mt-1">{vehicle.alert}</p>
                      </div>
                      <div className="text-xs text-gray-500 space-y-1">
                        <p>Manutenção pendente: {vehicle.maintenancePending ? 'Sim' : 'Nao'}</p>
                        <p>Documentação vencendo: {vehicle.docsExpiring ? 'Sim' : 'Nao'}</p>
                        <p>{vehicle.revisionForecast}</p>
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>

          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center gap-3 mb-5">
              <ShieldAlert className="text-brand-blue" size={20} />
              <div>
                <h3 className="text-lg font-bold text-gray-900">Controle operacional</h3>
                <p className="text-sm text-gray-500">Liberar, bloquear, marcar manutenção e retornar o veículo à operação.</p>
              </div>
            </div>

            {fleetOperationalRows.length === 0 ? (
              <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                Nenhum veículo disponível para controle operacional.
              </div>
            ) : (
              <div className="space-y-3">
                {fleetOperationalRows.map((vehicle) => (
                  <div key={`control-${vehicle.id}`} className="border border-gray-100 rounded-2xl px-4 py-4">
                    <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-3">
                      <div>
                        <p className="font-semibold text-gray-900">{vehicle.model} • {vehicle.plate}</p>
                        <p className="text-sm text-gray-500 mt-1">Status atual: {vehicle.status}</p>
                      </div>
                      <div className="flex flex-wrap gap-2">
                        <button type="button" className="px-3 py-2 rounded-xl bg-green-50 text-green-700 text-xs font-semibold">
                          Liberar veículo
                        </button>
                        <button type="button" className="px-3 py-2 rounded-xl bg-yellow-50 text-yellow-700 text-xs font-semibold">
                          Marcar manutenção
                        </button>
                        <button type="button" className="px-3 py-2 rounded-xl bg-red-50 text-red-700 text-xs font-semibold">
                          Tirar de operação
                        </button>
                        <button type="button" className="px-3 py-2 rounded-xl bg-blue-50 text-brand-blue text-xs font-semibold">
                          Voltar para operação
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>

        <section className="grid grid-cols-1 xl:grid-cols-[1fr_0.9fr] gap-6">
          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
            <div className="flex items-center gap-3 mb-5">
              <Smartphone className="text-brand-blue" size={20} />
              <div>
                <h3 className="text-lg font-bold text-gray-900">Base para o portal do motorista</h3>
                <p className="text-sm text-gray-500">Rota atribuída, veículo vinculado e status da execução por motorista.</p>
              </div>
            </div>

            {fleetOperationalRows.length === 0 ? (
              <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                Ainda não existe base operacional para o portal do motorista.
              </div>
            ) : (
              <div className="divide-y divide-gray-100">
                {fleetOperationalRows.map((vehicle) => (
                  <div key={`portal-${vehicle.id}`} className="py-4 flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                    <div>
                      <p className="font-semibold text-gray-900">{vehicle.currentRoute}</p>
                      <p className="text-sm text-gray-500 mt-1">
                        {vehicle.driverAssigned || 'Motorista nao atribuido'} • {vehicle.model} • {vehicle.plate}
                      </p>
                    </div>
                    <span className={`inline-flex px-3 py-1 rounded-full text-xs font-semibold ${
                      vehicle.executionStatus === 'Em rota'
                        ? 'bg-green-100 text-green-700'
                        : vehicle.executionStatus === 'Aguardando saida'
                          ? 'bg-yellow-100 text-yellow-700'
                          : vehicle.executionStatus === 'Concluida'
                            ? 'bg-blue-100 text-brand-blue'
                            : 'bg-slate-100 text-slate-700'
                    }`}>
                      {vehicle.executionStatus}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="bg-brand-black text-white rounded-3xl shadow-xl p-6">
            <div className="flex items-center gap-3 mb-4">
              <UserRound className="text-brand-yellow" size={22} />
              <div>
                <h3 className="text-lg font-bold">Leitura do motorista</h3>
                <p className="text-sm text-gray-400">Pré-requisitos que a visão frota já precisa garantir.</p>
              </div>
            </div>

            <div className="space-y-4 text-sm text-gray-300">
              <p className="border border-white/10 rounded-2xl px-4 py-3">Motorista vinculado ao veículo correto.</p>
              <p className="border border-white/10 rounded-2xl px-4 py-3">Rota atribuída antes da saída para execução.</p>
              <p className="border border-white/10 rounded-2xl px-4 py-3">Status da execução visível para o gestor em tempo real.</p>
              <p className="border border-white/10 rounded-2xl px-4 py-3">Base pronta para check-in, check-out e prova de entrega no celular.</p>
            </div>
          </div>
        </section>
        {actionStrip}
      </div>
    ),
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <Loader2 className="animate-spin text-brand-blue" size={48} />
      </div>
    )
  }

  return (
    <div className="space-y-8">
      <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6 lg:p-8">
        <div className="flex flex-col gap-6">
          <div className="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-6">
            <div className="space-y-3">
              <span className="inline-flex items-center gap-2 bg-brand-yellow/20 text-brand-black px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider">
                <CalendarRange size={14} />
                Centro de comando logistica
              </span>
              <div>
                <h1 className="text-3xl font-bold text-gray-900">Dashboard do RotaCerta</h1>
                <p className="text-gray-500 mt-2 max-w-2xl">
                  Estrutura principal da plataforma com visao operacional, financeira e de frota em um unico ambiente.
                </p>
              </div>
            </div>

            <div className="w-full xl:w-auto grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-5 gap-3">
              <label className="bg-gray-50 border border-gray-200 rounded-2xl px-4 py-3 text-sm">
                <span className="block text-gray-500 mb-1">Inicio</span>
                <input
                  type="date"
                  value={period.start}
                  onChange={(event) => setPeriod((current) => ({ ...current, start: event.target.value }))}
                  className="bg-transparent outline-none w-full text-gray-800"
                />
              </label>
              <label className="bg-gray-50 border border-gray-200 rounded-2xl px-4 py-3 text-sm">
                <span className="block text-gray-500 mb-1">Fim</span>
                <input
                  type="date"
                  value={period.end}
                  onChange={(event) => setPeriod((current) => ({ ...current, end: event.target.value }))}
                  className="bg-transparent outline-none w-full text-gray-800"
                />
              </label>
              <label className="bg-gray-50 border border-gray-200 rounded-2xl px-4 py-3 text-sm">
                <span className="block text-gray-500 mb-1">Periodo</span>
                <select
                  value={selectedGranularity}
                  onChange={(event) => setSelectedGranularity(event.target.value)}
                  className="bg-transparent outline-none w-full text-gray-800"
                >
                  {granularityOptions.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </label>
              <label className="bg-gray-50 border border-gray-200 rounded-2xl px-4 py-3 text-sm">
                <span className="block text-gray-500 mb-1">Veiculo</span>
                <select
                  value={selectedVehicleId}
                  onChange={(event) => setSelectedVehicleId(event.target.value)}
                  className="bg-transparent outline-none w-full text-gray-800"
                >
                  <option value="">Todos</option>
                  {vehicles.map((vehicle) => (
                    <option key={vehicle.id} value={vehicle.id}>
                      {vehicle.model} - {vehicle.plate}
                    </option>
                  ))}
                </select>
              </label>
              <button onClick={fetchDashboard} className="btn-primary rounded-2xl flex items-center justify-center gap-2">
                <RefreshCw size={18} />
                Atualizar
              </button>
            </div>
          </div>

          <div className="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-4">
            <div className="flex flex-wrap gap-3">
              {availableViews.map((view) => {
                const Icon = view.icon
                const isActive = currentView === view.key

                return (
                  <button
                    key={view.key}
                    type="button"
                    onClick={() => setCurrentView(view.key)}
                    className={`px-4 py-3 rounded-2xl border text-left transition-colors min-w-[180px] ${
                      isActive
                        ? 'border-brand-blue bg-blue-50 text-brand-blue'
                        : 'border-gray-200 bg-white text-gray-700 hover:border-gray-300'
                    }`}
                  >
                    <span className="inline-flex items-center gap-2 font-semibold">
                      <Icon size={18} />
                      {view.shortLabel}
                    </span>
                    <span className="block text-xs mt-2 opacity-80">{view.description}</span>
                  </button>
                )
              })}
            </div>

            {isAdmin && (
              <button
                type="button"
                onClick={() => {
                  setManagedEmail('')
                  setManagedAccess(createDefaultUserAccess())
                  setIsAccessModalOpen(true)
                }}
                className="px-4 py-3 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-800 inline-flex items-center gap-2"
              >
                <Settings2 size={18} />
                Gerenciar visoes por usuario
              </button>
            )}
          </div>
        </div>

        {error && (
          <div className="mt-6 bg-red-50 border border-red-100 text-red-700 rounded-2xl px-4 py-3 text-sm">
            {error}
          </div>
        )}
      </section>

      <section className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <p className="text-sm text-gray-500">Visao ativa</p>
            <h2 className="text-2xl font-bold text-gray-900 mt-1">{currentViewDefinition.label}</h2>
            <p className="text-sm text-gray-500 mt-2">{currentViewDefinition.description}</p>
          </div>
          <div className="text-sm text-gray-500">
            Usuario atual: <span className="font-semibold text-gray-900">{user?.email || 'sem email'}</span>
          </div>
        </div>
      </section>

      {viewContent[currentView]}

      {isSummaryOpen && (
        <div className="fixed inset-0 bg-black/50 z-40 flex items-center justify-center p-4">
          <div className="w-full max-w-2xl bg-white rounded-3xl shadow-2xl border border-gray-100 p-6">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Resumo da visao</p>
                <h3 className="text-2xl font-bold text-gray-900 mt-2">{currentViewDefinition.label}</h3>
                <p className="text-sm text-gray-500 mt-2">{currentViewDefinition.description}</p>
              </div>
              <button
                type="button"
                onClick={() => setIsSummaryOpen(false)}
                className="px-4 py-2 rounded-2xl border border-gray-200 text-sm font-medium text-gray-700"
              >
                Fechar
              </button>
            </div>

            <div className="mt-6 space-y-3">
              {summaryEntries[currentView].map((entry) => (
                <div key={entry} className="border border-gray-100 rounded-2xl px-4 py-3 text-sm text-gray-700">
                  {entry}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {isFinancialConfigOpen && (
        <div className="fixed inset-0 bg-black/50 z-40 flex items-center justify-center p-4">
          <div className="w-full max-w-4xl bg-white rounded-3xl shadow-2xl border border-gray-100 p-6">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Financeiro</p>
                <h3 className="text-2xl font-bold text-gray-900 mt-2">Configurar custos da operação</h3>
                <p className="text-sm text-gray-500 mt-2">
                  Preencha os parâmetros financeiros da empresa para o sistema calcular custo, economia e ROI com mais precisão.
                </p>
              </div>
              <button
                type="button"
                onClick={() => setIsFinancialConfigOpen(false)}
                className="px-4 py-2 rounded-2xl border border-gray-200 text-sm font-medium text-gray-700"
              >
                Fechar
              </button>
            </div>

            <div className="grid grid-cols-1 xl:grid-cols-2 gap-6 mt-6">
              <div className="border border-gray-100 rounded-3xl p-5 space-y-4">
                <div>
                  <h4 className="text-lg font-bold text-gray-900">Combustível por região</h4>
                  <p className="text-sm text-gray-500 mt-1">Base atual usada pela visão financeira.</p>
                </div>

                <label className="block text-sm">
                  <span className="block text-gray-600 mb-2">Região de referência</span>
                  <select
                    value={financialConfig.region}
                    onChange={(event) => updateFinancialConfigField('region', event.target.value)}
                    className="input-field"
                  >
                    <option value="Sao Paulo">São Paulo</option>
                    <option value="Campinas">Campinas</option>
                    <option value="Belo Horizonte">Belo Horizonte</option>
                    <option value="Curitiba">Curitiba</option>
                  </select>
                </label>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Gasolina</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.gasolinePrice}
                      onChange={(event) => updateFinancialConfigField('gasolinePrice', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Etanol</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.ethanolPrice}
                      onChange={(event) => updateFinancialConfigField('ethanolPrice', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Diesel</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.dieselPrice}
                      onChange={(event) => updateFinancialConfigField('dieselPrice', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                </div>
              </div>

              <div className="border border-gray-100 rounded-3xl p-5 space-y-4">
                <div>
                  <h4 className="text-lg font-bold text-gray-900">Custos operacionais</h4>
                  <p className="text-sm text-gray-500 mt-1">Valores usados na composição do custo logístico.</p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Custo fixo por veículo</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.fixedCostPerVehicle}
                      onChange={(event) => updateFinancialConfigField('fixedCostPerVehicle', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Reserva de manutenção</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.maintenanceReserve}
                      onChange={(event) => updateFinancialConfigField('maintenanceReserve', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Pedágio e extras</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.tollCost}
                      onChange={(event) => updateFinancialConfigField('tollCost', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Motorista por dia</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.driverDailyCost}
                      onChange={(event) => updateFinancialConfigField('driverDailyCost', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                </div>
              </div>

              <div className="border border-gray-100 rounded-3xl p-5 space-y-4">
                <div>
                  <h4 className="text-lg font-bold text-gray-900">Metas financeiras</h4>
                  <p className="text-sm text-gray-500 mt-1">Referências para leitura de desempenho.</p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Economia alvo</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.targetSavings}
                      onChange={(event) => updateFinancialConfigField('targetSavings', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                  <label className="block text-sm">
                    <span className="block text-gray-600 mb-2">Teto por entrega</span>
                    <input
                      type="number"
                      step="0.01"
                      value={financialConfig.maxCostPerDelivery}
                      onChange={(event) => updateFinancialConfigField('maxCostPerDelivery', Number(event.target.value))}
                      className="input-field"
                    />
                  </label>
                </div>
              </div>

              <div className="border border-gray-100 rounded-3xl p-5">
                <h4 className="text-lg font-bold text-gray-900">Prévia do cálculo</h4>
                <div className="space-y-3 mt-4 text-sm text-gray-600">
                  <div className="border border-gray-100 rounded-2xl px-4 py-3">
                    Preço médio ponderado do combustível: {formatCurrency(averageFuelPrice)}/L
                  </div>
                  <div className="border border-gray-100 rounded-2xl px-4 py-3">
                    Custo operacional estimado: {formatCurrency(estimatedOperationalCost)}
                  </div>
                  <div className="border border-gray-100 rounded-2xl px-4 py-3">
                    Custo por entrega configurado: {formatCurrency(configuredCostPerDelivery)}
                  </div>
                  <div className="border border-gray-100 rounded-2xl px-4 py-3">
                    ROI estimado: {formatNumber(estimatedRoi.toFixed(1))}%
                  </div>
                </div>
              </div>
            </div>

            <div className="flex flex-wrap gap-3 mt-6">
              <button
                type="button"
                onClick={persistFinancialConfig}
                className="btn-primary rounded-2xl px-5"
              >
                Salvar configuracao
              </button>
              <button
                type="button"
                onClick={resetFinancialConfig}
                className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-700"
              >
                Restaurar padrao
              </button>
            </div>
          </div>
        </div>
      )}

      {isAccessModalOpen && isAdmin && (
        <div className="fixed inset-0 bg-black/50 z-40 flex items-center justify-center p-4">
          <div className="w-full max-w-4xl bg-white rounded-3xl shadow-2xl border border-gray-100 p-6">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs uppercase tracking-wider text-gray-400 font-bold">Admin</p>
                <h3 className="text-2xl font-bold text-gray-900 mt-2">Controle de visoes por usuario</h3>
                <p className="text-sm text-gray-500 mt-2">
                  Defina quais usuarios podem ver a visao operacional, financeira e de frota.
                </p>
              </div>
              <button
                type="button"
                onClick={() => setIsAccessModalOpen(false)}
                className="px-4 py-2 rounded-2xl border border-gray-200 text-sm font-medium text-gray-700"
              >
                Fechar
              </button>
            </div>

            <div className="grid grid-cols-1 xl:grid-cols-[1fr_0.9fr] gap-6 mt-6">
              <div className="space-y-4">
                <div className="border border-gray-100 rounded-3xl p-5">
                  <label className="block text-sm font-medium text-gray-700 mb-2">Email do usuario</label>
                  <input
                    type="email"
                    value={managedEmail}
                    onChange={(event) => setManagedEmail(event.target.value)}
                    placeholder="usuario@empresa.com"
                    className="input-field"
                  />

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mt-4">
                    {DASHBOARD_VIEWS.map((view) => (
                      <button
                        key={view.key}
                        type="button"
                        onClick={() => handleManagedAccessChange(view.key)}
                        className={`rounded-2xl border px-4 py-4 text-left ${
                          managedAccess[view.key]
                            ? 'border-brand-blue bg-blue-50 text-brand-blue'
                            : 'border-gray-200 bg-white text-gray-600'
                        }`}
                      >
                        <p className="font-semibold">{view.shortLabel}</p>
                        <p className="text-xs mt-2 opacity-80">
                          {managedAccess[view.key] ? 'Liberado' : 'Bloqueado'}
                        </p>
                      </button>
                    ))}
                  </div>

                  <div className="flex flex-wrap gap-3 mt-5">
                    <button
                      type="button"
                      onClick={saveManagedPermissions}
                      className="btn-primary rounded-2xl px-5"
                    >
                      Salvar permissoes
                    </button>
                    <button
                      type="button"
                      onClick={() => {
                        setManagedEmail('')
                        setManagedAccess(createDefaultUserAccess())
                      }}
                      className="px-4 py-2 rounded-2xl border border-gray-200 bg-white text-sm font-medium text-gray-700"
                    >
                      Limpar formulario
                    </button>
                  </div>
                </div>
              </div>

              <div className="border border-gray-100 rounded-3xl p-5">
                <div className="flex items-center gap-3 mb-4">
                  <ShieldCheck className="text-green-600" size={20} />
                  <div>
                    <h4 className="text-lg font-bold text-gray-900">Usuarios configurados</h4>
                    <p className="text-sm text-gray-500">O administrador sempre continua vendo tudo.</p>
                  </div>
                </div>

                {configuredUsers.length === 0 ? (
                  <div className="text-sm text-gray-500 border border-dashed border-gray-200 rounded-2xl px-4 py-6">
                    Nenhum usuario configurado ainda.
                  </div>
                ) : (
                  <div className="space-y-3">
                    {configuredUsers.map((email) => {
                      const access = normalizeAccess(accessMatrix[email], createDefaultUserAccess())

                      return (
                        <button
                          key={email}
                          type="button"
                          onClick={() => editPermissionEntry(email)}
                          className="w-full text-left border border-gray-100 rounded-2xl px-4 py-4 hover:border-gray-200"
                        >
                          <p className="font-semibold text-gray-900">{email}</p>
                          <p className="text-xs text-gray-500 mt-2">
                            Operacional: {access.operational ? 'Sim' : 'Nao'} • Financeira: {access.financial ? 'Sim' : 'Nao'} • Frota: {access.fleet ? 'Sim' : 'Nao'}
                          </p>
                        </button>
                      )
                    })}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Dashboard
