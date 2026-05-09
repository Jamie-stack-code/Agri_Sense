import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { BookOpen, Send, Type, Image, Mic, Zap, AlertTriangle, CheckCircle, RefreshCw } from 'lucide-react';
import MediaStudio from '../components/MediaStudio';

const Advisories = () => {
  const [isPublishing, setIsPublishing] = useState(false);
  const [success, setSuccess] = useState(false);
  const [advisoryType, setAdvisoryType] = useState('NORMAL');
  const [mediaModal, setMediaModal] = useState({ isOpen: false, type: null });

  const handlePublish = () => {
    setIsPublishing(true);
    setTimeout(() => {
      setIsPublishing(false);
      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
    }, 2000);
  };

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-5xl mx-auto space-y-12 pb-20"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Advisory Studio</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Broadcast national-scale agricultural intelligence to the field.</p>
        </div>
        <div className="flex gap-4">
          <button 
            onClick={handlePublish}
            disabled={isPublishing}
            className="px-10 py-5 agri-gradient text-white rounded-3xl font-black shadow-2xl shadow-agri-green/20 flex items-center gap-3 hover:scale-[1.02] active:scale-[0.98] transition-all disabled:opacity-50"
          >
            {isPublishing ? <RefreshCw className="animate-spin" /> : <Send size={20} />}
            {isPublishing ? 'Synchronizing...' : 'Publish Advisory'}
          </button>
        </div>
      </div>

      {success && (
        <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} className="bg-emerald-50 border border-emerald-100 p-8 rounded-[2.5rem] flex items-center gap-6 text-emerald-700">
          <div className="w-12 h-12 bg-emerald-500 text-white rounded-full flex items-center justify-center shadow-lg shadow-emerald-200">
            <CheckCircle size={28} />
          </div>
          <div>
            <p className="text-xl font-black">Broadcast Successful</p>
            <p className="font-bold opacity-80 uppercase text-[10px] tracking-widest mt-1">Notification pushed to all connected Farmer Apps.</p>
          </div>
        </motion.div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-12">
        <div className="lg:col-span-2 space-y-8">
          <div className="agri-card p-12 space-y-10">
            <div>
              <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-4 block">Headline</label>
              <input type="text" placeholder="e.g. Critical Pest Alert: Fall Armyworm Detection" className="w-full p-6 bg-slate-50 border border-slate-100 rounded-3xl text-xl font-black text-agri-text focus:border-agri-gold outline-none transition-all" />
            </div>
            <div>
              <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-4 block">Intelligence Report</label>
              <textarea placeholder="Provide detailed field guidance..." className="w-full p-8 bg-slate-50 border border-slate-100 rounded-[2.5rem] min-h-[300px] text-lg font-medium text-agri-text focus:border-agri-gold outline-none transition-all resize-none" />
            </div>
          </div>
        </div>

        <div className="space-y-8">
          <div className="agri-card p-10 space-y-8">
            <h3 className="text-xl font-black text-agri-green border-b border-slate-50 pb-6">Studio Settings</h3>
            
            <div className="space-y-6">
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-widest mb-3 block">Advisory Level</label>
                <div className="grid grid-cols-2 gap-3">
                  <button 
                    onClick={() => setAdvisoryType('NORMAL')}
                    className={`p-4 rounded-2xl border-2 font-black text-xs transition-all ${advisoryType === 'NORMAL' ? 'border-agri-green bg-agri-green/5 text-agri-green' : 'border-slate-50 bg-slate-50 text-slate-400'}`}
                  >
                    NORMAL
                  </button>
                  <button 
                    onClick={() => setAdvisoryType('URGENT')}
                    className={`p-4 rounded-2xl border-2 font-black text-xs transition-all ${advisoryType === 'URGENT' ? 'border-rose-500 bg-rose-50 text-rose-500' : 'border-slate-50 bg-slate-50 text-slate-400'}`}
                  >
                    URGENT
                  </button>
                </div>
              </div>

              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-widest mb-3 block">Media Attachment</label>
                <div className="flex gap-4">
                  <button 
                    onClick={() => setMediaModal({ isOpen: true, type: 'IMAGE' })}
                    className="flex-1 p-5 bg-slate-50 text-slate-400 hover:text-agri-green rounded-2xl border border-slate-100 transition-all flex flex-col items-center gap-2"
                  >
                    <Image size={24} />
                    <span className="text-[8px] font-black uppercase tracking-widest">Image</span>
                  </button>
                  <button 
                    onClick={() => setMediaModal({ isOpen: true, type: 'VOICE' })}
                    className="flex-1 p-5 bg-slate-50 text-slate-400 hover:text-agri-green rounded-2xl border border-slate-100 transition-all flex flex-col items-center gap-2"
                  >
                    <Mic size={24} />
                    <span className="text-[8px] font-black uppercase tracking-widest">Voice</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="agri-gradient rounded-[3rem] p-10 text-white space-y-6 relative overflow-hidden group">
            <Zap size={120} className="absolute -right-10 -bottom-10 opacity-10 group-hover:rotate-12 transition-transform duration-1000" />
            <h3 className="text-2xl font-black relative z-10 leading-tight">Neural Reach</h3>
            <p className="text-white/60 font-medium text-sm relative z-10">This advisory will be instantly pushed to 12,842 active farmer devices via the neural handshake.</p>
          </div>
        </div>
      </div>

      <AnimatePresence>
        {mediaModal.isOpen && (
          <MediaStudio 
            type={mediaModal.type} 
            isOpen={mediaModal.isOpen} 
            onClose={() => setMediaModal({ isOpen: false, type: null })}
            onConfirm={() => console.log('Media synchronized')}
          />
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default Advisories;
