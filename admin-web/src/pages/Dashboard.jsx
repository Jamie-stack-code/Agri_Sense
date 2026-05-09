import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  Users, 
  Shield, 
  CreditCard, 
  Activity, 
  ArrowUpRight, 
  Download,
  RefreshCw,
  AlertCircle,
  Zap,
  CheckCircle,
  FileText
} from 'lucide-react';
import { adminService } from '../services/api';
import { io } from 'socket.io-client';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    experts: 0,
    activeAdvisories: 0,
    pendingCritical: 0,
    systemHealth: '99.9%',
    revenue: 'MK 0'
  });
  const [loading, setLoading] = useState(true);
  const [isExporting, setIsExporting] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [exportSuccess, setExportSuccess] = useState(false);

  useEffect(() => {
    fetchStats();

    const socket = io('http://localhost:5000');
    
    socket.on('NEW_USER_JOINED', (user) => {
      addNotification(`New Farmer Registered: ${user.name || user.phone}`);
      fetchStats();
    });

    socket.on('NEW_FARMER_QUESTION', () => {
      addNotification(`Critical Question Received from the Field`);
      fetchStats();
    });

    return () => socket.disconnect();
  }, []);

  const addNotification = (msg) => {
    const id = Date.now();
    setNotifications(prev => [{ id, msg }, ...prev].slice(0, 5));
    setTimeout(() => {
      setNotifications(prev => prev.filter(n => n.id !== id));
    }, 5000);
  };

  const fetchStats = async () => {
    setLoading(true);
    try {
      const response = await adminService.getSystemStats();
      setStats(response.data);
    } catch (error) {
      console.error("Dashboard Sync Failed", error);
    } finally {
      setLoading(false);
    }
  };

  const handleStrategicExport = () => {
    setIsExporting(true);
    // Simulate complex data compilation from all clusters
    setTimeout(() => {
      setIsExporting(false);
      setExportSuccess(true);
      
      // Real download logic: Create a blob and trigger download
      const data = JSON.stringify(stats, null, 2);
      const blob = new Blob([data], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `AgriSense_Strategic_Report_${new Date().toLocaleDateString()}.json`;
      link.click();

      setTimeout(() => setExportSuccess(false), 3000);
    }, 2500);
  };

  const statCards = [
    { label: 'National Registry', value: stats.totalUsers, icon: Users, color: 'text-agri-green', bg: 'bg-agri-green/5' },
    { label: 'Agronomy Experts', value: stats.experts, icon: Shield, color: 'text-agri-gold', bg: 'bg-agri-gold/5' },
    { label: 'Pending Critical', value: stats.pendingCritical, icon: AlertCircle, color: 'text-rose-500', bg: 'bg-rose-50' },
    { label: 'Financial Yield', value: stats.revenue, icon: CreditCard, color: 'text-emerald-600', bg: 'bg-emerald-50' },
  ];

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-12 pb-20 relative">
      {/* Neural Notifications */}
      <div className="fixed top-8 right-8 z-[100] space-y-4 pointer-events-none">
        <AnimatePresence>
          {notifications.map((n) => (
            <motion.div key={n.id} initial={{ opacity: 0, x: 50 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0 }} className="bg-agri-green text-white p-6 rounded-[2rem] shadow-2xl border border-white/20 flex items-center gap-4 min-w-[300px]">
              <div className="w-10 h-10 bg-agri-gold rounded-full flex items-center justify-center text-agri-green"><Zap size={20} fill="currentColor" /></div>
              <p className="font-bold text-sm">{n.msg}</p>
            </motion.div>
          ))}
        </AnimatePresence>
      </div>

      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">National Command</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Sovereign oversight with Real-Time Neural Sync.</p>
        </div>
        <div className="flex gap-4">
          <button 
            onClick={fetchStats}
            title="Refresh System Pulse"
            className="p-5 bg-white border border-slate-200 text-agri-green rounded-2xl hover:bg-slate-50 transition-all shadow-sm active:scale-90"
          >
            <RefreshCw className={loading ? 'animate-spin' : ''} size={24} />
          </button>
          <button 
            onClick={handleStrategicExport}
            disabled={isExporting}
            className="px-8 py-5 agri-gradient text-white rounded-2xl font-black flex items-center gap-3 shadow-xl hover:scale-[1.02] active:scale-95 transition-all disabled:opacity-50"
          >
            {isExporting ? <RefreshCw className="animate-spin" /> : <Download size={20} />}
            {isExporting ? 'Compiling Intelligence...' : 'Strategic Export'}
          </button>
        </div>
      </div>

      {exportSuccess && (
        <motion.div initial={{ opacity: 0, y: -20 }} animate={{ opacity: 1, y: 0 }} className="bg-emerald-50 border border-emerald-100 p-6 rounded-3xl flex items-center gap-4 text-emerald-700">
           <CheckCircle size={24} />
           <p className="font-black text-sm uppercase tracking-widest">Sovereign Report Exported Successfully</p>
        </motion.div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        {statCards.map((stat, idx) => (
          <div key={idx} className="agri-card p-10 group relative overflow-hidden">
            <div className="flex justify-between items-start relative z-10">
              <div className={`p-4 rounded-2xl ${stat.bg} ${stat.color}`}><stat.icon size={28} /></div>
              <div className="flex items-center gap-1 text-emerald-500 font-black text-xs"><ArrowUpRight size={14} /> LIVE</div>
            </div>
            <div className="mt-8 relative z-10">
              <h3 className="text-4xl font-black text-agri-text tracking-tighter">{loading ? '...' : stat.value}</h3>
              <p className="text-agri-muted font-bold text-[10px] uppercase tracking-[0.2em] mt-1">{stat.label}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
        <div className="lg:col-span-2 agri-card p-12 overflow-hidden relative">
          <div className="flex justify-between items-center mb-10">
            <h3 className="text-2xl font-black text-agri-green tracking-tight">System Integrity</h3>
            <div className="flex items-center gap-2 text-emerald-500 font-black text-xs uppercase tracking-widest bg-emerald-50 px-4 py-2 rounded-full">
              <Activity size={16} /> Live Pulse: {stats.systemHealth}
            </div>
          </div>
          <div className="h-64 bg-slate-50 rounded-[2rem] border border-dashed border-slate-200 flex flex-col items-center justify-center space-y-4">
             <FileText className="text-slate-200" size={64} />
             <p className="text-agri-muted font-black uppercase tracking-widest text-[10px]">Processing Real-Time Field Stream</p>
          </div>
        </div>

        <div className="agri-card p-12 space-y-8 bg-agri-green text-white relative overflow-hidden group">
          <Zap className="text-agri-gold relative z-10 group-hover:rotate-12 transition-transform duration-1000" size={48} fill="currentColor" />
          <h3 className="text-3xl font-black leading-tight relative z-10">Neural Handshake</h3>
          <p className="opacity-60 font-medium relative z-10">Sovereign connection established with the Farmer Mobile Fleet across all districts.</p>
          <div className="absolute -right-20 -top-20 w-64 h-64 bg-white/5 rounded-full blur-3xl"></div>
        </div>
      </div>
    </motion.div>
  );
};

export default Dashboard;
