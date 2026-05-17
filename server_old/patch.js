const fs = require('fs');
let c = fs.readFileSync('index.js', 'utf8');

c = c.replace(/language: user\.language\s*\}/, \language: user.language,
        isProfileComplete: user.isProfileComplete || false,
        district: user.district,
        farmSize: user.farmSize,
        cropsGrown: user.cropsGrown
      }\);

const appendText = \\n// 1c. Update Profile
app.post('/api/auth/update-profile', async (req, res) => {
  const { phone, district, farmSize, cropsGrown, language, isProfileComplete } = req.body;
  try {
    const user = await prisma.user.update({
      where: { phone },
      data: { district, farmSize, cropsGrown, language, isProfileComplete }
    });
    res.json({ message: 'Profile updated successfully', user });
  } catch (error) {
    res.status(500).json({ error: 'Failed to update profile.' });
  }
});\n\;

c = c.replace(/\/\/ 2\. Request Phone OTP \(Login\)/, appendText + "\n// 2. Request Phone OTP (Login)");

fs.writeFileSync('index.js', c, 'utf8');
console.log("Patched index.js");
