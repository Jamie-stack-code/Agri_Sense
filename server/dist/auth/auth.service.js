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
exports.AuthService = void 0;
const common_1 = require("@nestjs/common");
const firebase_service_1 = require("../firebase/firebase.service");
let AuthService = class AuthService {
    firebaseService;
    constructor(firebaseService) {
        this.firebaseService = firebaseService;
    }
    async verifyTokenAndGetProfile(idToken, fallbackPhone) {
        try {
            let uid;
            let phone = fallbackPhone;
            if (idToken) {
                const decodedToken = await this.firebaseService.getAuth().verifyIdToken(idToken);
                uid = decodedToken.uid;
                phone = decodedToken.phone_number || fallbackPhone;
            }
            else {
                uid = 'mock-uid-' + (phone || 'test');
            }
            const db = this.firebaseService.getDb();
            let userDoc = await db.collection('users').doc(uid).get();
            if (!userDoc.exists) {
                const newUser = {
                    id: uid,
                    phone: phone || null,
                    role: 'FARMER',
                    language: 'English',
                    isProfileComplete: false,
                    createdAt: new Date(),
                };
                await db.collection('users').doc(uid).set(newUser);
                return { token: idToken, user: newUser };
            }
            return { token: idToken, user: userDoc.data() };
        }
        catch (error) {
            console.error('Error verifying token:', error);
            throw new common_1.UnauthorizedException('Invalid token');
        }
    }
    async updateProfile(uid, data) {
        const db = this.firebaseService.getDb();
        const userRef = db.collection('users').doc(uid);
        await userRef.set(data, { merge: true });
        const updated = await userRef.get();
        return updated.data();
    }
};
exports.AuthService = AuthService;
exports.AuthService = AuthService = __decorate([
    (0, common_1.Injectable)(),
    __metadata("design:paramtypes", [firebase_service_1.FirebaseService])
], AuthService);
//# sourceMappingURL=auth.service.js.map