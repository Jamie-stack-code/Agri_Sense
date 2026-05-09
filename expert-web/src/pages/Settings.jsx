import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { User, Shield, Bell, Globe, Save, RefreshCw, CheckCircle, LogOut } from 'lucide-react';

const Settings = () => {
  const [isSaving, setIsSaving] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);

  const handleSave = () => {
    setIsSaving(true);
    setTimeout(() => {
      setIsSaving(false);
      setShowSuccess(true);
      setTimeout(() => setShowSuccess(false), 3000);
    }, 1500);
  };

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="max-w-4xl mx-auto space-y-12 pb-20"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Expert Profile</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Manage your professional credentials and system preferences.</p>
        </div>
        <button 
          onClick={handleSave}
          disabled={isSaving}
          className="px-10 py-5 agri-gradient text-white rounded-3xl font-black shadow-2xl shadow-agri-green/20 flex items-center gap-3 hover:scale-[1.02] active:scale-[0.98] transition-all disabled:opacity-50"
        >
          {isSaving ? <RefreshCw className="animate-spin" /> : <Save size={20} />}
          {isSaving ? 'Saving...' : 'Save Settings'}
        </button>
      </div>

      {showSuccess && (
        <motion.div initial={{ opacity: 0, y: -20 }} animate={{ opacity: 1, y: 0 }} className="bg-emerald-50 border border-emerald-100 p-8 rounded-[2.5rem] flex items-center gap-6 text-emerald-700">
          <CheckCircle size={28} />
          <p className="text-xl font-black">Professional Profile Synchronized.</p>
        </motion.div>
      )}

      <div className="grid grid-cols-1 gap-10">
        <div className="agri-card p-12">
          <div className="flex items-center gap-6 border-b border-slate-50 pb-8 mb-10">
            <div className="w-20 h-20 rounded-3xl bg-agri-gold flex items-center justify-center text-agri-green text-3xl font-black shadow-lg shadow-agri-gold/20">
              AM
            </div>
            <div>
              <h3 className="text-2xl font-black text-agri-green tracking-tight">Dr. Andrew Mwale</h3>
              <p className="text-agri-muted font-bold text-xs uppercase tracking-widest mt-1">Senior Agronomy Expert | ID: EXP-2024-001</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
            <div className="space-y-6">
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-3 block">Professional Title</label>
                <input type="text" defaultValue="Senior Agronomy Consultant" className="w-full p-5 bg-slate-50 border border-slate-100 rounded-3xl font-black text-agri-text focus:border-agri-gold outline-none transition-all" />
              </div>
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-3 block">Expertise Areas</label>
                <input type="text" defaultValue="Maize, Tobacco, Soil Health" className="w-full p-5 bg-slate-50 border border-slate-100 rounded-3xl font-black text-agri-text focus:border-agri-gold outline-none transition-all" />
              </div>
            </div>
            <div className="space-y-6">
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-3 block">Official Language</label>
                <select className="w-full p-5 bg-slate-50 border border-slate-100 rounded-3xl font-black text-agri-text focus:border-agri-gold outline-none transition-all">
                  <option>English / Chichewa</option>
                  <option>English / Tumbuka</option>
                </select>
              </div>
              <div className="pt-8 flex gap-4">
                <button 
                  onClick={() => { alert('Secure Termination Protocol Initiated.'); window.location.href = '/'; }}
                  className="w-full py-5 bg-rose-50 text-rose-500 rounded-3xl font-black text-xs uppercase tracking-widest border border-rose-100 hover:bg-rose-500 hover:text-white transition-all flex items-center justify-center gap-3"
                >
                  <LogOut size={20} />
                  Terminate Session
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default Settings;
