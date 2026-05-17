import { Controller, Post, Get, Param, Body } from '@nestjs/common';
import { SoilAnalysisService } from './soil-analysis.service';

@Controller('soil-analysis')
export class SoilAnalysisController {
  constructor(private readonly soilAnalysisService: SoilAnalysisService) {}

  @Post()
  async createAnalysis(@Body() body: any) {
    return this.soilAnalysisService.createAnalysis(body);
  }

  @Get('history/:farmerId')
  async getHistoryByFarmerId(@Param('farmerId') farmerId: string) {
    return this.soilAnalysisService.getHistoryByFarmerId(farmerId);
  }

  @Post('expert-review')
  async expertReview(@Body() body: any) {
    return this.soilAnalysisService.expertReview(body);
  }
}
