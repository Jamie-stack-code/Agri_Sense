import { MarketService } from './market.service';
export declare class MarketController {
    private readonly marketService;
    constructor(marketService: MarketService);
    getLivePrices(): Promise<FirebaseFirestore.DocumentData[]>;
    createAlert(body: any): Promise<any>;
    getAlerts(farmerId: string): Promise<FirebaseFirestore.DocumentData[]>;
}
