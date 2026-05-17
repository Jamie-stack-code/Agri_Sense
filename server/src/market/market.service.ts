import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import * as crypto from 'crypto';

@Injectable()
export class MarketService {
  constructor(private readonly firebaseService: FirebaseService) {}

  async getLivePrices() {
    const db = this.firebaseService.getDb();
    const snapshot = await db.collection('market_prices').orderBy('lastUpdated', 'desc').get();
    
    if (snapshot.empty) {
      // Default mock data just like the old server
      return [
        { cropName: 'Maize', pricePerKg: 1200, marketName: 'Lilongwe ADMARC', district: 'Lilongwe', trend: 0.05 },
        { cropName: 'Soya Beans', pricePerKg: 850, marketName: 'Limbe Market', district: 'Blantyre', trend: -0.02 },
        { cropName: 'Tobacco', pricePerKg: 2100, marketName: 'Kanengo Floors', district: 'Lilongwe', trend: 0.12 }
      ];
    }

    return snapshot.docs.map(doc => doc.data());
  }

  async createAlert(data: any) {
    const db = this.firebaseService.getDb();
    const id = require('crypto').randomUUID();
    const alert = {
      id,
      ...data,
      targetPrice: parseFloat(data.targetPrice),
      condition: data.condition || 'GREATER_THAN',
      createdAt: new Date(),
    };
    await db.collection('price_alerts').doc(id).set(alert);
    return alert;
  }

  async getAlerts(farmerId: string) {
    const db = this.firebaseService.getDb();
    const snapshot = await db.collection('price_alerts').where('farmerId', '==', farmerId).get();
    return snapshot.docs.map(doc => doc.data());
  }
}
