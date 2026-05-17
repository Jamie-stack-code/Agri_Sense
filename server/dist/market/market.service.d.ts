import { FirebaseService } from '../firebase/firebase.service';
export declare class MarketService {
    private readonly firebaseService;
    constructor(firebaseService: FirebaseService);
    getLivePrices(): Promise<FirebaseFirestore.DocumentData[]>;
    createAlert(data: any): Promise<any>;
    getAlerts(farmerId: string): Promise<FirebaseFirestore.DocumentData[]>;
}
