import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Settings, Shield, Globe, Bell, Database, Save, RefreshCw, CheckCircle } from 'lucide-react';

const SystemSettings = () => {
  const [isSaving, setIsSaving] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const [isResetting, setIsResetting] = useState(false);
  const [config, setConfig] = useState({
    twoFactor: true,
    dataSovereignty: false,
    language: 'English / Chichewa / Tumbuka',
    currency: 'Malawian Kwacha (MWK)'
  });

  const handleSave = () => {
    setIsSaving(true);
    setTimeout(() => {
      setIsSaving(false);
      setShowSuccess(true);
      setTimeout(() => setShowSuccess(false), 3000);
    }, 1500);
  };

  const handleReset = () => {
    setIsResetting(true);
    setTimeout(() => {
      setConfig({
        twoFactor: true,
        dataSovereignty: false,
        language: 'English / Chichewa / Tumbuka',
        currency: 'Malawian Kwacha (MWK)'
      });
      setIsResetting(false);
      setShowSuccess(true);
      setTimeout(() => setShowSuccess(false), 3000);
    }, 1000);
  };

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="space-y-12"
    >
      <div className="flex flex-col xl:flex-row xl:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Global Config</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Modify core system parameters and security protocols.</p>
        </div>
        <div className="flex gap-4">
          <button 
            onClick={handleReset}
            disabled={isResetting}
            className="px-8 py-4 bg-white border border-slate-200 text-agri-muted rounded-2xl font-black flex items-center gap-2 hover:text-agri-green transition-all shadow-sm active:scale-95"
          >
            <RefreshCw className={isResetting ? 'animate-spin' : ''} size={20} /> 
            {isResetting ? 'Resetting...' : 'Reset Defaults'}
          </button>
          <button 
            onClick={handleSave}
            disabled={isSaving}
            className={`px-8 py-4 agri-gradient text-white rounded-2xl font-black flex items-center gap-2 shadow-xl shadow-agri-green/20 transition-all ${isSaving ? 'opacity-50 cursor-not-allowed' : 'hover:scale-[1.02]'}`}
          >
            {isSaving ? <RefreshCw className="animate-spin" size={20} /> : <Save size={20} />}
            {isSaving ? 'Synchronizing...' : 'Synchronize Config'}
          </button>
        </div>
      </div>

      {showSuccess && (
        <motion.div 
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-emerald-50 border border-emerald-100 p-6 rounded-[2rem] flex items-center gap-4 text-emerald-700 font-bold"
        >
          <CheckCircle size={24} />
          System Authority updated. All configurations synchronized across the ecosystem.
        </motion.div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
        <div className="agri-card p-12 space-y-10">
          <div className="flex items-center gap-4 border-b border-slate-50 pb-8">
            <div className="p-4 bg-rose-50 text-rose-500 rounded-2xl">
              <Shield size={28} />
            </div>
            <h3 className="text-2xl font-black text-agri-green tracking-tight">Security & Encryption</h3>
          </div>
          
          <div className="space-y-8">
            <div className="flex items-center justify-between p-8 bg-slate-50 rounded-3xl border border-slate-100">
              <div>
                <p className="text-agri-text font-black text-lg">Two-Factor Authentication</p>
                <p className="text-agri-muted text-xs font-bold mt-1 uppercase tracking-widest tracking-tighter">Enforce MFA across the Expert Portal</p>
              </div>
              <div 
                onClick={() => setConfig({...config, twoFactor: !config.twoFactor})}
                className={`w-14 h-8 rounded-full relative p-1 cursor-pointer transition-colors ${config.twoFactor ? 'bg-agri-green' : 'bg-slate-300'}`}
              >
                <motion.div 
                  animate={{ x: config.twoFactor ? 24 : 0 }}
                  className="w-6 h-6 bg-white rounded-full shadow-lg"
                />
              </div>
            </div>
          </div>
        </div>

        <div className="agri-card p-12 space-y-10">
          <div className="flex items-center gap-4 border-b border-slate-50 pb-8">
            <div className="p-4 bg-agri-green/5 text-agri-green rounded-2xl">
              <Globe size={28} />
            </div>
            <h3 className="text-2xl font-black text-agri-green tracking-tight">Regional Settings</h3>
          </div>
          
          <div className="space-y-8">
            <div>
              <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-3 block">Language Cluster</label>
              <select 
                value={config.language}
                onChange={(e) => setConfig({...config, language: e.target.value})}
                className="w-full p-5 bg-slate-50 border border-slate-200 rounded-3xl text-agri-text font-black focus:ring-2 focus:ring-agri-gold outline-none"
              >
                <option>English / Chichewa / Tumbuka</option>
                <option>French / Swahili</option>
              </select>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default SystemSettings;
