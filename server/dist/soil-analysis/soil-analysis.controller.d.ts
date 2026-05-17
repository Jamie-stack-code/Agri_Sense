import { SoilAnalysisService } from './soil-analysis.service';
export declare class SoilAnalysisController {
    private readonly soilAnalysisService;
    constructor(soilAnalysisService: SoilAnalysisService);
    createAnalysis(body: any): Promise<any>;
    getHistoryByFarmerId(farmerId: string): Promise<FirebaseFirestore.DocumentData[]>;
    expertReview(body: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
