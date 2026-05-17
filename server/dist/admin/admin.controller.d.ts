import { AdminService } from './admin.service';
export declare class AdminController {
    private readonly adminService;
    constructor(adminService: AdminService);
    getStats(): Promise<{
        totalUsers: number;
        experts: number;
        activeAdvisories: number;
        pendingCritical: number;
        systemHealth: string;
        revenue: string;
    }>;
    getUsers(): Promise<FirebaseFirestore.DocumentData[]>;
    provisionUser(body: any): Promise<any>;
}
