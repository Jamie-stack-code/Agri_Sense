import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
export declare class DiagnosticsService {
    private readonly firebaseService;
    private readonly eventsGateway;
    constructor(firebaseService: FirebaseService, eventsGateway: EventsGateway);
    createDiagnostic(data: any): Promise<any>;
    getAllDiagnostics(): Promise<FirebaseFirestore.DocumentData[]>;
    getHistoryByFarmerId(farmerId: string): Promise<FirebaseFirestore.DocumentData[]>;
    recommend(data: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
