import { Injectable } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
import * as crypto from 'crypto';

@Injectable()
export class QuestionsService {
  constructor(
    private readonly firebaseService: FirebaseService,
    private readonly eventsGateway: EventsGateway,
  ) {}

  async createQuestion(data: any) {
    const db = this.firebaseService.getDb();
    const id = crypto.randomUUID();
    const question = {
      id,
      ...data,
      status: 'PENDING',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    await db.collection('questions').doc(id).set(question);
    this.eventsGateway.broadcast('NEW_FARMER_QUESTION', question);
    return question;
  }
}
