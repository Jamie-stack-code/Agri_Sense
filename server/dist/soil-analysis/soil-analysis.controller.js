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
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.SoilAnalysisController = void 0;
const common_1 = require("@nestjs/common");
const soil_analysis_service_1 = require("./soil-analysis.service");
let SoilAnalysisController = class SoilAnalysisController {
    soilAnalysisService;
    constructor(soilAnalysisService) {
        this.soilAnalysisService = soilAnalysisService;
    }
    async createAnalysis(body) {
        return this.soilAnalysisService.createAnalysis(body);
    }
    async getHistoryByFarmerId(farmerId) {
        return this.soilAnalysisService.getHistoryByFarmerId(farmerId);
    }
    async expertReview(body) {
        return this.soilAnalysisService.expertReview(body);
    }
};
exports.SoilAnalysisController = SoilAnalysisController;
__decorate([
    (0, common_1.Post)(),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], SoilAnalysisController.prototype, "createAnalysis", null);
__decorate([
    (0, common_1.Get)('history/:farmerId'),
    __param(0, (0, common_1.Param)('farmerId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], SoilAnalysisController.prototype, "getHistoryByFarmerId", null);
__decorate([
    (0, common_1.Post)('expert-review'),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], SoilAnalysisController.prototype, "expertReview", null);
exports.SoilAnalysisController = SoilAnalysisController = __decorate([
    (0, common_1.Controller)('soil-analysis'),
    __metadata("design:paramtypes", [soil_analysis_service_1.SoilAnalysisService])
], SoilAnalysisController);
//# sourceMappingURL=soil-analysis.controller.js.map