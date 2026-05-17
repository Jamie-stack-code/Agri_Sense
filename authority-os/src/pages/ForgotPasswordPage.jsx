import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { Mail, ShieldCheck, Loader2, ArrowLeft, MailCheck, AlertCircle } from 'lucide-react';
import { sendPasswordResetEmail } from 'firebase/auth';
import { auth } from '../firebase';

const ForgotPasswordPage = ({ onBack }) => {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [sent, setSent] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Firebase sends the password reset link directly to the user's email
      await sendPasswordResetEmail(auth, email);
      setSent(true);
    } catch (err) {
      console.error(err);
      if (err.code === 'auth/user-not-found') {
        setError('No account found with this email address.');
      } else if (err.code === 'auth/invalid-email') {
        setError('Please enter a valid email address.');
      } else if (err.code === 'auth/too-many-requests') {
        setError('Too many requests. Please wait a moment and try again.');
      } else {
        setError('Failed to send reset email. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  // ✅ Success screen — email sent
  if (sent) {
    return (
      <AuthLayout subtitle="Password Recovery">
        <div className="text-center space-y-8 py-4">
          {/* Icon */}
          <div className="w-24 h-24 bg-hero-accent/10 border-2 border-hero-accent/30 rounded-full flex items-center justify-center mx-auto">
            <MailCheck size={44} className="text-hero-accent" />
          </div>

          {/* Message */}
          <div className="space-y-3">
            <h2 className="text-3xl font-bold text-white">Check Your Email</h2>
            <p className="text-white/50 text-base leading-relaxed">
              We've sent a password reset link to:
            </p>
            <p className="text-hero-accent font-bold text-lg break-all">{email}</p>
            <p className="text-white/40 text-sm leading-relaxed">
              Click the link in that email to set a new password. The link expires in 1 hour.
            </p>
          </div>

          {/* Actions */}
          <div className="space-y-3">
            <button
              onClick={() => { setSent(false); setEmail(''); }}
              className="w-full bg-white/10 hover:bg-white/15 text-white/70 font-semibold py-4 rounded-2xl transition-all text-sm"
            >
              Use a Different Email
            </button>
            <button
              onClick={onBack}
              className="w-full bg-hero-accent text-agri-green font-bold py-5 rounded-2xl hover:scale-[1.02] active:scale-[0.98] transition-all shadow-[0_0_24px_rgba(255,179,0,0.3)] flex items-center justify-center gap-2"
            >
              <ShieldCheck size={20} />
              Back to Sign In
            </button>
          </div>
        </div>
      </AuthLayout>
    );
  }

  // 📧 Request form
  return (
    <AuthLayout subtitle="Password Recovery">
      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Header */}
        <div className="flex items-center gap-4">
          <button
            type="button"
            onClick={onBack}
            className="text-white/40 hover:text-white transition-colors p-2 rounded-xl hover:bg-white/10"
          >
            <ArrowLeft size={22} />
          </button>
          <div>
            <h2 className="text-3xl font-bold text-white">Forgot Password?</h2>
            <p className="text-white/40 text-sm mt-0.5">We'll send a reset link to your email</p>
          </div>
        </div>

        {/* Error */}
        {error && (
          <div className="bg-red-500/10 border border-red-500/30 text-red-400 px-5 py-4 rounded-2xl text-sm flex items-start gap-3">
            <AlertCircle size={18} className="mt-0.5 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {/* Email field */}
        <div className="space-y-2">
          <p className="text-white/50 text-base">Enter your official email address and we'll send you a secure link to reset your password.</p>
          <div className="relative mt-4">
            <Mail className="absolute left-5 top-1/2 -translate-y-1/2 text-white/30" size={18} />
            <input
              type="email"
              placeholder="Official Email Address"
              className="glass-input w-full pl-12 pr-5"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoFocus
            />
          </div>
        </div>

        {/* Submit */}
        <button
          type="submit"
          disabled={loading || !email}
          className={`w-full font-bold text-lg py-5 rounded-2xl transition-all flex items-center justify-center gap-3 ${
            email
              ? 'bg-hero-accent text-agri-green shadow-[0_0_24px_rgba(255,179,0,0.35)] hover:scale-[1.02] active:scale-[0.98]'
              : 'bg-white/10 text-white/30 cursor-not-allowed'
          }`}
        >
          {loading ? (
            <Loader2 className="animate-spin" size={22} />
          ) : (
            <>
              <MailCheck size={20} />
              Send Reset Link
            </>
          )}
        </button>

        {/* Back link */}
        <div className="text-center">
          <button
            type="button"
            onClick={onBack}
            className="text-white/30 hover:text-white/60 transition-colors text-sm"
          >
            Remember your password? Sign In
          </button>
        </div>
      </form>
    </AuthLayout>
  );
};

export default ForgotPasswordPage;
