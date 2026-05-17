import { Controller, Get, Post, Body, Param } from '@nestjs/common';
import { MarketService } from './market.service';

@Controller('market')
export class MarketController {
  constructor(private readonly marketService: MarketService) {}

  @Get('live')
  async getLivePrices() {
    return this.marketService.getLivePrices();
  }

  @Post('alerts')
  async createAlert(@Body() body: any) {
    return this.marketService.createAlert(body);
  }

  @Get('alerts/:farmerId')
  async getAlerts(@Param('farmerId') farmerId: string) {
    return this.marketService.getAlerts(farmerId);
  }
}
