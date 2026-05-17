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
exports.AdvisoriesController = void 0;
const common_1 = require("@nestjs/common");
const advisories_service_1 = require("./advisories.service");
let AdvisoriesController = class AdvisoriesController {
    advisoriesService;
    constructor(advisoriesService) {
        this.advisoriesService = advisoriesService;
    }
    async createAdvisory(body) {
        return this.advisoriesService.createAdvisory(body);
    }
    async getHistoryByExpertId(expertId) {
        return this.advisoriesService.getHistoryByExpertId(expertId);
    }
};
exports.AdvisoriesController = AdvisoriesController;
__decorate([
    (0, common_1.Post)(),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AdvisoriesController.prototype, "createAdvisory", null);
__decorate([
    (0, common_1.Get)('history/:expertId'),
    __param(0, (0, common_1.Param)('expertId')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], AdvisoriesController.prototype, "getHistoryByExpertId", null);
exports.AdvisoriesController = AdvisoriesController = __decorate([
    (0, common_1.Controller)('advisories'),
    __metadata("design:paramtypes", [advisories_service_1.AdvisoriesService])
], AdvisoriesController);
//# sourceMappingURL=advisories.controller.js.map