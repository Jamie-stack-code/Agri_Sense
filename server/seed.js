const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

async function main() {
  console.log('🌱 Starting Seed... 🚀');

  // 1. Create Experts
  const expert1 = await prisma.user.upsert({
    where: { phone: '+265881000111' },
    update: {},
    create: {
      phone: '+265881000111',
      name: 'Dr. Andrew Mwale',
      role: 'EXPERT',
      language: 'Chichewa'
    },
  });

  const admin1 = await prisma.user.upsert({
    where: { phone: '+265111222333' },
    update: {},
    create: {
      phone: '+265111222333',
      name: 'System Root',
      role: 'ADMIN'
    },
  });

  // 2. Create Farmers
  const farmers = [
    { name: 'Samuel Banda', phone: '+265991000001' },
    { name: 'Joyce Phiri', phone: '+265991000002' },
    { name: 'Peter Mwale', phone: '+265991000003' },
    { name: 'Grace Chunga', phone: '+265991000004' },
  ];

  for (const f of farmers) {
    const farmer = await prisma.user.upsert({
      where: { phone: f.phone },
      update: {},
      create: {
        phone: f.phone,
        name: f.name,
        role: 'FARMER'
      },
    });

    // 3. Create Subscriptions for each farmer
    await prisma.subscription.create({
      data: {
        planName: 'PREMIUM',
        status: 'ACTIVE',
        amount: 5000.0,
        provider: 'AIRTEL',
        farmerId: farmer.id,
        endDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000) // 30 days from now
      }
    });

    // 4. Create some Questions
    await prisma.question.create({
      data: {
        content: `I am seeing yellow spots on my maize leaves in ${f.name === 'Samuel Banda' ? 'Lilongwe' : 'Blantyre'}. What should I do?`,
        farmerId: farmer.id,
        status: 'PENDING'
      }
    });
  }

  // 5. Create some Advisories from the Expert
  await prisma.advisory.create({
    data: {
      title: 'Fall Armyworm Alert: Southern Region',
      content: 'Major outbreak detected. Please check your fields immediately and apply approved pesticides.',
      type: 'TEXT',
      category: 'PEST',
      expertId: expert1.id
    }
  });

  console.log('✅ Seeding Complete! The ecosystem is now populated.');
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
