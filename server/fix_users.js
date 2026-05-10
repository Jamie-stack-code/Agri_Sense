const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

async function main() {
    const result = await prisma.user.updateMany({
        data: {
            isProfileComplete: true
        }
    });
    console.log('Update result:', result);
}

main()
    .catch(console.error)
    .finally(() => prisma.$disconnect());
