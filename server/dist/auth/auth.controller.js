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
exports.AuthController = void 0;
const common_1 = require("@nestjs/common");
const auth_service_1 = require("./auth.service");
let AuthController = class AuthController {
    authService;
    constructor(authService) {
        this.authService = authService;
    }
    async verifyToken(authHeader) {
        const idToken = authHeader?.split('Bearer ')[1] || '';
        return this.authService.verifyTokenAndGetProfile(idToken);
    }
    async phoneSignup(body) {
        console.warn('phone-signup called - Client should use Firebase SDK directly');
        return { message: 'OTP sent to your phone (Mock)', otp: '123456' };
    }
    async verifyPhoneOtp(body) {
        console.warn('verify-phone-otp called - Client should use Firebase SDK directly');
        return this.authService.verifyTokenAndGetProfile('', body.phone);
    }
    async phoneSignin(body) {
        console.warn('phone-signin called - Client should use Firebase SDK directly');
        return this.authService.verifyTokenAndGetProfile('', body.phone);
    }
    async updateProfile(body) {
        const uid = body.uid || ('mock-uid-' + body.phone);
        return this.authService.updateProfile(uid, body);
    }
};
exports.AuthController = AuthController;
__decorate([
    (0, common_1.Post)('verify-token'),
    __param(0, (0, common_1.Headers)('authorization')),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [String]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "verifyToken", null);
__decorate([
    (0, common_1.Post)('phone-signup'),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "phoneSignup", null);
__decorate([
    (0, common_1.Post)('verify-phone-otp'),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "verifyPhoneOtp", null);
__decorate([
    (0, common_1.Post)('phone-signin'),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "phoneSignin", null);
__decorate([
    (0, common_1.Post)('update-profile'),
    __param(0, (0, common_1.Body)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], AuthController.prototype, "updateProfile", null);
exports.AuthController = AuthController = __decorate([
    (0, common_1.Controller)('auth'),
    __metadata("design:paramtypes", [auth_service_1.AuthService])
], AuthController);
//# sourceMappingURL=auth.controller.js.map