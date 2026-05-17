const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'index.js');
let content = fs.readFileSync(filePath, 'utf8');

// Update phone-signin response
const signinRegex = /language: user\.language\s*\}/;
const signinReplacement = `language: user.language,
        isProfileComplete: user.isProfileComplete || false,
        district: user.district,
        farmSize: user.farmSize,
        cropsGrown: user.cropsGrown
      }`;

if (signinRegex.test(content)) {
    content = content.replace(signinRegex, signinReplacement);
    console.log('Updated phone-signin response object.');
} else {
    console.error('Could not find language: user.language in index.js');
}

// Add update-profile endpoint
const updateProfileEndpoint = `
// 1c. Update Profile
app.post('/api/auth/update-profile', async (req, res) => {
  const { phone, district, farmSize, cropsGrown, language, isProfileComplete } = req.body;
  try {
    const user = await prisma.user.update({
      where: { phone },
      data: { 
        district, 
        farmSize: parseFloat(farmSize) || undefined, 
        cropsGrown, 
        language, 
        isProfileComplete: isProfileComplete === true || isProfileComplete === 'true'
      }
    });
    res.json({ message: 'Profile updated successfully', user });
  } catch (error) {
    console.error('Update Profile Error:', error);
    res.status(500).json({ error: 'Failed to update profile.' });
  }
});
`;

if (!content.includes('/api/auth/update-profile')) {
    const insertionPoint = "// 2. Request Phone OTP (Login)";
    if (content.includes(insertionPoint)) {
        content = content.replace(insertionPoint, updateProfileEndpoint + '\n' + insertionPoint);
        console.log('Added /api/auth/update-profile endpoint.');
    } else {
        // Just append to the end if insertion point not found
        content += updateProfileEndpoint;
        console.log('Appended /api/auth/update-profile endpoint to end of file.');
    }
} else {
    console.log('update-profile endpoint already exists.');
}

fs.writeFileSync(filePath, content, 'utf8');
console.log('index.js patched successfully.');
