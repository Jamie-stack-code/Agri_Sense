import React from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { Shield, UserCog, ArrowRight } from 'lucide-react';

const RoleSelectionPage = ({ onSelect }) => {
  return (
    <AuthLayout subtitle="Unified Authority System">
      <div className="space-y-8">
        <div className="text-center space-y-2">
          <h2 className="text-3xl font-bold text-white tracking-tight">Select Identity</h2>
          <p className="text-white/50 text-lg text-pretty">Choose your portal access level to continue.</p>
        </div>

        <div className="grid grid-cols-1 gap-6">
          <button
            onClick={() => onSelect('EXPERT')}
            className="group relative overflow-hidden glass-panel p-8 rounded-[2rem] text-left hover:border-hero-accent/50 transition-all active:scale-95"
          >
            <div className="flex items-center justify-between">
              <div className="space-y-4">
                <div className="w-14 h-14 bg-hero-accent/10 rounded-2xl flex items-center justify-center text-hero-accent group-hover:bg-hero-accent group-hover:text-agri-green transition-all">
                  <UserCog size={32} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold text-white">Expert Portal</h3>
                  <p className="text-white/40">Technical advisories & farmer support</p>
                </div>
              </div>
              <ArrowRight className="text-white/20 group-hover:text-hero-accent group-hover:translate-x-2 transition-all" size={32} />
            </div>
          </button>

          <button
            onClick={() => onSelect('ADMIN')}
            className="group relative overflow-hidden glass-panel p-8 rounded-[2rem] text-left hover:border-hero-accent/50 transition-all active:scale-95"
          >
            <div className="flex items-center justify-between">
              <div className="space-y-4">
                <div className="w-14 h-14 bg-white/5 rounded-2xl flex items-center justify-center text-white group-hover:bg-white group-hover:text-agri-green transition-all">
                  <Shield size={32} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold text-white">Admin Command</h3>
                  <p className="text-white/40">System configuration & oversight</p>
                </div>
              </div>
              <ArrowRight className="text-white/20 group-hover:text-hero-accent group-hover:translate-x-2 transition-all" size={32} />
            </div>
          </button>
        </div>

        <div className="text-center">
          <p className="text-white/20 text-sm">Authorized Personnel Only</p>
        </div>
      </div>
    </AuthLayout>
  );
};

export default RoleSelectionPage;
