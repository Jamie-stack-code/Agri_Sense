import { Injectable, OnModuleInit } from '@nestjs/common';
import * as admin from 'firebase-admin';
import * as path from 'path';

@Injectable()
export class FirebaseService implements OnModuleInit {
  private db: admin.firestore.Firestore;
  private auth: admin.auth.Auth;

  onModuleInit() {
    // Determine the path to the service account JSON
    // Note: It's in the root of the server directory
    const serviceAccountPath = path.join(process.cwd(), 'firebase-service-account.json');

    try {
      if (!admin.apps.length) {
        admin.initializeApp({
          credential: admin.credential.cert(serviceAccountPath),
        });
      }
      this.db = admin.firestore();
      this.auth = admin.auth();
      console.log('🔥 Firebase Admin initialized successfully.');
    } catch (error) {
      console.error('❌ Failed to initialize Firebase Admin:', error);
      throw error;
    }
  }

  getDb(): admin.firestore.Firestore {
    return this.db;
  }

  getAuth(): admin.auth.Auth {
    return this.auth;
  }
}
