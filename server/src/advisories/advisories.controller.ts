import { Controller, Post, Get, Param, Body } from '@nestjs/common';
import { AdvisoriesService } from './advisories.service';

@Controller('advisories')
export class AdvisoriesController {
  constructor(private readonly advisoriesService: AdvisoriesService) {}

  @Post()
  async createAdvisory(@Body() body: any) {
    return this.advisoriesService.createAdvisory(body);
  }

  @Get('history/:expertId')
  async getHistoryByExpertId(@Param('expertId') expertId: string) {
    return this.advisoriesService.getHistoryByExpertId(expertId);
  }
}
