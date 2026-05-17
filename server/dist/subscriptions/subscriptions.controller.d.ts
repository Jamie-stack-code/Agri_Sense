import { SubscriptionsService } from './subscriptions.service';
export declare class SubscriptionsController {
    private readonly subscriptionsService;
    constructor(subscriptionsService: SubscriptionsService);
    selectPlan(body: any): Promise<{
        id: `${string}-${string}-${string}-${string}-${string}`;
        farmerId: any;
        planName: any;
        amount: number;
        status: string;
        endDate: Date;
        startDate: Date;
    }>;
    verifyPayment(body: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
