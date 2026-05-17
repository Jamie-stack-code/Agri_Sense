import { FirebaseService } from '../firebase/firebase.service';
import { EventsGateway } from '../events/events.gateway';
export declare class QuestionsService {
    private readonly firebaseService;
    private readonly eventsGateway;
    constructor(firebaseService: FirebaseService, eventsGateway: EventsGateway);
    createQuestion(data: any): Promise<any>;
}
