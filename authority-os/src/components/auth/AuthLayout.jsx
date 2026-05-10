import { motion } from 'framer-motion';

const AuthLayout = ({ children, title, subtitle }) => {
  return (
    <div className="min-h-screen w-full hero-gradient flex items-center justify-center p-6 relative overflow-hidden">
      {/* Decorative Elements */}
      <div className="absolute top-[-10%] right-[-10%] w-[40%] h-[40%] bg-agri-green/20 rounded-full blur-[120px] animate-pulse"></div>
      <div className="absolute bottom-[-10%] left-[-10%] w-[40%] h-[40%] bg-hero-accent/10 rounded-full blur-[120px] animate-pulse" style={{ animationDelay: '2s' }}></div>
      
      {/* Floating Icons/Shapes */}
      <div className="absolute top-20 left-20 w-12 h-12 border border-white/10 rounded-xl rotate-12 animate-float"></div>
      <div className="absolute bottom-40 right-40 w-16 h-16 border border-white/5 rounded-full animate-float" style={{ animationDelay: '3s' }}></div>

      <motion.div 
        initial={{ opacity: 0, scale: 0.95, y: 20 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        transition={{ duration: 0.8, ease: "easeOut" }}
        className="w-full max-w-xl z-10"
      >
        <div className="text-center mb-12">
          <h1 className="text-5xl font-black text-white tracking-tighter mb-4">
            AGRI<span className="text-hero-accent">-</span>SENSE
          </h1>
          <div className="inline-block px-4 py-1 rounded-full bg-hero-accent/10 border border-hero-accent/20">
            <p className="text-hero-accent text-xs font-bold uppercase tracking-[0.2em]">{subtitle || 'Neural Authority Portal'}</p>
          </div>
        </div>

        <div className="glass-panel rounded-[2.5rem] p-10 md:p-16 relative overflow-hidden group">
          {/* Internal Glow */}
          <div className="absolute -top-24 -right-24 w-48 h-48 bg-hero-accent/10 rounded-full blur-3xl group-hover:bg-hero-accent/20 transition-all duration-700"></div>
          
          <div className="relative z-10">
            {children}
          </div>
        </div>

        <div className="mt-12 text-center">
          <p className="text-white/30 text-sm font-medium tracking-wide">
            © 2026 Agri-Sense Africa. Pioneering Agronomic Intelligence.
          </p>
        </div>
      </motion.div>
    </div>
  );
};

export default AuthLayout;
