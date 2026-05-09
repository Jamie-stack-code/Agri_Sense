import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  Users, 
  MessageSquare, 
  AlertTriangle, 
  TrendingUp, 
  MapPin, 
  Clock, 
  ArrowUpRight,
  ShieldCheck,
  Leaf,
  X,
  FileText
} from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const navigate = useNavigate();
  const [showLogs, setShowLogs] = useState(false);

  const stats = [
    { label: 'Verified Farmers', value: '12,842', icon: Users, color: 'text-agri-green', bg: 'bg-agri-green/5' },
    { label: 'Expert Queries', value: '24', icon: MessageSquare, color: 'text-amber-600', bg: 'bg-amber-50' },
    { label: 'Field Alerts', value: '3', icon: AlertTriangle, color: 'text-rose-600', bg: 'bg-rose-50' },
    { label: 'Ecosystem Health', value: '98%', icon: ShieldCheck, color: 'text-emerald-600', bg: 'bg-emerald-50' },
  ];

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-12 pb-20"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-4xl md:text-5xl font-black text-agri-green flex items-center gap-4 tracking-tight">
            <Leaf className="text-agri-gold w-10 h-10 md:w-12 md:h-12" />
            Agronomic Intelligence
          </h1>
          <p className="text-agri-muted mt-3 text-base md:text-lg font-medium">Monitoring the pulse of Malawi's agriculture in real-time.</p>
        </div>
        <div className="flex flex-wrap gap-4">
          <button 
            onClick={() => setShowLogs(true)}
            className="flex-1 md:flex-none flex items-center justify-center gap-2 px-6 md:px-8 py-4 bg-white border border-slate-200 rounded-2xl font-black text-agri-text hover:bg-slate-50 transition-all shadow-sm"
          >
            <Clock size={20} />
            Visit Logs
          </button>
          <Link to="/advisories" className="flex-1 md:flex-none flex items-center justify-center gap-2 px-6 md:px-8 py-4 agri-gradient text-white rounded-2xl font-black shadow-xl shadow-agri-green/20 hover:scale-[1.02] transition-all">
            New Advisory
            <ArrowUpRight size={20} />
          </Link>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        {stats.map((stat, idx) => (
          <motion.div 
            key={idx}
            whileHover={{ y: -5 }}
            className="agri-card p-8 group cursor-pointer"
            onClick={() => navigate(idx === 1 ? '/questions' : idx === 2 ? '/reports' : '/farmers')}
          >
            <div className="flex justify-between items-start">
              <div className={`p-4 rounded-2xl ${stat.bg} ${stat.color}`}>
                <stat.icon size={28} />
              </div>
              <TrendingUp className="text-emerald-500 group-hover:scale-125 transition-all" size={20} />
            </div>
            <div className="mt-8">
              <h3 className="text-4xl font-black text-agri-text tracking-tighter">{stat.value}</h3>
              <p className="text-agri-muted font-bold text-xs md:text-sm uppercase tracking-widest mt-1">{stat.label}</p>
            </div>
          </motion.div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
        <div className="lg:col-span-2 space-y-6">
          <h2 className="text-2xl font-black text-agri-green">Priority Field Reports</h2>
          <div className="space-y-4">
            {[1, 2, 3].map((id) => (
              <div key={id} className="agri-card p-6 md:p-8 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-6 hover:border-agri-gold/50 cursor-pointer group">
                <div className="flex gap-6">
                  <div className="w-14 h-14 rounded-full bg-slate-100 flex items-center justify-center font-black text-agri-muted text-lg flex-shrink-0">
                    {id === 1 ? 'SB' : id === 2 ? 'JP' : 'PM'}
                  </div>
                  <div>
                    <h4 className="font-black text-agri-text text-xl">Farmer Case #{2024 + id}</h4>
                    <p className="text-agri-muted mt-1 font-medium italic text-sm md:text-base">"The rainfall patterns are changing..."</p>
                    <div className="flex flex-wrap items-center gap-4 mt-4 text-[10px] font-black text-agri-gold uppercase tracking-widest">
                      <span className="flex items-center gap-1"><MapPin size={12} /> {id === 1 ? 'Kasungu' : 'Lilongwe'} Cluster</span>
                      <span className="flex items-center gap-1"><Clock size={12} /> {id * 10}m Ago</span>
                    </div>
                  </div>
                </div>
                <button 
                  onClick={() => navigate('/questions', { state: { farmerId: id === 1 ? 'SB-1' : 'JP-2' } })}
                  className="w-full sm:w-auto p-4 bg-agri-green/5 text-agri-green hover:bg-agri-green hover:text-white rounded-2xl transition-all flex items-center justify-center group-hover:scale-110"
                >
                  <MessageSquare size={24} />
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="space-y-6">
          <h2 className="text-2xl font-black text-agri-green">Regional Intelligence</h2>
          <div className="agri-gradient rounded-[3rem] p-10 text-white relative overflow-hidden group">
            <div className="relative z-10">
              <h3 className="text-[10px] font-black opacity-60 uppercase tracking-[0.4em] mb-2">Live Alert</h3>
              <p className="text-3xl font-black leading-tight">Maize Necrosis Detection</p>
              <p className="text-white/60 mt-4 leading-relaxed font-medium">
                Unusual patterns detected in the Central Region. Immediate validation required.
              </p>
              <button 
                onClick={() => navigate('/reports')}
                className="mt-10 px-8 py-4 bg-agri-gold text-agri-green font-black rounded-2xl w-full shadow-lg hover:bg-white transition-all active:scale-95"
              >
                Validate Zone
              </button>
            </div>
            <AlertTriangle size={150} className="absolute -right-10 -bottom-10 opacity-10 group-hover:rotate-12 transition-transform duration-1000" />
          </div>
        </div>
      </div>

      {/* Visit Logs Modal */}
      <AnimatePresence>
        {showLogs && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-8">
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setShowLogs(false)} className="absolute inset-0 bg-agri-green/40 backdrop-blur-md"></motion.div>
            <motion.div initial={{ scale: 0.9, y: 20 }} animate={{ scale: 1, y: 0 }} exit={{ scale: 0.9, y: 20 }} className="relative bg-white w-full max-w-2xl rounded-[3rem] p-12 shadow-2xl space-y-8">
              <div className="flex justify-between items-center">
                <h2 className="text-3xl font-black text-agri-green">System Visit Logs</h2>
                <button onClick={() => setShowLogs(false)} className="p-3 bg-slate-50 text-slate-300 hover:text-rose-500 rounded-2xl transition-all">
                  <X size={24} />
                </button>
              </div>
              <div className="space-y-4 max-h-[400px] overflow-y-auto pr-4 custom-scrollbar">
                {[1, 2, 3, 4, 5].map(i => (
                  <div key={i} className="p-6 bg-slate-50 rounded-2xl border border-slate-100 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                       <div className="p-2 bg-agri-green/10 text-agri-green rounded-lg"><FileText size={20} /></div>
                       <div>
                          <p className="font-bold text-agri-text">Field Report Validation</p>
                          <p className="text-[10px] text-agri-muted uppercase font-black">Region: Kasungu | ID: #482{i}</p>
                       </div>
                    </div>
                    <span className="text-[10px] font-black text-agri-gold">08/05/2026</span>
                  </div>
                ))}
              </div>
              <button onClick={() => setShowLogs(false)} className="w-full py-5 agri-gradient text-white font-black rounded-2xl">Close Audit Trail</button>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default Dashboard;
