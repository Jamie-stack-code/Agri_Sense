import { FirebaseService } from '../firebase/firebase.service';
export declare class SubscriptionsService {
    private readonly firebaseService;
    constructor(firebaseService: FirebaseService);
    selectPlan(data: any): Promise<{
        id: `${string}-${string}-${string}-${string}-${string}`;
        farmerId: any;
        planName: any;
        amount: number;
        status: string;
        endDate: Date;
        startDate: Date;
    }>;
    verifyPayment(data: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
