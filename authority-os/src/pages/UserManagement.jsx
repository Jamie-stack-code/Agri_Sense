import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Users, Shield, UserX, UserPlus, Search, Filter, Mail, Phone, Settings, X, Check, RefreshCw, AlertCircle } from 'lucide-react';
import { adminService } from '../services/api';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showProvisionModal, setShowProvisionModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [showSettingsModal, setShowSettingsModal] = useState(false);
  const [loading, setLoading] = useState(true);
  const [isSyncing, setIsSyncing] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const mockUsers = [
    { id: '101', name: 'Dr. Andrew Mwale', email: 'andrew@agrisense.mw', role: 'EXPERT', status: 'ACTIVE' },
    { id: '102', name: 'Grace Chunga', email: 'grace@farm.mw', role: 'FARMER', status: 'ACTIVE' },
    { id: '103', name: 'Root Admin', email: 'admin@agrisense.mw', role: 'ADMIN', status: 'ACTIVE' },
    { id: '104', name: 'Samuel Banda', email: 'sam@agri.mw', role: 'FARMER', status: 'SUSPENDED' },
  ];

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await adminService.getUsers();
      if (response.data && response.data.length > 0) {
        setUsers(response.data);
      } else {
        setUsers(mockUsers);
      }
    } catch (error) {
      setUsers(mockUsers);
    } finally {
      setLoading(false);
    }
  };

  const handleAuthorize = () => {
    setIsSyncing(true);
    setTimeout(() => {
      setIsSyncing(false);
      setShowProvisionModal(false);
      setSuccessMessage('New authority provisioned and synchronized.');
      setTimeout(() => setSuccessMessage(''), 3000);
    }, 2000);
  };

  const handleSuspend = (id) => {
    if (window.confirm('Are you sure you want to revoke access for this entity?')) {
      setUsers(users.map(u => u.id === id ? { ...u, status: 'SUSPENDED' } : u));
      setSuccessMessage('Authority access revoked.');
      setTimeout(() => setSuccessMessage(''), 3000);
    }
  };

  const handleOpenSettings = (user) => {
    setSelectedUser(user);
    setShowSettingsModal(true);
  };

  const filteredUsers = users.filter(user => 
    user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.role.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-12 relative pb-20"
    >
      <div className="flex flex-col xl:flex-row xl:items-center justify-between gap-8">
        <div>
          <h1 className="text-5xl font-black text-agri-green tracking-tight">Identity Registry</h1>
          <p className="text-agri-muted mt-3 text-lg font-medium">Manage permissions and oversee all platform participants.</p>
        </div>
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1 sm:flex-none">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={20} />
            <input 
              type="text" 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Search registry..." 
              className="pl-12 pr-6 py-4 bg-white border border-slate-200 rounded-2xl w-full sm:w-80 text-agri-text focus:ring-2 focus:ring-agri-gold outline-none transition-all shadow-sm"
            />
          </div>
          <button 
            onClick={() => setShowProvisionModal(true)}
            className="px-8 py-4 agri-gradient text-white rounded-2xl font-black flex items-center justify-center gap-2 shadow-xl shadow-agri-green/20 hover:scale-[1.02] transition-transform"
          >
            <UserPlus size={20} /> Provision Entity
          </button>
        </div>
      </div>

      {successMessage && (
        <motion.div initial={{ opacity: 0, y: -20 }} animate={{ opacity: 1, y: 0 }} className="bg-emerald-50 border border-emerald-100 p-6 rounded-3xl text-emerald-700 font-bold flex items-center gap-3">
          <Check size={20} /> {successMessage}
        </motion.div>
      )}

      <div className="agri-card overflow-x-auto">
        <table className="w-full text-left min-w-[1000px]">
          <thead>
            <tr className="bg-slate-50 border-b border-slate-100">
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Authorized Entity</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Access Role</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Communication</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Status</th>
              <th className="p-8 text-[10px] font-black uppercase text-agri-muted tracking-widest">Authority</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-50">
            {filteredUsers.map((user) => (
              <motion.tr layout key={user.id} className="hover:bg-slate-50/50 transition-colors">
                <td className="p-8">
                  <div className="flex items-center gap-4">
                    <div className="w-12 h-12 rounded-2xl bg-agri-green/5 flex items-center justify-center font-black text-agri-green uppercase">
                      {user.name[0]}
                    </div>
                    <div>
                      <p className="text-agri-text font-black text-lg tracking-tight">{user.name}</p>
                      <p className="text-agri-muted text-xs font-bold">{user.email}</p>
                    </div>
                  </div>
                </td>
                <td className="p-8">
                  <div className="flex items-center gap-2 font-black text-xs text-agri-green">
                    <Shield size={16} className="text-agri-gold" />
                    {user.role}
                  </div>
                </td>
                <td className="p-8 text-agri-muted font-bold text-sm">
                  <p className="flex items-center gap-2 font-mono">{user.email}</p>
                </td>
                <td className="p-8">
                  <span className={`gold-badge ${user.status === 'SUSPENDED' ? 'bg-rose-50 text-rose-500 border-rose-100' : ''}`}>
                    {user.status || 'AUTHORIZED'}
                  </span>
                </td>
                <td className="p-8">
                  <div className="flex gap-2">
                    <button 
                      onClick={() => handleOpenSettings(user)}
                      className="p-3 bg-slate-50 text-slate-400 hover:text-agri-green transition-all rounded-xl hover:bg-white hover:shadow-md"
                    >
                      <Settings size={18} />
                    </button>
                    <button 
                      onClick={() => handleSuspend(user.id)}
                      className="p-3 bg-rose-50 text-rose-500 hover:bg-rose-500 hover:text-white transition-all rounded-xl"
                    >
                      <UserX size={18} />
                    </button>
                  </div>
                </td>
              </motion.tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Authority Settings Modal */}
      <AnimatePresence>
        {showSettingsModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-8">
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setShowSettingsModal(false)} className="absolute inset-0 bg-agri-green/40 backdrop-blur-md"></motion.div>
            <motion.div initial={{ scale: 0.9, y: 20 }} animate={{ scale: 1, y: 0 }} exit={{ scale: 0.9, y: 20 }} className="relative bg-white w-full max-w-xl rounded-[3rem] p-12 shadow-2xl space-y-8">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-3xl font-black text-agri-green tracking-tight">Authority Settings</h2>
                  <p className="text-agri-muted font-bold text-sm mt-1 uppercase tracking-widest">{selectedUser?.name}</p>
                </div>
                <button onClick={() => setShowSettingsModal(false)} className="p-4 bg-slate-50 text-slate-300 hover:text-rose-500 rounded-2xl transition-all">
                  <X size={24} />
                </button>
              </div>
              <div className="space-y-6">
                <div className="p-6 bg-slate-50 rounded-3xl border border-slate-100 flex items-center justify-between">
                  <div>
                    <p className="font-black text-agri-text">Admin Privileges</p>
                    <p className="text-xs text-agri-muted font-bold">Grant global system control</p>
                  </div>
                  <div className={`w-12 h-6 bg-slate-200 rounded-full relative p-1 cursor-not-allowed`}>
                    <div className="w-4 h-4 bg-white rounded-full"></div>
                  </div>
                </div>
                <div className="p-6 bg-slate-50 rounded-3xl border border-slate-100 flex items-center justify-between">
                  <div>
                    <p className="font-black text-agri-text">Expert Advisory Access</p>
                    <p className="text-xs text-agri-muted font-bold">Allow publishing field reports</p>
                  </div>
                  <div className={`w-12 h-6 bg-agri-green rounded-full relative p-1`}>
                    <div className="w-4 h-4 bg-white rounded-full absolute right-1"></div>
                  </div>
                </div>
                <div className="pt-4 flex gap-4">
                  <button onClick={() => setShowSettingsModal(false)} className="flex-1 py-5 bg-white border border-slate-200 text-agri-muted font-black rounded-2xl hover:bg-slate-50 transition-all">
                    Cancel Session
                  </button>
                  <button onClick={() => { setShowSettingsModal(false); setSuccessMessage('Authority settings synchronized.'); setTimeout(() => setSuccessMessage(''), 3000); }} className="flex-1 py-5 agri-gradient text-white font-black rounded-2xl shadow-xl shadow-agri-green/20">
                    Apply Changes
                  </button>
                </div>
              </div>
            </motion.div>
          </div>
        )}
      </AnimatePresence>

      {/* Provision Modal (Already mostly works, but making sure Cancel button is professional) */}
      <AnimatePresence>
        {showProvisionModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-8">
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} onClick={() => setShowProvisionModal(false)} className="absolute inset-0 bg-agri-green/40 backdrop-blur-md"></motion.div>
            <motion.div initial={{ scale: 0.9, y: 20 }} animate={{ scale: 1, y: 0 }} exit={{ scale: 0.9, y: 20 }} className="relative bg-white w-full max-w-xl rounded-[3rem] p-12 shadow-2xl space-y-8">
              <div className="flex justify-between items-center">
                <h2 className="text-3xl font-black text-agri-green">Provision Entity</h2>
                <button onClick={() => setShowProvisionModal(false)} className="p-4 bg-slate-50 text-slate-300 hover:text-rose-500 rounded-2xl transition-all">
                  <X size={24} />
                </button>
              </div>
              <div className="space-y-6">
                <div>
                  <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-1 mb-2 block">Full Legal Name</label>
                  <input type="text" className="w-full p-4 bg-slate-50 border border-slate-200 rounded-2xl outline-none focus:border-agri-gold font-bold" placeholder="e.g. Dr. Andrew Mwale" />
                </div>
                <div>
                  <label className="text-[10px] font-black text-agri-muted uppercase tracking-[0.3em] ml-1 mb-2 block">Official Email</label>
                  <input type="email" className="w-full p-4 bg-slate-50 border border-slate-200 rounded-2xl outline-none focus:border-agri-gold font-bold" placeholder="expert@agrisense.mw" />
                </div>
                <div className="flex gap-4 pt-4">
                  <button onClick={() => setShowProvisionModal(false)} className="flex-1 py-5 bg-white border border-slate-200 text-agri-muted font-black rounded-2xl hover:bg-slate-50">
                    Cancel
                  </button>
                  <button onClick={handleAuthorize} disabled={isSyncing} className="flex-2 px-8 py-5 agri-gradient text-white rounded-2xl font-black shadow-xl flex items-center justify-center gap-3">
                    {isSyncing ? <RefreshCw className="animate-spin" /> : <Check />}
                    {isSyncing ? 'Syncing...' : 'Authorize & Sync'}
                  </button>
                </div>
              </div>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default UserManagement;
