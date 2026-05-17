import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
import * as crypto from 'crypto';

@Injectable()
export class DiagnosticsService {
  constructor(
    private readonly firebaseService: FirebaseService,
    private readonly eventsGateway: EventsGateway,
  ) {}

  async createDiagnostic(data: any) {
    const db = this.firebaseService.getDb();
    const id = crypto.randomUUID();
    const report = {
      id,
      ...data,
      status: 'PENDING',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    await db.collection('disease_reports').doc(id).set(report);
    this.eventsGateway.broadcast('NEW_DIAGNOSTIC_REPORT', report);
    return report;
  }

  async getAllDiagnostics() {
    const db = this.firebaseService.getDb();
    const snapshot = await db.collection('disease_reports').orderBy('createdAt', 'desc').get();
    return snapshot.docs.map(doc => doc.data());
  }

  async getHistoryByFarmerId(farmerId: string) {
    const db = this.firebaseService.getDb();
    const snapshot = await db
      .collection('disease_reports')
      .where('farmerId', '==', farmerId)
      .orderBy('createdAt', 'desc')
      .get();
      
    return snapshot.docs.map(doc => doc.data());
  }

  async recommend(data: any) {
    const db = this.firebaseService.getDb();
    const { reportId, expertId, recommendation } = data;
    
    const docRef = db.collection('disease_reports').doc(reportId);
    await docRef.update({
      expertId,
      expertRecommendation: recommendation,
      status: 'RESOLVED',
      updatedAt: new Date(),
    });

    const updated = (await docRef.get()).data();
    this.eventsGateway.broadcast('NEW_EXPERT_RECOMMENDATION', updated);
    return updated;
  }
}
