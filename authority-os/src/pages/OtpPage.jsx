import React, { useState, useEffect } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { ShieldCheck, Loader2, RefreshCcw } from 'lucide-react';
import axios from 'axios';

const OtpPage = ({ email, onVerified }) => {
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [timer, setTimer] = useState(60);

  useEffect(() => {
    const interval = setInterval(() => {
      setTimer((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  const handleChange = (index, value) => {
    if (value.length > 1) value = value[value.length - 1];
    if (!/^\d*$/.test(value)) return;

    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);

    // Auto-focus next
    if (value && index < 5) {
      document.getElementById(`otp-${index + 1}`).focus();
    }
  };

  const handleKeyDown = (index, e) => {
    if (e.key === 'Backspace' && !otp[index] && index > 0) {
      document.getElementById(`otp-${index - 1}`).focus();
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    const code = otp.join('');
    if (code.length < 6) return;

    setLoading(true);
    setError('');

    try {
      const response = await axios.post('http://localhost:5000/api/auth/verify-otp', { email, code });
      const { token, user } = response.data;
      localStorage.setItem('agri_portal_token', token);
      localStorage.setItem('agri_portal_user', JSON.stringify(user));
      onVerified();
    } catch (err) {
      setError(err.response?.data?.error || 'Verification failed. Code may be incorrect.');
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    if (timer > 0) return;
    setLoading(true);
    try {
      await axios.post('http://localhost:5000/api/auth/request-otp', { email });
      setTimer(60);
      setError('');
    } catch (err) {
      setError('Resend failed. Try again later.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout subtitle="Verification Required">
      <form onSubmit={handleVerify} className="space-y-10">
        <div className="space-y-2">
          <h2 className="text-3xl font-bold text-white tracking-tight">Security Check</h2>
          <p className="text-white/50 text-lg">
            We've sent a code to <span className="text-hero-accent">{email}</span>
          </p>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-400 px-6 py-4 rounded-2xl text-sm">
            {error}
          </div>
        )}

        <div className="flex justify-between gap-3">
          {otp.map((digit, i) => (
            <input
              key={i}
              id={`otp-${i}`}
              type="text"
              maxLength={1}
              value={digit}
              onChange={(e) => handleChange(i, e.target.value)}
              onKeyDown={(e) => handleKeyDown(i, e)}
              className="w-full aspect-square text-center text-3xl font-bold glass-input !px-0"
              required
            />
          ))}
        </div>

        <button
          type="submit"
          disabled={loading || otp.some(d => !d)}
          className="w-full bg-hero-accent text-agri-green font-bold text-lg py-5 rounded-2xl hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center justify-center gap-3 disabled:opacity-50 shadow-[0_0_20px_rgba(74,222,128,0.3)]"
        >
          {loading ? (
            <Loader2 className="animate-spin" size={24} />
          ) : (
            <>
              Verify Neural Link <ShieldCheck size={20} />
            </>
          )}
        </button>

        <div className="text-center">
          <button
            type="button"
            onClick={handleResend}
            disabled={timer > 0}
            className="text-white/40 hover:text-hero-accent transition-colors flex items-center justify-center gap-2 mx-auto disabled:opacity-30"
          >
            <RefreshCcw size={16} />
            {timer > 0 ? `Resend code in ${timer}s` : 'Resend Code Now'}
          </button>
        </div>
      </form>
    </AuthLayout>
  );
};

export default OtpPage;
