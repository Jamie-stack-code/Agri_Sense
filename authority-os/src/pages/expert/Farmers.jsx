import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Users, Search, Filter, MapPin, Phone, Mail, ChevronRight, MessageSquare, ShieldCheck } from 'lucide-react';
import { Link } from 'react-router-dom';

const Farmers = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const mockFarmers = [
    { id: 'F001', name: 'Samuel Banda', location: 'Kasungu Cluster', crops: 'Maize, Soybeans', status: 'PREMIUM', phone: '+265 991 000 001' },
    { id: 'F002', name: 'Joyce Phiri', location: 'Lilongwe South', crops: 'Groundnuts', status: 'FREE', phone: '+265 991 000 002' },
    { id: 'F003', name: 'Peter Mwale', location: 'Blantyre West', crops: 'Tobacco, Maize', status: 'PREMIUM', phone: '+265 991 000 003' },
    { id: 'F004', name: 'Grace Chunga', location: 'Mzimba North', crops: 'Potatoes', status: 'PREMIUM', phone: '+265 991 000 004' },
  ];

  const filteredFarmers = mockFarmers.filter(f => 
    f.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    f.location.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-12 pb-20"
    >
      <div className="flex flex-col xl:flex-row xl:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Field Registry</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Monitoring and supporting all participants in your jurisdiction.</p>
        </div>
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1 sm:flex-none">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={20} />
            <input 
              type="text" 
              placeholder="Search by name or cluster..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-12 pr-6 py-4 bg-white border border-slate-200 rounded-2xl w-full sm:w-96 text-agri-text focus:ring-2 focus:ring-agri-gold outline-none shadow-sm"
            />
          </div>
          <button className="px-8 py-4 bg-white border border-slate-200 text-agri-green rounded-2xl font-black flex items-center justify-center gap-2 hover:bg-slate-50 transition-all shadow-sm">
            <Filter size={20} />
            Filter Clusters
          </button>
        </div>
      </div>

      <div className="agri-card overflow-x-auto">
        <table className="w-full text-left min-w-[1000px]">
          <thead>
            <tr className="bg-slate-50 border-b border-slate-100">
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Farmer Identity</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Location</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Primary Crops</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Tier Status</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Interaction</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-50">
            {filteredFarmers.map((farmer) => (
              <motion.tr layout key={farmer.id} className="hover:bg-slate-50/50 transition-colors group">
                <td className="p-8">
                  <div className="flex items-center gap-4">
                    <div className="w-12 h-12 rounded-2xl bg-agri-green/5 flex items-center justify-center font-black text-agri-green text-lg">
                      {farmer.name[0]}
                    </div>
                    <div>
                      <p className="text-agri-text font-black text-lg tracking-tight">{farmer.name}</p>
                      <p className="text-agri-muted text-xs font-bold font-mono tracking-tighter">{farmer.phone}</p>
                    </div>
                  </div>
                </td>
                <td className="p-8">
                  <div className="flex items-center gap-2 font-bold text-agri-muted">
                    <MapPin size={16} className="text-agri-gold" />
                    {farmer.location}
                  </div>
                </td>
                <td className="p-8">
                  <div className="flex items-center gap-2">
                    <ShieldCheck size={16} className="text-agri-green" />
                    <span className="font-bold text-agri-text text-sm">{farmer.crops}</span>
                  </div>
                </td>
                <td className="p-8">
                  <span className={`gold-badge ${farmer.status === 'FREE' ? 'bg-slate-100 text-slate-500 border-slate-200' : ''}`}>
                    {farmer.status}
                  </span>
                </td>
                <td className="p-8">
                  <Link 
                    to="/questions" 
                    className="p-4 bg-white border border-slate-100 text-agri-green rounded-2xl shadow-sm hover:shadow-md hover:border-agri-green transition-all inline-flex items-center gap-2 group-hover:scale-105"
                  >
                    <MessageSquare size={18} />
                    <span className="font-black text-xs uppercase tracking-widest">Connect</span>
                  </Link>
                </td>
              </motion.tr>
            ))}
          </tbody>
        </table>
      </div>
    </motion.div>
  );
};

export default Farmers;
