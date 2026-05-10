import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { MessageSquare, Send, User, Clock, MapPin, Check, X, Shield, Image, Mic, Zap } from 'lucide-react';
import { useLocation } from 'react-router-dom';
import MediaStudio from '../../components/MediaStudio';
import { io } from 'socket.io-client';

const Questions = () => {
  const location = useLocation();
  const [selectedQuestion, setSelectedQuestion] = useState(null);
  const [replyText, setReplyText] = useState('');
  const [mediaModal, setMediaModal] = useState({ isOpen: false, type: null });
  const [questions, setQuestions] = useState([
    { id: 'SB-1', author: 'Samuel Banda', cluster: 'Kasungu', content: 'My maize leaves are turning yellow from the bottom up. Is this Nitrogen deficiency?', time: '12m ago', status: 'PENDING' },
    { id: 'JP-2', author: 'Joyce Phiri', cluster: 'Lilongwe', content: 'I found these small eggs on the underside of my tobacco leaves. Should I spray immediately?', time: '1h ago', status: 'PENDING' },
    { id: 'PM-3', author: 'Peter Mwale', cluster: 'Zomba', content: 'Best time to apply top dressing for late-planted maize in the South?', time: '3h ago', status: 'REPLIED' },
  ]);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    const socket = io('http://localhost:5000');

    socket.on('NEW_FARMER_QUESTION', (newQuestion) => {
      const formatted = {
        id: newQuestion.id,
        author: newQuestion.authorName || 'New Farmer',
        cluster: 'Field Cluster',
        content: newQuestion.content,
        aiResponse: newQuestion.aiResponse,
        time: 'Just now',
        status: newQuestion.status || 'PENDING'
      };
      setQuestions(prev => [formatted, ...prev]);
      setNotification(`New Inquiry: ${formatted.author} (${formatted.status})`);
      setTimeout(() => setNotification(null), 5000);
    });

    return () => socket.disconnect();
  }, []);

  useEffect(() => {
    if (location.state?.farmerId) {
      const found = questions.find(q => q.id === location.state.farmerId);
      if (found) setSelectedQuestion(found);
    }
  }, [location.state, questions]);

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="flex flex-col lg:flex-row h-[calc(100vh-140px)] gap-10 relative">
      <AnimatePresence>
        {notification && (
          <motion.div initial={{ opacity: 0, y: -50 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -50 }} className="fixed top-24 right-10 z-[100] bg-agri-green text-white p-6 rounded-[2rem] shadow-2xl flex items-center gap-4 border border-white/20">
             <div className="w-10 h-10 bg-agri-gold rounded-full flex items-center justify-center text-agri-green"><Zap size={20} fill="currentColor" /></div>
             <p className="font-black text-sm">{notification}</p>
          </motion.div>
        )}
      </AnimatePresence>

      <div className="lg:w-1/3 flex flex-col space-y-6">
        <div className="flex items-center justify-between">
          <h2 className="text-3xl font-black text-agri-green tracking-tight">Inquiry Queue</h2>
          <span className="gold-badge">Live Feed</span>
        </div>
        
        <div className="flex-1 overflow-y-auto space-y-4 pr-2 custom-scrollbar">
          {questions.map((q) => (
            <motion.div 
              key={q.id}
              onClick={() => setSelectedQuestion(q)}
              className={`p-6 rounded-[2rem] border-2 cursor-pointer transition-all ${selectedQuestion?.id === q.id ? 'border-agri-gold bg-white shadow-xl shadow-agri-gold/5' : 'border-slate-100 bg-white hover:border-agri-green/30'}`}
            >
              <div className="flex justify-between items-start mb-4">
                <div className="w-10 h-10 rounded-full bg-agri-green/10 flex items-center justify-center text-agri-green font-black">
                  {q.author[0]}
                </div>
                <span className={`text-[10px] font-black tracking-widest uppercase ${q.status === 'PENDING' ? 'text-rose-500' : 'text-emerald-500'}`}>
                  {q.status}
                </span>
              </div>
              <p className="font-bold text-agri-text leading-snug line-clamp-2 mb-4">{q.content}</p>
              <div className="flex items-center gap-3 text-[10px] font-black text-agri-muted uppercase tracking-widest">
                <span className="flex items-center gap-1"><MapPin size={12} /> {q.cluster}</span>
                <span className="flex items-center gap-1"><Clock size={12} /> {q.time}</span>
              </div>
            </motion.div>
          ))}
        </div>
      </div>

      <div className="lg:w-2/3">
        <AnimatePresence mode="wait">
          {selectedQuestion ? (
            <motion.div key={selectedQuestion.id} initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: -20 }} className="h-full agri-card p-12 flex flex-col">
              <div className="flex justify-between items-start border-b border-slate-50 pb-8 mb-10">
                <div className="flex items-center gap-6">
                  <div className="w-16 h-16 rounded-3xl bg-agri-green flex items-center justify-center text-white text-2xl font-black">{selectedQuestion.author[0]}</div>
                  <div>
                    <h3 className="text-2xl font-black text-agri-green tracking-tight">{selectedQuestion.author}</h3>
                    <p className="text-agri-gold font-bold uppercase text-xs tracking-widest mt-1">Verified Farmer | {selectedQuestion.cluster}</p>
                  </div>
                </div>
                <button onClick={() => setSelectedQuestion(null)} className="p-3 text-slate-300 hover:text-rose-500 transition-colors"><X size={24} /></button>
              </div>

              <div className="flex-1 overflow-y-auto space-y-10 pr-4 custom-scrollbar mb-8">
                <div className="bg-slate-50 p-8 rounded-[2.5rem] border border-slate-100 max-w-[80%]">
                  <p className="text-lg font-bold text-agri-text leading-relaxed">{selectedQuestion.content}</p>
                </div>

                {selectedQuestion.aiResponse && (
                  <div className="bg-emerald-50 p-8 rounded-[2.5rem] border border-emerald-100 max-w-[80%] self-end ml-auto">
                    <p className="text-[10px] font-black text-emerald-600 uppercase tracking-widest mb-2 flex items-center gap-2">
                      <Zap size={12} fill="currentColor" /> Gemini AI Response
                    </p>
                    <p className="text-lg font-bold text-agri-text leading-relaxed italic">"{selectedQuestion.aiResponse}"</p>
                  </div>
                )}
              </div>

              <div className="relative pt-6 border-t border-slate-50">
                <textarea 
                  value={replyText}
                  onChange={(e) => setReplyText(e.target.value)}
                  placeholder="Craft your professional agronomic advice..."
                  className="w-full p-8 bg-slate-50 border border-slate-100 rounded-[2rem] min-h-[150px] outline-none focus:border-agri-gold transition-all font-medium text-agri-text resize-none"
                />
                <div className="absolute bottom-10 right-10 flex gap-4">
                  <button onClick={() => setMediaModal({ isOpen: true, type: 'VOICE' })} className="p-4 bg-white border border-slate-200 text-agri-muted rounded-2xl hover:text-agri-green transition-all shadow-sm"><Mic size={20} /></button>
                  <button onClick={() => setMediaModal({ isOpen: true, type: 'IMAGE' })} className="p-4 bg-white border border-slate-200 text-agri-muted rounded-2xl hover:text-agri-green transition-all shadow-sm"><Image size={20} /></button>
                  <button 
                    onClick={() => { alert('Advice Synchronized and Sent to Farmer App.'); setReplyText(''); }}
                    className="px-8 py-4 agri-gradient text-white rounded-2xl font-black shadow-xl shadow-agri-green/20 flex items-center gap-2 hover:scale-[1.02] transition-all"
                  >
                    Send Response
                  </button>
                </div>
              </div>
            </motion.div>
          ) : (
            <div className="h-full agri-card p-12 flex flex-col items-center justify-center text-center space-y-6">
              <div className="w-24 h-24 bg-slate-50 rounded-full flex items-center justify-center text-slate-300"><MessageSquare size={48} /></div>
              <h3 className="text-2xl font-black text-agri-green">Workspace Inactive</h3>
              <p className="text-agri-muted font-medium max-w-sm">Select an inquiry from the queue to provide professional field intelligence.</p>
            </div>
          )}
        </AnimatePresence>
      </div>

      <AnimatePresence>
        {mediaModal.isOpen && (
          <MediaStudio 
            type={mediaModal.type} 
            isOpen={mediaModal.isOpen} 
            onClose={() => setMediaModal({ isOpen: false, type: null })}
            onConfirm={() => console.log('Media attached to inquiry')}
          />
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default Questions;
