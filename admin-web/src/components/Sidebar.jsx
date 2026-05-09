import React, { useState } from 'react';
import { NavLink, Link } from 'react-router-dom';
import { 
  BarChart2, 
  Users, 
  CreditCard, 
  Settings, 
  LogOut,
  Shield,
  ChevronRight,
  User,
  X
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

const Sidebar = ({ isOpen, toggleSidebar }) => {
  const [showProfileMenu, setShowProfileMenu] = useState(false);

  const menuItems = [
    { icon: BarChart2, label: 'Analytics', path: '/' },
    { icon: Users, label: 'User Registry', path: '/users' },
    { icon: CreditCard, label: 'Financials', path: '/subscriptions' },
    { icon: Settings, label: 'Configuration', path: '/settings' },
  ];

  return (
    <>
      <AnimatePresence>
        {isOpen && (
          <motion.div 
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={toggleSidebar}
            className="fixed inset-0 bg-agri-green/40 backdrop-blur-sm z-40 lg:hidden"
          ></motion.div>
        )}
      </AnimatePresence>

      <aside className={`
        fixed lg:sticky top-0 left-0 z-50 h-screen bg-white border-r border-slate-100 flex flex-col shadow-2xl transition-all duration-500 ease-in-out flex-shrink-0
        ${isOpen ? 'w-80 translate-x-0' : 'w-0 -translate-x-full lg:w-80 lg:translate-x-0'}
      `}>
        <div className="p-10 flex items-center justify-between">
          <div className="flex items-center gap-4 min-w-[200px]">
            <div className="w-14 h-14 bg-agri-green/5 rounded-2xl p-2 flex items-center justify-center">
              <img src="/logo.png" alt="Agri-Sense Logo" className="w-full h-full object-contain" />
            </div>
            <div>
              <h1 className="text-2xl font-black text-agri-green tracking-tighter leading-none">AGRI-SENSE</h1>
              <p className="text-[10px] text-agri-gold font-black uppercase tracking-[0.3em] mt-1">Authority OS</p>
            </div>
          </div>
          <button onClick={toggleSidebar} className="lg:hidden p-2 text-slate-300">
            <X size={24} />
          </button>
        </div>

        <nav className="flex-1 px-8 py-6 space-y-3 overflow-y-auto custom-scrollbar min-w-[320px]">
          {menuItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              onClick={() => window.innerWidth < 1024 && toggleSidebar()}
              className={({ isActive }) => `
                flex items-center space-x-4 px-6 py-4 rounded-2xl transition-all duration-300 group
                ${isActive 
                  ? 'bg-agri-green text-white shadow-xl shadow-agri-green/20' 
                  : 'text-slate-400 hover:text-agri-green hover:bg-slate-50'}
              `}
            >
              <item.icon size={22} />
              <span className="font-bold text-sm tracking-wide">{item.label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="p-8 border-t border-slate-50 relative min-w-[320px]">
          <AnimatePresence>
            {showProfileMenu && (
              <motion.div 
                initial={{ opacity: 0, y: 10, scale: 0.95 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                exit={{ opacity: 0, y: 10, scale: 0.95 }}
                className="absolute bottom-full left-8 right-8 mb-4 bg-white border border-slate-100 rounded-3xl shadow-2xl p-4 space-y-2 z-50"
              >
                <Link to="/settings" onClick={() => setShowProfileMenu(false)} className="flex items-center gap-3 p-3 hover:bg-slate-50 rounded-xl text-agri-text font-bold transition-colors">
                  <User size={18} className="text-agri-gold" /> Admin Profile
                </Link>
                <button 
                  onClick={() => {
                    alert('Terminating Secure Admin Session...');
                    window.location.href = '/';
                  }}
                  className="w-full flex items-center gap-3 p-3 hover:bg-rose-50 rounded-xl text-rose-500 font-bold transition-colors"
                >
                  <LogOut size={18} /> Termination
                </button>
              </motion.div>
            )}
          </AnimatePresence>

          <div 
            onClick={() => setShowProfileMenu(!showProfileMenu)}
            className="bg-slate-50 p-6 rounded-3xl border border-slate-100 cursor-pointer hover:border-agri-gold/30 transition-all group active:scale-95"
          >
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-full bg-agri-green flex items-center justify-center text-white font-black">
                A
              </div>
              <div className="flex-1 overflow-hidden">
                <p className="text-sm font-black text-agri-text truncate">Admin Root</p>
                <p className="text-[10px] text-agri-muted font-bold uppercase truncate">System Access</p>
              </div>
              <motion.div 
                animate={{ rotate: showProfileMenu ? 90 : 0 }}
                className="text-slate-300 group-hover:text-agri-gold transition-colors"
              >
                <ChevronRight size={20} />
              </motion.div>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
};

export default Sidebar;
