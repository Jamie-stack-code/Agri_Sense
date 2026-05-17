import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
import * as crypto from 'crypto';

@Injectable()
export class AdvisoriesService {
  constructor(
    private readonly firebaseService: FirebaseService,
    private readonly eventsGateway: EventsGateway,
  ) {}

  async createAdvisory(data: any) {
    const db = this.firebaseService.getDb();
    const id = crypto.randomUUID();
    const advisory = {
      id,
      ...data,
      category: data.category || 'DISEASE_PEST',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    await db.collection('advisories').doc(id).set(advisory);

    // Store in history
    const historyId = crypto.randomUUID();
    await db.collection('advisory_history').doc(historyId).set({
      id: historyId,
      advisoryId: id,
      expertId: data.expertId,
      action: 'PUBLISHED',
      snapshot: JSON.stringify(advisory),
      createdAt: new Date(),
    });

    this.eventsGateway.broadcast('NEW_ADVISORY_PUBLISHED', advisory);
    return advisory;
  }

  async getHistoryByExpertId(expertId: string) {
    const db = this.firebaseService.getDb();
    const snapshot = await db
      .collection('advisory_history')
      .where('expertId', '==', expertId)
      .orderBy('createdAt', 'desc')
      .get();
      
    return snapshot.docs.map(doc => doc.data());
  }
}
