import { AuthService } from './auth.service';
export declare class AuthController {
    private readonly authService;
    constructor(authService: AuthService);
    verifyToken(authHeader: string): Promise<{
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
    phoneSignup(body: any): Promise<{
        message: string;
        otp: string;
    }>;
    verifyPhoneOtp(body: any): Promise<{
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
    phoneSignin(body: any): Promise<{
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
    updateProfile(body: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
