import { Module } from '@nestjs/common';
import { SoilAnalysisController } from './soil-analysis.controller';
import { SoilAnalysisService } from './soil-analysis.service';

@Module({
  controllers: [SoilAnalysisController],
  providers: [SoilAnalysisService],
})
export class SoilAnalysisModule {}
