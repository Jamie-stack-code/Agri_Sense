import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import UserManagement from './pages/UserManagement';
import SubscriptionManagement from './pages/SubscriptionManagement';
import SystemSettings from './pages/SystemSettings';
import { Menu } from 'lucide-react';

import LoginPage from './pages/LoginPage';
import OtpPage from './pages/OtpPage';
import RoleSelectionPage from './pages/RoleSelectionPage';
import RegisterPage from './pages/RegisterPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage';

// Expert Pages
import Farmers from './pages/expert/Farmers';
import Questions from './pages/expert/Questions';
import Advisories from './pages/expert/Advisories';
import DiseaseReports from './pages/expert/DiseaseReports';

function App() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [user, setUser] = useState(() => {
    try {
      const saved = localStorage.getItem('agri_portal_user');
      return saved && saved !== 'undefined' ? JSON.parse(saved) : null;
    } catch (e) {
      return null;
    }
  });
  const [authEmail, setAuthEmail] = useState('');
  const [selectedRole, setSelectedRole] = useState(null);
  const [step, setStep] = useState(user ? 'dashboard' : 'choice');

  const handleLogout = () => {
    localStorage.removeItem('agri_portal_token');
    localStorage.removeItem('agri_portal_user');
    setUser(null);
    setStep('choice');
  };

  const handleVerified = () => {
    const savedUser = JSON.parse(localStorage.getItem('agri_portal_user'));
    setUser(savedUser);
    setStep('dashboard');
  };

  if (step === 'choice') return <RoleSelectionPage onSelect={(role) => { setSelectedRole(role); setStep('login'); }} />;
  if (step === 'register') return (
    <RegisterPage 
      role={selectedRole} 
      onBack={() => setStep('login')} 
      onRegistered={(email) => { setAuthEmail(email); setStep('otp'); }}
    />
  );
  if (step === 'login') return (
    <LoginPage 
      onOtpRequested={(email) => { setAuthEmail(email); setStep('otp'); }} 
      onRegister={() => setStep('register')} 
      onForgotPassword={() => setStep('forgot')}
      onVerified={handleVerified}
    />
  );
  if (step === 'otp') return <OtpPage email={authEmail} onVerified={handleVerified} />;
  if (step === 'forgot') return <ForgotPasswordPage onBack={() => setStep('login')} />;

  return (
      <div className="flex min-h-screen bg-agri-bg font-inter w-full overflow-x-hidden">
        <Sidebar 
          user={user}
          isOpen={isSidebarOpen} 
          toggleSidebar={() => setIsSidebarOpen(!isSidebarOpen)} 
          onLogout={handleLogout}
        />
        
        <main className="flex-1 min-w-0 min-h-screen flex flex-col relative overflow-x-hidden">
          <header className="lg:hidden h-20 bg-white border-b border-slate-100 flex items-center justify-between px-8 sticky top-0 z-30">
            <h1 className="text-xl font-black text-agri-green tracking-tighter">AGRI-SENSE</h1>
            <button onClick={() => setIsSidebarOpen(true)} className="p-3 bg-slate-50 text-agri-green rounded-xl">
              <Menu size={24} />
            </button>
          </header>

          <div className="flex-1 p-6 md:p-10 lg:p-12 overflow-y-auto max-w-[1600px] mx-auto w-full">
            <Routes>
              <Route path="/" element={<Dashboard user={user} />} />
              <Route path="/users" element={<UserManagement />} />
              <Route path="/subscriptions" element={<SubscriptionManagement />} />
              <Route path="/settings" element={<SystemSettings />} />
              
              {/* Expert Specific Routes */}
              <Route path="/farmers" element={<Farmers />} />
              <Route path="/questions" element={<Questions />} />
              <Route path="/advisories" element={<Advisories />} />
              <Route path="/reports" element={<DiseaseReports />} />
              
              <Route path="*" element={<Navigate to="/" />} />
            </Routes>
          </div>
        </main>
      </div>
  );
}

export default App;
