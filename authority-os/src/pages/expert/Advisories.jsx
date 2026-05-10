import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { BookOpen, Send, Type, Image, Mic, Zap, AlertTriangle, CheckCircle, RefreshCw, History, MapPin } from 'lucide-react';
import MediaStudio from '../../components/MediaStudio';
import { expertService } from '../../services/api';

const Advisories = () => {
  const [isPublishing, setIsPublishing] = useState(false);
  const [success, setSuccess] = useState(false);
  const [advisoryType, setAdvisoryType] = useState('NORMAL');
  const [category, setCategory] = useState('DISEASE_PEST');
  const [district, setDistrict] = useState('National');
  const [mediaModal, setMediaModal] = useState({ isOpen: false, type: null });
  
  const [formData, setFormData] = useState({
    title: '',
    titleChichewa: '',
    content: '',
    contentChichewa: ''
  });

  const [history, setHistory] = useState([]);
  const expert = JSON.parse(localStorage.getItem('agri_authority_user') || '{}');

  useEffect(() => {
    fetchHistory();
    const prefill = sessionStorage.getItem('prefill_advisory');
    if (prefill) {
      const data = JSON.parse(prefill);
      setFormData(prev => ({ ...prev, title: data.title, content: data.content }));
      setCategory(data.category || 'DISEASE_PEST');
      sessionStorage.removeItem('prefill_advisory');
    }
  }, []);

  const fetchHistory = async () => {
    try {
      const res = await expertService.getAdvisoryHistory(expert.id);
      setHistory(res.data);
    } catch (e) { console.error(e); }
  };

  const handlePublish = async () => {
    if (!formData.title || !formData.content) return alert('Headline and Intelligence Report are required.');
    
    setIsPublishing(true);
    try {
      await expertService.createAdvisory({
        ...formData,
        type: 'TEXT',
        category,
        district: district === 'National' ? null : district,
        expertId: expert.id
      });
      setIsPublishing(false);
      setSuccess(true);
      setFormData({ title: '', titleChichewa: '', content: '', contentChichewa: '' });
      fetchHistory();
      setTimeout(() => setSuccess(false), 3000);
    } catch (e) {
      setIsPublishing(false);
      alert('Broadcast failed. Check connection.');
    }
  };

  const reuseAdvisory = (snapshot) => {
    const data = JSON.parse(snapshot);
    setFormData({
      title: data.title,
      titleChichewa: data.titleChichewa || '',
      content: data.content,
      contentChichewa: data.contentChichewa || ''
    });
    setCategory(data.category);
    setDistrict(data.district || 'National');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-6xl mx-auto space-y-12 pb-20"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Disease/Pest Advisory</h1>
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
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-4 block">Headline (English)</label>
                <input 
                  type="text" 
                  value={formData.title}
                  onChange={(e) => setFormData({...formData, title: e.target.value})}
                  placeholder="e.g. Critical Pest Alert: Fall Armyworm" 
                  className="w-full p-6 bg-slate-50 border border-slate-100 rounded-3xl text-xl font-black text-agri-text focus:border-agri-gold outline-none transition-all" 
                />
              </div>
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-4 block">Headline (Chichewa)</label>
                <input 
                  type="text" 
                  value={formData.titleChichewa}
                  onChange={(e) => setFormData({...formData, titleChichewa: e.target.value})}
                  placeholder="Machenjezo: Mbozi ya Chilombo" 
                  className="w-full p-6 bg-slate-50 border border-slate-100 rounded-3xl text-xl font-black text-agri-text focus:border-agri-gold outline-none transition-all" 
                />
              </div>
            </div>

            <div>
              <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-4 block">Intelligence Report (English)</label>
              <textarea 
                value={formData.content}
                onChange={(e) => setFormData({...formData, content: e.target.value})}
                placeholder="Provide detailed field guidance..." 
                className="w-full p-8 bg-slate-50 border border-slate-100 rounded-[2.5rem] min-h-[200px] text-lg font-medium text-agri-text focus:border-agri-gold outline-none transition-all resize-none" 
              />
            </div>

            <div>
              <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-2 mb-4 block">Intelligence Report (Chichewa)</label>
              <textarea 
                value={formData.contentChichewa}
                onChange={(e) => setFormData({...formData, contentChichewa: e.target.value})}
                placeholder="Perekani malangizo kwa alimi..." 
                className="w-full p-8 bg-slate-50 border border-slate-100 rounded-[2.5rem] min-h-[200px] text-lg font-medium text-agri-text focus:border-agri-gold outline-none transition-all resize-none" 
              />
            </div>
          </div>

          <div className="agri-card p-10">
            <h3 className="text-xl font-black text-agri-green flex items-center gap-3 mb-8">
              <History size={24} /> Publication History
            </h3>
            <div className="space-y-6">
              {history.map((item) => {
                const snapshot = JSON.parse(item.snapshot);
                return (
                  <div key={item.id} className="p-6 bg-slate-50 rounded-3xl border border-slate-100 flex items-center justify-between group">
                    <div className="space-y-1">
                      <p className="font-black text-agri-text">{snapshot.title}</p>
                      <p className="text-[10px] font-black text-agri-muted uppercase tracking-widest">
                        {new Date(item.createdAt).toLocaleDateString()} | {snapshot.category}
                      </p>
                    </div>
                    <button 
                      onClick={() => reuseAdvisory(item.snapshot)}
                      className="p-4 bg-white text-agri-green rounded-2xl font-black text-[10px] uppercase tracking-widest opacity-0 group-hover:opacity-100 transition-all border border-agri-green/20 hover:bg-agri-green hover:text-white"
                    >
                      Reuse
                    </button>
                  </div>
                );
              })}
              {history.length === 0 && <p className="text-center py-10 text-agri-muted font-bold italic">No publication history found.</p>}
            </div>
          </div>
        </div>

        <div className="space-y-8">
          <div className="agri-card p-10 space-y-8">
            <h3 className="text-xl font-black text-agri-green border-b border-slate-50 pb-6">Studio Settings</h3>
            
            <div className="space-y-6">
              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-widest mb-3 block">Category</label>
                <select 
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full p-4 bg-slate-50 border border-slate-100 rounded-2xl font-black text-xs text-agri-text outline-none"
                >
                  <option value="DISEASE_PEST">DISEASE/PEST</option>
                  <option value="WEATHER">WEATHER</option>
                  <option value="CROP">CROP GUIDANCE</option>
                  <option value="FERTILIZER">FERTILIZER</option>
                </select>
              </div>

              <div>
                <label className="text-[10px] font-black text-agri-muted uppercase tracking-widest mb-3 block">District Scope</label>
                <div className="relative">
                  <MapPin className="absolute left-4 top-1/2 -translate-y-1/2 text-agri-muted" size={16} />
                  <select 
                    value={district}
                    onChange={(e) => setDistrict(e.target.value)}
                    className="w-full p-4 pl-12 bg-slate-50 border border-slate-100 rounded-2xl font-black text-xs text-agri-text outline-none"
                  >
                    <option value="National">All Malawi (National)</option>
                    <option value="Lilongwe">Lilongwe</option>
                    <option value="Blantyre">Blantyre</option>
                    <option value="Mzuzu">Mzuzu</option>
                    <option value="Kasungu">Kasungu</option>
                  </select>
                </div>
              </div>

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
            </div>
          </div>

          <div className="agri-gradient rounded-[3rem] p-10 text-white space-y-6 relative overflow-hidden group">
            <Zap size={120} className="absolute -right-10 -bottom-10 opacity-10 group-hover:rotate-12 transition-transform duration-1000" />
            <h3 className="text-2xl font-black relative z-10 leading-tight">Neural Reach</h3>
            <p className="text-white/60 font-medium text-sm relative z-10">This advisory will be instantly pushed to active farmer devices via the neural handshake.</p>
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
