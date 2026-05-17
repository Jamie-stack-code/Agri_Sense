"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AppModule = void 0;
const common_1 = require("@nestjs/common");
const app_controller_1 = require("./app.controller");
const app_service_1 = require("./app.service");
const firebase_module_1 = require("./firebase/firebase.module");
const auth_module_1 = require("./auth/auth.module");
const market_module_1 = require("./market/market.module");
const events_module_1 = require("./events/events.module");
const advisories_module_1 = require("./advisories/advisories.module");
const questions_module_1 = require("./questions/questions.module");
const diagnostics_module_1 = require("./diagnostics/diagnostics.module");
const soil_analysis_module_1 = require("./soil-analysis/soil-analysis.module");
const subscriptions_module_1 = require("./subscriptions/subscriptions.module");
const admin_module_1 = require("./admin/admin.module");
let AppModule = class AppModule {
};
exports.AppModule = AppModule;
exports.AppModule = AppModule = __decorate([
    (0, common_1.Module)({
        imports: [
            firebase_module_1.FirebaseModule,
            events_module_1.EventsModule,
            auth_module_1.AuthModule,
            market_module_1.MarketModule,
            advisories_module_1.AdvisoriesModule,
            questions_module_1.QuestionsModule,
            diagnostics_module_1.DiagnosticsModule,
            soil_analysis_module_1.SoilAnalysisModule,
            subscriptions_module_1.SubscriptionsModule,
            admin_module_1.AdminModule
        ],
        controllers: [app_controller_1.AppController],
        providers: [app_service_1.AppService],
    })
], AppModule);
//# sourceMappingURL=app.module.js.map