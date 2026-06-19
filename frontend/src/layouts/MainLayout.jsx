import { useState } from 'react'
import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom'
import { 
  LayoutDashboard, 
  Truck, 
  MapPin, 
  Route, 
  LogOut, 
  Menu, 
  ChevronRight
} from 'lucide-react'
import useAuthStore from '../store/useAuthStore'

const MainLayout = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const user = useAuthStore((state) => state.user)
  const logout = useAuthStore((state) => state.logout)

  const menuItems = [
    { path: '/', icon: LayoutDashboard, label: 'Dashboard' },
    { path: '/routing', icon: Route, label: 'Roteirização' },
    { path: '/vehicles', icon: Truck, label: 'Veículos' },
    { path: '/addresses', icon: MapPin, label: 'Endereços' },
  ]

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const userName = user?.name || 'Operador RotaCerta'
  const userRole = user?.role || 'ADMIN'
  const userInitials = userName
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((name) => name[0]?.toUpperCase())
    .join('')

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar Desktop */}
      <aside className={`fixed inset-y-0 left-0 bg-brand-black text-white w-64 transform ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'} lg:translate-x-0 transition-transform duration-200 ease-in-out z-30`}>
        <div className="p-6 flex flex-col h-full">
          <div className="flex items-center gap-3 mb-10">
            <div className="w-10 h-10 bg-brand-yellow rounded-lg flex items-center justify-center text-brand-black font-bold text-xl">
              RC
            </div>
            <span className="text-xl font-bold tracking-tight">RotaCerta</span>
          </div>

          <nav className="flex-1 space-y-2">
            {menuItems.map((item) => {
              const Icon = item.icon
              const isActive = location.pathname === item.path
              return (
                <Link
                  key={item.path}
                  to={item.path}
                  onClick={() => setIsSidebarOpen(false)}
                  className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                    isActive 
                      ? 'bg-brand-blue text-white' 
                      : 'text-gray-400 hover:bg-brand-dark hover:text-white'
                  }`}
                >
                  <Icon size={20} />
                  <span>{item.label}</span>
                </Link>
              )
            })}
          </nav>

          <button 
            onClick={handleLogout}
            className="flex items-center gap-3 px-4 py-3 text-gray-400 hover:bg-red-900/20 hover:text-red-400 rounded-lg transition-colors mt-auto"
          >
            <LogOut size={20} />
            <span>Sair</span>
          </button>
        </div>
      </aside>

      {/* Overlay mobile */}
      {isSidebarOpen && (
        <div 
          className="fixed inset-0 bg-black/50 z-20 lg:hidden" 
          onClick={() => setIsSidebarOpen(false)}
        />
      )}

      {/* Main Content */}
      <main className="flex-1 lg:ml-64 flex flex-col">
        {/* Header */}
        <header className="bg-white border-b border-gray-200 min-h-16 flex items-center justify-between px-6 py-3 sticky top-0 z-10">
          <button 
            className="lg:hidden text-gray-600"
            onClick={() => setIsSidebarOpen(true)}
          >
            <Menu size={24} />
          </button>

          <div className="flex items-center gap-4 ml-auto">
            <div className="hidden md:flex items-center gap-2 text-xs text-gray-400 bg-gray-50 border border-gray-200 rounded-full px-3 py-2">
              <span>Plataforma SaaS</span>
              <ChevronRight size={14} />
              <span>Operacao logistica</span>
            </div>

            <div className="text-right hidden sm:block">
              <p className="text-sm font-medium text-gray-900">{userName}</p>
              <p className="text-xs text-gray-500">{userRole}</p>
            </div>
            <div className="w-10 h-10 bg-brand-blue rounded-full flex items-center justify-center text-white font-bold">
              {userInitials || 'RC'}
            </div>
          </div>
        </header>

        {/* Page Content */}
        <div className="p-6">
          <Outlet />
        </div>
      </main>
    </div>
  )
}

export default MainLayout
