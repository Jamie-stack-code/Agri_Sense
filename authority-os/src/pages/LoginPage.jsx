import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { ArrowRight, Mail, Loader2, Lock, CheckCircle, Eye, EyeOff, AlertCircle } from 'lucide-react';
import axios from 'axios';
import { signInWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../firebase';

const LoginPage = ({ onOtpRequested, onRegister, onForgotPassword, onVerified }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // 1. Authenticate with Firebase
      const userCredential = await signInWithEmailAndPassword(auth, email, password);

      // 2. Check if email is verified
      if (!userCredential.user.emailVerified) {
        setError('Your email is not verified yet. Please check your inbox and click the verification link first.');
        setLoading(false);
        return;
      }

      // 3. Get ID token and sync with NestJS backend
      const idToken = await userCredential.user.getIdToken(true);
      const res = await axios.post('http://localhost:5000/api/auth/verify-token', {}, {
        headers: { Authorization: `Bearer ${idToken}` }
      });

      // 4. Save session
      localStorage.setItem('agri_portal_token', res.data.token);
      localStorage.setItem('agri_portal_user', JSON.stringify(res.data.user));

      setSuccess(true);
      setTimeout(() => onVerified(), 900);
    } catch (err) {
      console.error(err);
      if (err.code === 'auth/invalid-credential' || err.code === 'auth/wrong-password' || err.code === 'auth/user-not-found') {
        setError('Incorrect email or password. Please try again.');
      } else if (err.code === 'auth/too-many-requests') {
        setError('Too many failed attempts. Please wait a moment and try again.');
      } else {
        setError(err.message || 'Sign in failed. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout subtitle="Access Control">
      <form onSubmit={handleSubmit} className="space-y-6">

        {/* Header */}
        <div className="space-y-1">
          <h2 className="text-3xl font-bold text-white tracking-tight">Sign In</h2>
          <p className="text-white/40 text-base">Enter your authority credentials to access the portal.</p>
        </div>

        {/* Error */}
        {error && (
          <div className="bg-red-500/10 border border-red-500/30 text-red-400 px-5 py-4 rounded-2xl text-sm flex items-start gap-3">
            <AlertCircle size={18} className="mt-0.5 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {/* Fields */}
        <div className="space-y-4">
          {/* Email */}
          <div className="relative">
            <Mail className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Official Email Address"
              className="glass-input w-full pl-12 pr-5"
              required
              autoComplete="email"
            />
          </div>

          {/* Password */}
          <div className="relative">
            <Lock className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
            <input
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
              className="glass-input w-full pl-12 pr-12"
              required
              autoComplete="current-password"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-4 top-1/2 -translate-y-1/2 text-white/30 hover:text-white/70 transition-colors"
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>
        </div>

        {/* Sign In + Forgot Password row */}
        <div className="flex items-center justify-between gap-4">
          <button
            type="submit"
            disabled={loading || success || !email || !password}
            className={`flex-1 font-bold text-lg py-5 rounded-2xl transition-all flex items-center justify-center gap-3 ${
              success
                ? 'bg-green-500 text-white'
                : email && password
                ? 'bg-hero-accent text-agri-green shadow-[0_0_24px_rgba(255,179,0,0.35)] hover:scale-[1.02] active:scale-[0.98]'
                : 'bg-white/10 text-white/30 cursor-not-allowed'
            }`}
          >
            {loading ? (
              <Loader2 className="animate-spin" size={22} />
            ) : success ? (
              <><CheckCircle size={22} /> Access Granted</>
            ) : (
              <>Sign In <ArrowRight size={20} /></>
            )}
          </button>

          <button
            type="button"
            onClick={onForgotPassword}
            className="text-white/40 hover:text-hero-accent text-sm font-semibold transition-colors whitespace-nowrap px-2"
          >
            Forgot Password?
          </button>
        </div>

        {/* Register link */}
        <div className="text-center pt-1">
          <p className="text-white/40 text-sm">
            Don't have an account?{' '}
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
