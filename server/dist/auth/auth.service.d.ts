import { FirebaseService } from '../firebase/firebase.service';
export declare class AuthService {
    private readonly firebaseService;
    constructor(firebaseService: FirebaseService);
    verifyTokenAndGetProfile(idToken: string, fallbackPhone?: string): Promise<{
        token: string;
        user: {
            id: string;
            phone: string | null;
            role: string;
            language: string;
            isProfileComplete: boolean;
            createdAt: Date;
        };
    } | {
        token: string;
        user: FirebaseFirestore.DocumentData | undefined;
    }>;
    updateProfile(uid: string, data: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
