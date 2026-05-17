import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
import * as crypto from 'crypto';

@Injectable()
export class AdminService {
  constructor(
    private readonly firebaseService: FirebaseService,
    private readonly eventsGateway: EventsGateway,
  ) {}

  async getStats() {
    const db = this.firebaseService.getDb();
    
    // In a real app we might want to aggregate these properly, 
    // but for transition we do basic counting
    const usersSnap = await db.collection('users').get();
    const expertsSnap = await db.collection('users').where('role', '==', 'EXPERT').get();
    const advisoriesSnap = await db.collection('advisories').get();
    const pendingSnap = await db.collection('questions').where('status', '==', 'PENDING').get();

    return {
      totalUsers: usersSnap.size,
      experts: expertsSnap.size,
      activeAdvisories: advisoriesSnap.size,
      pendingCritical: pendingSnap.size,
      systemHealth: '99.9%',
      revenue: `MK ${(usersSnap.size * 5000).toLocaleString()}`,
    };
  }

  async getUsers() {
    const db = this.firebaseService.getDb();
    const snapshot = await db.collection('users').orderBy('createdAt', 'desc').get();
    return snapshot.docs.map(doc => doc.data());
  }

  async provisionUser(data: any) {
    const db = this.firebaseService.getDb();
    const id = data.uid || crypto.randomUUID(); 
    const user = {
      id,
      ...data,
      phone: data.phone || data.email,
      createdAt: new Date(),
    };

    await db.collection('users').doc(id).set(user);
    this.eventsGateway.broadcast('AUTHORITY_PROVISIONED', user);
    return user;
  }
}
