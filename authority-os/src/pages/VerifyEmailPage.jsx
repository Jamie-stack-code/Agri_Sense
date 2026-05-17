import React, { useState } from 'react';
import AuthLayout from '../components/auth/AuthLayout';
import { MailCheck, Loader2, RefreshCcw, ArrowLeft, CheckCircle } from 'lucide-react';
import { sendEmailVerification } from 'firebase/auth';
import { auth } from '../firebase';

const VerifyEmailPage = ({ email, onBack, onVerified }) => {
  const [resending, setResending] = useState(false);
  const [resent, setResent] = useState(false);
  const [error, setError] = useState('');
  const [timer, setTimer] = useState(0);

  const handleResend = async () => {
    if (timer > 0 || resending) return;
    setResending(true);
    setError('');
    try {
      const user = auth.currentUser;
      if (user) {
        await sendEmailVerification(user);
        setResent(true);
        setTimer(60);
        const interval = setInterval(() => {
          setTimer((prev) => {
            if (prev <= 1) { clearInterval(interval); return 0; }
            return prev - 1;
          });
        }, 1000);
      } else {
        setError('Session expired. Please go back and sign in again.');
      }
    } catch (err) {
      setError('Could not resend email. Please try again.');
    } finally {
      setResending(false);
    }
  };

  const handleContinue = async () => {
    const user = auth.currentUser;
    if (!user) { setError('Session expired. Please go back and try again.'); return; }
    await user.reload();
    if (user.emailVerified) {
      onVerified();
    } else {
      setError('Email not yet verified. Please click the link in your inbox first, then try again.');
    }
  };

  return (
    <AuthLayout subtitle="Email Verification">
      <div className="space-y-8 text-center">

        {/* Icon */}
        <div className="flex justify-center">
          <div className="w-24 h-24 rounded-full bg-hero-accent/10 border-2 border-hero-accent/30 flex items-center justify-center">
            <MailCheck size={44} className="text-hero-accent" />
          </div>
        </div>

        {/* Text */}
        <div className="space-y-3">
          <h2 className="text-3xl font-bold text-white">Check Your Email</h2>
          <p className="text-white/50 text-base leading-relaxed">
            We've sent a verification link to
          </p>
          <p className="text-hero-accent font-bold text-lg break-all">{email}</p>
          <p className="text-white/40 text-sm leading-relaxed">
            Click the link in that email to verify your account. Once verified, return here and click the button below.
          </p>
        </div>

        {/* Error */}
        {error && (
          <div className="bg-red-500/10 border border-red-500/30 text-red-400 px-5 py-4 rounded-2xl text-sm text-left">
            {error}
          </div>
        )}

        {/* Continue after verification */}
        <button
          type="button"
          onClick={handleContinue}
          className="w-full bg-hero-accent text-agri-green font-bold text-lg py-5 rounded-2xl hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center justify-center gap-3 shadow-[0_0_24px_rgba(255,179,0,0.35)]"
        >
          <CheckCircle size={22} />
          I've Verified My Email
        </button>

        {/* Resend */}
        <div className="space-y-2">
          {resent && (
            <p className="text-green-400 text-sm font-semibold">✓ Verification email resent successfully!</p>
          )}
          <button
            type="button"
            onClick={handleResend}
            disabled={timer > 0 || resending}
            className="text-white/40 hover:text-hero-accent transition-colors flex items-center justify-center gap-2 mx-auto text-sm disabled:opacity-30"
          >
            <RefreshCcw size={15} className={resending ? 'animate-spin' : ''} />
            {timer > 0 ? `Resend link in ${timer}s` : resending ? 'Sending…' : "Didn't receive it? Resend"}
          </button>
        </div>

        {/* Back */}
        <button
          type="button"
          onClick={onBack}
          className="text-white/30 hover:text-white/60 transition-colors flex items-center justify-center gap-2 mx-auto text-sm"
        >
          <ArrowLeft size={15} />
          Back to Sign In
        </button>
      </div>
    </AuthLayout>
  );
};

export default VerifyEmailPage;
