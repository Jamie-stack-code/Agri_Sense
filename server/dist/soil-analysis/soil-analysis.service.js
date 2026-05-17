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
exports.SoilAnalysisService = void 0;
const common_1 = require("@nestjs/common");
const firebase_service_1 = require("../firebase/firebase.service");
const events_gateway_1 = require("../events/events.gateway");
const crypto = __importStar(require("crypto"));
let SoilAnalysisService = class SoilAnalysisService {
    firebaseService;
    eventsGateway;
    constructor(firebaseService, eventsGateway) {
        this.firebaseService = firebaseService;
        this.eventsGateway = eventsGateway;
    }
    async createAnalysis(data) {
        const db = this.firebaseService.getDb();
        const id = crypto.randomUUID();
        const analysis = {
            id,
            ...data,
            pH: parseFloat(data.pH) || null,
            status: 'EXPERT_PENDING',
            createdAt: new Date(),
        };
        await db.collection('soil_analyses').doc(id).set(analysis);
        this.eventsGateway.broadcast('NEW_SOIL_ANALYSIS_REQUEST', analysis);
        return analysis;
    }
    async getHistoryByFarmerId(farmerId) {
        const db = this.firebaseService.getDb();
        const snapshot = await db
            .collection('soil_analyses')
            .where('farmerId', '==', farmerId)
            .orderBy('createdAt', 'desc')
            .get();
        return snapshot.docs.map(doc => doc.data());
    }
    async expertReview(data) {
        const db = this.firebaseService.getDb();
        const { analysisId, expertComment } = data;
        const docRef = db.collection('soil_analyses').doc(analysisId);
        await docRef.update({
            expertComment,
            status: 'EXPERT_COMPLETED',
        });
        const updated = (await docRef.get()).data();
        this.eventsGateway.broadcast('SOIL_ANALYSIS_REVIEWED', updated);
        return updated;
    }
};
exports.SoilAnalysisService = SoilAnalysisService;
exports.SoilAnalysisService = SoilAnalysisService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [firebase_service_1.FirebaseService,
        events_gateway_1.EventsGateway])
], SoilAnalysisService);
//# sourceMappingURL=soil-analysis.service.js.map