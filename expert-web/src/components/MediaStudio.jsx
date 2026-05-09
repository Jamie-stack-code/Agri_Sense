import React, { useState, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Image, Mic, Send, RefreshCw, CheckCircle, Upload, FileCheck, Circle, Square } from 'lucide-react';

const MediaStudio = ({ type, isOpen, onClose, onConfirm }) => {
  const [isProcessing, setIsProcessing] = useState(false);
  const [completed, setCompleted] = useState(false);
  const [isRecording, setIsRecording] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const fileInputRef = useRef(null);

  if (!isOpen) return null;

  const handleUploadClick = () => {
    if (!completed && !isProcessing && !isRecording) {
      fileInputRef.current.click();
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setSelectedFile(file);
      setIsProcessing(true);
      setTimeout(() => {
        setIsProcessing(false);
        setCompleted(true);
      }, 2000);
    }
  };

  const handleToggleRecord = () => {
    if (isRecording) {
      setIsRecording(false);
      setIsProcessing(true);
      setTimeout(() => {
        setIsProcessing(false);
        setCompleted(true);
      }, 2000);
    } else {
      setIsRecording(true);
      setCompleted(false);
    }
  };

  const handleFinalConfirm = () => {
    onConfirm(selectedFile || 'recorded-audio-blob');
    onClose();
    setCompleted(false);
    setSelectedFile(null);
  };

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 md:p-8">
      <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={onClose} className="absolute inset-0 bg-agri-green/40 backdrop-blur-md"></motion.div>
      
      <motion.div initial={{ scale: 0.9, y: 20 }} animate={{ scale: 1, y: 0 }} exit={{ scale: 0.9, y: 20 }} className="relative bg-white w-full max-w-xl rounded-[3rem] p-8 md:p-12 shadow-2xl space-y-10">
        <input type="file" ref={fileInputRef} onChange={handleFileChange} className="hidden" accept={type === 'IMAGE' ? "image/*" : "audio/*"} />

        <div className="flex justify-between items-center border-b border-slate-50 pb-6">
          <div className="flex items-center gap-4">
            <div className="p-3 bg-agri-green/10 text-agri-green rounded-xl">
              {type === 'IMAGE' ? <Image /> : <Mic />}
            </div>
            <h2 className="text-2xl md:text-3xl font-black text-agri-green">{type === 'IMAGE' ? 'Image Studio' : 'Voice Studio'}</h2>
          </div>
          <button onClick={onClose} className="p-3 text-slate-300 hover:text-rose-500 rounded-2xl transition-all">
            <X size={24} />
          </button>
        </div>

        <div className={`h-64 rounded-[2rem] border-2 border-dashed flex flex-col items-center justify-center space-y-6 transition-all overflow-hidden relative
            ${completed ? 'bg-emerald-50 border-emerald-200' : isRecording ? 'bg-rose-50 border-rose-200' : 'bg-slate-50 border-slate-200 hover:border-agri-gold'}`}>
          
          <AnimatePresence mode="wait">
            {completed ? (
              <motion.div key="done" initial={{ scale: 0 }} animate={{ scale: 1 }} className="flex flex-col items-center gap-4 text-emerald-600">
                 <FileCheck size={64} />
                 <p className="font-black uppercase tracking-widest text-sm">Field Stream Ready</p>
              </motion.div>
            ) : isProcessing ? (
              <motion.div key="processing" className="flex flex-col items-center gap-4">
                 <RefreshCw size={48} className="text-agri-gold animate-spin" />
                 <p className="font-black text-agri-muted uppercase tracking-widest text-[10px]">Processing Intelligence...</p>
              </motion.div>
            ) : isRecording ? (
              <motion.div key="recording" className="flex flex-col items-center gap-6">
                 <div className="flex gap-1 items-center h-12">
                   {[1,2,3,4,5,6,7].map(i => (
                     <motion.div 
                       key={i}
                       animate={{ height: [10, 40, 10] }}
                       transition={{ repeat: Infinity, duration: 0.6, delay: i * 0.1 }}
                       className="w-2 bg-rose-500 rounded-full"
                     />
                   ))}
                 </div>
                 <p className="font-black text-rose-500 uppercase tracking-widest text-[10px] animate-pulse">Live Recording...</p>
              </motion.div>
            ) : (
              <motion.div key="idle" className="flex flex-col items-center gap-6 w-full px-12">
                <div className="flex gap-4 w-full">
                  <button 
                    onClick={handleUploadClick}
                    className="flex-1 p-6 bg-white rounded-2xl border border-slate-100 flex flex-col items-center gap-2 hover:border-agri-gold transition-all group"
                  >
                    <Upload className="text-slate-400 group-hover:text-agri-gold" />
                    <span className="text-[10px] font-black uppercase tracking-widest">Upload File</span>
                  </button>
                  {type === 'VOICE' && (
                    <button 
                      onClick={handleToggleRecord}
                      className="flex-1 p-6 bg-white rounded-2xl border border-slate-100 flex flex-col items-center gap-2 hover:border-rose-500 transition-all group"
                    >
                      <Circle className="text-slate-400 group-hover:text-rose-500" fill={isRecording ? "currentColor" : "none"} />
                      <span className="text-[10px] font-black uppercase tracking-widest">Record Live</span>
                    </button>
                  )}
                </div>
                <p className="text-[10px] text-agri-muted font-bold uppercase tracking-widest text-center">Capture field evidence for the national database</p>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        <div className="flex gap-4">
          {isRecording ? (
            <button onClick={handleToggleRecord} className="w-full py-5 bg-rose-500 text-white font-black rounded-2xl shadow-xl flex items-center justify-center gap-3">
              <Square size={20} fill="currentColor" /> Stop & Sync
            </button>
          ) : (
            <>
              <button onClick={onClose} className="flex-1 py-5 bg-white border border-slate-200 text-agri-muted font-black rounded-2xl">Cancel</button>
              <button 
                onClick={handleFinalConfirm}
                disabled={!completed}
                className="flex-2 px-10 py-5 agri-gradient text-white font-black rounded-2xl shadow-xl flex items-center justify-center gap-3 disabled:opacity-50"
              >
                Confirm & Broadcast
              </button>
            </>
          )}
        </div>
      </motion.div>
    </div>
  );
};

export default MediaStudio;
