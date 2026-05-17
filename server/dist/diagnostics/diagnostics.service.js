"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.DiagnosticsService = void 0;
const common_1 = require("@nestjs/common");
const firebase_service_1 = require("../firebase/firebase.service");
const events_gateway_1 = require("../events/events.gateway");
const crypto = __importStar(require("crypto"));
let DiagnosticsService = class DiagnosticsService {
    firebaseService;
    eventsGateway;
    constructor(firebaseService, eventsGateway) {
        this.firebaseService = firebaseService;
        this.eventsGateway = eventsGateway;
    }
    async createDiagnostic(data) {
        const db = this.firebaseService.getDb();
        const id = crypto.randomUUID();
        const report = {
            id,
            ...data,
            status: 'PENDING',
            createdAt: new Date(),
            updatedAt: new Date(),
        };
        await db.collection('disease_reports').doc(id).set(report);
        this.eventsGateway.broadcast('NEW_DIAGNOSTIC_REPORT', report);
        return report;
    }
    async getAllDiagnostics() {
        const db = this.firebaseService.getDb();
        const snapshot = await db.collection('disease_reports').orderBy('createdAt', 'desc').get();
        return snapshot.docs.map(doc => doc.data());
    }
    async getHistoryByFarmerId(farmerId) {
        const db = this.firebaseService.getDb();
        const snapshot = await db
            .collection('disease_reports')
            .where('farmerId', '==', farmerId)
            .orderBy('createdAt', 'desc')
            .get();
        return snapshot.docs.map(doc => doc.data());
    }
    async recommend(data) {
        const db = this.firebaseService.getDb();
        const { reportId, expertId, recommendation } = data;
        const docRef = db.collection('disease_reports').doc(reportId);
        await docRef.update({
            expertId,
            expertRecommendation: recommendation,
            status: 'RESOLVED',
            updatedAt: new Date(),
        });
        const updated = (await docRef.get()).data();
        this.eventsGateway.broadcast('NEW_EXPERT_RECOMMENDATION', updated);
        return updated;
    }
};
exports.DiagnosticsService = DiagnosticsService;
exports.DiagnosticsService = DiagnosticsService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [firebase_service_1.FirebaseService,
        events_gateway_1.EventsGateway])
], DiagnosticsService);
//# sourceMappingURL=diagnostics.service.js.map