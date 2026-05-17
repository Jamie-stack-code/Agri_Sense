import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import {
  User, Mail, Phone, ArrowLeft, Loader2, CheckCircle,
  Eye, EyeOff, ShieldCheck, AlertCircle, Check, X
} from 'lucide-react';
import axios from 'axios';
import { createUserWithEmailAndPassword, sendEmailVerification } from 'firebase/auth';
import { auth } from '../firebase';

const COUNTRY_CODES = [
  { code: '+265', flag: '🇲🇼', name: 'Malawi' },
  { code: '+254', flag: '🇰🇪', name: 'Kenya' },
  { code: '+255', flag: '🇹🇿', name: 'Tanzania' },
  { code: '+256', flag: '🇺🇬', name: 'Uganda' },
  { code: '+27', flag: '🇿🇦', name: 'South Africa' },
  { code: '+234', flag: '🇳🇬', name: 'Nigeria' },
  { code: '+233', flag: '🇬🇭', name: 'Ghana' },
  { code: '+44', flag: '🇬🇧', name: 'UK' },
  { code: '+1', flag: '🇺🇸', name: 'USA' },
];

const PasswordRule = ({ met, text }) => (
  <div className={`flex items-center gap-2 text-sm transition-colors ${met ? 'text-green-400' : 'text-white/40'}`}>
    {met ? <Check size={13} /> : <X size={13} className="text-white/30" />}
    {text}
  </div>
);

