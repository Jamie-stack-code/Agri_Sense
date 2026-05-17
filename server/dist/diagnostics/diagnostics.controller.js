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
exports.DiagnosticsController = void 0;
const common_1 = require("@nestjs/common");
const diagnostics_service_1 = require("./diagnostics.service");
let DiagnosticsController = class DiagnosticsController {
    diagnosticsService;
    constructor(diagnosticsService) {
        this.diagnosticsService = diagnosticsService;
    }
    async createDiagnostic(body) {
        return this.diagnosticsService.createDiagnostic(body);
    }
    async getAllDiagnostics() {
        return this.diagnosticsService.getAllDiagnostics();
    }
    async getHistoryByFarmerId(farmerId) {
        return this.diagnosticsService.getHistoryByFarmerId(farmerId);
    }
    async recommend(body) {
        return this.diagnosticsService.recommend(body);
    }
};
exports.DiagnosticsController = DiagnosticsController;
__decorate([
    (0, common_1.Post)(),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], DiagnosticsController.prototype, "createDiagnostic", null);
__decorate([
    (0, common_1.Get)(),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], DiagnosticsController.prototype, "getAllDiagnostics", null);
__decorate([
    (0, common_1.Get)('history/:farmerId'),
    __param(0, (0, common_1.Param)('farmerId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], DiagnosticsController.prototype, "getHistoryByFarmerId", null);
__decorate([
    (0, common_1.Post)('recommend'),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], DiagnosticsController.prototype, "recommend", null);
exports.DiagnosticsController = DiagnosticsController = __decorate([
    (0, common_1.Controller)('diagnostics'),
    __metadata("design:paramtypes", [diagnostics_service_1.DiagnosticsService])
], DiagnosticsController);
//# sourceMappingURL=diagnostics.controller.js.map