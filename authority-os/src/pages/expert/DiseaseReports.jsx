import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { AlertTriangle, ShieldCheck, Zap, Activity, Info, Check, X, Search, Filter, Sprout, Bug } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { expertService } from '../../services/api';
import { io } from 'socket.io-client';

const DiseaseReports = () => {
  const [reports, setReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [recommendation, setRecommendation] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchReports();
    const socket = io('http://localhost:5000');
    socket.on('NEW_DIAGNOSTIC_REPORT', () => fetchReports());
    return () => socket.disconnect();
  }, []);

  const fetchReports = async () => {
    try {
      const response = await expertService.getDiseaseReports();
      setReports(response.data);
    } catch (error) {
      console.error('Failed to fetch reports', error);
    }
  };

  const handleRecommend = async () => {
    if (!recommendation) return;
    setIsSubmitting(true);
    try {
      await expertService.submitRecommendation({
        reportId: selectedReport.id,
        expertId: JSON.parse(localStorage.getItem('user'))?.id,
        recommendation
      });
      setSelectedReport(null);
      setRecommendation('');
      fetchReports();
    } catch (error) {
      console.error('Failed to submit recommendation', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="space-y-12 pb-20"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Diagnostic Queue</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Validate AI-detected field diseases and provide expert recommendations.</p>
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
              <img src={report.imageUrl} alt="Field Report" className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" />
              <div className="absolute top-6 right-6">
                <span className={`px-4 py-2 rounded-full font-black text-[10px] tracking-widest uppercase border backdrop-blur-md ${report.status === 'PENDING' ? 'bg-amber-500/80 text-white border-white/20' : 'bg-agri-green/80 text-white border-white/20'}`}>
                  {report.status}
                </span>
              </div>
            </div>
            <div className="p-8 space-y-6">
              <div>
                <h3 className="text-xl font-black text-agri-green">{report.aiDiagnosis || 'Awaiting Analysis'}</h3>
                <p className="text-agri-muted font-bold text-xs uppercase tracking-widest mt-1">{report.cropType} | {report.farmer?.name || 'Unknown Farmer'}</p>
              </div>
              <div className="flex items-center gap-2 text-[10px] font-black text-agri-muted uppercase tracking-widest pt-4 border-t border-slate-50">
                <Zap size={14} className="text-agri-gold" />
                Captured {new Date(report.createdAt).toLocaleString()}
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
                <img src={selectedReport.imageUrl} className="w-full h-full object-cover" alt="Disease" />
                <div className="absolute inset-0 bg-agri-green/20 mix-blend-overlay"></div>
              </div>
              <div className="md:w-1/2 p-8 md:p-12 overflow-y-auto custom-scrollbar flex flex-col">
                <div className="flex justify-between items-start mb-8">
                  <div>
                    <h2 className="text-3xl font-black text-agri-green tracking-tight">{selectedReport.aiDiagnosis}</h2>
                    <p className="text-agri-gold font-bold uppercase text-xs tracking-widest mt-2">{selectedReport.cropType} Field Analysis</p>
                  </div>
                  <button onClick={() => setSelectedReport(null)} className="p-3 bg-slate-50 text-slate-300 hover:text-rose-500 rounded-2xl">
                    <X size={24} />
                  </button>
                </div>

                <div className="space-y-8 flex-1">
                  <div className="p-6 bg-slate-50 rounded-3xl space-y-4 border border-slate-100">
                    <h4 className="text-[10px] font-black text-agri-muted uppercase tracking-widest flex items-center gap-2">
                      <Zap size={14} className="text-agri-gold" /> AI Plant Doctor Analysis
                    </h4>
                    <p className="font-bold text-agri-text leading-relaxed italic">
                      "{selectedReport.aiDiagnosis}"
                    </p>
                  </div>

                  <div className="space-y-4">
                    <h4 className="text-[10px] font-black text-agri-muted uppercase tracking-widest">Expert Recommendation (Crop & Pest)</h4>
                    <textarea 
                      value={recommendation}
                      onChange={(e) => setRecommendation(e.target.value)}
                      placeholder="Provide specific advice for both crop management and pest control..."
                      className="w-full p-6 bg-slate-50 border border-slate-200 rounded-3xl min-h-[150px] focus:ring-2 focus:ring-agri-green outline-none font-medium text-agri-text"
                    />
                    
                    <div className="flex gap-4">
                      <button 
                        onClick={handleRecommend}
                        disabled={isSubmitting || !recommendation}
                        className="flex-1 py-5 bg-agri-green text-white rounded-3xl font-black text-xs uppercase tracking-widest shadow-xl shadow-agri-green/20 flex items-center justify-center gap-3 hover:scale-[1.02] transition-all disabled:opacity-50"
                      >
                        {isSubmitting ? <RefreshCw className="animate-spin" /> : <ShieldCheck size={20} />}
                        Send to Farmer
                      </button>
                      <button 
                        onClick={() => {
                           sessionStorage.setItem('prefill_advisory', JSON.stringify({
                            title: `Outbreak Alert: ${selectedReport.aiDiagnosis}`,
                            content: recommendation || `Confirmed ${selectedReport.aiDiagnosis} in ${selectedReport.cropType}.`,
                            category: 'DISEASE_PEST'
                          }));
                          navigate('/advisories');
                        }}
                        className="px-8 py-5 bg-agri-gold text-agri-green rounded-3xl font-black text-xs uppercase tracking-widest shadow-lg hover:scale-[1.02] transition-all"
                      >
                        Global Advisory
                      </button>
                    </div>
                  </div>
                </div>

                <div className="mt-10 pt-8 border-t border-slate-50 text-[10px] font-black text-agri-muted uppercase tracking-widest flex items-center justify-between">
                  <span>Report ID: {selectedReport.id.substring(0,8)}</span>
                  <span>{new Date(selectedReport.createdAt).toLocaleDateString()}</span>
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
