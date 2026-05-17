import { Controller, Post, Get, Param, Body } from '@nestjs/common';
import { DiagnosticsService } from './diagnostics.service';

@Controller('diagnostics')
export class DiagnosticsController {
  constructor(private readonly diagnosticsService: DiagnosticsService) {}

  @Post()
  async createDiagnostic(@Body() body: any) {
    return this.diagnosticsService.createDiagnostic(body);
  }

  @Get()
  async getAllDiagnostics() {
    return this.diagnosticsService.getAllDiagnostics();
  }

  @Get('history/:farmerId')
  async getHistoryByFarmerId(@Param('farmerId') farmerId: string) {
    return this.diagnosticsService.getHistoryByFarmerId(farmerId);
  }

  @Post('recommend')
  async recommend(@Body() body: any) {
    return this.diagnosticsService.recommend(body);
  }
}
