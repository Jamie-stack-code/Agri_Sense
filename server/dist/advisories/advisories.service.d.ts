import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
export declare class AdvisoriesService {
    private readonly firebaseService;
    private readonly eventsGateway;
    constructor(firebaseService: FirebaseService, eventsGateway: EventsGateway);
    createAdvisory(data: any): Promise<any>;
    getHistoryByExpertId(expertId: string): Promise<FirebaseFirestore.DocumentData[]>;
}
