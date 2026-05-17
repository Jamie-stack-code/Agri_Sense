const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

async function main() {
  const userCount = await prisma.user.count();
  console.log(`Total Users: ${userCount}`);
  const users = await prisma.user.findMany({ take: 5 });
  console.log('Sample Users:', JSON.stringify(users, null, 2));
}

main()
  .catch(e => console.error(e))
  .finally(async () => await prisma.$disconnect());