const RegisterPage = ({ onBack, role, onRegistered }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    countryCode: '+265',
    phone: '',
    password: '',
    confirmPassword: '',
    role: role || 'EXPERT'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [passwordFocused, setPasswordFocused] = useState(false);

  const passwordRules = {
    length: formData.password.length >= 8,
    uppercase: /[A-Z]/.test(formData.password),
    number: /[0-9]/.test(formData.password),
    match: formData.password === formData.confirmPassword && formData.password.length > 0,
  };

  const isPasswordStrong = Object.values(passwordRules).every(Boolean);
  const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email);
  const isFormValid = formData.name.trim() && isEmailValid && formData.phone.trim() && isPasswordStrong;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid) return;
    setLoading(true);
    setError('');

    try {
      // 1. Create user in Firebase Auth
      const userCredential = await createUserWithEmailAndPassword(auth, formData.email, formData.password);

      // 2. Send verification email via Firebase (Option B)
      await sendEmailVerification(userCredential.user);

      // 3. Provision user profile in Firestore via NestJS
      const idToken = await userCredential.user.getIdToken(true);
      await axios.post('http://localhost:5000/api/admin/provision', {
        uid: userCredential.user.uid,
        name: formData.name.trim(),
        email: formData.email,
        phone: `${formData.countryCode}${formData.phone.trim()}`,
        role: formData.role
      }, {
        headers: { Authorization: `Bearer ${idToken}` }
      });

      setSuccess(true);
      setTimeout(() => onRegistered(formData.email), 1200);
    } catch (err) {
      console.error(err);
      if (err.code === 'auth/email-already-in-use') {
        setError('This email is already registered. Please sign in instead.');
      } else {
        setError(err.message || 'Registration failed. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout subtitle={`New ${formData.role} Enrollment`}>
      <form onSubmit={handleSubmit} className="space-y-5">
        {/* Header */}
        <div className="flex items-center gap-4 mb-2">
          <button type="button" onClick={onBack} className="text-white/40 hover:text-white transition-colors p-2 rounded-xl hover:bg-white/10">
            <ArrowLeft size={22} />
          </button>
          <div>
            <h2 className="text-3xl font-bold text-white">Create Account</h2>
            <p className="text-white/40 text-sm mt-0.5">Fill in your details to get started</p>
          </div>
        </div>

        {/* Error */}
        {error && (
          <div className="bg-red-500/10 border border-red-500/30 text-red-400 px-5 py-4 rounded-2xl text-sm flex items-start gap-3">
            <AlertCircle size={18} className="mt-0.5 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {/* Full Name */}
        <div className="relative">
          <User className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
          <input
            type="text"
            placeholder="Full Name"
            className="glass-input w-full pl-12 pr-5"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            required
          />
        </div>

        {/* Email */}
        <div className="relative">
          <Mail className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
          <input
            type="email"
            placeholder="Official Email Address"
            className={`glass-input w-full pl-12 pr-12 transition-colors ${formData.email && !isEmailValid ? 'border-red-500/50' : ''}`}
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            required
          />
          {formData.email && (
            <div className="absolute right-4 top-1/2 -translate-y-1/2">
              {isEmailValid
                ? <Check size={16} className="text-green-400" />
                : <X size={16} className="text-red-400" />
              }
            </div>
          )}
        </div>

        {/* Phone with Country Code */}
        <div className="flex gap-2">
          <div className="relative">
            <select
              value={formData.countryCode}
              onChange={(e) => setFormData({ ...formData, countryCode: e.target.value })}
              className="glass-input !px-3 !py-4 w-[110px] appearance-none text-center text-sm font-bold cursor-pointer"
            >
              {COUNTRY_CODES.map((c) => (
                <option key={c.code} value={c.code} className="bg-slate-800">
                  {c.flag} {c.code}
                </option>
              ))}
            </select>
          </div>
          <div className="relative flex-1">
            <Phone className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
            <input
              type="tel"
              placeholder="Phone Number"
              className="glass-input w-full pl-12 pr-5"
              value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value.replace(/\D/g, '') })}
              required
            />
          </div>
        </div>

        {/* Password */}
        <div className="space-y-2">
          <div className="relative">
            <ShieldCheck className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
            <input
              type={showPassword ? 'text' : 'password'}
              placeholder="Create Strong Password"
              className="glass-input w-full pl-12 pr-12"
              value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              onFocus={() => setPasswordFocused(true)}
              onBlur={() => setPasswordFocused(false)}
              required
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-4 top-1/2 -translate-y-1/2 text-white/30 hover:text-white/70 transition-colors"
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          {/* Password strength rules */}
          {(passwordFocused || formData.password) && (
            <div className="bg-white/5 rounded-xl px-4 py-3 space-y-1.5 border border-white/10">
              <PasswordRule met={passwordRules.length} text="At least 8 characters" />
              <PasswordRule met={passwordRules.uppercase} text="At least one uppercase letter (A-Z)" />
              <PasswordRule met={passwordRules.number} text="At least one number (0-9)" />
            </div>
          )}
        </div>

        {/* Confirm Password */}
        <div className="relative">
          <ShieldCheck className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
          <input
            type={showConfirm ? 'text' : 'password'}
            placeholder="Confirm Password"
            className={`glass-input w-full pl-12 pr-12 transition-colors ${formData.confirmPassword && !passwordRules.match ? 'border-red-500/50' : ''}`}
            value={formData.confirmPassword}
            onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
            required
          />
          <button
            type="button"
            onClick={() => setShowConfirm(!showConfirm)}
            className="absolute right-4 top-1/2 -translate-y-1/2 text-white/30 hover:text-white/70 transition-colors"
          >
            {showConfirm ? <EyeOff size={18} /> : <Eye size={18} />}
          </button>
          {formData.confirmPassword && (
            <div className="absolute right-11 top-1/2 -translate-y-1/2">
              {passwordRules.match
                ? <Check size={16} className="text-green-400" />
                : <X size={16} className="text-red-400" />
              }
            </div>
          )}
        </div>

        {/* Submit */}
        <button
          type="submit"
          disabled={loading || success || !isFormValid}
          className={`w-full font-bold text-lg py-5 rounded-2xl transition-all flex items-center justify-center gap-3 relative overflow-hidden ${
            success
              ? 'bg-green-500 text-white'
              : isFormValid
              ? 'bg-hero-accent text-agri-green shadow-[0_0_24px_rgba(255,179,0,0.35)] hover:scale-[1.02] active:scale-[0.98] cursor-pointer'
              : 'bg-white/10 text-white/30 cursor-not-allowed'
          }`}
        >
          {loading ? (
            <Loader2 className="animate-spin" size={24} />
          ) : success ? (
            <>Verification Sent <CheckCircle size={22} className="animate-bounce" /></>
          ) : (
            'Create Account'
          )}
        </button>
      </form>
    </AuthLayout>
  );
};

export default RegisterPage;
