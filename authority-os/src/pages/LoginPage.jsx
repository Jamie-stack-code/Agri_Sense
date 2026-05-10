import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { ArrowRight, Mail, Loader2, Lock, CheckCircle } from 'lucide-react';
import axios from 'axios';

const LoginPage = ({ onOtpRequested, onRegister, onForgotPassword, onVerified }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const res = await axios.post('http://localhost:5000/api/auth/signin', { email, password });
      localStorage.setItem('agri_portal_token', res.data.token);
      localStorage.setItem('agri_portal_user', JSON.stringify(res.data.user));
      setSuccess(true);
      setTimeout(() => onVerified(), 1000); // Small delay for the "WOW" factor
    } catch (err) {
      if (err.response?.data?.unverified) {
        await axios.post('http://localhost:5000/api/auth/request-otp', { email });
        onOtpRequested(email);
      } else {
        setError(err.response?.data?.error || 'Neural link failed.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout subtitle="Access Control">
      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="space-y-2">
          <h2 className="text-3xl font-bold text-white tracking-tight">Identity Check</h2>
          <p className="text-white/50 text-lg">Sign in with your authority credentials.</p>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-400 px-6 py-4 rounded-2xl text-sm">
            {error}
          </div>
        )}

        <div className="space-y-4">
          <div className="relative">
            <Mail className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Official Email"
              className="glass-input w-full pl-14"
              required
            />
          </div>

          <div className="relative">
            <Lock className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Access Key"
              className="glass-input w-full pl-14"
              required
            />
          </div>
        </div>

        <div className="flex justify-end">
          <button 
            type="button" 
            onClick={() => onForgotPassword()} 
            className="text-white/40 hover:text-hero-accent text-sm font-bold"
          >
            Forgot Access Key?
          </button>
        </div>

        <button
          type="submit"
          disabled={loading || success}
          className={`w-full font-bold text-lg py-5 rounded-2xl transition-all flex items-center justify-center gap-3 disabled:opacity-50 relative overflow-hidden ${
            success ? 'bg-agri-green text-white' : 'bg-hero-accent text-agri-green shadow-[0_0_20px_rgba(255,179,0,0.3)] hover:scale-[1.02] active:scale-[0.98]'
          }`}
        >
          {loading ? (
            <Loader2 className="animate-spin" size={24} />
          ) : success ? (
            <>
              Access Granted <CheckCircle size={24} className="animate-bounce" />
            </>
          ) : (
            <>
              Establish Neural Link <ArrowRight size={20} />
            </>
          )}
        </button>

        <div className="text-center">
          <p className="text-white/40">
            Don't have access?{' '}
            <button
              type="button"
              onClick={onRegister}
              className="text-hero-accent font-bold hover:underline"
            >
              Sign Up here
            </button>
          </p>
        </div>
      </form>
    </AuthLayout>
  );
};

export default LoginPage;
