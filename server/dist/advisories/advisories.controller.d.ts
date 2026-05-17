import { AdvisoriesService } from './advisories.service';
export declare class AdvisoriesController {
    private readonly advisoriesService;
    constructor(advisoriesService: AdvisoriesService);
    createAdvisory(body: any): Promise<any>;
    getHistoryByExpertId(expertId: string): Promise<FirebaseFirestore.DocumentData[]>;
}
