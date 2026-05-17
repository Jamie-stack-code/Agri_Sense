"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MarketService = void 0;
const common_1 = require("@nestjs/common");
const firebase_service_1 = require("../firebase/firebase.service");
let MarketService = class MarketService {
    firebaseService;
    constructor(firebaseService) {
        this.firebaseService = firebaseService;
    }
    async getLivePrices() {
        const db = this.firebaseService.getDb();
        const snapshot = await db.collection('market_prices').orderBy('lastUpdated', 'desc').get();
        if (snapshot.empty) {
            return [
                { cropName: 'Maize', pricePerKg: 1200, marketName: 'Lilongwe ADMARC', district: 'Lilongwe', trend: 0.05 },
                { cropName: 'Soya Beans', pricePerKg: 850, marketName: 'Limbe Market', district: 'Blantyre', trend: -0.02 },
                { cropName: 'Tobacco', pricePerKg: 2100, marketName: 'Kanengo Floors', district: 'Lilongwe', trend: 0.12 }
            ];
        }
        return snapshot.docs.map(doc => doc.data());
    }
    async createAlert(data) {
        const db = this.firebaseService.getDb();
        const id = require('crypto').randomUUID();
        const alert = {
            id,
            ...data,
            targetPrice: parseFloat(data.targetPrice),
            condition: data.condition || 'GREATER_THAN',
            createdAt: new Date(),
        };
        await db.collection('price_alerts').doc(id).set(alert);
        return alert;
    }
    async getAlerts(farmerId) {
        const db = this.firebaseService.getDb();
        const snapshot = await db.collection('price_alerts').where('farmerId', '==', farmerId).get();
        return snapshot.docs.map(doc => doc.data());
    }
};
exports.MarketService = MarketService;
exports.MarketService = MarketService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [firebase_service_1.FirebaseService])
], MarketService);
//# sourceMappingURL=market.service.js.map