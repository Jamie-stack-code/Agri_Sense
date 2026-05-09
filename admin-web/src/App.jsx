import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import UserManagement from './pages/UserManagement';
import SubscriptionManagement from './pages/SubscriptionManagement';
import SystemSettings from './pages/SystemSettings';
import { Menu } from 'lucide-react';

function App() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  return (
    <Router>
      <div className="flex min-h-screen bg-agri-bg font-inter w-full overflow-x-hidden">
        <Sidebar isOpen={isSidebarOpen} toggleSidebar={() => setIsSidebarOpen(!isSidebarOpen)} />
        
        <main className="flex-1 min-w-0 min-h-screen flex flex-col relative overflow-x-hidden">
          {/* Mobile Header */}
          <header className="lg:hidden h-20 bg-white border-b border-slate-100 flex items-center justify-between px-8 sticky top-0 z-30">
            <h1 className="text-xl font-black text-agri-green tracking-tighter">AGRI-SENSE</h1>
            <button 
              onClick={() => setIsSidebarOpen(true)}
              className="p-3 bg-slate-50 text-agri-green rounded-xl"
            >
              <Menu size={24} />
            </button>
          </header>

          <div className="flex-1 p-6 md:p-10 lg:p-12 overflow-y-auto max-w-[1600px] mx-auto w-full">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/users" element={<UserManagement />} />
              <Route path="/subscriptions" element={<SubscriptionManagement />} />
              <Route path="/settings" element={<SystemSettings />} />
              <Route path="*" element={<Dashboard />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  );
}

export default App;
