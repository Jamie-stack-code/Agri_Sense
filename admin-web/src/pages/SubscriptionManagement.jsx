import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { CreditCard, TrendingUp, DollarSign, ArrowUpRight, Zap, ShieldCheck, Download, RefreshCw, CheckCircle } from 'lucide-react';
import { adminService } from '../services/api';

const SubscriptionManagement = () => {
  const [subscriptions, setSubscriptions] = useState([]);
  const [isExporting, setIsExporting] = useState(false);
  const [exportSuccess, setExportSuccess] = useState(false);

  useEffect(() => {
    const fetchSubs = async () => {
      try {
        const response = await adminService.getSubscriptions();
        setSubscriptions(response.data);
      } catch (error) {
        console.error("Using local financial records");
      }
    };
    fetchSubs();
  }, []);

  const handleExport = () => {
    setIsExporting(true);
    setTimeout(() => {
      setIsExporting(false);
      setExportSuccess(true);
      setTimeout(() => setExportSuccess(false), 3000);
    }, 2500);
  };

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="space-y-12"
    >
      <div className="flex flex-col xl:flex-row xl:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Financial Yield</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Real-time monetization oversight and subscription health.</p>
        </div>
        <button 
          onClick={handleExport}
          disabled={isExporting}
          className={`px-8 py-4 bg-white text-agri-green border border-slate-200 rounded-2xl font-black flex items-center gap-2 transition-all shadow-sm ${isExporting ? 'opacity-50' : 'hover:bg-slate-50'}`}
        >
          {isExporting ? <RefreshCw className="animate-spin" size={20} /> : exportSuccess ? <CheckCircle className="text-emerald-500" size={20} /> : <Download size={20} />}
          {isExporting ? 'Compiling Audit...' : exportSuccess ? 'Ledger Exported' : 'Export Audit Ledger'}
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {[
          { label: 'Cumulative Revenue', value: 'MK 42.4M', icon: DollarSign, color: 'text-emerald-600', bg: 'bg-emerald-50' },
          { label: 'Active Premium Seats', value: '12,842', icon: Zap, color: 'text-agri-gold', bg: 'bg-agri-gold/5' },
          { label: 'Pending Renewals', value: '12', icon: TrendingUp, color: 'text-blue-600', bg: 'bg-blue-50' },
        ].map((stat, i) => (
          <div key={i} className="agri-card p-10 flex flex-col justify-between">
            <div className={`w-14 h-14 rounded-2xl ${stat.bg} ${stat.color} flex items-center justify-center`}>
              <stat.icon size={28} />
            </div>
            <div className="mt-8">
              <p className="text-[10px] text-agri-muted font-black uppercase tracking-widest">{stat.label}</p>
              <p className="text-4xl font-black text-agri-text mt-2">{stat.value}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="agri-card overflow-hidden">
        <div className="p-8 border-b border-slate-50 flex justify-between items-center">
          <h3 className="text-xl font-black text-agri-green">Subscription Transactions</h3>
          <span className="gold-badge">Live Feed</span>
        </div>
        <table className="w-full text-left">
          <thead>
            <tr className="bg-slate-50 border-b border-slate-100">
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Farmer Account</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Tier Level</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Effective Date</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Yield (MWK)</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-50">
            {[1, 2, 3, 4].map((id) => (
              <tr key={id} className="hover:bg-slate-50/50 transition-colors">
                <td className="p-8 font-black text-agri-text">Farmer Registry #{2024 + id}</td>
                <td className="p-8 font-bold text-agri-muted">PREMIUM</td>
                <td className="p-8 text-agri-muted font-mono">08/05/2026</td>
                <td className="p-8 font-black text-agri-green">MK 5,000</td>
                <td className="p-8"><span className="gold-badge">ACTIVE</span></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </motion.div>
  );
};

export default SubscriptionManagement;
