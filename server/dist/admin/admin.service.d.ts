import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
export declare class AdminService {
    private readonly firebaseService;
    private readonly eventsGateway;
    constructor(firebaseService: FirebaseService, eventsGateway: EventsGateway);
    getStats(): Promise<{
        totalUsers: number;
        experts: number;
        activeAdvisories: number;
        pendingCritical: number;
        systemHealth: string;
        revenue: string;
    }>;
    getUsers(): Promise<FirebaseFirestore.DocumentData[]>;
    provisionUser(data: any): Promise<any>;
}
