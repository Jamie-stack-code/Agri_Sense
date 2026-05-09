const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const { PrismaClient } = require('@prisma/client');
const http = require('http');
const { Server } = require('socket.io');

dotenv.config();

const app = express();
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "*", // In production, restrict this to your portal URLs
    methods: ["GET", "POST"]
  }
});

const prisma = new PrismaClient();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// ── REAL-TIME SOCKET LOGIC ───────────────────────────────────────────────────
io.on('connection', (socket) => {
  console.log('⚡ Neural Link Established:', socket.id);
  
  // Relay farmer questions to portals
  socket.on('NEW_FARMER_QUESTION', (data) => {
    broadcast('NEW_FARMER_QUESTION', data);
  });

  // Relay expert replies to mobile app
  socket.on('NEW_EXPERT_REPLY', (data) => {
    broadcast('NEW_EXPERT_REPLY', data);
  });

  socket.on('disconnect', () => {
    console.log('🔌 Neural Link Terminated:', socket.id);
  });
});

// Helper to broadcast events
const broadcast = (event, data) => {
  io.emit(event, data);
  console.log(`📢 Broadcasting [${event}]:`, data);
};

// ── HEALTH CHECK ─────────────────────────────────────────────────────────────
app.get('/health', (req, res) => {
  res.json({ status: 'OK', message: 'Agri-Sense AI Core is live', timestamp: new Date() });
});

// ── AUTHENTICATION ───────────────────────────────────────────────────────────
app.post('/api/auth/verify', async (req, res) => {
  const { phone, code } = req.body;
  if (code === '4829') {
    let user = await prisma.user.findUnique({ where: { phone } });
    if (!user) {
      user = await prisma.user.create({
        data: { phone, name: 'New Farmer', role: 'FARMER' }
      });
      broadcast('NEW_USER_JOINED', user);
    }
    res.json({ token: 'mock-jwt-token-' + user.id, user });
  } else {
    res.status(400).json({ error: 'Invalid OTP code' });
  }
});

// ── ADVISORIES ───────────────────────────────────────────────────────────────
app.post('/api/advisories', async (req, res) => {
  const { title, content, type, category, expertId } = req.body;
  const advisory = await prisma.advisory.create({
    data: { title, content, type, category, expertId }
  });
  broadcast('NEW_ADVISORY_PUBLISHED', advisory);
  res.json(advisory);
});

// ── QUESTIONS & REPLIES ──────────────────────────────────────────────────────
app.post('/api/questions', async (req, res) => {
  const { content, farmerId, voiceUrl, imageUrl } = req.body;
  const question = await prisma.question.create({
    data: { content, farmerId, voiceUrl, imageUrl }
  });
  broadcast('NEW_FARMER_QUESTION', question);
  res.json(question);
});

// --- ADMIN AUTHORITY ROUTES ---
app.get('/api/admin/stats', async (req, res) => {
  try {
    const userCount = await prisma.user.count();
    const expertCount = await prisma.user.count({ where: { role: 'EXPERT' } });
    const advisoryCount = await prisma.advisory.count();
    const pendingQuestions = await prisma.question.count({ where: { status: 'PENDING' } });
    
    res.json({
      totalUsers: userCount,
      experts: expertCount,
      activeAdvisories: advisoryCount,
      pendingCritical: pendingQuestions,
      systemHealth: '99.9%',
      revenue: `MK ${(userCount * 5000).toLocaleString()}` 
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

app.get('/api/admin/users', async (req, res) => {
  const users = await prisma.user.findMany({ orderBy: { createdAt: 'desc' } });
  res.json(users);
});

// Provision new authority and broadcast
app.post('/api/admin/provision', async (req, res) => {
  const { name, email, phone, role } = req.body;
  const user = await prisma.user.create({
    data: { name, phone: phone || email, role }
  });
  broadcast('AUTHORITY_PROVISIONED', user);
  res.json(user);
});

server.listen(PORT, () => {
  console.log(`
  🚀 Agri-Sense Real-Time Core Live
  📡 Neural Server: http://localhost:${PORT}
  ⚡ WebSockets: ACTIVE
  `);
}).on('error', (err) => {
  if (err.code === 'EADDRINUSE') {
    console.log(`⚠️ Port ${PORT} is occupied. Retrying on Port ${parseInt(PORT) + 1}...`);
    server.listen(parseInt(PORT) + 1);
  } else {
    console.error('❌ Server Crash:', err);
  }
});
