import { OnModuleInit } from '@nestjs/common';
import * as admin from 'firebase-admin';
export declare class FirebaseService implements OnModuleInit {
    private db;
    private auth;
    onModuleInit(): void;
    getDb(): admin.firestore.Firestore;
    getAuth(): admin.auth.Auth;
}
