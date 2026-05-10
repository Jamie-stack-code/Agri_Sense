import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { Mail, ShieldCheck, Loader2, ArrowLeft, Lock } from 'lucide-react';
import axios from 'axios';

const ForgotPasswordPage = ({ onBack }) => {
  const [step, setStep] = useState('request'); // request, verify, reset
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleRequestOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await axios.post('http://localhost:5000/api/auth/request-otp', { email });
      setStep('verify');
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to dispatch security code.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = (e) => {
    e.preventDefault();
    const code = otp.join('');
    if (code.length === 6) setStep('reset');
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    setLoading(true);
    setError('');
    try {
      await axios.post('http://localhost:5000/api/auth/reset-password', {
        email,
        code: otp.join(''),
        newPassword
      });
      setStep('success');
    } catch (err) {
      setError(err.response?.data?.error || 'Reset failed.');
    } finally {
      setLoading(false);
    }
  };

  const handleChangeOtp = (index, value) => {
    if (value.length > 1) value = value[value.length - 1];
    if (!/^\d*$/.test(value)) return;
    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);
    if (value && index < 5) document.getElementById(`otp-${index + 1}`).focus();
  };

  if (step === 'success') {
    return (
      <AuthLayout subtitle="Reset Complete">
        <div className="text-center space-y-8 py-10">
          <div className="w-24 h-24 bg-hero-accent/10 rounded-full flex items-center justify-center mx-auto text-hero-accent animate-pulse">
            <ShieldCheck size={48} />
          </div>
          <div className="space-y-4">
            <h2 className="text-3xl font-bold text-white">Authority Restored</h2>
            <p className="text-white/50 text-lg">Your access credentials have been successfully updated.</p>
          </div>
          <button onClick={onBack} className="w-full bg-hero-accent text-agri-green font-bold py-4 rounded-2xl">
            Go to Sign In
          </button>
        </div>
      </AuthLayout>
    );
  }

  return (
    <AuthLayout subtitle="Identity Recovery">
      <div className="space-y-8">
        <div className="flex items-center gap-4">
          <button onClick={onBack} className="text-white/40 hover:text-white transition-colors">
            <ArrowLeft size={24} />
          </button>
          <h2 className="text-3xl font-bold text-white">
            {step === 'request' ? 'Forgot Access Key?' : step === 'verify' ? 'Neural Verification' : 'Update Credentials'}
          </h2>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-400 px-6 py-4 rounded-2xl text-sm">
            {error}
          </div>
        )}

        {step === 'request' && (
          <form onSubmit={handleRequestOtp} className="space-y-8">
            <p className="text-white/50 text-lg">Enter your official email to receive a recovery code.</p>
            <div className="relative">
              <Mail className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
              <input
                type="email"
                placeholder="Official Email"
                className="glass-input w-full pl-14"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <button disabled={loading} className="w-full bg-hero-accent text-agri-green font-bold py-5 rounded-2xl">
              {loading ? <Loader2 className="animate-spin mx-auto" /> : 'Send Recovery Code'}
            </button>
          </form>
        )}

        {step === 'verify' && (
          <form onSubmit={handleVerifyOtp} className="space-y-8">
            <p className="text-white/50 text-lg">Enter the 6-digit code sent to your email.</p>
            <div className="flex justify-between gap-3">
              {otp.map((digit, i) => (
                <input
                  key={i}
                  id={`otp-${i}`}
                  type="text"
                  maxLength={1}
                  value={digit}
                  onChange={(e) => handleChangeOtp(i, e.target.value)}
                  className="w-full aspect-square text-center text-3xl font-bold glass-input !px-0"
                  required
                />
              ))}
            </div>
            <button disabled={otp.some(d => !d)} className="w-full bg-hero-accent text-agri-green font-bold py-5 rounded-2xl">
              Verify Code
            </button>
          </form>
        )}

        {step === 'reset' && (
          <form onSubmit={handleResetPassword} className="space-y-6">
            <p className="text-white/50 text-lg">Set a new secure access key for your authority account.</p>
            <div className="relative">
              <Lock className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
              <input
                type="password"
                placeholder="New Access Key"
                className="glass-input w-full pl-14"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
            </div>
            <div className="relative">
              <ShieldCheck className="absolute left-6 top-1/2 -translate-y-1/2 text-white/30" size={20} />
              <input
                type="password"
                placeholder="Confirm Key"
                className="glass-input w-full pl-14"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </div>
            <button disabled={loading} className="w-full bg-hero-accent text-agri-green font-bold py-5 rounded-2xl">
              {loading ? <Loader2 className="animate-spin mx-auto" /> : 'Update Access Key'}
            </button>
          </form>
        )}
      </div>
    </AuthLayout>
  );
};

export default ForgotPasswordPage;
