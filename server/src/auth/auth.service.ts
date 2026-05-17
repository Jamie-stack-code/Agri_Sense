import { Injectable, UnauthorizedException } from '@nestjs/common';
import { FirebaseService } from '../firebase/firebase.service';

@Injectable()
export class AuthService {
  constructor(private readonly firebaseService: FirebaseService) {}

  async verifyTokenAndGetProfile(idToken: string, fallbackPhone?: string) {
    try {
      let uid: string;
      let phone: string | undefined = fallbackPhone;
      
      // If we are given an ID token, verify it
      if (idToken) {
        const decodedToken = await this.firebaseService.getAuth().verifyIdToken(idToken);
        uid = decodedToken.uid;
        phone = decodedToken.phone_number || fallbackPhone;
      } else {
        // Mock fallback for testing without proper client integration yet
        uid = 'mock-uid-' + (phone || 'test');
      }

      const db = this.firebaseService.getDb();
      let userDoc = await db.collection('users').doc(uid).get();

      if (!userDoc.exists) {
        // Create new user profile in Firestore
        const newUser = {
          id: uid,
          phone: phone || null,
          role: 'FARMER', // Default
          language: 'English',
          isProfileComplete: false,
          createdAt: new Date(),
        };
        await db.collection('users').doc(uid).set(newUser);
        return { token: idToken, user: newUser };
      }

      return { token: idToken, user: userDoc.data() };
    } catch (error) {
      console.error('Error verifying token:', error);
      throw new UnauthorizedException('Invalid token');
    }
  }

  async updateProfile(uid: string, data: any) {
    const db = this.firebaseService.getDb();
    const userRef = db.collection('users').doc(uid);
    await userRef.set(data, { merge: true });
    const updated = await userRef.get();
    return updated.data();
  }
}
