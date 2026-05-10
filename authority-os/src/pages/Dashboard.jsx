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
  FileText,
  MessageSquare,
  Leaf,
  Clock,
  MapPin,
  TrendingUp,
  X
} from 'lucide-react';
import { adminService } from '../services/api';
import { io } from 'socket.io-client';
import { useNavigate } from 'react-router-dom';

const Dashboard = ({ user }) => {
  const navigate = useNavigate();
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
  const [showLogs, setShowLogs] = useState(false);

  const isExpert = user?.role === 'EXPERT';

  useEffect(() => {
    fetchStats();
    const socket = io('http://localhost:5000');
    
    socket.on('NEW_USER_JOINED', (data) => {
      addNotification(`New Farmer Registered: ${data.name || data.phone}`);
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
    setTimeout(() => {
      setIsExporting(false);
      setExportSuccess(true);
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

  const adminStatCards = [
    { label: 'National Registry', value: stats.totalUsers, icon: Users, color: 'text-agri-green', bg: 'bg-agri-green/5' },
    { label: 'Agronomy Experts', value: stats.experts, icon: Shield, color: 'text-agri-gold', bg: 'bg-agri-gold/5' },
    { label: 'Pending Critical', value: stats.pendingCritical, icon: AlertCircle, color: 'text-rose-500', bg: 'bg-rose-50' },
    { label: 'Financial Yield', value: stats.revenue, icon: CreditCard, color: 'text-emerald-600', bg: 'bg-emerald-50' },
  ];

  const expertStatCards = [
    { label: 'Farmer Fleet', value: stats.totalUsers, icon: Users, color: 'text-agri-green', bg: 'bg-agri-green/5' },
    { label: 'Expert Queries', value: stats.pendingCritical + 12, icon: MessageSquare, color: 'text-amber-600', bg: 'bg-amber-50' },
    { label: 'Field Alerts', value: stats.pendingCritical, icon: AlertCircle, color: 'text-rose-600', bg: 'bg-rose-50' },
    { label: 'Ecosystem Health', value: stats.systemHealth, icon: Activity, color: 'text-emerald-600', bg: 'bg-emerald-50' },
  ];

  const statCards = isExpert ? expertStatCards : adminStatCards;

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-12 pb-20 relative">
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
          <h1 className="text-5xl font-black text-agri-green tracking-tight flex items-center gap-4">
            {isExpert ? <Leaf className="text-agri-gold" size={48} /> : <Shield className="text-agri-gold" size={48} />}
            {isExpert ? 'Expert Intelligence' : 'National Command'}
          </h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">
            {isExpert ? "Monitoring the pulse of Malawi's agriculture in real-time." : "Sovereign oversight with Real-Time Neural Sync."}
          </p>
        </div>
        <div className="flex gap-4">
          <button 
            onClick={fetchStats}
            className="p-5 bg-white border border-slate-200 text-agri-green rounded-2xl hover:bg-slate-50 transition-all shadow-sm active:scale-90"
          >
            <RefreshCw className={loading ? 'animate-spin' : ''} size={24} />
          </button>
          {isExpert ? (
             <button 
                onClick={() => setShowLogs(true)}
                className="px-8 py-5 bg-white border border-slate-200 text-agri-text rounded-2xl font-black flex items-center gap-3 shadow-sm hover:bg-slate-50 transition-all"
              >
                <Clock size={20} /> Audit Logs
              </button>
          ) : (
            <button 
              onClick={handleStrategicExport}
              disabled={isExporting}
              className="px-8 py-5 agri-gradient text-white rounded-2xl font-black flex items-center gap-3 shadow-xl hover:scale-[1.02] active:scale-95 transition-all disabled:opacity-50"
            >
              {isExporting ? <RefreshCw className="animate-spin" /> : <Download size={20} />}
              {isExporting ? 'Compiling...' : 'Strategic Export'}
            </button>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        {statCards.map((stat, idx) => (
          <div key={idx} className="agri-card p-10 group relative overflow-hidden cursor-pointer" onClick={() => {
            if (isExpert) {
              if (idx === 1) navigate('/questions');
              else if (idx === 2) navigate('/reports');
              else navigate('/farmers');
            }
          }}>
            <div className="flex justify-between items-start relative z-10">
              <div className={`p-4 rounded-2xl ${stat.bg} ${stat.color}`}><stat.icon size={28} /></div>
              <div className="flex items-center gap-1 text-emerald-500 font-black text-xs"><TrendingUp size={14} /> LIVE</div>
            </div>
            <div className="mt-8 relative z-10">
              <h3 className="text-4xl font-black text-agri-text tracking-tighter">{loading ? '...' : stat.value}</h3>
              <p className="text-agri-muted font-bold text-[10px] uppercase tracking-[0.2em] mt-1">{stat.label}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
        <div className="lg:col-span-2 space-y-6">
          <h3 className="text-2xl font-black text-agri-green tracking-tight">
            {isExpert ? 'Priority Field Reports' : 'System Integrity'}
          </h3>
          {isExpert ? (
            <div className="space-y-4">
              {[1, 2].map((id) => (
                <div key={id} onClick={() => navigate('/questions')} className="agri-card p-8 flex items-center justify-between group cursor-pointer hover:border-agri-gold/50 transition-all">
                  <div className="flex gap-6">
                    <div className="w-14 h-14 rounded-full bg-slate-100 flex items-center justify-center font-black text-agri-muted text-lg">{id === 1 ? 'SB' : 'JP'}</div>
                    <div>
                      <h4 className="font-black text-agri-text text-xl">Farmer Case #{2024 + id}</h4>
                      <p className="text-agri-muted mt-1 font-medium italic text-sm">"The rainfall patterns are changing..."</p>
                      <div className="flex items-center gap-4 mt-4 text-[10px] font-black text-agri-gold uppercase tracking-widest">
                        <span className="flex items-center gap-1"><MapPin size={12} /> Cluster {id === 1 ? 'A' : 'B'}</span>
                        <span className="flex items-center gap-1"><Clock size={12} /> {id * 15}m Ago</span>
                      </div>
                    </div>
                  </div>
                  <div className="p-4 bg-agri-green/5 text-agri-green group-hover:bg-agri-green group-hover:text-white rounded-2xl transition-all">
                    <MessageSquare size={24} />
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="agri-card p-12 h-full relative overflow-hidden">
               <div className="flex justify-between items-center mb-10">
                <div className="flex items-center gap-2 text-emerald-500 font-black text-xs uppercase tracking-widest bg-emerald-50 px-4 py-2 rounded-full">
                  <Activity size={16} /> Live Pulse: {stats.systemHealth}
                </div>
              </div>
              <div className="h-64 bg-slate-50 rounded-[2rem] border border-dashed border-slate-200 flex flex-col items-center justify-center space-y-4">
                 <FileText className="text-slate-200" size={64} />
                 <p className="text-agri-muted font-black uppercase tracking-widest text-[10px]">Processing Real-Time Field Stream</p>
              </div>
            </div>
          )}
        </div>

        <div className="space-y-6">
           <h3 className="text-2xl font-black text-agri-green tracking-tight">
            {isExpert ? 'Regional Intelligence' : 'Neural Handshake'}
          </h3>
          <div className="agri-card p-12 bg-agri-green text-white relative overflow-hidden group h-full flex flex-col justify-between">
            <Zap className="text-agri-gold relative z-10 group-hover:rotate-12 transition-transform duration-1000" size={48} fill="currentColor" />
            <div className="space-y-4 relative z-10">
              <h3 className="text-3xl font-black leading-tight">
                {isExpert ? 'Maize Necrosis Detection' : 'Global Core Active'}
              </h3>
              <p className="opacity-60 font-medium">
                {isExpert ? "Unusual patterns detected in the Central Region. Immediate validation required." : "Sovereign connection established with the Farmer Mobile Fleet."}
              </p>
              {isExpert && (
                <button onClick={() => navigate('/reports')} className="w-full py-4 bg-agri-gold text-agri-green font-black rounded-2xl shadow-lg hover:bg-white transition-all">
                  Validate Zone
                </button>
              )}
            </div>
            <div className="absolute -right-20 -top-20 w-64 h-64 bg-white/5 rounded-full blur-3xl"></div>
          </div>
        </div>
      </div>

      <AnimatePresence>
        {showLogs && (
          <div className="fixed inset-0 z-[100] flex items-center justify-center p-8">
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setShowLogs(false)} className="absolute inset-0 bg-agri-green/40 backdrop-blur-md"></motion.div>
            <motion.div initial={{ scale: 0.9, y: 20 }} animate={{ scale: 1, y: 0 }} exit={{ scale: 0.9, y: 20 }} className="relative bg-white w-full max-w-2xl rounded-[3rem] p-12 shadow-2xl space-y-8">
              <div className="flex justify-between items-center">
                <h2 className="text-3xl font-black text-agri-green">Audit Trail</h2>
                <button onClick={() => setShowLogs(false)} className="p-3 bg-slate-50 text-slate-300 hover:text-rose-500 rounded-2xl transition-all"><X size={24} /></button>
              </div>
              <div className="space-y-4 max-h-[400px] overflow-y-auto pr-4 custom-scrollbar">
                {[1, 2, 3].map(i => (
                  <div key={i} className="p-6 bg-slate-50 rounded-2xl border border-slate-100 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                       <div className="p-2 bg-agri-green/10 text-agri-green rounded-lg"><FileText size={20} /></div>
                       <div>
                          <p className="font-bold text-agri-text">Session Access</p>
                          <p className="text-[10px] text-agri-muted uppercase font-black">Region: Central | ID: #X4{i}</p>
                       </div>
                    </div>
                    <span className="text-[10px] font-black text-agri-gold">09/05/2026</span>
                  </div>
                ))}
              </div>
              <button onClick={() => setShowLogs(false)} className="w-full py-5 agri-gradient text-white font-black rounded-2xl">Close Audit</button>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default Dashboard;
