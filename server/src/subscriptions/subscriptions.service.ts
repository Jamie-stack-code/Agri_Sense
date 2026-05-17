import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import * as crypto from 'crypto';

@Injectable()
export class SubscriptionsService {
  constructor(private readonly firebaseService: FirebaseService) {}

  async selectPlan(data: any) {
    const db = this.firebaseService.getDb();
    const { userId, planName } = data;
    
    const amount = planName === 'PREMIUM' ? 5000 : 0;
    const endDate = new Date();
    endDate.setMonth(endDate.getMonth() + 1);

    const id = crypto.randomUUID();
    const subscription = {
      id,
      farmerId: userId,
      planName,
      amount,
      status: planName === 'PREMIUM' ? 'PENDING_PAYMENT' : 'ACTIVE',
      endDate,
      startDate: new Date(),
    };

    await db.collection('subscriptions').doc(id).set(subscription);
    return subscription;
  }

  async verifyPayment(data: any) {
    const db = this.firebaseService.getDb();
    const { subscriptionId, paymentId, provider } = data;
    
    const docRef = db.collection('subscriptions').doc(subscriptionId);
    await docRef.update({
      status: 'ACTIVE',
      paymentId,
      provider,
    });

    return (await docRef.get()).data();
  }
}
