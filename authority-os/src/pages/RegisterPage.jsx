import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { User, Mail, Phone, ArrowLeft, Loader2, CheckCircle, Shield, ShieldCheck } from 'lucide-react';
import axios from 'axios';

const RegisterPage = ({ onBack, role, onRegistered }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    role: role || 'EXPERT'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await axios.post('http://localhost:5000/api/auth/register', formData);
      setSuccess(true);
      setTimeout(() => onRegistered(formData.email), 1000);
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout subtitle={`New ${formData.role} Enrollment`}>
      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="flex items-center gap-4 mb-4">
          <button type="button" onClick={onBack} className="text-white/40 hover:text-white transition-colors">
            <ArrowLeft size={24} />
          </button>
          <h2 className="text-3xl font-bold text-white">Join Core</h2>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-400 px-6 py-4 rounded-2xl text-sm">
            {error}
          </div>
        )}

        <div className="space-y-4">
          <div className="relative">
            <User className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
            <input
              type="text"
              placeholder="Full Name"
              className="glass-input w-full pl-14"
              value={formData.name}
              onChange={(e) => setFormData({...formData, name: e.target.value})}
              required
            />
          </div>

          <div className="relative">
            <Mail className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
            <input
              type="email"
              placeholder="Official Email"
              className="glass-input w-full pl-14"
              value={formData.email}
              onChange={(e) => setFormData({...formData, email: e.target.value})}
              required
            />
          </div>

          <div className="relative">
            <Phone className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
            <input
              type="tel"
              placeholder="Phone Number"
              className="glass-input w-full pl-14"
              value={formData.phone}
              onChange={(e) => setFormData({...formData, phone: e.target.value})}
              required
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="relative">
              <Shield className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
              <input
                type="password"
                placeholder="Password"
                className="glass-input w-full pl-14"
                value={formData.password}
                onChange={(e) => setFormData({...formData, password: e.target.value})}
                required
              />
            </div>
            <div className="relative">
              <ShieldCheck className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
              <input
                type="password"
                placeholder="Confirm"
                className="glass-input w-full pl-14"
                value={formData.confirmPassword}
                onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
                required
              />
            </div>
          </div>
        </div>

        <button
          type="submit"
          disabled={loading || success || (formData.password !== formData.confirmPassword)}
          className={`w-full font-bold text-lg py-5 rounded-2xl transition-all flex items-center justify-center gap-3 disabled:opacity-50 relative overflow-hidden ${
            success ? 'bg-agri-green text-white' : 'bg-hero-accent text-agri-green shadow-[0_0_20px_rgba(255,179,0,0.3)] hover:scale-[1.02] active:scale-[0.98]'
          }`}
        >
          {loading ? (
            <Loader2 className="animate-spin" size={24} />
          ) : success ? (
            <>
              Provisioned <CheckCircle size={24} className="animate-bounce" />
            </>
          ) : (
            'Complete Registration'
          )}
        </button>
      </form>
    </AuthLayout>
  );
};

export default RegisterPage;
