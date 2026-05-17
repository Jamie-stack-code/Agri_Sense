import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { FirebaseModule } from './firebase/firebase.module';
import { AuthModule } from './auth/auth.module';
import { MarketModule } from './market/market.module';
import { EventsModule } from './events/events.module';
import { AdvisoriesModule } from './advisories/advisories.module';
import { QuestionsModule } from './questions/questions.module';
import { DiagnosticsModule } from './diagnostics/diagnostics.module';
import { SoilAnalysisModule } from './soil-analysis/soil-analysis.module';
import { SubscriptionsModule } from './subscriptions/subscriptions.module';
import { AdminModule } from './admin/admin.module';

@Module({
  imports: [
    FirebaseModule, 
    EventsModule, 
    AuthModule, 
    MarketModule, 
    AdvisoriesModule, 
    QuestionsModule,
    DiagnosticsModule,
    SoilAnalysisModule,
    SubscriptionsModule,
    AdminModule
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
