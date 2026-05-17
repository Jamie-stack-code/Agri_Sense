const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
  host: process.env.SMTP_HOST,
  port: process.env.SMTP_PORT,
  secure: false, // true for 465, false for other ports
  auth: {
    user: process.env.SMTP_USER,
    pass: process.env.SMTP_PASS,
  },
});

const sendOTPEmail = async (email, otp) => {
  const mailOptions = {
    from: '"Agri-Sense Neural Core" <no-reply@agri-sense.ai>',
    to: email,
    subject: '🔐 Your Secure Access Code - Agri-Sense',
    html: `
      <div style="font-family: 'Inter', sans-serif; max-width: 600px; margin: auto; padding: 40px; background: #0a0a0a; color: #ffffff; border-radius: 24px; border: 1px solid #333;">
        <div style="text-align: center; margin-bottom: 40px;">
          <h1 style="color: #4ade80; font-size: 32px; letter-spacing: -1px; margin: 0;">AGRI-SENSE</h1>
          <p style="color: #888; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;">Neural Intelligence Portal</p>
        </div>
        
        <div style="background: rgba(255, 255, 255, 0.03); border-radius: 16px; padding: 32px; text-align: center; border: 1px solid rgba(255, 255, 255, 0.05);">
          <p style="font-size: 18px; color: #ccc; margin-bottom: 24px;">Your verification code is</p>
          <div style="font-size: 48px; font-weight: 800; color: #4ade80; letter-spacing: 8px; margin-bottom: 24px; font-family: monospace;">
            ${otp}
          </div>
          <p style="font-size: 14px; color: #666;">This code expires in 10 minutes. Please do not share this with anyone.</p>
        </div>

        <div style="margin-top: 40px; text-align: center; color: #444; font-size: 12px;">
          <p>© 2026 Agri-Sense Africa. Pioneering Agronomic Intelligence.</p>
          <p>Lilongwe, Malawi • Johannesburg, South Africa</p>
        </div>
      </div>
    `,
  };

  try {
    // For development, if SMTP is not configured, we log the OTP
    if (!process.env.SMTP_USER) {
      console.log(`
      -----------------------------------------
      📧 MOCK EMAIL SENT TO: ${email}
      🔐 OTP CODE: ${otp}
      -----------------------------------------
      `);
      return true;
    }
    await transporter.sendMail(mailOptions);
    return true;
  } catch (error) {
    console.error('❌ Email dispatch failed:', error);
    return false;
  }
};

module.exports = { sendOTPEmail };
