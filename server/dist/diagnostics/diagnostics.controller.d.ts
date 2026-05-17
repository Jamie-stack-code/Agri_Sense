import { DiagnosticsService } from './diagnostics.service';
export declare class DiagnosticsController {
    private readonly diagnosticsService;
    constructor(diagnosticsService: DiagnosticsService);
    createDiagnostic(body: any): Promise<any>;
    getAllDiagnostics(): Promise<FirebaseFirestore.DocumentData[]>;
    getHistoryByFarmerId(farmerId: string): Promise<FirebaseFirestore.DocumentData[]>;
    recommend(body: any): Promise<FirebaseFirestore.DocumentData | undefined>;
}
