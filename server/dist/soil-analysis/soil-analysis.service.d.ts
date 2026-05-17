import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
export declare class SoilAnalysisService {
    private readonly firebaseService;
    private readonly eventsGateway;
    constructor(firebaseService: FirebaseService, eventsGateway: EventsGateway);
    createAnalysis(data: any): Promise<any>;
    getHistoryByFarmerId(farmerId: string): Promise<FirebaseFirestore.DocumentData[]>;
    expertReview(data: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
