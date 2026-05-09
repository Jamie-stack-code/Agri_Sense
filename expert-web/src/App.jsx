import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Farmers from './pages/Farmers';
import Questions from './pages/Questions';
import Advisories from './pages/Advisories';
import DiseaseReports from './pages/DiseaseReports';
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
            <div className="flex items-center gap-3 text-agri-green">
              <div className="w-10 h-10 bg-agri-green rounded-xl flex items-center justify-center text-white">A</div>
              <h1 className="text-xl font-black tracking-tighter">AGRI-SENSE</h1>
            </div>
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
              <Route path="/farmers" element={<Farmers />} />
              <Route path="/questions" element={<Questions />} />
              <Route path="/advisories" element={<Advisories />} />
              <Route path="/reports" element={<DiseaseReports />} />
              <Route path="*" element={<Dashboard />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  );
}

export default App;
