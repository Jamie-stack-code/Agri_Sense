import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { AlertTriangle, ShieldCheck, Zap, Activity, Info, Check, X, Search, Filter } from 'lucide-react';

const DiseaseReports = () => {
  const [selectedReport, setSelectedReport] = useState(null);

  const reports = [
    { id: 'DR-101', farmer: 'Samuel Banda', crop: 'Maize', disease: 'Fall Armyworm', confidence: '94%', severity: 'HIGH', time: '14m ago', img: 'https://images.unsplash.com/photo-1599940824399-b87987ceb72a?auto=format&fit=crop&q=80&w=400' },
    { id: 'DR-102', farmer: 'Joyce Phiri', crop: 'Tobacco', disease: 'Blue Mold', confidence: '82%', severity: 'MEDIUM', time: '1h ago', img: 'https://images.unsplash.com/photo-1530507629858-e4977d30e9e0?auto=format&fit=crop&q=80&w=400' },
    { id: 'DR-103', farmer: 'Peter Mwale', crop: 'Maize', disease: 'Maize Lethal Necrosis', confidence: '98%', severity: 'CRITICAL', time: '3h ago', img: 'https://images.unsplash.com/photo-1592919016383-496350319ca3?auto=format&fit=crop&q=80&w=400' },
  ];

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="space-y-12 pb-20"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Diagnostic Queue</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Validate and verify AI-detected field disease reports.</p>
        </div>
        <div className="flex gap-4">
          <div className="p-4 bg-white border border-slate-200 rounded-2xl flex items-center gap-3">
             <Activity className="text-agri-gold" size={20} />
             <span className="font-black text-xs uppercase tracking-widest text-agri-text">AI Accuracy: 98.4%</span>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {reports.map((report) => (
          <motion.div 
            key={report.id}
            layoutId={report.id}
            onClick={() => setSelectedReport(report)}
            className="agri-card overflow-hidden group cursor-pointer"
          >
            <div className="relative h-64 overflow-hidden">
              <img src={report.img} alt="Field Report" className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" />
              <div className="absolute top-6 right-6">
                <span className={`px-4 py-2 rounded-full font-black text-[10px] tracking-widest uppercase border backdrop-blur-md ${report.severity === 'CRITICAL' ? 'bg-rose-500/80 text-white border-white/20' : 'bg-white/80 text-agri-text border-slate-200'}`}>
                  {report.severity}
                </span>
              </div>
              <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity flex items-end p-8">
                <p className="text-white font-black text-sm uppercase tracking-widest">View Lab Analysis</p>
              </div>
            </div>
            <div className="p-8 space-y-6">
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="text-xl font-black text-agri-green">{report.disease}</h3>
                  <p className="text-agri-muted font-bold text-xs uppercase tracking-widest mt-1">{report.crop} | {report.farmer}</p>
                </div>
                <div className="text-right">
                  <p className="text-agri-gold font-black text-lg">{report.confidence}</p>
                  <p className="text-[8px] font-black uppercase tracking-widest text-agri-muted">AI Confidence</p>
                </div>
              </div>
              <div className="flex items-center gap-2 text-[10px] font-black text-agri-muted uppercase tracking-widest pt-4 border-t border-slate-50">
                <Zap size={14} className="text-agri-gold" />
                Detected {report.time}
              </div>
            </div>
          </motion.div>
        ))}
      </div>

      <AnimatePresence>
        {selectedReport && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 md:p-8">
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setSelectedReport(null)} className="absolute inset-0 bg-agri-green/60 backdrop-blur-lg"></motion.div>
            <motion.div 
              layoutId={selectedReport.id}
              className="relative bg-white w-full max-w-4xl rounded-[3rem] overflow-hidden shadow-2xl flex flex-col md:flex-row max-h-[90vh]"
            >
              <div className="md:w-1/2 relative h-64 md:h-auto">
                <img src={selectedReport.img} className="w-full h-full object-cover" alt="Disease" />
                <div className="absolute inset-0 bg-agri-green/20 mix-blend-overlay"></div>
              </div>
              <div className="md:w-1/2 p-8 md:p-12 overflow-y-auto custom-scrollbar flex flex-col">
                <div className="flex justify-between items-start mb-8">
                  <div>
                    <h2 className="text-3xl font-black text-agri-green tracking-tight">{selectedReport.disease}</h2>
                    <p className="text-agri-gold font-bold uppercase text-xs tracking-widest mt-2">{selectedReport.crop} Diagnostic Analysis</p>
                  </div>
                  <button onClick={() => setSelectedReport(null)} className="p-3 bg-slate-50 text-slate-300 hover:text-rose-500 rounded-2xl">
                    <X size={24} />
                  </button>
                </div>

                <div className="space-y-8 flex-1">
                  <div className="p-6 bg-slate-50 rounded-3xl space-y-4">
                    <h4 className="text-[10px] font-black text-agri-muted uppercase tracking-widest flex items-center gap-2">
                      <Zap size={14} className="text-agri-gold" /> AI Observation
                    </h4>
                    <p className="font-bold text-agri-text leading-relaxed italic">
                      "Image shows characteristic irregular-shaped holes in the whorl and fresh frass. 94% probability of Spodoptera frugiperda (Fall Armyworm)."
                    </p>
                  </div>

                  <div className="space-y-4">
                    <h4 className="text-[10px] font-black text-agri-muted uppercase tracking-widest">Expert Verification</h4>
                    <div className="grid grid-cols-2 gap-4">
                      <button 
                        onClick={() => { alert('Report Verified. Notification sent to local clusters.'); setSelectedReport(null); }}
                        className="p-6 bg-agri-green text-white rounded-3xl font-black text-xs uppercase tracking-widest shadow-xl shadow-agri-green/20 flex flex-col items-center gap-3 hover:scale-105 transition-all"
                      >
                        <ShieldCheck size={28} />
                        Confirm AI
                      </button>
                      <button 
                        onClick={() => { alert('Diagnosis Corrected. AI model notified.'); setSelectedReport(null); }}
                        className="p-6 bg-rose-50 text-rose-500 rounded-3xl font-black text-xs uppercase tracking-widest border border-rose-100 flex flex-col items-center gap-3 hover:scale-105 transition-all"
                      >
                        <AlertTriangle size={28} />
                        Reject / Correct
                      </button>
                    </div>
                  </div>
                </div>

                <div className="mt-10 pt-8 border-t border-slate-50 text-[10px] font-black text-agri-muted uppercase tracking-widest flex items-center justify-between">
                  <span>Report ID: {selectedReport.id}</span>
                  <span className="text-agri-gold flex items-center gap-1 cursor-pointer hover:underline">
                    <Info size={12} /> View Full Meta-Data
                  </span>
                </div>
              </div>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default DiseaseReports;
