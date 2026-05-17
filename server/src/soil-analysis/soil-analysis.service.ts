import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
import * as crypto from 'crypto';

@Injectable()
export class SoilAnalysisService {
  constructor(
    private readonly firebaseService: FirebaseService,
    private readonly eventsGateway: EventsGateway,
  ) {}

  async createAnalysis(data: any) {
    const db = this.firebaseService.getDb();
    const id = crypto.randomUUID();
    const analysis = {
      id,
      ...data,
      pH: parseFloat(data.pH) || null,
      status: 'EXPERT_PENDING',
      createdAt: new Date(),
    };

    await db.collection('soil_analyses').doc(id).set(analysis);
    this.eventsGateway.broadcast('NEW_SOIL_ANALYSIS_REQUEST', analysis);
    return analysis;
  }

  async getHistoryByFarmerId(farmerId: string) {
    const db = this.firebaseService.getDb();
    const snapshot = await db
      .collection('soil_analyses')
      .where('farmerId', '==', farmerId)
      .orderBy('createdAt', 'desc')
      .get();
      
    return snapshot.docs.map(doc => doc.data());
  }

  async expertReview(data: any) {
    const db = this.firebaseService.getDb();
    const { analysisId, expertComment } = data;
    
    const docRef = db.collection('soil_analyses').doc(analysisId);
    await docRef.update({
      expertComment,
      status: 'EXPERT_COMPLETED',
    });

    const updated = (await docRef.get()).data();
    this.eventsGateway.broadcast('SOIL_ANALYSIS_REVIEWED', updated);
    return updated;
  }
}
